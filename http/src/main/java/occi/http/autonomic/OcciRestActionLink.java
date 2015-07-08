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
package occi.http.autonomic;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import occi.application.Application;
import occi.config.OcciConfig;
import occi.core.Kind;
import occi.core.Resource;
import occi.http.check.OcciCheck;
import occi.platform.Container;
import occi.platform.Database;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berniecode.mixin4j.DynamicProxyMixer;
import com.berniecode.mixin4j.Factory;
import com.berniecode.mixin4j.Mixin;

import telecom.sudparis.occi.autonomic.ActionLink;
import telecom.sudparis.occi.autonomic.ActionLink.State;
import telecom.sudparis.occi.autonomic.ReconfigurationManager;
import telecom.sudparis.occi.autonomic.impl.ActionToolMixinImpl;
import telecom.sudparis.occi.autonomic.interfaces.ActionToolMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Alert;
import telecom.sudparis.occi.autonomic.tools.ReconfigurationAction;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class OcciRestActionLink extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestActionLink.class);

	private final OcciCheck occiCheck = new OcciCheck();

	/**
	 * Method to create a new container instance.
	 * 
	 * @param representation
	 * @return string
	 * @throws Exception
	 */
	@Post
	public String postOCCIRequest(Representation representation)
			throws Exception {
		LOGGER.info("Incoming POST request.");
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		HashMap<String, Object> xoccimap = new HashMap<String, Object>();
		if (getReference().toString().contains("reconfigure")) {
			Uniform uniform = new UniformActionLinkhandler(representation, getReference());
			uniform.handle(getRequest(), getResponse());
			return "alert received by the reconfiguration manager";
		}

		try {
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			LOGGER.debug("Current request: " + requestHeaders);
			String attributeCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("x-occi-attribute");
			String xocciattributes = requestHeaders.getValues(attributeCase)
					.replace(",", " ");
			String acceptCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("accept");
			LOGGER.debug("Media-Type: "
					+ requestHeaders.getFirstValue(acceptCase));
			LOGGER.debug("getref getlastseg: "
					+ getReference().getLastSegment());

			// OcciCheck.countColons(xocciattributes, 1);

			// split the single occi attributes and put it into a
			// (key,value)
			// map
			LOGGER.debug("Raw X-OCCI Attributes: " + xocciattributes);
			StringTokenizer xoccilist = new StringTokenizer(xocciattributes);
			// HashMap<String, Object> xoccimap = new HashMap<String,
			// Object>();
			LOGGER.debug("Tokens in XOCCIList: " + xoccilist.countTokens());
			while (xoccilist.hasMoreTokens()) {
				String[] temp = xoccilist.nextToken().split("\\=");
				if (temp[0] != null && temp[1] != null) {
					LOGGER.debug(temp[0] + " " + temp[1] + "\n");
					xoccimap.put(temp[0], temp[1]);
				}
			}
			// Check if last part of the URI is not action
			if (getReference().toString().contains("action")) {
				// put occi attributes into a buffer for the response
				StringBuffer buffer = new StringBuffer();

				buffer.append("occi.core.source=").append(
						xoccimap.get("occi.core.source"));
				buffer.append("occi.core.target=").append(
						xoccimap.get("occi.core.target"));
				buffer.append("occi.actionlink.name=").append(
						xoccimap.get("occi.actionlink.name"));

				buffer.append(" occi.actionlink.version=").append(
						xoccimap.get("occi.actionlink.version"));
				buffer.append(" occi.actionlink.state=").append("unavailable");

				Set<String> set = new HashSet<String>();
				set.add("summary: ");
				set.add(buffer.toString());
				set.add(requestHeaders.getFirstValue("scheme"));
				LOGGER.debug("Attribute set: " + set.toString());

				// create new container instance with the given attributes
				// TODO Let the mixin handle that

				DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();

				Mixin<ActionLink> actionLinkWithMixin = new Mixin(
						ActionToolMixinInterface.class,
						ActionToolMixinImpl.class, ActionLink.class);
				Factory factory = dynamicMixer.getFactory(ActionLink.class,
						actionLinkWithMixin);
				Class[] clazz0 = { Resource.class, Resource.class,
						String.class, String.class, State.class, Set.class };

				UUID sourceID = UUID.fromString(((String) xoccimap
						.get("occi.core.source")).replaceFirst(".*/([^/?]+).*",
						"$1"));
				UUID targetID = UUID.fromString(((String) xoccimap
						.get("occi.core.target")).replaceFirst(".*/([^/?]+).*",
						"$1"));
				Resource source = (ReconfigurationManager
						.getReconfigurationManagerList().containsKey(sourceID)) ? ReconfigurationManager
						.getReconfigurationManagerList().get(sourceID)
						: ReconfigurationManager
								.getReconfigurationManagerList().get(targetID);
				Resource target = (Application.getApplicationList()
						.containsKey(sourceID)) ? Application
						.getApplicationList().get(sourceID) : Application
						.getApplicationList().get(targetID);
				Object[] objects0 = { source, target,
						(String) xoccimap.get("occi.actionlink.name"),
						(String) xoccimap.get("occi.actionlink.version"),
						State.available, set };
				ActionLink actionLink = (ActionLink) factory.newInstance(
						clazz0, objects0);

				actionLink.setKind(new Kind(null, "actionlink", "actionlink",
						null));
				// URI uri = new URI(actionLink.getId().toString());
				// Create libvirt domain
				// CreateAction createAction = new CreateAction();
				// createAction.execute(uri, null);

				StringBuffer resource = new StringBuffer();
				resource.append("/").append(actionLink.getKind().getTerm())
						.append("/");
				getRootRef().setPath(resource.toString());

				// check of the category
				if (xoccimap.get("occi.actionlink.Category") != null)
					if (!"actionlink".equalsIgnoreCase(xoccimap.get(
							"occi.actionlink.Category").toString())) {
						throw new IllegalArgumentException(
								"Illegal Category type: "
										+ xoccimap
												.get("occi.actionlink.Category"));
					}
				// for (Mixin mixin : Mixin.getMixins()) {
				// if (mixin.getEntities() != null) {
				// if (mixin.getEntities().contains(actionLink)) {
				// buffer.append("Category: " + mixin.getTitle()
				// + "; scheme=\"" + mixin.getScheme()
				// + "\"; class=\"mixin\"");
				// }
				// }
				// }
				// System.out.println("Uuid####"+actionLink.getUuid());
				LOGGER.debug("ActionLink Uuid: " + actionLink.getUuid());
				LOGGER.debug("ActionLink Kind scheme: "
						+ actionLink.getKind().getScheme());
				// Check accept header
				if (requestHeaders.getFirstValue(acceptCase)
						.equals("text/occi")
						|| requestHeaders.getFirstValue("content-type", true)
								.equals("text/occi")) {
					// Generate header rendering
					occiCheck.setHeaderRendering(null, actionLink,
							buffer.toString(), null);
					getResponse().setEntity(representation);
					getResponse().setStatus(Status.SUCCESS_OK);
				}
				// Location Rendering in HTTP Header, not in body
				setLocationRef((getRootRef().toString() + actionLink.getId()));
				representation = OcciCheck.checkContentType(requestHeaders,
						buffer, getResponse());
				getResponse().setEntity(representation);
				// set response status
				getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
				return Response.getCurrent().toString();
			} else {
				String[] splitURI = getReference().toString().split("\\/");
				LOGGER.debug("splitURI length: " + splitURI.length);
				UUID id = null;
				for (String element : splitURI) {
					if (OcciCheck.isUUID(element)) {
						id = UUID.fromString(element);
					}
				}
				LOGGER.debug("UUID: " + id);
				// Get the container resource by the given UUID
				ActionLink actionLink = ActionLink.getActionLinkList().get(id);

				String location = "http:"
						+ getReference().getHierarchicalPart();
				// System.out.println("Actions Available##### verify the if condition");
				// Extract the action type / name from the last part of the
				// given
				// location URI and split it after the "=" (../?action=stop)
				// String[] actionName = getReference()
				// .getRemainingPart()
				// .subSequence(1,
				// getReference().getRemainingPart().length())
				// .toString().split("\\=");
				// LOGGER.debug("Action Name: " + actionName[1]);

				// // Check if actionName[1] is set
				// if (actionName.length >= 2) {
				// // Call the Start action of the container resource
				// if (actionName[1].equalsIgnoreCase("start")) {
				// LOGGER.debug("Start Action called.");
				// LOGGER.debug(xoccimap.toString());
				// actionLink.getStart().execute(URI.create(location),null);
				// // Set the current state of the container resource
				// actionLink.setState(State.available);
				// }
				//
				// if (actionName[1].equalsIgnoreCase("stop")) {
				// LOGGER.debug("Stop Action called.");
				// // Call the Stop action of the container resource
				// actionLink.getStop().execute(URI.create(location),null);
				// // Set the current state of the container resource
				// actionLink.setState(State.unvailable);
				// }
				//
				// if (actionName[1].equalsIgnoreCase("restart")) {
				// LOGGER.debug("Restart Action called.");
				// // Call the Restart action of the container resource
				// actionLink.getRestart().execute(URI.create(location), null);
				// // Set the current state of the container resource
				// actionLink.setState(State.available);
				// }
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.toString());
			return "Exception caught: " + e.toString() + "\n";
		}
		return " ";
	}

	@Get
	public String getOCCIRequest() {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		LOGGER.debug("getReference().getLastSegment(): "
				+ getReference().getLastSegment().toString());
		try {
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			String acceptCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("accept");
			OcciCheck.isUUID(getReference().getLastSegment());
			// get the container instance by the given UUID
			ActionLink actionLink = ActionLink.getActionLinkList().get(
					UUID.fromString(getReference().getLastSegment()));

			// put all attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			StringBuffer linkBuffer = new StringBuffer();
			buffer.append(" Category: ").append(actionLink.getKind().getTerm())
					.append(" scheme= ")
					.append(actionLink.getKind().getScheme())
					.append(" class=\"kind\";").append(" Name: ")
					.append(actionLink.getName()).append(" Version: ")
					.append(actionLink.getState());

			// TODO: see infrastructure to display links

			// access the request headers and get the Accept attribute
			Representation representation = OcciCheck.checkContentType(
					requestHeaders, buffer, getResponse());
			// Check the accept header
			if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
				// generate header rendering
				this.occiCheck.setHeaderRendering(null, actionLink,
						buffer.toString(), linkBuffer);
				// set right representation and status code
				getResponse().setEntity(representation);
				getResponse().setStatus(Status.SUCCESS_OK);
				return " ";
			}
			// set right representation and status code
			getResponse().setEntity(representation);
			getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
			return buffer.toString();
		} catch (NullPointerException e) {
			e.printStackTrace();
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString() + "\n";
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.toString());
			return e.toString();
		}
	}

	/**
	 * Deletes the resource which applies to the parameters in the header.
	 * 
	 * @return string deleted or not
	 */
	@Delete
	public String deleteOCCIRequest() {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		LOGGER.debug("Incoming delete request");
		try {
			OcciCheck.isUUID(getReference().getLastSegment());
			// get container resource that should be deleted
			ActionLink actionLink = ActionLink.getActionLinkList().get(
					UUID.fromString(getReference().getLastSegment()));
			// TODO
			// DeleteAction deleteAction = new DeleteAction();
			// deleteAction.execute(new URI(actionLink.getId().toString()),
			// null);
			// remove it from container resource list
			if (ActionLink.getActionLinkList().remove(
					UUID.fromString(actionLink.getId().toString())) == null) {
				throw new Exception("There is no resorce with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set container resource to null
			actionLink = null;
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString()
					+ "\n ActionLink resource could not be deleted.";
			// Exception for isUUID method
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return e.toString();
		}
	}

	// TODO update
	/**
	 * Edit the parameters of a given resource instance.
	 * 
	 * @param representation
	 * @return data of altered instance
	 * @throws Exception
	 */
	@Put
	public String putOCCIRequest(Representation representation)
			throws Exception {
		try {
			// set occi version info
			getServerInfo().setAgent(
					OcciConfig.getInstance().config.getString("occi.version"));
			OcciCheck.isUUID(getReference().getLastSegment());
			ActionLink actionLink = ActionLink.getActionLinkList().get(
					UUID.fromString(getReference().getLastSegment()));
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			LOGGER.debug("Raw Request Headers: " + requestHeaders);

			String attributeCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("x-occi-attribute");
			String xocciattributes = requestHeaders.getValues(attributeCase)
					.replace(",", " ");

			LOGGER.debug("X-OCCI-Attributes != null?: "
					+ xocciattributes.isEmpty());
			// Check if some attributes are given by the request
			if (xocciattributes != null) {
				// Count the colons in the Request
				OcciCheck.countColons(xocciattributes, 1);
				/*
				 * split the single occi attributes and put it into a
				 * (key,value) map
				 */
				LOGGER.debug("Raw X-OCCI Attributes: " + xocciattributes);
				StringTokenizer xoccilist = new StringTokenizer(xocciattributes);
				HashMap<String, Object> xoccimap = new HashMap<String, Object>();
				LOGGER.debug("Tokens in XOCCIList: " + xoccilist.countTokens());
				while (xoccilist.hasMoreTokens()) {
					String[] temp = xoccilist.nextToken().split("\\=");
					if (temp.length > 1 && temp[0] != null && temp[1] != null) {
						xoccimap.put(temp[0], temp[1]);
					}
				}
				LOGGER.debug("X-OCCI-Map empty?: " + xoccimap.isEmpty());
				if (!xoccimap.isEmpty()) {
					// Change the container attribute if it is send by the
					// request

					if (xoccimap.containsKey("occi.container.version")) {
						LOGGER.info((String) xoccimap
								.get("occi.container.version"));
						actionLink.setVersion(xoccimap.get(
								"occi.container.version").toString());
					}
					if (xoccimap.containsKey("occi.container.name")) {
						LOGGER.info((String) xoccimap
								.get("occi.container.name"));
						actionLink.setName((String) xoccimap
								.get("occi.container.name"));
					}

					// Location Rendering in HTTP Header, not in body
					setLocationRef(getRootRef().toString());

					// set response status
					getResponse().setStatus(Status.SUCCESS_OK);

					return Response.getCurrent().toString();
				} else {
					getResponse().setStatus(Status.SUCCESS_OK);
					return "Nothing changed";
				}
			}
			// Catch possible exceptions
		} catch (Exception e) {
			LOGGER.error("Exception caught: " + e.toString());
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.toString());
			return "Exception: " + e.getMessage() + "\n";
		}
		return " ";
	}
}

class UniformActionLinkhandler implements Uniform {
	private Representation representation;
	private Reference reference;

	UniformActionLinkhandler(Representation representation, Reference reference) {
		this.representation = representation;
		this.reference = reference;
		
	}

	public void handle(Request request, Response response) {
		try {
			String reconfigurationText = representation.getText();
			ReconfigurationAction reconfigurationAction = XMLTools
					.getReconfigurationActionFromXML(reconfigurationText);
			ActionLink actionLink = ActionLink.getActionLinkList().get(
					UUID.fromString(reference.getLastSegment()));
			actionLink.applyAction(reconfigurationAction);
		} catch (Exception e) {
			System.err.println("Uniform2 exception ActionLink");
		}
	}
}