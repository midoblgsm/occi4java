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

import occi.application.Application;
import occi.application.Application.State;
import occi.application.coaps.client.CoapsCaller;
import occi.application.interfaces.ApplicationInterface;
//import occi.core.Method;
import occi.application.manifest.ManifestManagement;
import xml.application.ApplicationType;

/**
 * 
 * @author Sami Yangui
 * 
 */

public class ApplicationInterfaceImpl implements ApplicationInterface {
	
	private WebResource client = CoapsCaller.createClient();
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ApplicationInterfaceImpl.class);

	@Override
	public Application createApplication(Application application) {
		LOGGER.info("Create Application: " + application.getName());
		LOGGER.info(ManifestManagement.getApplicationOnly());
		ClientResponse cr= CoapsCaller.createApplication(client,ManifestManagement.getApplicationOnly());
		String id=CoapsCaller.getAppID(cr);
		application.setCoapsID(id);
		
		ClientResponse cr1= CoapsCaller.describeApplication(client, id);
		application.setUrl(CoapsCaller.getAppURL(cr1));
		return application;
	}

	@Override
	public Application startApplication(Application application) {
		LOGGER.info("Start Application: " + application.getName());
		CoapsCaller.startApplication(client,application.getCoapsID());
		application.setState(State.available);
		return application;
	}

	@Override
	public Application stopApplication(Application application) {
		LOGGER.info("Stop Application: " + application.getName());
		CoapsCaller.stopApplication(client,application.getCoapsID());
		application.setState(State.unavailable);
		return application;
	}
	
	@Override
	public Application restartApplication(Application application) {
		LOGGER.info("Restart Application: " + application.getName());
		CoapsCaller.stopApplication(client,application.getCoapsID());
		CoapsCaller.startApplication(client,application.getCoapsID());
		application.setState(State.available);
		return application;
	}

	@Override
	public Application updateApplication(Application application) {
		LOGGER.info("Update application: " + application.getName());
	//	CoapsCaller.updateApplication(client, application.getCoapsID(), application.getCoapsEnvID(), application.ge)
		CoapsCaller.updateApplication(client, ManifestManagement.getApplicationOnly(), application.getCoapsID());
		ManifestManagement.getEnvironmentOnly();
		return null;
	}


	
	@Override
	public Application suspendApplication(Application application) {
		LOGGER.info("Suspend Application: " + application.getName());
		return null;
	}

	@Override
	public Application deleteApplication(Application application) {
		// TODO Auto-generated method stub
		return null;
	}

}
