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

import telecom.sudparis.occi.autonomic.interfaces.PollingMixinInterface;
import telecom.sudparis.occi.autonomic.testresources.MixedApplication;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.NotificationFactory;

import java.io.StringWriter;
import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.berniecode.mixin4j.MixinAware;

public class PollingMixinImpl implements PollingMixinInterface,
		MixinAware<MixedApplication> {

	/**
	 * MONITORING CLOUDFOUNDRY APPS URI
	 */
	private final static String newRelicURI = "https://api.newrelic.com/v2/applications.xml";

	/**
	 * Authorization key to connect to newRelic
	 */

	private final static String api_key = "37a10b0b241d2c0182ab81d5a1b9f4e5ae620276c4fe849";

	private MixedApplication mixinBase;

	// @Override
	public Notification poll(String attributeName) {

		ClientResource resource = new ClientResource(newRelicURI);
		resource.setProtocol(Protocol.HTTPS);

		StringWriter response = new StringWriter();
		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("X-Api-Key", api_key);
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			Representation representation = resource.get();
			representation.write(response);

		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return NotificationFactory.createNotification(response.toString(),
				attributeName, mixinBase.getName());

	}

	/**
	 * @return the newRelicURI
	 */
	public String getNewRelicURI() {
		return newRelicURI;
	}

	public static void main(String arg[]) {
		PollingMixinImpl p = new PollingMixinImpl();
		System.out.println(p.poll("response_time"));
	}

	@Override
	public void setMixinBase(MixedApplication mixinBase) {
		this.mixinBase = mixinBase;

	}

}
