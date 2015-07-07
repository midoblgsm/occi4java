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
package test;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import occi.http.autonomic.OcciRestActionLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import telecom.sudparis.occi.autonomic.Analyzer;
import telecom.sudparis.occi.autonomic.Analyzer.State;
import telecom.sudparis.occi.autonomic.analyzer.actions.CreateAction;
import telecom.sudparis.occi.autonomic.impl.RuleSetMixinToolImpl;
import telecom.sudparis.occi.autonomic.interfaces.RuleSetMixinToolInterface;

import com.berniecode.mixin4j.DynamicProxyMixer;
import com.berniecode.mixin4j.Factory;
import com.berniecode.mixin4j.Mixin;

public class MainTest {
	
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestActionLink.class);
	public static void main(String[] args){

		LOGGER.info("Incoming POST request.");
		// set occi version info
		
		HashMap<String, Object> xoccimap = new HashMap<String, Object>();
		try {
			
	

				Set<String> set = new HashSet<String>();
				set.add("summary: ");
				set.add("this is the summary");
				set.add(" this is the scheme");
				
				// create new container instance with the given attributes

				// TODO Let the Mix4j do the job
				DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();

				Mixin<Analyzer> analyzerWithMixin = new Mixin(
						RuleSetMixinToolInterface.class,
						RuleSetMixinToolImpl.class, Analyzer.class);
				Factory factory = dynamicMixer.getFactory(Analyzer.class,
						analyzerWithMixin);
				Class[] clazz0 = { String.class ,String.class, State.class, Set.class};

				Object[] objects0 = {
						"MyAnalyzer",
						"V2",
						State.unavailable, set };
				Analyzer analyzer = (Analyzer) factory.newInstance(clazz0,
						objects0);

				URI uri = new URI(analyzer.getId().toString());
				// Create libvirt domain
				CreateAction createAction = new CreateAction();
				createAction.execute(uri, null);
//
//				StringBuffer resource = new StringBuffer();
//				resource.append("/").append(analyzer.getKind().getTerm())
//						.append("/");
//				getRootRef().setPath(resource.toString());
//
//				// check of the category
//				if (xoccimap.get("occi.analyzer.Category") != null)
//					if (!"analyzer".equalsIgnoreCase(xoccimap.get(
//							"occi.analyzer.Category").toString())) {
//						throw new IllegalArgumentException(
//								"Illegal Category type: "
//										+ xoccimap
//												.get("occi.analyzer.Category"));
//					}
//				for (Mixin mixin : Mixin.getMixins()) {
//					if (mixin.getEntities() != null) {
//						if (mixin.getEntities().contains(analyzer)) {
//							buffer.append("Category: " + mixin.getTitle()
//									+ "; scheme=\"" + mixin.getScheme()
//									+ "\"; class=\"mixin\"");
//						}
//					}
				//}
//				LOGGER.debug("Analyzer Uuid: " + analyzer.getUuid());
//				LOGGER.debug("Analyzer Kind scheme: "
//						+ analyzer.getKind().getScheme());
//				// Check accept header
//				if (requestHeaders.getFirstValue(acceptCase)
//						.equals("text/occi")
//						|| requestHeaders.getFirstValue("content-type", true)
//								.equals("text/occi")) {
//					// Generate header rendering
//					this.occiCheck.setHeaderRendering(null, analyzer,
//							buffer.toString(), null);
//					getResponse().setEntity(representation);
//					getResponse().setStatus(Status.SUCCESS_OK);
//				}
//				// Location Rendering in HTTP Header, not in body
//				setLocationRef((getRootRef().toString() + analyzer.getId()));
//				representation = OcciCheck.checkContentType(requestHeaders,
//						buffer, getResponse());
//				getResponse().setEntity(representation);
//				// set response status
//				getResponse().setStatus(Status.SUCCESS_OK, buffer.toString());
//				return Response.getCurrent().toString();
//			} else {
//				String[] splitURI = getReference().toString().split("\\/");
//				LOGGER.debug("splitURI length: " + splitURI.length);
//				UUID id = null;
//				for (String element : splitURI) {
//					if (OcciCheck.isUUID(element)) {
//						id = UUID.fromString(element);
//					}
//				}
//				LOGGER.debug("UUID: " + id);
//				// Get the container resource by the given UUID
//				Analyzer analyzer = Analyzer.getAnalyzerList().get(id);
//
//				String location = "http:"
//						+ getReference().getHierarchicalPart();

				// Extract the action type / name from the last part of the
//				// given
//				// location URI and split it after the "=" (../?action=stop)
//				String[] actionName = getReference()
//						.getRemainingPart()
//						.subSequence(1,
//								getReference().getRemainingPart().length())
//						.toString().split("\\=");
//				LOGGER.debug("Action Name: " + actionName[1]);
//
//				// Check if actionName[1] is set
//				if (actionName.length >= 2) {
//					// Call the Start action of the container resource
//					if (actionName[1].equalsIgnoreCase("start")) {
//						LOGGER.debug("Start Action called.");
//						LOGGER.debug(xoccimap.toString());
//						analyzer.getStart().execute(URI.create(location), null);
//						// Set the current state of the container resource
//						analyzer.setState(State.available);
//					}
//
//					if (actionName[1].equalsIgnoreCase("stop")) {
//						LOGGER.debug("Stop Action called.");
//						// Call the Stop action of the container resource
//						analyzer.getStop().execute(URI.create(location), null);
//						// Set the current state of the container resource
//						analyzer.setState(State.unavailable);
//					}
//
//					if (actionName[1].equalsIgnoreCase("restart")) {
//						LOGGER.debug("Restart Action called.");
//						// Call the Restart action of the container resource
//						analyzer.getRestart().execute(URI.create(location),
//								null);
//						// Set the current state of the container resource
//						analyzer.setState(State.available);
//					}
//					if (actionName[1].equalsIgnoreCase("reconfigure")) {
//						LOGGER.debug("Reconfigure Action called.");
//						// Call the Restart action of the container resource
//						analyzer.getReconfigure().execute(URI.create(location),
//								null);
//						// Set the current state of the container resource
//						analyzer.setState(State.reconfiguring);
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST,
//					e.toString());
//			return "Exception caught: " + e.toString() + "\n";
//		}
//		return " ";	
//	}

}catch(Exception e){ e.printStackTrace();}
	}
}

