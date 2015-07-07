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

import occi.core.Method;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import occi.core.Action;

import com.berniecode.mixin4j.MixinAware;

import telecom.sudparis.occi.autonomic.ActionLink;
import telecom.sudparis.occi.autonomic.ReconfigurationManager;
import telecom.sudparis.occi.autonomic.interfaces.StrategySetMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Alert;
import telecom.sudparis.occi.autonomic.tools.ReconfigurationAction;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class StrategySetMixinImpl implements StrategySetMixinInterface,
		MixinAware<ReconfigurationManager> {
	private final String actionLinkReconfigureURI = "http://157.159.110.227:8182/actionlink/reconfigure/";
	private ReconfigurationManager mixinBase;
	private ClientResource resource;

	@Override
	public void setMixinBase(ReconfigurationManager mixinBase) {
		this.mixinBase = mixinBase;

	}

	@Override
	public void applyStrategy(Alert alert) {
		System.out.println("Applying Strategy");

		// StringWriter response = new StringWriter();
		String methodName = null;

		if (alert.getType().equals("scaledown"))
			methodName = "scaledown";
		else
			methodName = "scaleup";
		Object args[] = { 1 };
		ReconfigurationAction reconfigurationAction = new ReconfigurationAction(
				methodName, args);
		// Client client = new Client(Protocol.HTTP);
		// client.setConnectTimeout(100000);
		try {
			resource = new ClientResource(getActionLinkURI());
			// resource.setNext(client);
			resource.post(XMLTools
					.getReconfigurationActionXML(reconfigurationAction));
			resource.release();
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		/**
		 * TODO verify that the release do not break the thread
		 */

	}

	private String getActionLinkURI() {
		Map<UUID, ActionLink> actionLinkList = ActionLink.getActionLinkList();
		for (UUID id : actionLinkList.keySet()) {

			ActionLink link = actionLinkList.get(id);
			String reconfigManID = link.getLink().getId().toString();

			System.out.println("strategyset mixin base id" + mixinBase.getId());
			if (reconfigManID.equals(mixinBase.getId().toString())) {
				String uri = link.getId().toString();

				// / System.out.println("StrategyMixin ::" + uri);
				return actionLinkReconfigureURI + uri;
			}
		}
		System.out.println("Strategy NULLLL");
		return null;
	}

	@Override
	public void applyStrategy(Alert alert, URI uri, Method method) {
		if (alert.getType().contains("scaleup"))
			// System.out.println("StrategySetMixinImpl scaleup" +
			// ":  activated alert");
			System.err.println("StrategySetMixinImpl scaleup"
					+ ":  activated alert");
		Action action = null;
		try {
			action = ActionLink.getReconfigureAction();
			action.execute(uri, method);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("StrategySet error " + e.getMessage());
		}

	}

}
