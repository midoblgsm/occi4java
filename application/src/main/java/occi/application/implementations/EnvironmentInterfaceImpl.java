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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


import occi.application.Environment;
import occi.application.coaps.client.CoapsCaller;
import occi.application.interfaces.EnvironmentInterface;
//import occi.core.Method;
import occi.application.manifest.ManifestManagement;


/**
 * 
 * @author Sami Yangui
 * 
 */

public class EnvironmentInterfaceImpl implements EnvironmentInterface {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EnvironmentInterfaceImpl.class);
	
	private WebResource client = CoapsCaller.createClient();

	@Override
	public Environment createEnvironment(Environment environment) {
		LOGGER.info("Create Environment: " + environment.getName());
		LOGGER.info(ManifestManagement.getEnvironmentOnly());		
		// Create Environment on COAPS
		
		ClientResponse cr= CoapsCaller.createEnvironment(client, ManifestManagement.getEnvironmentOnly());
		//cr.bufferEntity();
		environment.setCoapsID(CoapsCaller.getEnvID(cr));
		return environment;
	}

	@Override
	public Environment startEnvironment(Environment environment) {
		LOGGER.info("Start Environment: " + environment.getName());
		return null;
	}

	@Override
	public Environment stopEnvironment(Environment environment) {
		LOGGER.info("Stop Environment: " + environment.getName());
		return null;
	}

	@Override
	public Environment updateEnvironment(Environment environment) {
		LOGGER.info("Update Environment: " + environment.getName());
		return null;
	}

	@Override
	public Environment restartEnvironment(Environment environment) {
		LOGGER.info("Restart Environment: " + environment.getName());
		return null;
	}
	
	@Override
	public Environment suspendEnvironment(Environment environment) {
		LOGGER.info("Suspend Environment: " + environment.getName());
		return null;
	}

	@Override
	public Environment deleteEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}

}
