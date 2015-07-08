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

import javax.naming.NamingException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;

/**
 * @author nguyen_n
 *
 */
public class Database extends Resource {

	private String name;
	
	private String version;
	
	public enum Architecture {
		x86, x64
	}
	
	private Architecture architecture;
	
	public enum Type {
		relational, keyValue, document, graph 
	}
	
	private Type type;
	
	/**
	 * Current State of the instance
	 */
	public enum State {
		available, unavailable
	}
	
	private State state;



	/**
	 * Static Hashmap of all Compute Resources. The Key is a UUID, the Value a
	 * Compute Object.
	 */
	private static Map<UUID, Database> databaseList = new HashMap<UUID, Database>();

	/**
	 * Static HashSet of all database attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the database resource.
	 */
	private final UUID uuid;

	/*
	 * All possible database actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiPaasConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action backup = (Action) beanFactory.getBean("backup");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public Database(String name, Type type, Architecture architecture, String version, State state, Set<String> attributes)
			throws URISyntaxException, NumberFormatException,
			IllegalArgumentException, NamingException {
		super("Database", links, attributes);
		this.architecture = architecture;
		this.name = name;
		this.type = type;
		this.version = version;
		this.state = state;

		generateActionNames();

		// check if all attributes are correct
		if (name.length() == 0) {
			throw new NamingException(
					"Name of the Database resource can not be null");
		}
		/*
		 * set Category
		 */
		setKind(new Kind(actionSet, null, null, null, "database", "Database",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/paas#", attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into database list
		databaseList.put(uuid, this);

		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Return the full databaseList
	 * 
	 * @return all database resources
	 */
	public static final Map<UUID, Database> getDatabaseList() {
		return databaseList;
	}

	/**
	 * Set database list
	 * 
	 * @param databaseList
	 */
	public static final void setComputeList(Map<UUID, Database> databaseList) {
		Database.databaseList = databaseList;
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
	 * Returns the Name represented as a String of the current Instance
	 * 
	 * @return Name as a String
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the Name of the current Instance
	 * 
	 * @param name
	 *            of the current Instance as a String
	 */
	public final void setName(String name) {
		this.name = name;
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
	
	public final Type getType() {
		return type;
	}
	
	public final void setType(Type type) {
		this.type = type;
	}

	public final String getVersion() {
		return version;
	}
	
	public final void setVersion(String version) {
		this.version = version;
	}
	

	/**
	 * Returns the Architecture of the current Instance
	 * 
	 * @return architecture of the current Instance of enum-type Architectures
	 */
	public final Architecture getArchitecture() {
		return architecture;
	}

	/**
	 * Sets the Architecture of the current Instance
	 * 
	 * @param architecture
	 *            of the current Instance
	 */
	public final void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
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
						.toString().contains("database")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/database/action#"
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
						.toString().contains("database")) {
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
			attributes.add("occi.database.name");
			attributes.add("occi.database.type");
			attributes.add("occi.database.architecture");
			attributes.add("occi.database.version");
			attributes.add("occi.database.state");
		}
	}
	
	/**
	 * Return the database attributes.
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

	/**
	 * @param backup the backup to set
	 */
	public final void setBackup(Action backup) {
		this.backup = backup;
	}

	/**
	 * @return the backup
	 */
	public final Action getBackup() {
		return backup;
	}
}
