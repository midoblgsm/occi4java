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
package occi.platform;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * The Compute type represents a generic Information resource. For example a
 * virtual machine. This Compute type Inherits the Resource base type defined in
 * OCCI Core Specification. Compute is assigned to the Kind instance. [T. Metsch, A. Edmonds - Open Cloud Computing Interface - Infrastructure, http://ogf.org/documents/GFD.184.pdf, Apr. 2011]
 * 
 * @author Sebastian Heckmann
 * @author Sebastian Laag
 */
public class Container extends Resource {
	/**
	 * Name of the instance
	 */
	public String name;
	
	/**
	 * Version of the instance
	 */
	public String version;
	
	/**
	 * Architecture of the compute resource
	 */
	private Architecture architecture;


	/**
	 * Enumeration for CPU Architecture of the instance
	 */
	public enum Architecture {
		x86, x64
	}

	/**
	 * State of the compute resource
	 */
	private State state;

	/**
	 * Current State of the instance
	 */
	public enum State {
		unvailable, available
	}

	/**
	 * Static Hashmap of all Compute Resources. The Key is a UUID, the Value a
	 * Compute Object.
	 */
	private static Map<UUID, Container> containerList = new HashMap<UUID, Container>();

	/**
	 * Static HashSet of all compute attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the compute resource.
	 */
	private final UUID uuid;

	/*
	 * All possible compute actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	
	public Container(String name, String version, Architecture architecture,State state,Set<String> attributes) throws URISyntaxException,
			SchemaViolationException {
		super("Container", links, attributes);
		this.name = name;
		this.version = version;
		this.architecture=architecture;
		this.state = state;

		generateActionNames();

		/*
		 * set Category
		 */
		setKind(new Kind(actionSet, null, null, null, "container", "Container",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/platform#", attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into compute list
		containerList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Return the full containereList
	 * 
	 * @return all container resources
	 */
	public static final Map<UUID, Container> getContainerList() {
		return containerList;
	}

	/**
	 * Set container list
	 * 
	 * @param containerList
	 */
	public static final void setContainerList(Map<UUID, Container> containerList) {
		Container.containerList = containerList;
	}

	/**
	 * Returns the current UUID as a UUID Value
	 * 
	 * @return current UUID assigned to the Instance
	 */
	public final UUID getUuid() {
		return uuid;
	}

	

	/**
	 * Returns the State of the current Instance
	 * 
	 * @return State of the current Instance
	 */
	public final State getState() {
		return state;
	}

	/**
	 * Sets the State of the current Instance
	 * 
	 * @param State
	 *            state of the current Instance
	 */
	public final void setState(State state) {
		this.state = state;
	}

	/**
	 * Return list of all action names.
	 * 
	 * @return action names
	 */
	public static final HashSet<String> getActionNames() {
		return actionNames;
	}

	/**
	 * Generate list with action names.
	 */
	public static final HashSet<String> generateActionNames() {
		if (actionNames.isEmpty()) {
			for (int i = 0; i < beanFactory.getBeanDefinitionNames().length; i++) {
				if (beanFactory
						.getBean(beanFactory.getBeanDefinitionNames()[i])
						.toString().contains("container")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/platform/compute/action#"
							+ beanFactory.getBeanDefinitionNames()[i]);
				}
			}
		}
		return actionNames;
	}

	/**
	 * Generate list with actions.
	 */
	public static final HashSet<Action> generateActionSet() {
		if (actionSet.isEmpty()) {
			for (int i = 0; i < beanFactory.getBeanDefinitionNames().length; i++) {
				if (beanFactory
						.getBean(beanFactory.getBeanDefinitionNames()[i])
						.toString().contains("container")) {
					actionSet.add((Action) beanFactory.getBean(beanFactory
							.getBeanDefinitionNames()[i]));
				}
			}
		}
		return actionSet;
	}

	/**
	 * Generate attribute List.
	 */
	public static final void generateAttributeList() {
		if (attributes.isEmpty()) {
			// add all attributes to attribute list
			attributes.add("occi.container.name");
			attributes.add("occi.container.version");
			attributes.add("occi.container.architecture");
			attributes.add("occi.container.state");
		}
	}
	
	/**
	 * Return the container attributes.
	 * 
	 * @return attributes
	 */
	public static final HashSet<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param start the start to set
	 */
	public final void setStart(Action start) {
		this.start = start;
	}

	/**
	 * @return the start
	 */
	public final Action getStart() {
		return start;
	}

	/**
	 * @param stop the stop to set
	 */
	public final void setStop(Action stop) {
		this.stop = stop;
	}

	/**
	 * @return the stop
	 */
	public final Action getStop() {
		return stop;
	}

	/**
	 * @param restart the restart to set
	 */
	public final void setRestart(Action restart) {
		this.restart = restart;
	}

	/**
	 * @return the restart
	 */
	public final Action getRestart() {
		return restart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}
}