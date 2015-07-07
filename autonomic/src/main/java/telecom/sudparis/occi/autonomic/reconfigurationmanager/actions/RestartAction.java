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
/**
 * @author mohame_m
 * 
 */
package telecom.sudparis.occi.autonomic.reconfigurationmanager.actions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import occi.core.Action;
import occi.core.Category;
import occi.core.Method;
import telecom.sudparis.occi.autonomic.ReconfigurationManager;
import telecom.sudparis.occi.autonomic.impl.ReconfigurationManagerInterfaceImpl;


public class RestartAction extends Action {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestartAction.class);

	private static ReconfigurationManagerInterfaceImpl reconfigurationManagerImplementation = new ReconfigurationManagerInterfaceImpl();

	/**
	 * Enum for the Restart Actions.
	 */
	public enum Restart implements Method {
		restart
	}

	private Restart restart;
	private HashSet<String> attributes = new HashSet<String>();

	public RestartAction() throws SchemaViolationException, URISyntaxException {
		attributes.add("restart");
		Category category = new Category("restart",
				"http://schemas.ogf.org/occi/reconfigurationmanager/action#", "Action",
				attributes);
	}

	@Override
	public void execute(URI uri, Method restart) {
		LOGGER.debug("libvirt: Restart");
		ReconfigurationManager database = null;
		String uriString = uri.toString();
		if (uri.toString().endsWith("/")) {
			uriString = uriString.substring(0, uri.toString().length() - 1);
			uriString = uriString.substring(uriString.length() - 36);
		}
		LOGGER.debug("URI " + uriString);
		UUID reconfigurationManagerUuid = UUID.fromString(uriString);
		LOGGER.debug("UUID " + reconfigurationManagerUuid.toString());
		for (UUID uuid : ReconfigurationManager.getReconfigurationManagerList().keySet()) {
			if (uuid.equals(reconfigurationManagerUuid)) {
				database = ReconfigurationManager.getReconfigurationManagerList().get(reconfigurationManagerUuid);
			}
		}
		reconfigurationManagerImplementation.restartReconfigurationManager(database);
	}

	public HashSet<String> getAttributes() {
		if (attributes.isEmpty()) {
			for (Restart restart : Restart.values()) {
				attributes.add(restart.toString());
			}
		}
		return attributes;
	}

	/**
	 * Returns the possible Restart Attributes of the Enum
	 * 
	 * @return String
	 */
	public static String getRestartAttributesAsString() {
		StringBuffer restartAttributes = new StringBuffer();
		for (Restart restart : Restart.values()) {
			restartAttributes.append(restart.toString() + ", ");
		}
		return restartAttributes.toString();
	}
}
