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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import occi.config.OcciConfig;
import occi.http.check.OcciCheck;
import occi.application.Application;
import occi.application.Deployable;
import occi.application.Application.State;
import occi.application.application.actions.CreateAction;
import occi.application.application.actions.DeleteAction;
import occi.application.manifest.ManifestManagement;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
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

import telecom.sudparis.occi.autonomic.impl.PollingMixinImpl;
import telecom.sudparis.occi.autonomic.impl.SubscriptionMixinImpl;
import telecom.sudparis.occi.autonomic.interfaces.PollingMixinInterface;
import telecom.sudparis.occi.autonomic.interfaces.SubscriptionMixinInterface;
import telecom.sudparis.occi.autonomic.testresources.MixedApplication;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

import com.berniecode.mixin4j.DynamicProxyMixer;
import com.berniecode.mixin4j.Factory;
import com.berniecode.mixin4j.Mixin;

/**
 * 
 * @author mohame_m
 */
public class OcciRestMixedApplication extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestMixedApplication.class);

	private final OcciCheck occiCheck = new OcciCheck();

	/**
	 * Method to create a new Application instance.
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

		if (getReference().toString().contains("poll")) {
			
			Uniform uniform= new UniformPollingApplicationHandler(representation, getReference());
			uniform.handle(getRequest(), getResponse());
			return "polling ok ";
		}
		if (getReference().toString().contains("subscribe")) {
	
			Uniform uniform= new UniformSubscribeApplicationHandler(representation, getReference());
			uniform.handle(getRequest(), getResponse());
			return "subscription ok ";
		}
		if (getReference().toString().contains("notify")) {
			Uniform uniform= new UniformNotifyApplicationHandler(representation, getReference());
			uniform.handle(getRequest(), getResponse());
			return "notification ok ";
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
			if (!getReference().toString().contains("action")) {

				String deployableString = (String) xoccimap
						.get("occi.deployables.uuids");

				ArrayList<Deployable> depList = parseDeployable(deployableString);

				if (depList == null) {
					getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					getResponse().setEntity("deployable ID does not exist",
							MediaType.TEXT_PLAIN);

				} else {

					DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();

					Mixin<MixedApplication> applicationWithPollingMixin = new Mixin(
							PollingMixinInterface.class,
							PollingMixinImpl.class, MixedApplication.class);

					Mixin<MixedApplication> applicationWithSubscriptionMixin = new Mixin(
							SubscriptionMixinInterface.class,
							SubscriptionMixinImpl.class, MixedApplication.class);

					List<Mixin<MixedApplication>> mixinsList = (List<Mixin<MixedApplication>>) Arrays
							.asList(applicationWithPollingMixin,
									applicationWithSubscriptionMixin);

					Factory factory = dynamicMixer.getFactory(
							MixedApplication.class, mixinsList);
					Class[] clazz0 = { String.class, String.class,
							Integer.class, String.class, String.class,
							State.class, ArrayList.class };

					Object[] objects0 = {
							(String) xoccimap.get("occi.application.name"),
							(String) xoccimap
									.get("occi.application.description"),
							Integer.parseInt((String) xoccimap
									.get("occi.application.instances")),
							(String) xoccimap.get("occi.application.url"),
							(String) xoccimap
									.get("occi.application.start_command"),
							State.unavailable, depList };
					MixedApplication mixedApplication = (MixedApplication) factory
							.newInstance(clazz0, objects0);

					/**
					 * TODO to be verify
					 */

					// create new Application instance with the given attributes

					ManifestManagement.createApplicationManifest(
							mixedApplication, depList);

					URI uri = new URI(mixedApplication.getId().toString());
					// Create libvirt domain
					CreateAction createAction = new CreateAction();
					createAction.execute(uri, null);

					// put occi attributes into a buffer for the response

					// update the application URL with the one generated by
					// COAPS
					xoccimap.put("occi.application.url",
							mixedApplication.getUrl());

					StringBuffer buffer = new StringBuffer();

					buffer.append("occi.application.name=").append(
							xoccimap.get("occi.application.name"));
					buffer.append(" occi.application.description=").append(
							xoccimap.get("occi.application.description"));
					buffer.append(" occi.application.instances=").append(
							xoccimap.get("occi.application.instances"));
					buffer.append(" occi.application.url=").append(
							xoccimap.get("occi.application.url"));
					buffer.append(" occi.application.start_command=").append(
							xoccimap.get("occi.application.start_command"));
					buffer.append(" occi.deployables.uuids=").append(
							xoccimap.get("occi.deployables.uuids"));
					buffer.append(" occi.application.state=").append(
							"unavailable");

					Set<String> set = new HashSet<String>();
					set.add("summary: ");
					set.add(buffer.toString());
					set.add(requestHeaders.getFirstValue("scheme"));
					LOGGER.debug("Attribute set: " + set.toString());

					StringBuffer resource = new StringBuffer();
					resource.append("/")
							.append(mixedApplication.getKind().getTerm())
							.append("/");
					getRootRef().setPath(resource.toString());

					// check of the category
					if (xoccimap.get("occi.application.Category") != null)
						if (!"application".equalsIgnoreCase(xoccimap.get(
								"occi.application.Category").toString())) {
							throw new IllegalArgumentException(
									"Illegal Category type: "
											+ xoccimap
													.get("occi.application.Category"));
						}
					// for (Mixin mixin : Mixin.getMixins()) {
					// if (mixin.getEntities() != null) {
					// if (mixin.getEntities().contains(application)) {
					// buffer.append("Category: " + mixin.getTitle()
					// + "; scheme=\"" + mixin.getScheme()
					// + "\"; class=\"mixin\"");
					// }
					// }
					// }
					LOGGER.debug("Application Uuid: "
							+ mixedApplication.getUuid());
					LOGGER.debug("Application Kind scheme: "
							+ mixedApplication.getKind().getScheme());
					// Check accept header
					if (requestHeaders.getFirstValue(acceptCase).equals(
							"text/occi")
							|| requestHeaders.getFirstValue("content-type",
									true).equals("text/occi")) {
						// Generate header rendering
						this.occiCheck.setHeaderRendering(null,
								mixedApplication, buffer.toString(), null);
						getResponse().setEntity(representation);
						getResponse().setStatus(Status.SUCCESS_OK);
					}
					// Location Rendering in HTTP Header, not in body
					setLocationRef((getRootRef().toString() + mixedApplication
							.getId()));
					representation = OcciCheck.checkContentType(requestHeaders,
							buffer, getResponse());
					getResponse().setEntity(representation);
					// set response status
					getResponse().setStatus(Status.SUCCESS_OK,
							buffer.toString());
				}
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
				// Get the application resource by the given UUID
				Application application = MixedApplication.getApplicationList()
						.get(id);

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
					boolean flag = false;
					// Call the Start action of the container resource
					if (actionName[1].equalsIgnoreCase("start")) {
						LOGGER.debug("Start Action called.");
						LOGGER.debug(xoccimap.toString());

						if (application.getCoapsID() != null) {
							application.getStart().execute(
									URI.create(location), null);
							// Set the current state of the application resource
							application.setState(State.available);
							flag = true;
						}
					}

					if (actionName[1].equalsIgnoreCase("stop")) {
						LOGGER.debug("Stop Action called.");
						// Call the Stop action of the application resource
						if (application.getCoapsID() != null) {
							application.getStop().execute(URI.create(location),
									null);
							// Set the current state of the application resource
							application.setState(State.unavailable);
							flag = true;
						}
					}

					if (actionName[1].equalsIgnoreCase("restart")) {
						LOGGER.debug("Restart Action called.");
						// Call the Restart action of the application resource
						if (application.getCoapsID() != null) {
							application.getRestart().execute(
									URI.create(location), null);
							// Set the current state of the application resource
							application.setState(State.available);
							flag = true;
						}
					}

					if (flag == true) {
						getResponse().setStatus(Status.SUCCESS_OK);
						getResponse().setEntity(
								"occi.application.state = "
										+ application.getState().toString(),
								MediaType.TEXT_PLAIN);
					} else {
						getResponse()
								.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						getResponse().setEntity("application not exist",
								MediaType.TEXT_PLAIN);
					}
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

	@Get
	public String getOCCIRequest() {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));
		LOGGER.debug("getReference().getLastSegment(): "
				+ getReference().getLastSegment().toString());
		if (getReference().toString().contains("")) {
			MixedApplication mixedApplication = (MixedApplication) Application
					.getApplicationList().get(
							UUID.fromString(getReference().getLastSegment()));
			Notification notification = mixedApplication.poll("response_time");

			return XMLTools.getNotificationXML(notification);
		} else {
			try {

				OcciCheck.isUUID(getReference().getLastSegment());
				// get the Application instance by the given UUID
				MixedApplication mixedApplication = (MixedApplication) Application
						.getApplicationList()
						.get(UUID.fromString(getReference().getLastSegment()));

				// put all attributes into a buffer for the response
				StringBuffer buffer = new StringBuffer();
				StringBuffer linkBuffer = new StringBuffer();
				buffer.append(" Category: ")
						.append(mixedApplication.getKind().getTerm())
						.append(" scheme= ")
						.append(mixedApplication.getKind().getScheme())
						.append(" class=\"kind\";").append(" Name: ")
						.append(mixedApplication.getName())
						.append(" Description: ")
						.append(mixedApplication.getDescription())
						.append(" Instances: ")
						.append(mixedApplication.getInstances())
						.append(" URI: ").append(mixedApplication.getUrl())
						.append(" Start_command: ")
						.append(mixedApplication.getStart_command())
						.append(" State: ").append(mixedApplication.getState());

				// access the request headers and get the Accept attribute
				Representation representation = OcciCheck.checkContentType(
						null, buffer, getResponse());
				// Check the accept header

				this.occiCheck.setHeaderRendering(null, mixedApplication,
						buffer.toString(), linkBuffer);
				// set right representation and status code

				// set right representation and status code
				getResponse().setEntity(representation);
				getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
				return buffer.toString();
			} catch (NullPointerException e) {
				e.printStackTrace();
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return "UUID("
						+ UUID.fromString(getReference().getLastSegment())
						+ ") not found! " + e.toString() + "\n";
			} catch (Exception e) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
						e.toString());
				return e.toString();
			}
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
			// get application resource that should be deleted
			Application application = Application.getApplicationList().get(
					UUID.fromString(getReference().getLastSegment()));
			DeleteAction deleteAction = new DeleteAction();
			deleteAction.execute(new URI(application.getId().toString()), null);
			// remove it from Application resource list
			if (Application.getApplicationList().remove(
					UUID.fromString(application.getId().toString())) == null) {
				throw new Exception("There is no resource with the given ID");
			}
			getResponse().setStatus(Status.SUCCESS_OK);

			// set Application resource to null
			application = null;
			return " ";
		} catch (NullPointerException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			return "UUID(" + UUID.fromString(getReference().getLastSegment())
					+ ") not found! " + e.toString()
					+ "\n Application resource could not be deleted.";
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
			Application application = Application.getApplicationList().get(
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
					// Change the Application attribute if it is send by the
					// request

					if (xoccimap.containsKey("occi.application.name")) {
						LOGGER.info((String) xoccimap
								.get("occi.application.name"));
						application.setName((String) xoccimap
								.get("occi.application.name"));
					}
					if (xoccimap.containsKey("occi.application.description")) {
						LOGGER.info((String) xoccimap
								.get("occi.application.description"));
						application.setDescription((String) xoccimap
								.get("occi.application.description"));
					}
					if (xoccimap.containsKey("occi.application.instances")) {
						LOGGER.info((String) xoccimap
								.get("occi.application.instances"));
						application.setInstances((Integer) xoccimap
								.get("occi.application.instances"));
					}
					if (xoccimap.containsKey("occi.application.url")) {
						LOGGER.info((String) xoccimap
								.get("occi.application.url"));
						application.setUrl((String) xoccimap
								.get("occi.application.url"));
					}
					if (xoccimap.containsKey("occi.application.start_command")) {
						LOGGER.info((String) xoccimap
								.get("occi.application.start_command"));
						application.setStart_command((String) xoccimap
								.get("occi.application.start_command"));
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

	private ArrayList<Deployable> parseDeployable(String deployables) {
		String[] deps = deployables.split("&");
		ArrayList<Deployable> depList = new ArrayList<Deployable>();

		for (String dep : deps) {
			UUID uuid = UUID
					.fromString(dep.replaceFirst(".*/([^/?]+).*", "$1"));
			if (!Deployable.getDeployablesList().containsKey(uuid))
				return null;
			else
				depList.add(Deployable.getDeployablesList().get(uuid));
		}

		return depList;
	}
}
class UniformNotifyApplicationHandler implements Uniform {
	private Representation representation;
	private Reference reference;

	UniformNotifyApplicationHandler(Representation representation,
			Reference reference) {
		this.representation = representation;
		this.reference = reference;
	}

	public void handle(Request request, Response response) {
		try {
			System.out.println("Handling Notification");
			MixedApplication mixedApplication = (MixedApplication) Application
					.getApplicationList()
					.get(UUID.fromString(reference.getLastSegment()));
			mixedApplication.notify(XMLTools
					.getNotificationFromXML(representation.getText()));
		
		} catch (Exception e) {
			System.err.println("Uniform exception ApplicationNotify");
		}
	}
}
class UniformSubscribeApplicationHandler implements Uniform {
	private Representation representation;
	private Reference reference;

	UniformSubscribeApplicationHandler(Representation representation,
			Reference reference) {
		this.representation = representation;
		this.reference = reference;
	}

	public void handle(Request request, Response response) {
		try {
			MixedApplication mixedApplication = (MixedApplication) Application
					.getApplicationList()
					.get(UUID.fromString(reference.getLastSegment()));
			mixedApplication.subscribe(XMLTools
					.getSubscriptionFromXML(representation.getText()));
		
		} catch (Exception e) {
			System.err.println("Uniform exception ApplicationSubscribe");
		}
	}
}
class UniformPollingApplicationHandler implements Uniform {
	private Representation representation;
	private Reference reference;

	UniformPollingApplicationHandler(Representation representation,
			Reference reference) {
		this.representation = representation;
		this.reference = reference;
	}

	public void handle(Request request, Response response) {
		
			try {
				MixedApplication mixedApplication = (MixedApplication) Application
						.getApplicationList()
						.get(UUID.fromString(reference.getLastSegment()));
			
				System.err.println(mixedApplication.poll("response_time"));
			} 
		catch (Exception e) {
			System.err.println("Uniform exception ApplicationPolling");
		}
	}
}