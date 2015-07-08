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

public class StopAction extends Action {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StopAction.class);

	private static ReconfigurationManagerInterfaceImpl reconfigurationManagerImplementation = new ReconfigurationManagerInterfaceImpl();

	/**
	 * Enum for the Stop Actions.
	 */
	public enum Stop implements Method {
		stop
	}

	private Stop stop;
	private HashSet<String> attributes = new HashSet<String>();

	public StopAction() throws SchemaViolationException, URISyntaxException {
		attributes.add("stop");
		Category category = new Category("stop",
				"http://schemas.ogf.org/occi/reconfigurationmanager/action#",
				"Action", attributes);
	}

	@Override
	public void execute(URI uri, Method stop) {
		LOGGER.debug("libvirt: Stop");
		ReconfigurationManager database = null;
		String uriString = uri.toString();
		if (uri.toString().endsWith("/")) {
			uriString = uriString.substring(0, uri.toString().length() - 1);
			uriString = uriString.substring(uriString.length() - 36);
		}
		LOGGER.debug("URI " + uriString);
		UUID reconfigurationManagerUuid = UUID.fromString(uriString);
		LOGGER.debug("UUID " + reconfigurationManagerUuid.toString());
		for (UUID uuid : ReconfigurationManager.getReconfigurationManagerList()
				.keySet()) {
			if (uuid.equals(reconfigurationManagerUuid)) {
				database = ReconfigurationManager
						.getReconfigurationManagerList().get(
								reconfigurationManagerUuid);
			}
		}
		reconfigurationManagerImplementation
				.stopReconfigurationManager(database);
	}

	public HashSet<String> getAttributes() {
		if (attributes.isEmpty()) {
			for (Stop stop : Stop.values()) {
				attributes.add(stop.toString());
			}
		}
		return attributes;
	}

	/**
	 * Returns the possible Stop Attributes of the Enum
	 * 
	 * @return String
	 */
	public static String getStopAttributesAsString() {
		StringBuffer stopAttributes = new StringBuffer();
		for (Stop stop : Stop.values()) {
			stopAttributes.append(stop.toString() + ", ");
		}
		return stopAttributes.toString();
	}

}
