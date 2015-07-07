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
import occi.platform.Container;
import occi.platform.Database;
import occi.platform.links.DatabaseLink;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * The Environment type represents a generic Information resource. This
 * Application type Inherits the Resource base type defined in OCCI Core
 * Specification. Application is assigned to the Kind instance. [T. Metsch, A.
 * Edmonds - Open Cloud Computing Interface - Infrastructure,
 * http://ogf.org/documents/GFD.184.pdf, Apr. 2011]
 * 
 * @author Sami Yangui
 * 
 */
public class Environment extends Resource {
	/**
	 * Name of the instance
	 */
	public String name;
	
	/**
	 * the ID used by COAPS
	 */
	private String coapsID=null;

	/**
	 * Description of the instance
	 */
	public String description;

	/**
	 * Dedicated memory to the instance
	 */
	public int memory;

	/**
	 * Variables related to the instance
	 */
	public HashMap<String, String> variables;

	/**
	 * List of Containers instances related to the current Environment instance
	 */
	HashMap<UUID,Container> containersList = new HashMap<UUID, Container>();

	/**
	 * List of Databases instances related to the current Environment instance
	 */
	HashMap<UUID,Database> databasesList = new HashMap<UUID, Database>();
	
	/**
	 * List of DatabaseLink instances related to the current Environment instance
	 */
	HashMap<UUID,DatabaseLink> databasesLink = new HashMap<UUID, DatabaseLink>();

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
	 * Static Hashmap of all Environment Resources. The Key is a UUID, the Value
	 * an Environment Object.
	 */
	private static Map<UUID, Environment> environmentList = new HashMap<UUID, Environment>();

	/**
	 * Static HashSet of all Environment attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the environment resource.
	 */
	private final UUID uuid;

	/*
	 * All possible Environment actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiEnvironmentConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action update = (Action) beanFactory.getBean("update");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public Environment(String name, String description, int memory,
			HashMap<String, String> variables, HashMap<UUID,Container> containersList,
			HashMap<UUID,Database> databasesList, HashMap<UUID,DatabaseLink> databasesLink,State state)
			throws URISyntaxException, SchemaViolationException {
		super("Environment", links, attributes);
		this.name = name;
		this.description = description;
		this.memory = memory;
		this.variables = variables;
		this.containersList = containersList;
		this.databasesList = databasesList;
		this.databasesLink = databasesLink;
		this.state = state;

		generateActionNames();

		/*
		 * set Category
		 */
		setKind(new Kind(actionSet, null, null, null, "environment",
				"Environment",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/environment#", attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into an Environment list
		environmentList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Return the full environmentList
	 * 
	 * @return all environment resources
	 */
	public static final Map<UUID, Environment> getEnvironmentList() {
		return environmentList;
	}

	/**
	 * Set environment list
	 * 
	 * @param environmentList
	 * 
	 */
	public static final void setEnvironmentList(
			Map<UUID, Environment> environmentList) {
		Environment.environmentList = environmentList;
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
						.toString().contains("environment")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/environment/action#"
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
						.toString().contains("environment")) {
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
			attributes.add("occi.environment.name");
			attributes.add("occi.environment.description");
			attributes.add("occi.environment.memory");
			attributes.add("occi.environment.variables");
			attributes.add("occi.environment.containersList");
			attributes.add("occi.environment.databasesList");
			attributes.add("occi.environment.databasesLink");
			attributes.add("occi.environment.state");
		}
	}

	/**
	 * Return the environment attributes.
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int instances) {
		this.memory = memory;
	}

	public HashMap<String, String> getVariables() {
		return variables;
	}

	public void setVariables(HashMap<String, String> variables) {
		this.variables = variables;
	}

	public HashMap<UUID,Container> getContainersList() {
		return containersList;
	}

	public void setContainersList(HashMap<UUID,Container> containersList) {
		this.containersList = containersList;
	}

	public HashMap<UUID,Database> getDatabasesList() {
		return databasesList;
	}

	public void setDatabasesList(HashMap<UUID,Database> databasesList) {
		this.databasesList = databasesList;
	}
	public HashMap<UUID,DatabaseLink> getDatabasesLink() {
		return databasesLink;
	}

	public void setDatabasesLink(HashMap<UUID,DatabaseLink> databasesLink) {
		this.databasesLink = databasesLink;
	}
	
	public String getCoapsID() {
		return coapsID;
	}

	public void setCoapsID(String coapsID) {
		this.coapsID = coapsID;
	}
}
