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

import java.util.Map;
import java.util.UUID;

import occi.config.OcciConfig;
import occi.http.check.OcciCheck;



import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import telecom.sudparis.occi.autonomic.AlertLink;

/**
 * Interface for all container resources. Only HTTP GET Request is possible. A GET
 * request returns all available container resources.
 * 
 * @author Sebastian Heckmann
 * @author Sebastian Laag
 */
public class OcciRestAlertLinks extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestAlertLinks.class);

	/**
	 * Returns all AlertLink resources.
	 * 
	 * @return AlertLink List String
	 */
	@Get
	public String getOCCIRequest(Representation representation) {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));

		// initialize container list
		Map<UUID, AlertLink> alertLinkList = AlertLink.getAlertLinkList();
		// initialize buffer
		StringBuffer buffer = new StringBuffer();
		AlertLink alertLink = null;

		// Access the request Headers
		Form requestHeaders = (Form) getRequest().getAttributes().get(
				"org.restlet.http.headers");
		LOGGER.debug("Raw Request Headers: " + requestHeaders);

		LOGGER.debug("Size of AlertLink"
				+ " list: " + alertLinkList.size());
		// iterate through all available container resources
		int i = 1;
		for (UUID id : alertLinkList.keySet()) {
			alertLink = alertLinkList.get(id);
			buffer.append(getReference());
			buffer.append(alertLink.getId());
			if (i < alertLinkList.size()) {
				buffer.append(",");
			}
			i++;
		}

		representation = OcciCheck.checkContentType(requestHeaders, buffer,
				getResponse());
		getResponse().setEntity(representation);
		if (alertLinkList.size() <= 0) {
			// return http status code
			getResponse().setStatus(Status.SUCCESS_NO_CONTENT,
					buffer.toString());
			return "There are no AlertLink resources";
		} else if (representation.getMediaType().toString().equals("text/occi")) {
			// Set Location Attribute
			setLocationRef(buffer.toString());
			// return http status code
			getResponse().setStatus(Status.SUCCESS_OK, " ");
		} else {
			// return http status code
			getResponse().setStatus(Status.SUCCESS_OK);
		}
		return " ";
	}

	/**
	 * Changes the state of a specific container resource and calls the related
	 * actions for it.
	 * 
	 * @return String
	 * @throws Exception
	 */
//	@Post
//	public String postOCCIRequest() throws Exception {
//		try {
//			LOGGER.info("Incoming POST request.");
//			// set occi version info
//			getServerInfo().setAgent(
//					OcciConfig.getInstance().config.getString("occi.version"));
//
//			// OcciCheck.isUUID(getReference().getLastSegment());
//			Form requestHeaders = (Form) getRequest().getAttributes().get(
//					"org.restlet.http.headers");
//			LOGGER.debug("Current request: " + requestHeaders);
//			String attributeCase = OcciCheck.checkCaseSensitivity(
//					requestHeaders.toString()).get("x-occi-attribute");
//			String xocciattributes = requestHeaders.getValues(attributeCase)
//					.replace(",", " ");
//			String linkAttributes = "";
//			StringTokenizer linkList = null;
//			if (requestHeaders.toString().contains("Link")
//					|| requestHeaders.toString().contains("link")) {
//				linkAttributes = requestHeaders.getFirstValue("link", true);
//				linkList = new StringTokenizer(linkAttributes);
//				LOGGER.debug("Tokens in LinkList: " + linkList.countTokens());
//
//			}
//
//			LOGGER.debug("Media-Type: "
//					+ requestHeaders.getFirstValue("accept", true));
//			HashMap<String, Object> xoccimap = new HashMap<String, Object>();
//
//			LOGGER.debug("Raw X-OCCI Attributes: " + xocciattributes);
//			StringTokenizer xoccilist = new StringTokenizer(xocciattributes);
//
//			// Obsolet
//			// OcciCheck.countColons(xocciattributes, 2);
//
//			// split the single occi attributes and put it into a (key,value)
//			// map
//			LOGGER.debug("Raw X-OCCI Attributes: " + xocciattributes);
//			LOGGER.debug("RAW Link Attributes: " + linkAttributes);
//			LOGGER.debug("Tokens in XOCCIList: " + xoccilist.countTokens());
//
//			while (xoccilist.hasMoreTokens()) {
//				String[] temp = xoccilist.nextToken().split("\\=");
//				if (temp[0] != null && temp[1] != null) {
//					LOGGER.debug(temp[0] + " " + temp[1] + "\n");
//					xoccimap.put(temp[0], temp[1]);
//				}
//			}
//			if (linkList != null) {
//				while (linkList.hasMoreTokens()) {
//					String[] temp = linkList.nextToken().split("\\=");
//					if (temp.length > 1) {
//						if (temp[0] != null && temp[1] != null) {
//							LOGGER.debug(temp[0] + " " + temp[1] + "\n");
//							xoccimap.put(temp[0], temp[1]);
//						}
//					} else {
//						xoccimap.put("Link", temp[0]);
//					}
//				}
//			}
//
//			// Several possibilities to create links between resources
//
//			/*
//			 * to create a container resource and create a link to a existing
//			 * network.
//			 */
//
//			if (requestHeaders.toString().contains("Link")
//					&& requestHeaders.toString().contains("Category")) {
//				getResponse().setLocationRef(
//						createContainerResourceAndNetworkInterface(
//								requestHeaders, xoccimap, xoccilist));
//
//			}
//			// to create a link between a existing Network and Container Resource.
//			LOGGER.debug(requestHeaders.getFirstValue("Category", true)
//					.contains("networkinterface")
//					+ "");
//			LOGGER.debug(requestHeaders.getFirstValue("Category", true));
//			if (requestHeaders.getFirstValue("category", true).contains(
//					"networkinterface")) {
//				LOGGER.debug(requestHeaders.toString());
//				LOGGER.debug(xoccimap.toString());
//				LOGGER.debug(xoccilist.toString());
//				LOGGER.debug("getResponse() "
//						+ getRequest().getHostRef().toString());
//				getResponse().setLocationRef(
//						getRequest().getHostRef()
//								+ "/networkinterface/"
//								+ createLinkBetweenNetworkAndContainerResource(
//										requestHeaders, xoccimap, xoccilist));
//			}
//
//			for (UUID id : Container.getContainerList().keySet()) {
//				Container container = Container.getContainerList().get(id);
//				String location = "http:"
//						+ getReference().getHierarchicalPart()
//						+ container.getId();
//				// check if the URI contains a action
//				if (getReference().getRemainingPart().contains("action")) {
//
//					String[] actionName = getReference().getRemainingPart()
//							.subSequence(1,
//									getReference().getRemainingPart().length())
//							.toString().split("\\=");
//					LOGGER.debug("Action Name: " + actionName[1]);
//
//					// Check if actionName[1] is set
//					if (actionName.length >= 2) {
//						// Call the Start action of the container resource
//						if (actionName[1].equalsIgnoreCase("start")) {
//							LOGGER.debug("Start Action called.");
//							container.getStart().execute(URI.create(location), Start
//									.valueOf((String) xoccimap.get("method")));
//							// Set the current state of the container resource
//							container.setState(State.active);
//						}
//
//						if (actionName[1].equalsIgnoreCase("stop")) {
//							LOGGER.debug("Stop Action called.");
//							// Call the Stop action of the container resource
//							container.getStop().execute(URI.create(location), Stop
//									.valueOf((String) xoccimap.get("method")));
//							// Set the current state of the container resource
//							container.setState(State.inactive);
//						}
//
//						if (actionName[1].equalsIgnoreCase("restart")) {
//							LOGGER.debug("Restart Action called.");
//							// Call the Restart action of the container resource
//							container.getRestart().execute(URI.create(location),
//									Restart.valueOf((String) xoccimap
//											.get("method")));
//							// Set the current state of the container resource
//							container.setState(State.active);
//						}
//
//						if (actionName[1].equalsIgnoreCase("suspend")) {
//							LOGGER.debug("Suspend Action called.");
//							// Call the Suspend action of the container resource
//							container.getSuspend().execute(URI.create(location),
//									Suspend.valueOf((String) xoccimap
//											.get("method")));
//							// Set the current state of the container resource
//							container.setState(State.suspended);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error("Exception caught: " + e.toString());
//			e.printStackTrace();
//			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
//					e.toString());
//			return "Exception: " + e.getMessage() + "\n";
//		}
//		return " ";
//	}
//
//	/**
//	 * Method to create a link between a existing Network and Container Resource.
//	 * 
//	 * @param requestHeaders
//	 * @param xoccimap
//	 * @param xoccilist
//	 * @throws URISyntaxException
//	 * @throws SchemaViolationException
//	 */
//	private String createLinkBetweenNetworkAndContainerResource(
//			Form requestHeaders, HashMap<String, Object> xoccimap,
//			StringTokenizer xoccilist) throws URISyntaxException,
//			SchemaViolationException {
//		String source = xoccimap.get("source").toString();
//		String sourceUUID = source.substring(source.length() - 36, source
//				.length());
//		String target = xoccimap.get("target").toString();
//		String targetUUID = target.substring(target.length() - 36, target
//				.length());
//
//		UUID sourceUuid = UUID.fromString(sourceUUID);
//		UUID targetUuid = UUID.fromString(targetUUID);
//		if (!xoccimap.containsKey("occi.networkinterface.mac")) {
//			xoccimap.put("occi.networkinterface.mac", "54:52:00:11:22:33");
//		}
//
//		if (!xoccimap.containsKey("occi.networkinterface.networkinterface")) {
//			xoccimap.put("occi.networkinterface.networkinterface",
//					"networkinterface");
//		}
//
//		if (OcciCheck.isUUID(sourceUuid.toString())
//				&& OcciCheck.isUUID(targetUuid.toString())) {
//
//			Container sourceContainer = Container.getContainerList().get(sourceUuid);
//			Network targetNetwork = Network.getNetworkList().get(targetUuid);
//			NetworkInterface networkInterface = new NetworkInterface(xoccimap
//					.get("occi.networkinterface.networkinterface").toString(),
//					xoccimap.get("occi.networkinterface.mac").toString(),
//					occi.infrastructure.links.NetworkInterface.State.inactive,
//					sourceContainer, targetNetwork);
//			networkInterface.setKind(new Kind(null, "networkinterface",
//					"networkinterface", null));
//
//			// Link linked = new Link(sourceContainer, targetNetwork);
//			// sourceContainer.getLinks().add(linked);
//			// targetNetwork.getLinks().add(linked);
//			LOGGER.debug("LINK URI: " + networkInterface.getId());
//			return networkInterface.getId().toString();
//		}
//		return " ";
//
//	}
//
//	/**
//	 * Method to create a container resource and create a link to a existing
//	 * network.
//	 * 
//	 * @param requestHeaders
//	 * @param xoccimap
//	 * @param xoccilist
//	 */
//	private String createContainerResourceAndNetworkInterface(
//			Form requestHeaders, HashMap<String, Object> xoccimap,
//			StringTokenizer xoccilist) {
//		// TODO siehe Spezifikation
//		// split the single occi attributes and put it into a
//		// (key,value) map
//		try {
//			LOGGER.debug("Tokens in XOCCIList: " + xoccilist.countTokens());
//			while (xoccilist.hasMoreTokens()) {
//				String[] temp = xoccilist.nextToken().split("\\=");
//				if (temp[0] != null && temp[1] != null) {
//					LOGGER.debug(temp[0] + " " + temp[1] + "\n");
//					xoccimap.put(temp[0], temp[1]);
//				}
//			}
//			// Check if last part of the URI is not action
//			if (!getReference().toString().contains("action")) {
//				// put occi attributes into a buffer for the response
//				StringBuffer buffer = new StringBuffer();
//				buffer.append("occi.container.architecture=").append(
//						xoccimap.get("occi.container.architecture"));
//				buffer.append(" occi.container.cores=").append(
//						xoccimap.get("occi.container.cores"));
//				buffer.append(" occi.container.hostname=").append(
//						xoccimap.get("occi.container.hostname"));
//				buffer.append(" occi.container.speed=").append(
//						xoccimap.get("occi.container.speed"));
//				buffer.append(" occi.container.memory=").append(
//						xoccimap.get("occi.container.memory"));
//				buffer.append(" occi.container.state=").append(
//						xoccimap.get("occi.container.state"));
//
//				Set<String> set = new HashSet<String>();
//				set.add("summary: ");
//				set.add(buffer.toString());
//				set.add(requestHeaders.getFirstValue("scheme"));
//				LOGGER.debug("Attribute set: " + set.toString());
//
//				// create new Container instance with the given attributes
//				Container container = new Container(Architecture
//						.valueOf((String) xoccimap
//								.get("occi.container.architecture")), Integer
//						.parseInt((String) xoccimap.get("occi.container.cores")),
//						(String) xoccimap.get("occi.container.hostname"), Float
//								.parseFloat((String) xoccimap
//										.get("occi.container.speed")), Float
//								.parseFloat((String) xoccimap
//										.get("occi.container.memory")),
//						State.inactive, set);
//
//				// Get the Link UUID
//				String link = requestHeaders.getFirstValue("link", true);
//				String[] linkUUID = link.replaceAll("</[\\w]+/", "")
//						.split(">;");
//				LOGGER.debug("RegEx'ed UUID: " + linkUUID[0]);
//
//				NetworkInterface network = new NetworkInterface(
//						xoccimap.get("occi.networkinterface.interface")
//								.toString(),
//						xoccimap.get("occi.networkinterface.mac").toString(),
//						occi.infrastructure.links.NetworkInterface.State.inactive,
//						Network.getNetworkList().get(
//								UUID.fromString(linkUUID[0])), container);
//
//				// network.setId(URI.create(linkUUID[0]));
//				network.setLink(container);
//				network.getLink().setTitle("network");
//				container.getLinks().add(network);
//				LOGGER.debug("NetworkInterface UUID: "
//						+ network.getId().toString()
//						+ network.getNetworkInterface());
//
//				buffer.append("occi.networkinterface.interface=").append(
//						xoccimap.get("occi.networkinterface.interface"));
//				buffer.append("occi.networkinterface.mac=").append(
//						xoccimap.get("occi.networkinterface.mac"));
//
//				StringBuffer resource = new StringBuffer();
//				resource.append("/").append(container.getKind().getTerm())
//						.append("/");
//				getRootRef().setPath(resource.toString());
//
//				// check of the category
//				if (!"container".equalsIgnoreCase(xoccimap.get(
//						"occi.container.Category").toString())) {
//					throw new IllegalArgumentException(
//							"Illegal Category type: "
//									+ xoccimap.get("occi.container.Category"));
//				}
//				return container.getId().toString();
//			}
//		} catch (Exception e) {
//			LOGGER.error("Exception caught: " + e.toString());
//			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
//					e.toString());
//			e.printStackTrace();
//			return "Exception: " + e.getMessage() + "\n";
//		}
//		return " ";
//	}
}
