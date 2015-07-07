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

import occi.config.OcciConfig;
import occi.http.check.OcciCheck;

import org.restlet.Response;
import org.restlet.data.Form;
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

import telecom.sudparis.occi.autonomic.AutonomicManager;
import telecom.sudparis.occi.autonomic.AutonomicManager.State;
import telecom.sudparis.occi.autonomic.autonomicmanager.actions.DeleteAction;
import telecom.sudparis.occi.autonomic.impl.SpecificSLAMixinToolImpl;
import telecom.sudparis.occi.autonomic.interfaces.SpecificSLAMixinInterface;

public class OcciRestAutonomicManager extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestAutonomicManager.class);

	private final OcciCheck occiCheck = new OcciCheck();

	/**
	 * Method to create a new container instance.
	 * 
	 * @param representation
	 * @return string
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Post
	public String postOCCIRequest(Representation representation)
			throws Exception {
		LOGGER.info("Incoming POST request.");
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		HashMap<String, Object> xoccimap = new HashMap<String, Object>();
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

			OcciCheck.countColons(xocciattributes, 1);

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
			if (!getReference().toString().contains("action")) {
				// put occi attributes into a buffer for the response
				StringBuffer buffer = new StringBuffer();

				buffer.append("occi.autonomicmanager.name=").append(
						xoccimap.get("occi.autonomicmanager.name"));
				buffer.append(" occi.autonomicmanager.version=").append(
						xoccimap.get("occi.autonomicmanager.version"));
				buffer.append(" occi.autonomicmanager.slalocation=").append(
						xoccimap.get("occi.autonomicmanager.slalocation"));
				buffer.append(" occi.autonomicmanager.state=").append(
						"unavailable");

				Set<String> set = new HashSet<String>();
				set.add("summary: ");
				set.add(buffer.toString());
				set.add(requestHeaders.getFirstValue("scheme"));
				LOGGER.debug("Attribute set: " + set.toString());

				// create new container instance with the given attributes

				// TODO Let the mixin4j do it

				DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();

				Mixin<AutonomicManager> autonomicManagerWithMixin = new Mixin(
						SpecificSLAMixinInterface.class,
						SpecificSLAMixinToolImpl.class, AutonomicManager.class);
				Factory factory = dynamicMixer.getFactory(
						AutonomicManager.class, autonomicManagerWithMixin);
				Class[] clazz0 = { String.class, String.class, String.class,
						State.class, Set.class };
				Object[] objects0 = {
						(String) xoccimap.get("occi.autonomicmanager.name"),
						(String) xoccimap.get("occi.autonomicmanager.version"),
						(String) xoccimap
								.get("occi.autonomicmanager.slalocation"),
						State.unavailable, set };
				AutonomicManager autonomicManager = (AutonomicManager) factory
						.newInstance(clazz0, objects0);
				autonomicManager.establishAutonomicLoop();
			//	URI uri = new URI(autonomicManager.getId().toString());
				
				

				StringBuffer resource = new StringBuffer();
				resource.append("/")
						.append(autonomicManager.getKind().getTerm())
						.append("/");
				getRootRef().setPath(resource.toString());

				// check of the category
				if (xoccimap.get("occi.autonomicmanager.Category") != null)
					if (!"autonomicmanager".equalsIgnoreCase(xoccimap.get(
							"occi.autonomicmanager.Category").toString())) {
						throw new IllegalArgumentException(
								"Illegal Category type: "
										+ xoccimap
												.get("occi.autonomicManager.Category"));
					}
				// for (Mixin mixin : Mixin.getMixins()) {
				// if (mixin.getEntities() != null) {
				// if (mixin.getEntities().contains(autonomicManager)) {
				// buffer.append("Category: " + mixin.getTitle()
				// + "; scheme=\"" + mixin.getScheme()
				// + "\"; class=\"mixin\"");
				// }
				// }
				// }
				LOGGER.debug("AutonomicManager Uuid: "
						+ autonomicManager.getUuid());
				LOGGER.debug("AutonomicManager Kind scheme: "
						+ autonomicManager.getKind().getScheme());
				// Check accept header
				if (requestHeaders.getFirstValue(acceptCase)
						.equals("text/occi")
						|| requestHeaders.getFirstValue("content-type", true)
								.equals("text/occi")) {
					// Generate header rendering
					this.occiCheck.setHeaderRendering(null, autonomicManager,
							buffer.toString(), null);
					getResponse().setEntity(representation);
					getResponse().setStatus(Status.SUCCESS_OK);
				}
				// Location Rendering in HTTP Header, not in body
				setLocationRef((getRootRef().toString() + autonomicManager
						.getId()));
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
				AutonomicManager autonomicManager = AutonomicManager
						.getAutonomicManagerList().get(id);

				String location = "http:"
						+ getReference().getHierarchicalPart();

				// Extract the action type / name from the last part of the
				// given
				// location URI and split it after the "=" (../?action=stop)
				String[] actionName = getReference()
						.getRemainingPart()
						.subSequence(1,
								getReference().getRemainingPart().length())
						.toString().split("\\=");
				LOGGER.debug("Action Name: " + actionName[1]);

				// Check if actionName[1] is set
				if (actionName.length >= 2) {
					// Call the Start action of the container resource
					if (actionName[1].equalsIgnoreCase("start")) {
						LOGGER.debug("Start Action called.");
						LOGGER.debug(xoccimap.toString());
						autonomicManager.getStart().execute(
								URI.create(location), null);
						// Set the current state of the container resource
						autonomicManager.setState(State.available);
					}

					if (actionName[1].equalsIgnoreCase("stop")) {
						LOGGER.debug("Stop Action called.");
						// Call the Stop action of the container resource
						autonomicManager.getStop().execute(
								URI.create(location), null);
						// Set the current state of the container resource
						autonomicManager.setState(State.unavailable);
					}

					if (actionName[1].equalsIgnoreCase("restart")) {
						LOGGER.debug("Restart Action called.");
						// Call the Restart action of the container resource
						autonomicManager.getRestart().execute(
								URI.create(location), null);
						// Set the current state of the container resource
						autonomicManager.setState(State.available);
					}
				}
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
			AutonomicManager autonomicManager = AutonomicManager
					.getAutonomicManagerList().get(
							UUID.fromString(getReference().getLastSegment()));

			// put all attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			StringBuffer linkBuffer = new StringBuffer();
			buffer.append(" Category: ")
					.append(autonomicManager.getKind().getTerm())
					.append(" scheme= ")
					.append(autonomicManager.getKind().getScheme())
					.append(" class=\"kind\";").append(" Name: ")
					.append(autonomicManager.getName()).append(" Version: ")
					.append(autonomicManager.getState());

			// TODO: see infrastructure to display links

			// access the request headers and get the Accept attribute
			Representation representation = OcciCheck.checkContentType(
					requestHeaders, buffer, getResponse());
			// Check the accept header
			if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
				// generate header rendering
				this.occiCheck.setHeaderRendering(null, autonomicManager,
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
			AutonomicManager autonomicManager = AutonomicManager
					.getAutonomicManagerList().get(
							UUID.fromString(getReference().getLastSegment()));
			DeleteAction deleteAction = new DeleteAction();
			deleteAction.execute(new URI(autonomicManager.getId().toString()),
					null);
			// remove it from container resource list
			if (AutonomicManager.getAutonomicManagerList().remove(
					UUID.fromString(autonomicManager.getId().toString())) == null) {
				throw new Exception("There is no resorce with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set container resource to null
			autonomicManager = null;
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString()
					+ "\n Container resource could not be deleted.";
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
	@SuppressWarnings("unused")
	@Put
	public String putOCCIRequest(Representation representation)
			throws Exception {
		try {
			// set occi version info
			getServerInfo().setAgent(
					OcciConfig.getInstance().config.getString("occi.version"));
			OcciCheck.isUUID(getReference().getLastSegment());
			AutonomicManager autonomicManager = AutonomicManager
					.getAutonomicManagerList().get(
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

					if (xoccimap
							.containsKey("occi.autonomicmanager.architecture")) {
						LOGGER.info((String) xoccimap
								.get("occi.autonomicmanager.architecture"));

					}
					if (xoccimap.containsKey("occi.autonomicmanager.version")) {
						LOGGER.info((String) xoccimap
								.get("occi.autonomicmanager.version"));
						autonomicManager.setVersion(xoccimap.get(
								"occi.container.version").toString());
					}
					if (xoccimap.containsKey("occi.autonomicmanager.name")) {
						LOGGER.info((String) xoccimap
								.get("occi.autonomicmanager.name"));
						autonomicManager.setName((String) xoccimap
								.get("occi.autonomicmanager.name"));
					}
					if (xoccimap
							.containsKey("occi.autonomicmanager.slalocation")) {
						LOGGER.info((String) xoccimap
								.get("occi.autonomicmanager.slalocation"));
						autonomicManager.setSlaLocation((String) xoccimap
								.get("occi.autonomicmanager.slalocation"));
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