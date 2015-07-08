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
package telecom.sudparis.occi.autonomic;

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

import telecom.sudparis.occi.autonomic.interfaces.StrategySetMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Alert;
import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;

import com.berniecode.mixin4j.MixinBase;

@MixinBase
public abstract class ReconfigurationManager extends Resource implements
		StrategySetMixinInterface {
	private String name;

	private String version;

	/**
	 * Current State of the instance
	 */
	public enum State {
		available, unavailable, reconfiguring
	}

	private State state;

	/**
	 * Static Hashmap of all ReconfigurationManager Resources. The Key is a UUID, the Value a
	 * Compute Object.
	 */
	private static Map<UUID, ReconfigurationManager> reconfigurationManagerList = new HashMap<UUID, ReconfigurationManager>();

	/**
	 * Static HashSet of all ReconfigurationManager attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the ReconfigurationManager resource.
	 */
	private final UUID uuid;

	/*
	 * All possible database actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiAutonomicReconfigurationManConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action create = (Action) beanFactory.getBean("create");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action reconfigure = (Action) beanFactory.getBean("reconfigure");

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();


	public ReconfigurationManager(String name, String version, State state, Set<String> set)
			throws URISyntaxException, NamingException {
	
		super(name,set);

		this.name = name;

		this.version = version;
		this.state = state;

		generateActionNames();

		// check if all attributes are correct
		if (name.length() == 0) {
			throw new NamingException(
					"Name of the ReconfigurationManager resource can not be null");
		}
		/*
		 * set Category
		 */
		//TODO expected error: incompatible variable attributes 
		setKind(new Kind(actionSet, null, null, null, "reconfigurationmanager", "ReconfigurationManager",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/autonomic#", (Set<String>) attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into database list
		reconfigurationManagerList.put(uuid, this);

		// Generate attribute list
		generateAttributeList();

	}

	public void alert(Alert alert) {
		System.out.println("Reconfiguration Manager:: alert received");

		applyStrategy(alert);

	}


	/**
	 * Return the full databaseList
	 * 
	 * @return all database resources
	 */
	public static final Map<UUID, ReconfigurationManager> getReconfigurationManagerList() {
		return reconfigurationManagerList;
	}

	/**
	 * Set database list
	 * 
	 * @param databaseList
	 */
	public static final void setReconfigurationManagerList(
			Map<UUID, ReconfigurationManager> reconfigurationManagerList) {
		ReconfigurationManager.reconfigurationManagerList = reconfigurationManagerList;
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

	public final String getVersion() {
		return version;
	}

	public final void setVersion(String version) {
		this.version = version;
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
						.toString().toLowerCase().contains("econfigurationmanager")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/reconfigurationmanager/action#"
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
						.toString().toLowerCase().contains("reconfigurationmanager")) {
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
			attributes.add("occi.database.strategies");
			attributes.add("occi.database.activation");
		
		}
	}

	/**
	 * Return the ReconfigurationManager attributes.
	 * 
	 * @return attributes
	 */
	public static final HashSet<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param start
	 *            the start to set
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
	 * @param backup
	 *            the backup to set
	 */
	public final void setReconfigure(Action reconfigure) {
		this.reconfigure = reconfigure;
	}

	/**
	 * @return the backup
	 */
	public final Action getReconfigure() {
		return reconfigure;
	}

	/**
	 * @return the create
	 */
	public Action getCreate() {
		return create;
	}

	/**
	 * @param create the create to set
	 */
	public void setCreate(Action create) {
		this.create = create;
	}

}
