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

import javax.naming.directory.SchemaViolationException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;
import telecom.sudparis.occi.autonomic.interfaces.RuleSetMixinToolInterface;
import telecom.sudparis.occi.autonomic.tools.Notification;

import com.berniecode.mixin4j.MixinBase;

@MixinBase
public abstract class Analyzer extends Resource implements
		RuleSetMixinToolInterface {

	/**
	 * State of the compute resource
	 */
	private State state;

	/**
	 * Current State of the instance
	 */
	public enum State {
		unavailable, available, reconfiguring
	}

	/**
	 * Static Hashmap of all Analyzer Resources. The Key is a UUID, the Value a
	 * Analyzer Object.
	 */
	private static Map<UUID, Analyzer> analyzerList = new HashMap<UUID, Analyzer>();

	/**
	 * Static HashSet of all analyzer attributes.
	 */
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the compute resource.
	 */
	private final UUID uuid;

	/*
	 * All possible analyzer actions.
	 */
	private static XmlBeanFactory beanFactory = new XmlBeanFactory(
			new ClassPathResource("occiAutonomicAnalyzerConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action reconfigure = (Action) beanFactory.getBean("reconfigure");

	private String name;

	private String version;
	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public Analyzer(String name, String version, State state, Set<String> set)
			throws SchemaViolationException, URISyntaxException {

		super(name, set);
		generateActionNames();
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// TODO expected error: incompatible variable attributes

		setKind(new Kind(actionSet, null, null, null, "analyzer", "Analyzer",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/autonomic#", attributes));
		getKind().setActionNames(actionNames);

		setId(new URI(uuid.toString()));
		// put resource into compute list
		analyzerList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();
	}

	public void notify(Notification notification) {
		System.out.println("Analyzer: got the notification ");

		analyze(notification);
	}

	public static Map<UUID, Analyzer> getAnalyzerList() {
		// TODO Auto-generated method stub
		return analyzerList;
	}

	/**
	 * Set container list
	 * 
	 * @param containerList
	 */
	public static final void setAnalyzerList(Map<UUID, Analyzer> analyzerList) {
		Analyzer.analyzerList = analyzerList;
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
							+ "/autonomic/analyzer/action#"
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
						.toString().contains("analyzer")) {
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
			attributes.add("occi.analyzer.name");
			attributes.add("occi.analyzer.version");

		}
	}

	/**
	 * Return the Analyzer attributes.
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
	 * @param stop
	 *            the stop to set
	 */
	public final void setReconfigure(Action reconfigure) {
		this.reconfigure = reconfigure;
	}

	/**
	 * @return the stop
	 */
	public final Action getReconfigure() {
		return reconfigure;
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
}
