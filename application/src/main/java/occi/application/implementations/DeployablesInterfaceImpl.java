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
package occi.application.implementations;

import occi.application.Deployable;
import occi.application.interfaces.DeployablesInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



//import occi.core.Method;


/**
 * 
 * @author Sami Yangui
 * 
 */

public class DeployablesInterfaceImpl implements DeployablesInterface {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeployablesInterfaceImpl.class);

	@Override
	public Deployable createDeployables(Deployable deployables) {
		LOGGER.info("Create Deployables: " + deployables.getName());
		return null;
	}

	@Override
	public Deployable startDeployables(Deployable deployables) {
		LOGGER.info("Start Deployables: " + deployables.getName());
		return null;
	}

	@Override
	public Deployable stopDeployables(Deployable deployables) {
		LOGGER.info("Stop Deployables: " + deployables.getName());
		return null;
	}

	@Override
	public Deployable updateDeployables(Deployable deployables) {
		LOGGER.info("Update Deployables: " + deployables.getName());
		return null;
	}

	@Override
	public Deployable restartDeployables(Deployable deployables) {
		LOGGER.info("Restart Deployables: " + deployables.getName());
		return null;
	}
	
	@Override
	public Deployable suspendDeployables(Deployable deployables) {
		LOGGER.info("Suspend Deployables: " + deployables.getName());
		return null;
	}

	@Override
	public Deployable deleteDeployables(Deployable deployables) {
		// TODO Auto-generated method stub
		return null;
	}

}
