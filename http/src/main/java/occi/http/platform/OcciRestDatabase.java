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
package occi.http.platform;

import java.net.URI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import occi.config.OcciConfig;
import occi.core.Link;
import occi.core.Mixin;
import occi.http.check.OcciCheck;
import occi.platform.database.actions.DeleteAction;
import occi.platform.Database;
import occi.platform.Database.Architecture;
import occi.platform.Database.State;
import occi.platform.database.actions.CreateAction;
import occi.platform.database.actions.RestartAction.Restart;
import occi.platform.database.actions.StartAction.Start;
import occi.platform.database.actions.StopAction.Stop;
import occi.platform.database.actions.BackupAction.Backup;
import occi.platform.Database.Type;

import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
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
 * @author nguyen_n
 *
 */
public class OcciRestDatabase extends ServerResource{
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(OcciRestDatabase.class);

	private final OcciCheck occiCheck = new OcciCheck();
	
	@Get
	public String treatingGETRequest(){
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
			// get the database instance by the given UUID
			Database database = Database.getDatabaseList().get(UUID
					.fromString(getReference().getLastSegment()));

			// put all attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			StringBuffer linkBuffer = new StringBuffer();
			buffer.append(" Category: ").append(database.getKind().getTerm())
					.append(" scheme= ").append(database.getKind().getScheme())
					.append(" class=\"kind\";").append(" Name: ")
					.append(database.getName()).append(" Type: ")
					.append(database.getType()).append(" Architecture: ")
					.append(database.getArchitecture()).append(" Version: ")
					.append(database.getVersion()).append(" State: ")
					.append(database.getState());

			for (Link l : database.getLinks()) {
				//TODO: replace NetworkInterface by DatabaseLink
				/*
				if (l instanceof NetworkInterface) {
					NetworkInterface networkInterface = (NetworkInterface) l;
					linkBuffer.append("</");
					linkBuffer.append(l.getLink().getTitle());
					linkBuffer.append("/");
					linkBuffer.append(l.getId());
					linkBuffer.append(">; ");
					linkBuffer.append("rel=\""
							+ l.getLink().getKind().getScheme());
					linkBuffer.append(l.getLink().getTitle());
					linkBuffer.append("\"");
					linkBuffer.append(" self=\"/link/\"");
					linkBuffer.append("networkinterface/");
					linkBuffer.append(networkInterface.getId() + "\";");
					linkBuffer.append(" category=\"");
					linkBuffer.append(l.getLink().getKind().getScheme()
							+ "networkinterface\";");
					linkBuffer.append(" occi.networkinterface.interface="
							+ networkInterface.getNetworkInterface());
					linkBuffer.append(" occi.networkinterface.mac="
							+ networkInterface.getMac());
					linkBuffer.append(" occi.networkinterface.state="
							+ networkInterface.getState());

				}*/
				buffer.append(linkBuffer);

				LOGGER.debug("Links: " + linkBuffer.toString());
			}

			// access the request headers and get the Accept attribute
			Representation representation = OcciCheck.checkContentType(
					requestHeaders, buffer, getResponse());
			// Check the accept header
			if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
				// generate header rendering
				this.occiCheck.setHeaderRendering(null, database,
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
	
	@Post
	public String treatingPOSTRequest(Representation representation)
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
				//LOGGER.info(requestHeaders.toString());
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
				
				// check of the category
				/*if (!"database".equalsIgnoreCase(xoccimap.get(
						"occi.database.Category").toString())) {
					throw new IllegalArgumentException(
							"Illegal Category type: "
									+ xoccimap.get("occi.database.Category"));
				}*/
				
				// Check if last part of the URI is not action
				if (!getReference().toString().contains("action")) {
					// put occi attributes into a buffer for the response
					StringBuffer buffer = new StringBuffer();
					buffer.append("occi.database.name=").append(
							xoccimap.get("occi.database.name"));
					buffer.append(" occi.database.type=").append(
							xoccimap.get("occi.database.type"));
					buffer.append(" occi.database.architecture=").append(
							xoccimap.get("occi.database.architecture"));
					buffer.append(" occi.database.version=").append(
							xoccimap.get("occi.database.version"));
					buffer.append(" occi.database.state=").append("unavailable");

					Set<String> set = new HashSet<String>();
					set.add("summary: ");
					set.add(buffer.toString());
					set.add(requestHeaders.getFirstValue("scheme"));
					LOGGER.debug("Attribute set: " + set.toString());					
					
					// create new Database instance with the given attributes
					
					Database database = new Database(
							(String) xoccimap.get("occi.database.name"),
							Type.valueOf((String) xoccimap.get("occi.database.type")),
							Architecture.valueOf((String) xoccimap
									.get("occi.database.architecture")),
							(String) xoccimap.get("occi.database.version"),
							State.unavailable,
							set);

					URI uri = new URI(database.getId().toString());
					// Create libvirt domain
					CreateAction createAction = new CreateAction();
					createAction.execute(uri, null);

					StringBuffer resource = new StringBuffer();
					resource.append("/").append(database.getKind().getTerm())
							.append("/");
					getRootRef().setPath(resource.toString());
					
					for (Mixin mixin : Mixin.getMixins()) {
						if (mixin.getEntities() != null) {
							if (mixin.getEntities().contains(database)) {
								buffer.append("Category: " + mixin.getTitle()
										+ "; scheme=\"" + mixin.getScheme()
										+ "\"; class=\"mixin\"");
							}
						}
					}
					LOGGER.debug("Database Uuid: " + database.getUuid());
					LOGGER.debug("Database Kind scheme: "
							+ database.getKind().getScheme());
					// Check accept header
					if (requestHeaders.getFirstValue(acceptCase)
							.equals("text/occi")
							|| requestHeaders.getFirstValue("content-type", true)
									.equals("text/occi")) {
						// Generate header rendering
						this.occiCheck.setHeaderRendering(null, database,
								buffer.toString(), null);
						getResponse().setEntity(representation);
						getResponse().setStatus(Status.SUCCESS_OK);
					}
					// Location Rendering in HTTP Header, not in body
					setLocationRef((getRootRef().toString() + database.getId()));
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
					// Get the database resource by the given UUID
					Database database = Database.getDatabaseList().get(id);

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
						// Call the Start action of the database resource
						if (actionName[1].equalsIgnoreCase("start")) {
							LOGGER.debug("Start Action called.");
							LOGGER.debug(xoccimap.toString());
							//LOGGER.info(location);
							database.getStart().execute(URI.create(location), Start.valueOf("start"));
									//Start.valueOf((String) xoccimap.get("method")));							
							// Set the current state of the database resource
							database.setState(State.available);
						}

						if (actionName[1].equalsIgnoreCase("stop")) {
							LOGGER.debug("Stop Action called.");
							// Call the Stop action of the database resource
							database.getStop().execute(URI.create(location), Stop.valueOf("stop"));
									//Stop.valueOf((String) xoccimap.get("method")));
							// Set the current state of the database resource
							database.setState(State.unavailable);
						}

						if (actionName[1].equalsIgnoreCase("restart")) {
							LOGGER.debug("Restart Action called.");
							// Call the Restart action of the database resource
							database.getRestart().execute(URI.create(location), Restart.valueOf("restart"));
									//Restart.valueOf((String) xoccimap.get("method")));
							// Set the current state of the database resource
							database.setState(State.available);
						}

						if (actionName[1].equalsIgnoreCase("backup")) {
							LOGGER.debug("Backup Action called.");
							// Call the Backup action of the database resource
							database.getBackup().execute(URI.create(location),  Backup.valueOf("backup"));
									//Backup.valueOf((String) xoccimap.get("method")));
							// Set the current state of the database resource
							//database.setState(State.suspended);
						}
						
						getResponse().setStatus(Status.SUCCESS_OK);
						getResponse().setEntity("occi.database.state = " + database.getState().toString(), MediaType.TEXT_PLAIN);

						return Response.getCurrent().toString();
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
	
	@Put
	public String treatingPUTRequest(Representation representation)
			throws Exception {
		try {
			// set occi version info
			getServerInfo().setAgent(
					OcciConfig.getInstance().config.getString("occi.version"));
			OcciCheck.isUUID(getReference().getLastSegment());
			Database database = Database.getDatabaseList().get(UUID
					.fromString(getReference().getLastSegment()));
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			LOGGER.debug("Raw Request Headers: " + requestHeaders);
			String caseAttributes = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("x-occi-attribute");
			String xocciattributes = "";
			xocciattributes = requestHeaders.getFirstValue(caseAttributes);

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
					// Change the database attribute if it is send by the request
					if (xoccimap.containsKey("occi.database.name")) {
						LOGGER.info((String) xoccimap
								.get("occi.database.name"));
						database.setName((String) xoccimap
								.get("occi.database.name"));
					}
					
					if (xoccimap.containsKey("occi.database.type")) {
						LOGGER.info((String) xoccimap
								.get("occi.database.type"));
						database.setType(Type
								.valueOf((String) xoccimap
										.get("occi.database.type")));
					}
					
					if (xoccimap.containsKey("occi.database.architecture")) {
						LOGGER.info((String) xoccimap
								.get("occi.database.architecture"));
						database.setArchitecture(Architecture
								.valueOf((String) xoccimap
										.get("occi.database.architecture")));
					}
					
					if (xoccimap.containsKey("occi.database.version")) {
						LOGGER.info((String) xoccimap
								.get("occi.database.version"));
						database.setVersion((String) xoccimap
								.get("occi.database.version"));
					}
					
					if (xoccimap.containsKey("occi.database.state")) {
						LOGGER.info((String) xoccimap.get("occi.database.state"));
						database.setState(State.valueOf((String) xoccimap
								.get("occi.database.state")));
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
		return "";
	}
	
	@Delete
	public String treatingDELETERequest(){
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		LOGGER.debug("Incoming delete request");
		try {
			OcciCheck.isUUID(getReference().getLastSegment());
			// get database resource that should be deleted
			Database database = Database.getDatabaseList().get(UUID
					.fromString(getReference().getLastSegment()));
			DeleteAction deleteAction = new DeleteAction();
			deleteAction.execute(new URI(database.getId().toString()), null);
			// remove it from database resource list
			if (Database.getDatabaseList().remove(UUID.fromString(database.getId()
					.toString())) == null) {
				throw new Exception("There is no resorce with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set database resource to null
			database = null;
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString()
					+ "\n Database resource could not be deleted.";
			// Exception for isUUID method
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return e.toString();
		}
	}

}
