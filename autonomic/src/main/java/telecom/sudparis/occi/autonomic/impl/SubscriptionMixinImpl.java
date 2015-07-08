/**
 * Copyright (C) 2010-2011 Sebastian Heckmann, Sebastian Laag
 *
 * Contact Email: <sebastian.heckmann@udo.edu>, <sebastian.laag@udo.edu>
 *
 * Contact Email for Autonomic Resources: <mohamed.mohamed@telecom-sudparis.eu>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package telecom.sudparis.occi.autonomic.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.StringWriter;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.berniecode.mixin4j.MixinAware;

import telecom.sudparis.occi.autonomic.interfaces.SubscriptionMixinInterface;
import telecom.sudparis.occi.autonomic.testresources.MixedApplication;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.Subscription;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class SubscriptionMixinImpl extends Thread implements
		SubscriptionMixinInterface, MixinAware<MixedApplication> {
	private final String POLLING_URI = "http://157.159.110.227:8182/mixedapplication/poll/";
	private final String notificationLinkAdress = "http://157.159.110.227:8182/notificationlink";

	public long duration = System.currentTimeMillis();

	private List<String> subscribers;

	private long interval;
	private MixedApplication mixinBase;

	public List<String> getSubscribers() {

		return subscribers;
	}

	public void setSubscribers(List<String> subscribers) {
		this.subscribers = subscribers;
	}

	@Override
	public synchronized void notify(Notification notification) {
		ClientResource resource;
//		 Client client = new Client(Protocol.HTTP);
//		 client.setConnectTimeout(100000);
		for (String subscriber : subscribers) {
			try{resource = new ClientResource(subscriber);
			//resource.setNext(client);
			resource.post(XMLTools.getNotificationXML(notification));
			resource.release();
		}
			catch(Exception e ){System.err.println("subscriptionMixin error: "+e.getMessage());}
		}
		System.out
				.println("SubscriptionMixinImpl: all subscribers are notified");
	}
	@Override
	public void push(Notification notification) {
		for (String subscriber : subscribers) {
			ClientResource resource = new ClientResource(subscriber);
			resource.post(XMLTools.getNotificationXML(notification));
		}
		System.out
				.println("SubscriptionMixinImpl: all subscribers are notified");
	}

	@Override
	public void subscribe(Subscription subscription) {
		

		setDuration(subscription.getDuration());
		setInterval(subscription.getInterval());

		/**
		 * Post Notification Link
		 */

		ClientResource resource = new ClientResource(notificationLinkAdress);

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add(
						"Category",
						"notificationlink; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.core.source=http://localhost:8182/application/"
								+ getApplicationId());

				headers.add("X-OCCI-Attribute",
						"occi.core.target=http://157.159.110.227:8182/analyzer/"
								+ subscription.getSubscriber());
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.name=notificationlinkM");
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.version=1");
				headers.add(
						"X-OCCI-Attribute",
						"occi.notificationlink.refreshrate="
								+ subscription.getInterval());
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			String subscriberURI = headersResponse.getFirstValue("Location");

			String subscriberUuid = getLastSegment(subscriberURI).toString();
		//	System.out.println("\n" + subscriberUuid);

			/**
			 * End Notification Link creation
			 */

			addSubscriber(subscriberUuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
if (subscription.getType().toLowerCase().equals("oninterval"))
		new Thread(this).start();
	}

	private String getLastSegment(String uri) {
		return uri.substring(uri.lastIndexOf("/") + 1);

	}

	private String getApplicationId() {
		// TODO Auto-generated method stub
		return mixinBase.getUuid().toString();
	}

	private void setInterval(long interval) {
		this.interval = interval;

	}

	public long getInterval() {
		return interval;

	}

	private void addSubscriber(String subscriber) {
		if (subscribers == null)
			subscribers = Arrays.asList(notificationLinkAdress+"/notify/" + subscriber);
		else
			subscribers.add(notificationLinkAdress+"/notify/" + subscriber);

	}

	@Override
	public void unsubscribe(String subscriber) {
		subscribers.remove(subscriber);
		System.out.println("SubscriptionMixinImpl: Subscriber removed");

	}

	public void setDuration(long duration) {
		this.duration = System.currentTimeMillis() + duration;
	};

	/**
	 * TODO verify this method
	 */
	@Override
	public void run() {

		ClientResource resource = new ClientResource(getPollingURI(POLLING_URI));
		// Get XML

		while (duration > System.currentTimeMillis()) {

			/**
			 * TODO update applicationName
			 */
			StringWriter response = new StringWriter();
			try {
				resource.get().write(response);
			} catch (ResourceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Notification notification = null;

			System.out.println(response.toString());

			notification = XMLTools.getNotificationFromXML(response.toString());
			System.out.println(notification);
			// System.out.println("run::run" + subscribers.size());
			notify(notification);
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				System.out.println("SubscriptionMixinImpl::" + e.getMessage());
			}
		}
	}

	private String getPollingURI(String pOLLING_URI2) {
		// TODO Auto-generated method stub
		return POLLING_URI + getApplicationId();
	}

	// @Override
	// public void run() {
	// while (duration > System.currentTimeMillis()) {
	// Notification notification = mixinBase.poll2("freeSpace");
	//
	// System.out.println("run::run" + subscribers.size());
	//
	// try {
	// Thread.sleep(1000);
	//
	// for (int i = 0; i < subscribers.size(); i++) {
	// notification.setNotifier(subscribers.get(i));
	// subscribers.get(i).notify(notification);
	// }
	//
	// } catch (InterruptedException e) {
	// System.out.println("SubscriptionMixinImpl::" + e.getMessage());
	// }
	// }
	// }
	/**
	 * @return the newRelicURI
	 */
	// public String getNewRelicURI() {
	// return newRelicURI;
	// }
	//

	public static void main(String args[]) {
		SubscriptionMixinImpl subscriptionMixinImpl = new SubscriptionMixinImpl();
		subscriptionMixinImpl.setDuration(1000000);

		System.out.println("end");
		Subscription s = new Subscription(12,
				"1a227c03-1d50-4491-b5e2-8582d0c4d38d", 1000);
		subscriptionMixinImpl.subscribe(s);
	}

	@Override
	public void setMixinBase(MixedApplication mixinBase) {
		this.mixinBase = mixinBase;

	}
}
