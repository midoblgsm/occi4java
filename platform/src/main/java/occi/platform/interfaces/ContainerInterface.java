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
package occi.platform.interfaces;

import occi.core.Method;
import occi.platform.Container;

/**
 * Interface to execute operations on container resources. Necessary interface to
 * connect to the occi implementation.
 * 
 * @author Sebastian Heckmann
 * @author Sebastian Laag
 */
public interface ContainerInterface {
	/**
	 * Creates a new definition for a container resource.
	 * 
	 * @param container
	 * @return container
	 */
	Container createContainer(Container container);
	/**
	 * Starts a existing container resource.
	 * 
	 * @param container
	 * @return Container
	 */
	Container startContainer(Container container);

	/**
	 * Stops a existing container resource.
	 * 
	 * @param container
	 * @param stop
	 * @return Container
	 */
	Container stopContainer(Container container);
	
	/**
	 * Suspends a existing container resource.
	 * 
	 * @param container
	 * @return Container
	 */
	Container suspendContainer(Container container, Method suspend);
	
	/**
	 * Starts a started container resource.
	 * 
	 * @param container
	 * @return Container
	 */
	Container restartContainer(Container container);
	
	/**
	 * Deletes all files for a existing container resource.
	 * 
	 * @param container
	 * @return container
	 */
	Container deleteContainer(Container container);
}