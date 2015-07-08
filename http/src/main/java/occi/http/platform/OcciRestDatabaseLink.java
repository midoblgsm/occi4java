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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import occi.application.Application;
import occi.config.OcciConfig;
import occi.core.Kind;
import occi.core.Mixin;
import occi.http.check.OcciCheck;
import occi.infrastructure.Network;
import occi.infrastructure.Network.State;
import occi.platform.Container;
import occi.platform.Database;
import occi.platform.links.DatabaseLink;

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

public class OcciRestDatabaseLink extends ServerResource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestDatabaseLink.class);

	private final OcciCheck occiCheck = new OcciCheck();

	@Get
	public String getOCCIRequest() {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));

		Form requestHeaders = (Form) getRequest().getAttributes().get(
				"org.restlet.http.headers");
		LOGGER.debug("Current request: " + requestHeaders);

		String acceptCase = OcciCheck.checkCaseSensitivity(
				requestHeaders.toString()).get("accept");

		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		String stringID = getReference().getLastSegment().toString();
		UUID uuid = UUID.fromString(stringID);
		Map<UUID,DatabaseLink> dbLink = DatabaseLink.getDatabaselinkList();
		DatabaseLink datbaselink = dbLink.get(uuid);
		buffer.append("Category: datbaselink;");
		buffer.append("\t\t scheme=\""
				+ datbaselink.getLink().getKind().getScheme() + "\";");
		buffer.append("\r\n");
		buffer.append("\t\t class=\"kind\";\n");
		// buffer.append("\r\n");
		buffer2.append("X-OCCI-Attribute:");
		buffer2.append(" occi.datbaselink.state="
				+ datbaselink.getState() + " ");
		// buffer.append("Link: ");
		// buffer.append(networkinterface.getLink());
		buffer2.append(" source=/"
				+ datbaselink.getLink().getKind().getTerm() + "/"
				+ datbaselink.getLink().getId());
		buffer2.append(" target=/"
				+ datbaselink.getTarget().getKind().getTerm() + "/"
				+ datbaselink.getTarget().getId());

		buffer.append(buffer2.toString());
		// access the request headers and get the Accept attribute
		Representation representation = OcciCheck.checkContentType(
				requestHeaders, buffer, getResponse());
		// Check the accept header
		if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
			// generate header rendering
			occiCheck.setHeaderRendering(null, datbaselink, buffer
					.toString(), buffer2);

			// set right representation and status code
			getResponse().setEntity(representation);
			getResponse().setStatus(Status.SUCCESS_OK);
			return " ";
		}
		// set right representation and status code

		getResponse().setEntity(representation);
		getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
		return buffer.toString();

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
		LOGGER.debug("Incoming delete request at network");
		try {
			// get network resource that should be deleted
			DatabaseLink databaseLink = DatabaseLink.getDatabaselinkList()
					.get(UUID.fromString(getReference().getLastSegment()));
			// remove it from network resource list
			if (DatabaseLink.getDatabaselinkList().remove(UUID
					.fromString(databaseLink.getId().toString())) == null) {
				throw new NullPointerException(
						"There is no resource with the given ID");
			}
			// set network resource to null
			databaseLink = null;
			getResponse().setStatus(Status.SUCCESS_OK);
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID not found! " + e.toString()
					+ "\n DatabaseLink resource could not be deleted.";
		} catch (Exception e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return e.toString();
		}
	}

	/**
	 * Method to create a new datbaseLink instance.
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
		try {
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			LOGGER.debug("Current request: " + requestHeaders);
			String attributeCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("x-occi-attribute");
			String xocciattributes = requestHeaders.getValues(attributeCase)
					.replace(",", " ");
			LOGGER.debug("Media-Type: "
					+ requestHeaders.getFirstValue("accept", true));

			// OcciCheck.countColons(xocciattributes, 2);

			// split the single occi attributes and put it into a (key,value)
			// map
			LOGGER.debug("Raw X-OCCI Attributes: " + xocciattributes);
			StringTokenizer xoccilist = new StringTokenizer(xocciattributes);
			HashMap<String, Object> xoccimap = new HashMap<String, Object>();
			LOGGER.debug("Tokens in XOCCIList: " + xoccilist.countTokens());
			while (xoccilist.hasMoreTokens()) {
				String[] temp = xoccilist.nextToken().split("\\=");
				if (temp[0] != null && temp[1] != null) {
					LOGGER.debug(temp[0] + " " + temp[1] + "\n");
					xoccimap.put(temp[0], temp[1]);
				}
			}

			// put occi attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			buffer.append("occi.core.source=").append(
					xoccimap.get("occi.core.source"));
			buffer.append("occi.core.target=").append(
					xoccimap.get("occi.core.target"));
			buffer.append(" occi.databaselink.state=").append("inactive");

			Set<String> set = new HashSet<String>();
			set.add("summary: ");
			set.add(buffer.toString());
			set.add(requestHeaders.getFirstValue("scheme"));

			if (!requestHeaders.contains("Link")) {
				
				UUID sourceID = UUID.fromString(((String) xoccimap.get("occi.core.source"))
						.replaceFirst(".*/([^/?]+).*", "$1"));
				UUID targetID = UUID.fromString(((String) xoccimap.get("occi.core.target"))
						.replaceFirst(".*/([^/?]+).*", "$1"));

				Container con = (Container.getContainerList().containsKey(sourceID))?
						Container.getContainerList().get(sourceID):
							Container.getContainerList().get(targetID);
				Database db = (Database.getDatabaseList().containsKey(sourceID))?
						Database.getDatabaseList().get(sourceID):
							Database.getDatabaseList().get(targetID);
				
				if (con!=null&&db!=null){
				// create new DatabaseLink instance with the given
				// attributes
				DatabaseLink databaseLink = new DatabaseLink(
						occi.platform.links.DatabaseLink.State.inactive,
						con, db);
				databaseLink.setKind(new Kind(null, "databaselink",
						"databaselink", null));

				StringBuffer resource = new StringBuffer();
				resource.append("/").append(
						databaseLink.getKind().getTerm()).append("/");
				getRootRef().setPath(resource.toString());

				// check of the category
				if(xoccimap.get("occi.database.Category")!=null)
				if (!"databaselink".equalsIgnoreCase(xoccimap.get(
						"occi.databaselink.Category").toString())) {
					throw new IllegalArgumentException(
							"Illegal Category type: "
									+ xoccimap
											.get("occi.databaselink.Category"));
				}
				for (Mixin mixin : Mixin.getMixins()) {
					if (mixin.getEntities() != null) {
						if (mixin.getEntities().contains(databaseLink)) {
							buffer.append("Category: " + mixin.getTitle()
									+ "; scheme=\"" + mixin.getScheme()
									+ "\"; class=\"mixin\"");
						}
					}
				}
				// Check accept header
				if (requestHeaders.getFirstValue("accept", true).equals(
						"text/occi")
						|| requestHeaders.getFirstValue("content-type", true)
								.equals("text/occi")) {
					// Generate header rendering
					occiCheck.setHeaderRendering(null, databaseLink, buffer
							.toString(), null);
					getResponse().setEntity(representation);
					getResponse().setStatus(Status.SUCCESS_OK);
				}
				// Location Rendering in HTTP Header, not in body
				setLocationRef((getRootRef().toString() + databaseLink
						.getId()));
				representation = OcciCheck.checkContentType(requestHeaders,
						buffer, getResponse());
				getResponse().setEntity(representation);
				// set response status
				getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
				
				}
				else{
						getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						getResponse().setEntity("source ID or target ID does not exist", MediaType.TEXT_PLAIN);
				}
				return Response.getCurrent().toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
					e.toString());
			return "Exception caught: " + e.toString() + "\n";
		}
		return " ";
	}

	//TODO: est ce qu'uil ya des parameter qu'on peut MAJ?
	/**
	 * Edit the parameters of a given resource instance.
	 * 
	 * @param representation
	 * @return data of altered instance
	 * @throws Exception
	 */
	@Put
	public String putOCCIRequest(Representation representation) {
		try {
			// set occi version info
			getServerInfo().setAgent(
					OcciConfig.getInstance().config.getString("occi.version"));
			OcciCheck.isUUID(getReference().getLastSegment());
			DatabaseLink databaseLink = DatabaseLink.getDatabaselinkList().get(UUID
					.fromString(getReference().getLastSegment()));
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");
			LOGGER.debug("Raw Request Headers: " + requestHeaders);
			
			String attributeCase = OcciCheck.checkCaseSensitivity(
					requestHeaders.toString()).get("x-occi-attribute");
			String xocciattributes = requestHeaders.getValues(attributeCase)
					.replace(",", " ");

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
				while (xoccilist.hasMoreTokens()) {
					String[] temp = xoccilist.nextToken().split("\\=");
					if (temp.length > 1 && temp[0] != null && temp[1] != null) {
						xoccimap.put(temp[0], temp[1]);
					}
				}
				LOGGER.debug("X-OCCI-Map empty?: " + xoccimap.isEmpty());
				if (!xoccimap.isEmpty()) {
					// Change the network attribute if it is send by the request
	
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