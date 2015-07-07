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
package occi.http.application;

/**
 * Copyright (C) 2010-2011 Sebastian Heckmann, Sebastian Laag
 *
 * Contact Email: <sebastian.heckmann@udo.edu>, <sebastian.laag@udo.edu>
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
import java.net.URI;

import occi.http.check.OcciCheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import occi.application.Environment;
import occi.application.Environment.State;
import occi.application.environment.actions.CreateAction;
import occi.application.environment.actions.DeleteAction;
import occi.application.manifest.ManifestManagement;
import occi.platform.Container;
import occi.platform.Database;
import occi.platform.links.DatabaseLink;
import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Link;
import occi.core.Mixin;

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

/**
 * 
 * @author Sami Yangui
 */
public class OcciRestEnvironment extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestEnvironment.class);

	private final OcciCheck occiCheck = new OcciCheck();

	/**
	 * Method to create a new Environment instance.
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

			//OcciCheck.countColons(xocciattributes, 1);

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

				buffer.append("occi.environment.name=").append(
						xoccimap.get("occi.environment.name"));
				buffer.append(" occi.environment.description=").append(
						xoccimap.get("occi.environment.description"));
				buffer.append(" occi.environment.memory=").append(
						xoccimap.get("occi.environment.memory"));
				buffer.append(" occi.environment.variables=").append(
						xoccimap.get("occi.environment.variables"));
				buffer.append(" occi.environment.containersList=").append(
						xoccimap.get("occi.environment.containersList"));
				buffer.append(" occi.environment.databasesList=").append(
						xoccimap.get("occi.environment.databasesList"));
				buffer.append(" occi.environment.databasesLink=").append(
						xoccimap.get("occi.environment.databasesLink"));
				buffer.append(" occi.environment.state=").append("unavailable");

				Set<String> set = new HashSet<String>();
				set.add("summary: ");
				set.add(buffer.toString());
				set.add(requestHeaders.getFirstValue("scheme"));
				LOGGER.debug("Attribute set: " + set.toString());

				HashMap<UUID, Container> containerList = buildContainerMap((String) xoccimap
						.get("occi.environment.containersList"));

				if (containerList != null) {
					HashMap<UUID, Database> databaseList = buildDatabaseMap((String) xoccimap
							.get("occi.environment.databasesList"));
					HashMap<UUID, DatabaseLink> databaseLinkList = buildDatabaseLinkMap((String) xoccimap
							.get("occi.environment.databasesLink"));
					HashMap<String, String> variables = buildVariablesMap((String) xoccimap
							.get("occi.environment.variables"));

					// create new Environment instance with the given attributes
					Environment environment = new Environment(
							(String) xoccimap.get("occi.environment.name"),
							(String) xoccimap
									.get("occi.environment.description"),
							Integer.parseInt((String) xoccimap
									.get("occi.environment.memory")),
							variables,containerList,
							databaseList,databaseLinkList,
							State.unvailable);

					ManifestManagement.createEnvironmentManifest(environment);
										
					URI uri = new URI(environment.getId().toString());
					// Create libvirt domain
					CreateAction createAction = new CreateAction();
					createAction.execute(uri, null);

					StringBuffer resource = new StringBuffer();
					resource.append("/")
							.append(environment.getKind().getTerm())
							.append("/");
					getRootRef().setPath(resource.toString());

					// check of the category
					if (xoccimap.get("occi.environment.Category") != null)
						if (!"environment".equalsIgnoreCase(xoccimap.get(
								"occi.environment.Category").toString())) {
							throw new IllegalArgumentException(
									"Illegal Category type: "
											+ xoccimap
													.get("occi.environment.Category"));
						}
					for (Mixin mixin : Mixin.getMixins()) {
						if (mixin.getEntities() != null) {
							if (mixin.getEntities().contains(environment)) {
								buffer.append("Category: " + mixin.getTitle()
										+ "; scheme=\"" + mixin.getScheme()
										+ "\"; class=\"mixin\"");
							}
						}
					}
					LOGGER.debug("Environment Uuid: " + environment.getUuid());
					LOGGER.debug("Environment Kind scheme: "
							+ environment.getKind().getScheme());
					// Check accept header
					if (requestHeaders.getFirstValue(acceptCase).equals(
							"text/occi")
							|| requestHeaders.getFirstValue("content-type",
									true).equals("text/occi")) {
						// Generate header rendering
						this.occiCheck.setHeaderRendering(null, environment,
								buffer.toString(), null);
						getResponse().setEntity(representation);
						getResponse().setStatus(Status.SUCCESS_OK);
					}
					// Location Rendering in HTTP Header, not in body
					setLocationRef((getRootRef().toString() + environment
							.getId()));
					representation = OcciCheck.checkContentType(requestHeaders,
							buffer, getResponse());
					getResponse().setEntity(representation);
					// set response status
					getResponse().setStatus(Status.SUCCESS_OK,
							buffer.toString());
					return Response.getCurrent().toString();
				}
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
				// Get the application resource by the given UUID
				Environment environment = Environment.getEnvironmentList().get(
						id);

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
						environment.getStart().execute(URI.create(location),
								null);
						// Set the current state of the Environment resource
						environment.setState(State.available);
					}

					if (actionName[1].equalsIgnoreCase("stop")) {
						LOGGER.debug("Stop Action called.");
						// Call the Stop action of the Environment resource
						environment.getStop().execute(URI.create(location),
								null);
						// Set the current state of the Environment resource
						environment.setState(State.unvailable);
					}

					if (actionName[1].equalsIgnoreCase("restart")) {
						LOGGER.debug("Restart Action called.");
						// Call the Restart action of the Environment resource
						environment.getRestart().execute(URI.create(location),
								null);
						// Set the current state of the Environment resource
						environment.setState(State.available);
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
			// get the Environment instance by the given UUID
			Environment environment = Environment.getEnvironmentList().get(
					UUID.fromString(getReference().getLastSegment()));

			// put all attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			StringBuffer linkBuffer = new StringBuffer();
			buffer.append(" Category: ")
					.append(environment.getKind().getTerm())
					.append(" scheme= ")
					.append(environment.getKind().getScheme())
					.append(" class=\"kind\";").append(" Name: ")
					.append(environment.getName()).append(" Description: ")
					.append(environment.getDescription()).append(" Memory: ")
					.append(environment.getMemory()).append(" Variables: ")
					.append(environment.getVariables())
					.append(" List of Containers: ")
					.append(environment.getContainersList())
					.append(" List of Databases: ")
					.append(environment.getDatabasesList())
					.append(" List of Databases Link: ")
					.append(environment.getDatabasesLink()).append(" State: ")
					.append(environment.getState());

			// access the request headers and get the Accept attribute
			Representation representation = OcciCheck.checkContentType(
					requestHeaders, buffer, getResponse());
			// Check the accept header
			if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
				// generate header rendering
				this.occiCheck.setHeaderRendering(null, environment,
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
			// get Environment resource that should be deleted
			Environment environment = Environment.getEnvironmentList().get(
					UUID.fromString(getReference().getLastSegment()));
			DeleteAction deleteAction = new DeleteAction();
			deleteAction.execute(new URI(environment.getId().toString()), null);
			// remove it from Environment resource list
			if (Environment.getEnvironmentList().remove(
					UUID.fromString(environment.getId().toString())) == null) {
				throw new Exception("There is no resource with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set Environment resource to null
			environment = null;
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString()
					+ "\n Environment resource could not be deleted.";
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
	@SuppressWarnings("unchecked")
	@Put
	public String putOCCIRequest(Representation representation)
			throws Exception {
		try {
			// set occi version info
			getServerInfo().setAgent(
					OcciConfig.getInstance().config.getString("occi.version"));
			OcciCheck.isUUID(getReference().getLastSegment());
			Environment environment = Environment.getEnvironmentList().get(
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
					// Change the Environment attribute if it is send by the
					// request

					if (xoccimap.containsKey("occi.environment.name")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.name"));
						environment.setName((String) xoccimap
								.get("occi.environment.name"));
					}
					if (xoccimap.containsKey("occi.environment.description")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.description"));
						environment.setDescription((String) xoccimap
								.get("occi.environment.description"));
					}
					if (xoccimap.containsKey("occi.environment.memory")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.memory"));
						environment.setMemory(Integer.parseInt((String) xoccimap
								.get("occi.environment.memory")));
					}
					if (xoccimap.containsKey("occi.environment.variables")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.variables"));
						environment.setVariables(buildVariablesMap((String) xoccimap
								.get("occi.environment.variables")));
					}
					if (xoccimap.containsKey("occi.environment.containersList")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.containersList"));
						environment
								.setContainersList(buildContainerMap((String) xoccimap
										.get("occi.environment.containersList")));
					}
					if (xoccimap.containsKey("occi.environment.databasesList")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.databasesList"));
						environment
								.setDatabasesList(buildDatabaseMap((String) xoccimap
										.get("occi.environment.databasesList")));
					}
					if (xoccimap.containsKey("occi.environment.databasesLink")) {
						LOGGER.info((String) xoccimap
								.get("occi.environment.databasesLink"));
						environment
								.setDatabasesLink(buildDatabaseLinkMap((String) xoccimap
										.get("occi.environment.databasesLink")));
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

	private HashMap<UUID, Container> buildContainerMap(String input) {
		if (Container.getContainerList().isEmpty())
			return new HashMap<UUID, Container>();
		HashMap<UUID, Container> map = new HashMap<UUID, Container>();
		String[] listURIs = input.split("&");
		for (String uri : listURIs)
		{
			UUID id = UUID.fromString(uri.replaceFirst(".*/([^/?]+).*", "$1"));
			if (!Container.getContainerList().containsKey(id))
				return new HashMap<UUID, Container>();
			else
				map.put(id,	Container.getContainerList().get(id));
		}
		return map;
	}

	private HashMap<UUID, Database> buildDatabaseMap(String input) {
		if (Database.getDatabaseList() == null)
			return new HashMap<UUID, Database>();
		HashMap<UUID, Database> map = new HashMap<UUID, Database>();
		String[] listURIs = input.split("&");
		for (String uri : listURIs)
		{
			UUID id = UUID.fromString(uri.replaceFirst(".*/([^/?]+).*", "$1"));
			if (!Database.getDatabaseList().containsKey(id))
				return new HashMap<UUID, Database>();
			else
				map.put(id,	Database.getDatabaseList().get(id));
		}
		return map;
	}

	private HashMap<UUID, DatabaseLink> buildDatabaseLinkMap(String input) {
		if (DatabaseLink.getDatabaselinkList() == null)
			return new HashMap<UUID, DatabaseLink>();
		HashMap<UUID, DatabaseLink> map = new HashMap<UUID, DatabaseLink>();
		String[] listURIs = input.split("&");
		for (String uri : listURIs)
		{
			UUID id = UUID.fromString(uri.replaceFirst(".*/([^/?]+).*", "$1"));
			if (!DatabaseLink.getDatabaselinkList().containsKey(id))
				return new HashMap<UUID, DatabaseLink>();
			else
				map.put(id, DatabaseLink.getDatabaselinkList()
						.get(id));
		}
		return map;
	}
	
	private HashMap<String, String> buildVariablesMap(String input) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] listVariables = input.split("&");
		for (String variable : listVariables){
			String [] option = variable.split("\\|");
			map.put(option[0], option[1]); //to check
		}
		return map;
	}
}