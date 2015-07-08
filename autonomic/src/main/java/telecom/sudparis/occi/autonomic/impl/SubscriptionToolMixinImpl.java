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
import java.io.StringWriter;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.berniecode.mixin4j.MixinAware;

//import telecom.sudparis.occi.autonomic.NotificationLink;
import telecom.sudparis.occi.autonomic.SubscriptionLink;
import telecom.sudparis.occi.autonomic.interfaces.SubscriptionToolMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Subscription;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class SubscriptionToolMixinImpl implements
		SubscriptionToolMixinInterface, MixinAware<SubscriptionLink> {

	private final String targetURI="http://localhost:8182/mixedapplication/subscribe/";
	private SubscriptionLink mixinBase;

	@Override
	public void setMixinBase(SubscriptionLink mixinBase) {
		this.mixinBase = mixinBase;

	}

	@Override
	public void subscribe(Subscription subscription) {

		String target = mixinBase.getTarget().getId().toString();
		//String source = mixinBase.getLink().getId().toString();

		System.out.println("SubscriptionToolMixinImpl NotificationLink Source "
				+ target);

		/**
		 * TODO
		 */
		ClientResource resource = new ClientResource(getTargetURI());
		StringWriter response = new StringWriter();

		

		try {
			resource.post(XMLTools.getSubscriptionXML(subscription)).write(
					response);
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("SubscriptionToolMixinImpl subscribersList");
		System.out.println("SubscriptionToolMixinImpl:   subscribed");
	}

	private String getTargetURI() {
		String uri= mixinBase.getTarget().getId().toString();
		return targetURI+uri.substring(uri.lastIndexOf("/")+1);
	}

	@Override
	public void unsubscribe(String notificationLinkUuid) {
		// TODO Auto-generated method stub

	}

}
