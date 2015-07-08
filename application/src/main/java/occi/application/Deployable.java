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
package occi.application;

/**
 * Copyright (C) 2010-2011 Sebastian Heckmann, Sebastian Laag
 *
 * Contact Email: <sebastian.heckmann@udo.edu>, <sebastian.laag@udo.edu>
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.naming.directory.SchemaViolationException;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * The Deployables type represents a generic Information resource. This type
 * Inherits the Resource base type defined in OCCI Core Specification.
 * Deployables is assigned to the Kind instance. [T. Metsch, A. Edmonds - Open
 * Cloud Computing Interface - Infrastructure,
 * http://ogf.org/documents/GFD.184.pdf, Apr. 2011]
 * 
 * @author Sami Yangui
 * 
 */
public class Deployable extends Resource {
	/**
	 * Name of the instance
	 */
	public String name;

	/**
	 * Content_type of the instance
	 */
	public Content_type content_type;

	/**
	 * List values of content_type
	 */
	public enum Content_type {
		artifact, config_folder, file
	}

	/**
	 * local folder of the instance
	 */
	public String location;

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
	 * Static Hashmap of all Deployable Resources. The Key is a UUID, the Value
	 * a Deployable Object.
	 */
	private static Map<UUID, Deployable> deployablesList = new HashMap<UUID, Deployable>();

	/**
	 * Static HashSet of all Deployable attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the Deployable resource.
	 */
	private final UUID uuid;

	/*
	 * All possible Deployable actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiDeployablesConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action update = (Action) beanFactory.getBean("update");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public Deployable(String name, Content_type content_type, String location,
			State state) throws URISyntaxException, SchemaViolationException {
		super("Deployables", links, attributes);
		this.name = name;
		this.content_type = content_type;
		this.location = location;
		this.state = state;
		generateActionNames();

		/*
		 * set Category
		 */
		setKind(new Kind(actionSet, null, null, null, "deployables",
				"Environment",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/deployables#", attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into a Deployables list
		deployablesList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Return the full deployablesList
	 * 
	 * @return all Deployables resources
	 */
	public static final Map<UUID, Deployable> getDeployablesList() {
		return deployablesList;
	}

	/**
	 * Set deployablesList
	 * 
	 * @param deployablesList
	 * 
	 */
	public static final void setDeployablesList(
			Map<UUID, Deployable> deployablesList) {
		Deployable.deployablesList = deployablesList;
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
	 *            of the current Instance
	 * 
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
						.toString().contains("deployables")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/deployables/action#"
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
						.toString().contains("deployables")) {
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
			attributes.add("occi.deployables.name");
			attributes.add("occi.deployables.content_type");
			attributes.add("occi.deployables.location");
			attributes.add("occi.deployables.state");
		}
	}

	/**
	 * Return the Deployables attributes.
	 * 
	 * @return attributes
	 */
	public static final HashSet<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param start
	 *            the start to set
	 * 
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
	 * @param stop
	 *            the stop to set
	 * 
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
	 * @param restart
	 *            the restart to set
	 * 
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

	/**
	 * @param restart
	 *            the restart to set
	 * 
	 */
	public final void setUpdate(Action update) {
		this.update = update;
	}

	/**
	 * @return the update
	 */
	public final Action getUpdate() {
		return update;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Content_type getContent_type() {
		return content_type;
	}

	public void setContent_type(Content_type content_type) {
		this.content_type = content_type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
