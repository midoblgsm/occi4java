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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * The Application type represents a generic Information resource. This
 * Application type Inherits the Resource base type defined in OCCI Core
 * Specification. Application is assigned to the Kind instance. [T. Metsch, A.
 * Edmonds - Open Cloud Computing Interface - Infrastructure,
 * http://ogf.org/documents/GFD.184.pdf, Apr. 2011]
 * 
 * @author Sami Yangui
 * 
 */
public  class Application extends Resource  {
	/**
	 * Name of the instance
	 */
	public String name;

	/**
	 * list of deployables
	 */
	public ArrayList<Deployable> depList;

	/**
	 * the ID used by COAPS
	 */
	private String coapsID=null;

	/**
	 * Description of the instance
	 */
	public String description;

	/**
	 * Number of instances
	 */
	public int instances;
	
	/**
	 * instance URL
	 */
	public String url;

	/**
	 * start_command (Only for standalone Application instance)
	 */
	public String start_command;

	/**
	 * State of the compute resource
	 */
	private State state;

	/**
	 * Current State of the instance
	 */
	public enum State {
		unavailable, available
	}

	/**
	 * Static Hashmap of all Application Resources. The Key is a UUID, the Value
	 * an Application Object.
	 */
	private static Map<UUID, Application> applicationList = new HashMap<UUID, Application>();

	/**
	 * Static HashSet of all Application attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the Application resource.
	 */
	private final UUID uuid;

	/*
	 * All possible Application actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiApplicationConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action update = (Action) beanFactory.getBean("update");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public Application(String name, String description, int instances,
			String url, String start_command, State state,ArrayList<Deployable> depList)
			throws URISyntaxException, SchemaViolationException {
		super("Application", links, attributes);
		this.name = name;
		this.description = description;
		this.instances = instances;
		this.url = url;
		this.start_command = start_command;
		this.state = state;
		this.depList=depList;

		generateActionNames();

		/*
		 * set Category
		 */
		setKind(new Kind(actionSet, null, null, null, "application",
				"Application",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/application#", attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into an Application list
		applicationList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Return the full applicationList
	 * 
	 * @return all application resources
	 */
	public static final Map<UUID, Application> getApplicationList() {
		return applicationList;
	}

	/**
	 * Set application list
	 * 
	 * @param applicationList
	 * 
	 */
	public static final void setApplicationList(
			Map<UUID, Application> applicationList) {
		Application.applicationList = applicationList;
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
						.toString().contains("application")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/application/action#"
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
						.toString().contains("application")) {
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
			attributes.add("occi.application.name");
			attributes.add("occi.application.description");
			attributes.add("occi.application.instances");
			attributes.add("occi.application.memory");
			attributes.add("occi.application.url");
			attributes.add("occi.application.start_command");
			attributes.add("occi.application.state");
		}
	}

	/**
	 * Return the application attributes.
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

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getStart_command() {
		return start_command;
	}

	public void setStart_command(String start_command) {
		this.start_command = start_command;
	}
	
	public String getCoapsID() {
		return coapsID;
	}

	public void setCoapsID(String coapsID) {
		this.coapsID = coapsID;
	}
	
	//TODO get the envId
	
	public String getCoapsEnvID() {
		return "1";
	}
	public ArrayList<Deployable> getDepList() {
		return depList;
	}

	public void setDepList(ArrayList<Deployable> depList) {
		this.depList = depList;
	}

	
}