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
package occi.application.interfaces;

import occi.application.Application;
//import occi.core.Method;


/**
 * Interface to execute operations on application resources. 
 * Necessary interface to connect to the occi implementation.
 * 
 * @author Sami Yangui
 */
public interface ApplicationInterface {
	/**
	 * Creates a new definition for an application resource.
	 * 
	 * @param application
	 * @return application
	 */
	Application createApplication(Application application);
	/**
	 * Starts a existing application resource.
	 * 
	 * @param application
	 * @return COAPS Application ID
	 */
	Application startApplication(Application application);

	/**
	 * Stops an existing application resource.
	 * 
	 * @param application
	 * @param stop
	 * @return application
	 */
	Application stopApplication(Application application);
	
	/**
	 * Update an existing application resource.
	 * 
	 * @param application
	 * @return Application
	 */
	Application updateApplication(Application application);
	
	/**
	 * Starts a started application resource.
	 * 
	 * @param application
	 * @return Application
	 */
	Application restartApplication(Application application);
	
	/**
	 * Deletes an existing application resource.
	 * 
	 * @param application
	 * @return Application
	 */
	Application deleteApplication(Application application);
	
	/**
	 * Suspends an existing application resource.
	 * 
	 * @param application
	 * @return Application
	 */
	Application suspendApplication(Application application);
	
	
}