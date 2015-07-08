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
package occi.application.application.actions;

import java.net.URI;
import java.util.UUID;

import occi.application.Application;
import occi.application.implementations.ApplicationInterfaceImpl;
import occi.application.interfaces.ApplicationInterface;
import occi.core.Action;
import occi.core.Method;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Action Interface. 
 * In this case the CreateAction creates a new definition for an Application resource.
 * 
 * @see Action
 * @author Sami Yangui
 */
public class RestartAction extends Action {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestartAction.class);

	@Override
	public void execute(URI uri, Method method) {
		LOGGER.debug("libvirt: RestartAction Application");
		Application application = null;
		String uriString = uri.toString();
		if (uri.toString().endsWith("/")) {
			uriString = uriString.substring(0, uri.toString().length() - 1);
			uriString = uriString.substring(uriString.length() - 36);
		}
		LOGGER.debug("URI " + uriString);
		UUID applicationUuid = UUID.fromString(uriString);
		LOGGER.debug("UUID " + applicationUuid.toString());
		for (UUID uuid : Application.getApplicationList().keySet()) {
			if (uuid.equals(applicationUuid)) {
				application = Application.getApplicationList().get(applicationUuid);
			}
		}
		ApplicationInterface applicationInterface=new ApplicationInterfaceImpl();
		applicationInterface.restartApplication(application);

	}
}