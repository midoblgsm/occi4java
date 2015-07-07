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

import com.berniecode.mixin4j.MixinAware;

import telecom.sudparis.occi.autonomic.NotificationLink;
import telecom.sudparis.occi.autonomic.interfaces.NotificationToolMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class NotificationToolMixinImpl implements
		NotificationToolMixinInterface, MixinAware<NotificationLink> {
	private final String targetURI = "http://157.159.110.227:8182/analyzer/notify/";
	private NotificationLink mixinBase;
	ClientResource resource;

	@Override
	public void setMixinBase(NotificationLink mixinBase) {
		this.mixinBase = mixinBase;
		// TODO Auto-generated method stub

	}

	@Override
	public void notify(Notification notification) {

//		 Client client = new Client(Protocol.HTTP);
//		 client.setConnectTimeout(100000);

		try {
			resource = new ClientResource(getTargetUri());
			//resource.setNext(client);
			resource.post(XMLTools.getNotificationXML(notification));
			resource.release();
		} catch (ResourceException e) {
			
			System.out.println("NotificationToolMixin error: "+e.getMessage());
		}
		/**
		 * TODO To be verified
		 */

	}

	private String getTargetUri() {
		/**
		 * TODO verify
		 */

		String target = targetURI
				+ ((mixinBase.getTarget().getId().toString()));

		return target;
	}

	// public static void main(String args[]) {
	// // Notification n= new Notification("12", "454", "54654", "12", "65");
	// // new NotificationToolMixinImpl().notify(n);
	// Map<UUID, NotificationLink> link = NotificationLink
	// .getNotificationLinkList();//
	// .get("http://localhost:8182/notificationlink/5dbe2618-5f58-44f1-80a1-dd312630d5ec");
	// System.out.println(link.size());
	// // System.out.println(link.getId());
	//
	// }

}
