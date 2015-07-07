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

import occi.application.Environment;
//import occi.core.Method;


/**
 * Interface to execute operations on environment resources. 
 * Necessary interface to connect to the occi implementation.
 * 
 * @author Sami Yangui
 */
public interface EnvironmentInterface {
	/**
	 * Creates a new definition for an environment resource.
	 * 
	 * @param environment
	 * @return environment
	 */
	Environment createEnvironment(Environment environment);
	/**
	 * Starts a existing environment resource.
	 * 
	 * @param environment
	 * @return environment
	 */
	Environment startEnvironment(Environment environment);

	/**
	 * Stops an existing environment resource.
	 * 
	 * @param environment
	 * @param stop
	 * @return environment
	 */
	Environment stopEnvironment(Environment environment);
	
	/**
	 * Update an existing environment resource.
	 * 
	 * @param environment
	 * @return Environment
	 */
	Environment updateEnvironment(Environment environment);
	
	/**
	 * Starts a started environment resource.
	 * 
	 * @param environment
	 * @return Environment
	 */
	Environment restartEnvironment(Environment environment);
	
	/**
	 * Deletes an existing environment resource.
	 * 
	 * @param environment
	 * @return Environment
	 */
	Environment deleteEnvironment(Environment environment);
	
	/**
	 * Suspends an existing environment resource.
	 * 
	 * @param environment
	 * @return Environment
	 */
	Environment suspendEnvironment(Environment environment);
	
	
}