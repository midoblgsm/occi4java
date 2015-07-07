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

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import telecom.sudparis.occi.autonomic.AlertLink;
import telecom.sudparis.occi.autonomic.interfaces.AlertToolMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Alert;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class AlertToolMixinImpl implements AlertToolMixinInterface,
		MixinAware<AlertLink> {

	private final String RECONFIGURATION_MANAGER_URI = "http://localhost:8182/reconfigurationmanager/alert/";

	private AlertLink mixinBase;
	private ClientResource resource;

	@Override
	public void setMixinBase(AlertLink mixinBase) {
		this.mixinBase = mixinBase;

	}

	@Override
	public void alert(Alert alert) {
		System.out.println("AlertToolMixin Alerting");
		// Client client = new Client(Protocol.HTTP);
		// client.setConnectTimeout(100000);
		try {
			resource = new ClientResource(getReconfigurationManagerAlertURI());
			// resource.setNext(client);

			resource.post(XMLTools.getAlertXML(alert));
			// resource.release();
		} catch (ResourceException e) {
			System.err.println("AlertToolMixin error: " + e.getMessage());
		}
		/**
		 * verify that the release do not break the thread
		 */

	}

	private String getReconfigurationManagerAlertURI() {
		String uri = mixinBase.getTarget().getId().toString();
		// System.out.println("AlertTool uri from mixinbase"+uri);
		return RECONFIGURATION_MANAGER_URI + uri;
	}

}
