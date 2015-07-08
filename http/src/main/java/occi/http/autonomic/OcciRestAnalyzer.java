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

import telecom.sudparis.occi.autonomic.Analyzer;
import telecom.sudparis.occi.autonomic.Analyzer.State;
import telecom.sudparis.occi.autonomic.analyzer.actions.DeleteAction;
import telecom.sudparis.occi.autonomic.impl.RuleSetMixinToolImpl;
import telecom.sudparis.occi.autonomic.interfaces.RuleSetMixinToolInterface;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.XMLTools;
import occi.config.OcciConfig;
import occi.http.check.OcciCheck;

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

public class OcciRestAnalyzer extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestAnalyzer.class);

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
		if (getReference().toString().contains("notify")) {
			Uniform uniform= new UniformAnalyzerHandler(representation, getReference());
			uniform.handle(getRequest(), getResponse());
			return "notification received";
		}
		String acceptCase = "";
		try {
			// access the request headers and get the X-OCCI-Attribute
			Form requestHeaders = (Form) getRequest().getAttributes().get(
					"org.restlet.http.headers");

			{
				LOGGER.debug("Current request: " + requestHeaders);
				String attributeCase = OcciCheck.checkCaseSensitivity(
						requestHeaders.toString()).get("x-occi-attribute");
				String xocciattributes = requestHeaders
						.getValues(attributeCase).replace(",", " ");
				acceptCase = OcciCheck.checkCaseSensitivity(
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

					buffer.append("occi.analyzer.name=").append(
							xoccimap.get("occi.analyzer.name"));
					buffer.append(" occi.analyzer.version=").append(
							xoccimap.get("occi.analyzer.version"));
					buffer.append(" occi.analyzer.state=")
							.append("unavailable");

					Set<String> set = new HashSet<String>();
					set.add("summary: ");
					set.add(buffer.toString());
					set.add(requestHeaders.getFirstValue("scheme"));
					LOGGER.debug("Attribute set: " + set.toString());

					// create new container instance with the given attributes

					// TODO Let the Mix4j do the job
					DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();

					Mixin<Analyzer> analyzerWithMixin = new Mixin(
							RuleSetMixinToolInterface.class,
							RuleSetMixinToolImpl.class, Analyzer.class);
					Factory factory = dynamicMixer.getFactory(Analyzer.class,
							analyzerWithMixin);
					Class[] clazz0 = { String.class, String.class, State.class,
							Set.class };
					System.err.println("set.class####" + set.size());
					Object[] objects0 = {
							(String) xoccimap.get("occi.analyzer.name"),
							(String) xoccimap.get("occi.analyzer.version"),
							State.unavailable, set };
					Analyzer analyzer = (Analyzer) factory.newInstance(clazz0,
							objects0);

					analyzer.setMaxThreshold(Integer.parseInt((String) xoccimap
							.get("occi.analyzer.maxthreshold")));
					analyzer.setMinThreshold(Integer.parseInt((String) xoccimap
							.get("occi.analyzer.minthreshold")));
					analyzer.setAggregationInterval(Integer
							.parseInt((String) xoccimap
									.get("occi.analyzer.aggregationinterval")));

					// URI uri = new URI(analyzer.getId().toString());
					// // Create libvirt domain
					// CreateAction createAction = new CreateAction();
					// createAction.execute(uri, null);

					StringBuffer resource = new StringBuffer();
					resource.append("/").append(analyzer.getKind().getTerm())
							.append("/");
					getRootRef().setPath(resource.toString());

					// check of the category
					if (xoccimap.get("occi.analyzer.Category") != null)
						if (!"analyzer".equalsIgnoreCase(xoccimap.get(
								"occi.analyzer.Category").toString())) {
							throw new IllegalArgumentException(
									"Illegal Category type: "
											+ xoccimap
													.get("occi.analyzer.Category"));
						}
					// for (Mixin mixin : Mixin.getMixins()) {
					// if (mixin.getEntities() != null) {
					// if (mixin.getEntities().contains(analyzer)) {
					// buffer.append("Category: " + mixin.getTitle()
					// + "; scheme=\"" + mixin.getScheme()
					// + "\"; class=\"mixin\"");
					// }
					// }
					// }
					LOGGER.debug("Analyzer Uuid: " + analyzer.getUuid());
					LOGGER.debug("Analyzer Kind scheme: "
							+ analyzer.getKind().getScheme());
					// Check accept header
					if (requestHeaders.getFirstValue(acceptCase).equals(
							"text/occi")
							|| requestHeaders.getFirstValue("content-type",
									true).equals("text/occi")) {
						// Generate header rendering
						this.occiCheck.setHeaderRendering(null, analyzer,
								buffer.toString(), null);
						getResponse().setEntity(representation);
						getResponse().setStatus(Status.SUCCESS_OK);
					}
					// Location Rendering in HTTP Header, not in body
					setLocationRef((getRootRef().toString() + analyzer.getId()));
					representation = OcciCheck.checkContentType(requestHeaders,
							buffer, getResponse());
					getResponse().setEntity(representation);
					// set response status
					getResponse().setStatus(Status.SUCCESS_OK,
							buffer.toString());
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
					Analyzer analyzer = Analyzer.getAnalyzerList().get(id);

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
							analyzer.getStart().execute(URI.create(location),
									null);
							// Set the current state of the container resource
							analyzer.setState(State.available);
						}

						if (actionName[1].equalsIgnoreCase("stop")) {
							LOGGER.debug("Stop Action called.");
							// Call the Stop action of the container resource
							analyzer.getStop().execute(URI.create(location),
									null);
							// Set the current state of the container resource
							analyzer.setState(State.unavailable);
						}

						if (actionName[1].equalsIgnoreCase("restart")) {
							LOGGER.debug("Restart Action called.");
							// Call the Restart action of the container resource
							analyzer.getRestart().execute(URI.create(location),
									null);
							// Set the current state of the container resource
							analyzer.setState(State.available);
						}
						if (actionName[1].equalsIgnoreCase("reconfigure")) {
							LOGGER.debug("Reconfigure Action called.");
							// Call the Restart action of the container resource
							analyzer.getReconfigure().execute(
									URI.create(location), null);
							// Set the current state of the container resource
							analyzer.setState(State.reconfiguring);
						}
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
			Analyzer analyzer = Analyzer.getAnalyzerList().get(
					UUID.fromString(getReference().getLastSegment()));

			// put all attributes into a buffer for the response
			StringBuffer buffer = new StringBuffer();
			StringBuffer linkBuffer = new StringBuffer();
			buffer.append(" Category: ").append(analyzer.getKind().getTerm())
					.append(" scheme= ").append(analyzer.getKind().getScheme())
					.append(" class=\"kind\";").append(" Name: ")
					.append(analyzer.getName()).append(" Version: ")
					.append(analyzer.getState());

			// TODO: see infrastructure to display links

			// access the request headers and get the Accept attribute
			Representation representation = OcciCheck.checkContentType(
					requestHeaders, buffer, getResponse());
			// Check the accept header
			if (requestHeaders.getFirstValue(acceptCase).equals("text/occi")) {
				// generate header rendering
				this.occiCheck.setHeaderRendering(null, analyzer,
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
			Analyzer analyzer = Analyzer.getAnalyzerList().get(
					UUID.fromString(getReference().getLastSegment()));
			DeleteAction deleteAction = new DeleteAction();
			deleteAction.execute(new URI(analyzer.getId().toString()), null);
			// remove it from container resource list
			if (Analyzer.getAnalyzerList().remove(
					UUID.fromString(analyzer.getId().toString())) == null) {
				throw new Exception("There is no resorce with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set container resource to null
			analyzer = null;
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
			Analyzer analyzer = Analyzer.getAnalyzerList().get(
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

					if (xoccimap.containsKey("occi.analyzer.version")) {
						LOGGER.info((String) xoccimap
								.get("occi.analyzer.version"));
						analyzer.setVersion(xoccimap.get(
								"occi.container.version").toString());
					}
					if (xoccimap.containsKey("occi.analyzer.name")) {
						LOGGER.info((String) xoccimap.get("occi.analyzer.name"));
						analyzer.setName((String) xoccimap
								.get("occi.analyzer.name"));
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

class UniformAnalyzerHandler implements Uniform {
	private Representation representation;
	private Reference reference;

	UniformAnalyzerHandler(Representation representation, Reference reference) {
		this.representation = representation;
		this.reference = reference;
	}

	public void handle(Request request, Response response) {
		try {
			
			String notificationText = representation.getText();
			Notification notification = XMLTools
					.getNotificationFromXML(notificationText);
			Analyzer analyzer = Analyzer.getAnalyzerList().get(
					UUID.fromString(reference.getLastSegment()));
			analyzer.notify(notification);
		} catch (Exception e) {
			System.err.println("Uniform exception Analyzer");
		}
	}
}