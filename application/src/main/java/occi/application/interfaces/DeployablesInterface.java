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

import occi.application.Deployable;


//import occi.core.Method;


/**
 * Interface to execute operations on Deployables resources. 
 * Necessary interface to connect to the occi implementation.
 * 
 * @author Sami Yangui
 */
public interface DeployablesInterface {
	/**
	 * Creates a new definition for a Deployables resource.
	 * 
	 * @param deployables
	 * @return deployables
	 */
	Deployable createDeployables(Deployable deployables);
	/**
	 * Starts a existing Deployables resource.
	 * 
	 * @param deployables
	 * @return deployables
	 */
	Deployable startDeployables(Deployable deployables);

	/**
	 * Stops an existing Deployables resource.
	 * 
	 * @param deployables
	 * @param stop
	 * @return deployables
	 */
	Deployable stopDeployables(Deployable deployables);
	
	/**
	 * Update an existing Deployables resource.
	 * 
	 * @param deployables
	 * @return Deployables
	 */
	Deployable updateDeployables(Deployable deployables);
	
	/**
	 * Starts a started Deployables resource.
	 * 
	 * @param deployables
	 * @return Deployables
	 */
	Deployable restartDeployables(Deployable deployables);
	
	/**
	 * Deletes an existing deployables resource.
	 * 
	 * @param deployables
	 * @return Deployables
	 */
	Deployable deleteDeployables(Deployable deployables);
	
	/**
	 * Suspends an existing Deployables resource.
	 * 
	 * @param deployables
	 * @return Deployables
	 */
	Deployable suspendDeployables(Deployable deployables);
	
	
}