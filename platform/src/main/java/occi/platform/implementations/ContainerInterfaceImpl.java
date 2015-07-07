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
package occi.platform.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import occi.core.Method;
import occi.platform.Container;
import occi.platform.interfaces.ContainerInterface;

public class ContainerInterfaceImpl implements ContainerInterface {

	private static final Logger LOGGER = LoggerFactory
	.getLogger(DatabaseInterfaceImpl.class);
	
	@Override
	public Container createContainer(Container container) {
		LOGGER.info("Create Container: "+container.getName());
	    return null;
	}

	@Override
	public Container startContainer(Container container) {
		LOGGER.info("Start Container: "+container.getName());
		return null;
	}

	@Override
	public Container stopContainer(Container container) {
		LOGGER.info("Stop Container: "+container.getName());
		return null;
	}

	@Override
	public Container suspendContainer(Container container, Method suspend) {
		LOGGER.info("Suspend Container: "+container.getName());
		return null;
	}

	@Override
	public Container restartContainer(Container container) {
		LOGGER.info("Restart Container: "+container.getName());
		return null;
	}

	@Override
	public Container deleteContainer(Container container) {
		LOGGER.info("Delete Container: "+container.getName());
		return null;
	}
}
