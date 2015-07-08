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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Resource;
import telecom.sudparis.occi.autonomic.interfaces.SpecificSLAMixinInterface;
import telecom.sudparis.occi.autonomic.tools.Subscription;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

import com.berniecode.mixin4j.MixinBase;

@MixinBase
public abstract class AutonomicManager extends Resource implements
		SpecificSLAMixinInterface {

	private String slaLocation;
	private String containerName;
	private String containerVersion;
	private String containerArchitecture;
	private String databaseName;
	private String databaseVersion;
	private String databaseArchitecture;
	private String databaseType;
	private String databaseLinkName;
	private String environmentName;
	private String environmentMemory;
	private String environmentVariables;
	private String deployableName;
	private String deployableType;
	private String deployableLocation;
	private String applicationName;
	private String applicationDescription;
	private String applicationInstances;
	private String analyzerName;
	private String analyzerVersion;
	private int maxThreshold;
	private int	minThreshold;
	private int aggregationInterval;
	private String subscriptionDuration;
	private String subscriptionInterval;
	private String reconfigurationManagerName;
	private String reconfigurationManagerVersion;
	private String subscriptionLinkName;
	private String subscriptionLinkVersion;
	private String notificationLinkName;
	private String notificationLinkVersion;
	private String notificationRefreshRate;
	private String alertLinkName;
	private String alertLinkVersion;
	private String actionLinkName;
	private String actionLinkVersion;

	
	public void establishAutonomicLoop() {

		
		/**
		 * 
		 */
		this.inspectSLA(slaLocation);

		 postContainer();
		 postDataBase();
		 postDatabaseLink();
		 postDeployable();
		 postEnvironment();
		 postMixedApplication();
		 postEnvironmentLink();
		 startApplication();
		 postAnalyzer();
		 postReconfigurationManager();
		 postSubscriptionLink();
		 postAlertLink();
		 postActionLink();
		 createSubscription();
		 System.out.println("Infrastructure established");
	}
	
	
	
	/**
	 * @return the containerName
	 */
	public String getContainerName() {
		return containerName;
	}

	/**
	 * @param containerName
	 *            the containerName to set
	 */
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	/**
	 * @return the containerVersion
	 */
	public String getContainerVersion() {
		return containerVersion;
	}

	/**
	 * @param containerVersion
	 *            the containerVersion to set
	 */
	public void setContainerVersion(String containerVersion) {
		this.containerVersion = containerVersion;
	}

	/**
	 * @return the containerArchitecture
	 */
	public String getContainerArchitecture() {
		return containerArchitecture;
	}

	/**
	 * @param containerArchitecture
	 *            the containerArchitecture to set
	 */
	public void setContainerArchitecture(String containerArchitecture) {
		this.containerArchitecture = containerArchitecture;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @return the databaseVersion
	 */
	public String getDatabaseVersion() {
		return databaseVersion;
	}

	/**
	 * @param databaseVersion
	 *            the databaseVersion to set
	 */
	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	/**
	 * @return the databaseArchitecture
	 */
	public String getDatabaseArchitecture() {
		return databaseArchitecture;
	}

	/**
	 * @param databaseArchitecture
	 *            the databaseArchitecture to set
	 */
	public void setDatabaseArchitecture(String databaseArchitecture) {
		this.databaseArchitecture = databaseArchitecture;
	}

	/**
	 * @return the databaseType
	 */
	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param databaseType
	 *            the databaseType to set
	 */
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * @return the databaseLinkName
	 */
	public String getDatabaseLinkName() {
		return databaseLinkName;
	}

	/**
	 * @param databaseLinkName
	 *            the databaseLinkName to set
	 */
	public void setDatabaseLinkName(String databaseLinkName) {
		this.databaseLinkName = databaseLinkName;
	}

	/**
	 * @return the environmentMemory
	 */
	public String getEnvironmentMemory() {
		return environmentMemory;
	}

	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}



	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}



	/**
	 * @param environmentMemory
	 *            the environmentMemory to set
	 */
	public void setEnvironmentMemory(String environmentMemory) {
		this.environmentMemory = environmentMemory;
	}

	/**
	 * @return the environmentVariables
	 */
	public String getEnvironmentVariables() {
		return environmentVariables;
	}

	/**
	 * @param environmentVariables
	 *            the environmentVariables to set
	 */
	public void setEnvironmentVariables(String environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	/**
	 * @return the deployableName
	 */
	public String getDeployableName() {
		return deployableName;
	}

	/**
	 * @param deployableName
	 *            the deployableName to set
	 */
	public void setDeployableName(String deployableName) {
		this.deployableName = deployableName;
	}

	/**
	 * @return the deployableType
	 */
	public String getDeployableType() {
		return deployableType;
	}

	/**
	 * @param deployableType
	 *            the deployableType to set
	 */
	public void setDeployableType(String deployableType) {
		this.deployableType = deployableType;
	}

	/**
	 * @return the deployableLocation
	 */
	public String getDeployableLocation() {
		return deployableLocation;
	}

	/**
	 * @param deployableLocation
	 *            the deployableLocation to set
	 */
	public void setDeployableLocation(String deployableLocation) {
		this.deployableLocation = deployableLocation;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the applicationDescription
	 */
	public String getApplicationDescription() {
		return applicationDescription;
	}

	/**
	 * @param applicationDescription
	 *            the applicationDescription to set
	 */
	public void setApplicationDescription(String applicationDescription) {
		this.applicationDescription = applicationDescription;
	}

	/**
	 * @return the applicationInstances
	 */
	public String getApplicationInstances() {
		return applicationInstances;
	}

	/**
	 * @param applicationInstances
	 *            the applicationInstances to set
	 */
	public void setApplicationInstances(String applicationInstances) {
		this.applicationInstances = applicationInstances;
	}

	/**
	 * @return the analyzerName
	 */
	public String getAnalyzerName() {
		return analyzerName;
	}

	/**
	 * @param analyzerName
	 *            the analyzerName to set
	 */
	public void setAnalyzerName(String analyzerName) {
		this.analyzerName = analyzerName;
	}

	/**
	 * @return the analyzerVersion
	 */
	public String getAnalyzerVersion() {
		return analyzerVersion;
	}

	/**
	 * @param analyzerVersion
	 *            the analyzerVersion to set
	 */
	public void setAnalyzerVersion(String analyzerVersion) {
		this.analyzerVersion = analyzerVersion;
	}

	/**
	 * @return the subscriptionDuration
	 */
	public String getSubscriptionDuration() {
		return subscriptionDuration;
	}

	/**
	 * @param subscriptionDuration
	 *            the subscriptionDuration to set
	 */
	public void setSubscriptionDuration(String subscriptionDuration) {
		this.subscriptionDuration = subscriptionDuration;
	}

	/**
	 * @return the subscriptionInterval
	 */
	public String getSubscriptionInterval() {
		return subscriptionInterval;
	}

	/**
	 * @param subscriptionInterval
	 *            the subscriptionInterval to set
	 */
	public void setSubscriptionInterval(String subscriptionInterval) {
		this.subscriptionInterval = subscriptionInterval;
	}

	/**
	 * @return the reconfigurationManagerName
	 */
	public String getReconfigurationManagerName() {
		return reconfigurationManagerName;
	}

	/**
	 * @param reconfigurationManagerName
	 *            the reconfigurationManagerName to set
	 */
	public void setReconfigurationManagerName(String reconfigurationManagerName) {
		this.reconfigurationManagerName = reconfigurationManagerName;
	}

	/**
	 * @return the reconfigurationManagerVersion
	 */
	public String getReconfigurationManagerVersion() {
		return reconfigurationManagerVersion;
	}

	/**
	 * @param reconfigurationManagerVersion
	 *            the reconfigurationManagerVersion to set
	 */
	public void setReconfigurationManagerVersion(
			String reconfigurationManagerVersion) {
		this.reconfigurationManagerVersion = reconfigurationManagerVersion;
	}

	/**
	 * @return the subscriptionLinkName
	 */
	public String getSubscriptionLinkName() {
		return subscriptionLinkName;
	}

	/**
	 * @param subscriptionLinkName
	 *            the subscriptionLinkName to set
	 */
	public void setSubscriptionLinkName(String subscriptionLinkName) {
		this.subscriptionLinkName = subscriptionLinkName;
	}

	/**
	 * @return the subscriptionLinkVersion
	 */
	public String getSubscriptionLinkVersion() {
		return subscriptionLinkVersion;
	}

	/**
	 * @param subscriptionLinkVersion
	 *            the subscriptionLinkVersion to set
	 */
	public void setSubscriptionLinkVersion(String subscriptionLinkVersion) {
		this.subscriptionLinkVersion = subscriptionLinkVersion;
	}

	/**
	 * @return the notificationLinkName
	 */
	public String getNotificationLinkName() {
		return notificationLinkName;
	}

	/**
	 * @param notificationLinkName
	 *            the notificationLinkName to set
	 */
	public void setNotificationLinkName(String notificationLinkName) {
		this.notificationLinkName = notificationLinkName;
	}

	/**
	 * @return the notificationLinkVersion
	 */
	public String getNotificationLinkVersion() {
		return notificationLinkVersion;
	}

	/**
	 * @param notificationLinkVersion
	 *            the notificationLinkVersion to set
	 */
	public void setNotificationLinkVersion(String notificationLinkVersion) {
		this.notificationLinkVersion = notificationLinkVersion;
	}

	/**
	 * @return the notificationRefreshRate
	 */
	public String getNotificationRefreshRate() {
		return notificationRefreshRate;
	}

	/**
	 * @param notificationRefreshRate
	 *            the notificationRefreshRate to set
	 */
	public void setNotificationRefreshRate(String notificationRefreshRate) {
		this.notificationRefreshRate = notificationRefreshRate;
	}

	/**
	 * @return the alertLinkName
	 */
	public String getAlertLinkName() {
		return alertLinkName;
	}

	/**
	 * @param alertLinkName
	 *            the alertLinkName to set
	 */
	public void setAlertLinkName(String alertLinkName) {
		this.alertLinkName = alertLinkName;
	}

	/**
	 * @return the alertLinkVersion
	 */
	public String getAlertLinkVersion() {
		return alertLinkVersion;
	}

	/**
	 * @param alertLinkVersion
	 *            the alertLinkVersion to set
	 */
	public void setAlertLinkVersion(String alertLinkVersion) {
		this.alertLinkVersion = alertLinkVersion;
	}

	/**
	 * @return the actionLinkName
	 */
	public String getActionLinkName() {
		return actionLinkName;
	}

	/**
	 * @param actionLinkName
	 *            the actionLinkName to set
	 */
	public void setActionLinkName(String actionLinkName) {
		this.actionLinkName = actionLinkName;
	}

	/**
	 * @return the actionLinkVersion
	 */
	public String getActionLinkVersion() {
		return actionLinkVersion;
	}

	/**
	 * @param actionLinkVersion
	 *            the actionLinkVersion to set
	 */
	public void setActionLinkVersion(String actionLinkVersion) {
		this.actionLinkVersion = actionLinkVersion;
	}

	/**
	 * @return the subscription
	 */
	public static Subscription getSubscription() {
		return subscription;
	}

	/**
	 * @param subscription
	 *            the subscription to set
	 */
	public static void setSubscription(Subscription subscription) {
		AutonomicManager.subscription = subscription;
	}

	/**
	 * @return the host
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public static void setHost(String host) {
		AutonomicManager.host = host;
	}

	/**
	 * @return the deployableURI
	 */
	public static String getDeployableURI() {
		return deployableURI;
	}

	/**
	 * @param deployableURI
	 *            the deployableURI to set
	 */
	public static void setDeployableURI(String deployableURI) {
		AutonomicManager.deployableURI = deployableURI;
	}

	/**
	 * @return the applicationURI
	 */
	public static String getApplicationURI() {
		return applicationURI;
	}

	/**
	 * @param applicationURI
	 *            the applicationURI to set
	 */
	public static void setApplicationURI(String applicationURI) {
		AutonomicManager.applicationURI = applicationURI;
	}

	/**
	 * @return the containerURI
	 */
	public static String getContainerURI() {
		return containerURI;
	}

	/**
	 * @param containerURI
	 *            the containerURI to set
	 */
	public static void setContainerURI(String containerURI) {
		AutonomicManager.containerURI = containerURI;
	}

	/**
	 * @return the databaseURI
	 */
	public static String getDatabaseURI() {
		return databaseURI;
	}

	/**
	 * @param databaseURI
	 *            the databaseURI to set
	 */
	public static void setDatabaseURI(String databaseURI) {
		AutonomicManager.databaseURI = databaseURI;
	}

	/**
	 * @return the databaseLinkURI
	 */
	public static String getDatabaseLinkURI() {
		return databaseLinkURI;
	}

	/**
	 * @param databaseLinkURI
	 *            the databaseLinkURI to set
	 */
	public static void setDatabaseLinkURI(String databaseLinkURI) {
		AutonomicManager.databaseLinkURI = databaseLinkURI;
	}

	/**
	 * @return the environmentURI
	 */
	public static String getEnvironmentURI() {
		return environmentURI;
	}

	/**
	 * @param environmentURI
	 *            the environmentURI to set
	 */
	public static void setEnvironmentURI(String environmentURI) {
		AutonomicManager.environmentURI = environmentURI;
	}

	/**
	 * @return the environmentLinkURI
	 */
	public static String getEnvironmentLinkURI() {
		return environmentLinkURI;
	}

	/**
	 * @param environmentLinkURI
	 *            the environmentLinkURI to set
	 */
	public static void setEnvironmentLinkURI(String environmentLinkURI) {
		AutonomicManager.environmentLinkURI = environmentLinkURI;
	}

	/**
	 * @return the analyzerURI
	 */
	public static String getAnalyzerURI() {
		return analyzerURI;
	}

	/**
	 * @param analyzerURI
	 *            the analyzerURI to set
	 */
	public static void setAnalyzerURI(String analyzerURI) {
		AutonomicManager.analyzerURI = analyzerURI;
	}

	/**
	 * @return the reconfigurationManagerURI
	 */
	public static String getReconfigurationManagerURI() {
		return reconfigurationManagerURI;
	}

	/**
	 * @param reconfigurationManagerURI
	 *            the reconfigurationManagerURI to set
	 */
	public static void setReconfigurationManagerURI(
			String reconfigurationManagerURI) {
		AutonomicManager.reconfigurationManagerURI = reconfigurationManagerURI;
	}

	/**
	 * @return the subscriptionLinkURI
	 */
	public static String getSubscriptionLinkURI() {
		return subscriptionLinkURI;
	}

	/**
	 * @param subscriptionLinkURI
	 *            the subscriptionLinkURI to set
	 */
	public static void setSubscriptionLinkURI(String subscriptionLinkURI) {
		AutonomicManager.subscriptionLinkURI = subscriptionLinkURI;
	}

	/**
	 * @return the notificationLinkURI
	 */
	public static String getNotificationLinkURI() {
		return notificationLinkURI;
	}

	/**
	 * @param notificationLinkURI
	 *            the notificationLinkURI to set
	 */
	public static void setNotificationLinkURI(String notificationLinkURI) {
		AutonomicManager.notificationLinkURI = notificationLinkURI;
	}

	/**
	 * @return the beanFactory
	 */
	public static XmlBeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param beanFactory
	 *            the beanFactory to set
	 */
	public static void setBeanFactory(XmlBeanFactory beanFactory) {
		AutonomicManager.beanFactory = beanFactory;
	}

	/**
	 * @return the autonomicManagerName
	 */
	public String getAutonomicManagerName() {
		return autonomicManagerName;
	}

	/**
	 * @param autonomicManagerName
	 *            the autonomicManagerName to set
	 */
	public void setAutonomicManagerName(String autonomicManagerName) {
		this.autonomicManagerName = autonomicManagerName;
	}

	/**
	 * @return the autonomicManagerVersion
	 */
	public String getAutonomicManagerVersion() {
		return autonomicManagerVersion;
	}

	/**
	 * @param autonomicManagerVersion
	 *            the autonomicManagerVersion to set
	 */
	public void setAutonomicManagerVersion(String autonomicManagerVersion) {
		this.autonomicManagerVersion = autonomicManagerVersion;
	}

	/**
	 * @return the actionset
	 */
	public static HashSet<Action> getActionset() {
		return actionSet;
	}

	/**
	 * @return the actionnames
	 */
	public static HashSet<String> getActionnames() {
		return actionNames;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public static void setAttributes(HashSet<String> attributes) {
		AutonomicManager.attributes = attributes;
	}

	/**
	 * @param reconfigure
	 *            the reconfigure to set
	 */
	public void setReconfigure(Action reconfigure) {
		this.reconfigure = reconfigure;
	}

	/**
	 * 
	 */
	private static String applicationId;
	private static Subscription subscription = null;
	/***
	 * 
	 */
	private static String host = "http://157.159.110.227:8182";
	/**
	 * 
	 */
	private static String deployableURI = "";
	private static String applicationURI = "";
	private static String containerURI = "";
	private static String databaseURI = "";
	private static String databaseLinkURI = "";
	private static String environmentURI = "";
	private static String environmentLinkURI = "";
	private static String analyzerURI = "";
	private static String reconfigurationManagerURI = "";
	private static String subscriptionLinkURI = "http://157.159.110.227:8182/subscriptionlink/subscribe/";
	private static String notificationLinkURI = "";

	/**
	 * Current State of the instance
	 */
	public enum State {
		available, unavailable, reconfiguring
	}

	private State state;

	/**
	 * Static Hashmap of all AutonomicManager Resources. The Key is a UUID, the
	 * Value a AutonomicManager Object.
	 */
	private static Map<UUID, AutonomicManager> autonomicManagerList = new HashMap<UUID, AutonomicManager>();

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
			new ClassPathResource("occiAutonomicManConfig.xml"));
	private Action start = (Action) beanFactory.getBean("start");
	private Action create = (Action) beanFactory.getBean("create");
	private Action delete = (Action) beanFactory.getBean("delete");
	private Action stop = (Action) beanFactory.getBean("stop");
	private Action restart = (Action) beanFactory.getBean("restart");
	private Action reconfigure = (Action) beanFactory.getBean("reconfigure");

	private String autonomicManagerName;

	private String autonomicManagerVersion;

	private static final HashSet<Action> actionSet = new HashSet<Action>();
	private static final HashSet<String> actionNames = new HashSet<String>();

	public AutonomicManager(String name, String version, String slaLocation,
			State state, Set<String> set) throws URISyntaxException,
			NamingException {
		super(name, set);
		this.autonomicManagerName = name;

		this.autonomicManagerVersion = version;

		this.state = state;
		this.slaLocation = slaLocation;

		generateActionNames();

		// check if all attributes are correct
		if (name.length() == 0) {
			throw new NamingException(
					"Name of the AutonomicManager resource can not be null");
		}
		/*
		 * set Category
		 */

		setKind(new Kind(actionSet, null, null, null, "autonomicmanager",
				"AutonomicManager",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/autonomic#", (Set<String>) attributes));
		getKind().setActionNames(actionNames);
		// set uuid for the resource
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into database list
		autonomicManagerList.put(uuid, this);

		// Generate attribute list
		generateAttributeList();
	}

	public void setSlaLocation(String sla) {
		this.slaLocation = sla;
	}

	public String getSlaLocation() {
		return slaLocation;
	}

	/**
	 * Return the full databaseList
	 * 
	 * @return all database resources
	 */
	public static final Map<UUID, AutonomicManager> getAutonomicManagerList() {
		return autonomicManagerList;
	}

	/**
	 * Set database list
	 * 
	 * @param autonomicManagerList
	 */
	public static final void setAutonomicManagerList(
			Map<UUID, AutonomicManager> autonomicManagerList) {
		AutonomicManager.autonomicManagerList = autonomicManagerList;
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
		return autonomicManagerName;
	}

	/**
	 * Sets the Name of the current Instance
	 * 
	 * @param name
	 *            of the current Instance as a String
	 */
	public final void setName(String name) {
		this.autonomicManagerName = name;
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
		return autonomicManagerVersion;
	}

	public final void setVersion(String version) {
		this.autonomicManagerVersion = version;
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
						.toString().contains("autonomic")) {
					actionNames.add(OcciConfig.getInstance().config
							.getString("occi.scheme")
							+ "/autonomicmanager/action#"
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
						.toString().contains("autonomic")) {
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
			attributes.add("occi.autonomicmanager.name");
			attributes.add("occi.autonomicmanager.visibility");
			attributes.add("occi.autonomicmanager.scope");

		}
	}

	/**
	 * Return the attributes.
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
	public final void Reconfigure(Action reconfigure) {
		this.reconfigure = reconfigure;
	}

	/**
	 * @return the backup
	 */
	public final Action getReconfigure() {
		return reconfigure;
	}

	public Action getCreate() {
		return create;
	}

	public void setCreate(Action create) {
		this.create = create;
	}

	public Action getDelete() {
		return delete;
	}

	public void setDelete(Action delete) {
		this.delete = delete;
	}

	public  void postContainer() {

		ClientResource resource = new ClientResource(host + "/container");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"container; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.container.name="+containerName);

				headers.add("X-OCCI-Attribute", "occi.container.version="+containerVersion);
				headers.add("X-OCCI-Attribute",
						"occi.container.architecture="+containerArchitecture);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			containerURI = headersResponse.getFirstValue("Location");

			System.out.println("\n container Location" + containerURI);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  void postDataBase() {

		ClientResource resource = new ClientResource(host + "/database");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"database; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.database.name="+databaseName);

				headers.add("X-OCCI-Attribute", "occi.database.version="+databaseVersion);
				headers.add("X-OCCI-Attribute",
						"occi.database.architecture="+databaseArchitecture);
				headers.add("X-OCCI-Attribute", "occi.database.type="+databaseType);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			databaseURI = headersResponse.getFirstValue("Location");

			System.out.println("\n database Location" + databaseURI);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  void postDatabaseLink() {
		ClientResource resource = new ClientResource(host + "/databaselink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"databaselink; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.database.name="+databaseLinkName);

				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ containerURI);
				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ databaseURI);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			databaseLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n databaseLinkURI Location" + databaseLinkURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postEnvironment() {
		ClientResource resource = new ClientResource(host + "/environment");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"environment; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.environment.name="+environmentName);

				headers.add("X-OCCI-Attribute", "occi.environment.memory="+environmentMemory);
				headers.add("X-OCCI-Attribute",
						"occi.environment.containersList=" + containerURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.databasesList=" + databaseURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.databasesLink=" + databaseLinkURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.variables="+environmentVariables);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			environmentURI = headersResponse.getFirstValue("Location");

			System.out.println("\n environmentURI Location" + environmentURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postDeployable() {
		ClientResource resource = new ClientResource(host + "/deployable");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"deployable; scheme='http://schemas.ogf.org/occi/application#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.deployables.name="+deployableName);

				headers.add("X-OCCI-Attribute",
						"occi.deployables.content_type="+deployableType);
				headers.add(
						"X-OCCI-Attribute",
						"occi.deployables.location="+deployableLocation);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			deployableURI = headersResponse.getFirstValue("Location");

			System.out.println("\n deployableURI Location" + deployableURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postMixedApplication() {
		ClientResource resource = new ClientResource(host + "/mixedapplication");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"application; scheme='http://schemas.ogf.org/occi/application#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.application.name="+applicationName);
				headers.add("X-OCCI-Attribute",
						"occi.application.description="+applicationDescription);
				headers.add("X-OCCI-Attribute", "occi.application.instances="+applicationInstances);
				headers.add("X-OCCI-Attribute", "occi.deployables.uuids="
						+ deployableURI);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			applicationURI = headersResponse.getFirstValue("Location");

			System.out.println("\n applicationURI Location" + applicationURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postEnvironmentLink() {
		ClientResource resource = new ClientResource(host + "/environmentlink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"environmentlink; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ applicationURI);

				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ environmentURI);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			environmentLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n environmentLinkURI Location"
					+ environmentLinkURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postAnalyzer() {
		ClientResource resource = new ClientResource(host + "/analyzer");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"analyzer; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.analyzer.name="+analyzerName);
				headers.add("X-OCCI-Attribute", "occi.analyzer.version="+analyzerVersion);
				headers.add("X-OCCI-Attribute", "occi.analyzer.maxthreshold="+maxThreshold);
				headers.add("X-OCCI-Attribute", "occi.analyzer.minthreshold="+minThreshold);
				headers.add("X-OCCI-Attribute", "occi.analyzer.aggregationinterval="+aggregationInterval);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			analyzerURI = headersResponse.getFirstValue("Location");

			System.out.println("\n analyzerURI Location \n" + analyzerURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Subscription
	 */
	public  void postSubscription() {
		ClientResource resource = new ClientResource(subscriptionLinkURI);

		try {

			Subscription subscription = new Subscription(Long.parseLong(subscriptionDuration),
					getLastSegment(analyzerURI), Integer.parseInt(subscriptionInterval));

			;
			resource.post(XMLTools.getSubscriptionXML(subscription)).write(
					System.out);
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void postReconfigurationManager() {
		ClientResource resource = new ClientResource(host
				+ "/reconfigurationmanager");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add(
						"Category",
						"reconfigurationmanager; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.reconfigurationmanager.name="+reconfigurationManagerName);
				headers.add("X-OCCI-Attribute",
						"occi.reconfigurationmanager.version="+reconfigurationManagerVersion);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			reconfigurationManagerURI = headersResponse
					.getFirstValue("Location");

			System.out.println("\n reconfigurationManagerURI Location"
					+ reconfigurationManagerURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postSubscriptionLink() {
		ClientResource resource = new ClientResource(host + "/subscriptionlink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add(
						"Category",
						"subscriptionlink; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ analyzerURI);

				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ applicationURI);
				headers.add("X-OCCI-Attribute",
						"occi.subscriptionlink.name=sublink"+subscriptionLinkName);
				headers.add("X-OCCI-Attribute",
						"occi.subscriptionlink.version="+subscriptionLinkVersion);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			subscriptionLinkURI = subscriptionLinkURI
					+ getLastSegment(headersResponse.getFirstValue("Location"));

			System.out.println("\n subscriptionLinkURI Location \n"
					+ subscriptionLinkURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postNotificationLink() {

		ClientResource resource = new ClientResource(host + "/notificationlink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add(
						"Category",
						"notificationlink; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ applicationURI);

				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ analyzerURI);
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.name="+notificationLinkName);
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.version="+notificationLinkVersion);
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.refreshrate=" + notificationRefreshRate);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			notificationLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n NotificationLink URI" + notificationLinkURI);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postAlertLink() {
		ClientResource resource = new ClientResource(host + "/alertlink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"alertlink; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ analyzerURI);

				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ reconfigurationManagerURI);
				headers.add("X-OCCI-Attribute",
						"occi.actionlink.name="+alertLinkName);
				headers.add("X-OCCI-Attribute", "occi.actionlink.version="+alertLinkVersion);

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			String alertLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n alertLinkURI Location" + alertLinkURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void postActionLink() {
		ClientResource resource = new ClientResource(host + "/actionlink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"actionlink; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.core.source="
						+ reconfigurationManagerURI);

				headers.add("X-OCCI-Attribute", "occi.core.target="
						+ applicationURI);
				headers.add("X-OCCI-Attribute",
						"occi.actionlink.name="+actionLinkName);
				headers.add("X-OCCI-Attribute", "occi.actionlink.version="+actionLinkVersion);
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			String actionLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n actionLinkURI Location" + actionLinkURI);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void startApplication() {
		ClientResource resource = new ClientResource(applicationURI
				+ "/?action=start");
		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the applicationId
	 */
	public static String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public static void setApplicationId(String appId) {
		applicationId = appId;
	}

	public static void createSubscription() {
		ClientResource resource = new ClientResource(subscriptionLinkURI);

		try {

			subscription = new Subscription(1000000,
					getLastSegment(analyzerURI), -1);

			;
			resource.post(XMLTools.getSubscriptionXML(subscription));
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getLastSegment(String string) {
		return string.substring(string.lastIndexOf("/") + 1);
	}

	

//	@SuppressWarnings("rawtypes")
//	public static void main(String args[]) {
//
//		/**
//		 * 
//		 */
//		DynamicProxyMixer dynamicMixer = new DynamicProxyMixer();
//
//		@SuppressWarnings("unchecked")
//		Mixin<AutonomicManager> autonomicManagerWithMixin = new Mixin(
//				SpecificSLAMixinInterface.class,
//				SpecificSLAMixinToolImpl.class, AutonomicManager.class);
//
//		Factory factory = dynamicMixer.getFactory(AutonomicManager.class,
//				autonomicManagerWithMixin);
//		Class[] clazz0 = { String.class, String.class, String.class,
//				State.class, Set.class };
//		Object[] objects0 = { "name", "2", "", State.unavailable, null };
//		@SuppressWarnings("unchecked")
//		AutonomicManager autonomicManager = (AutonomicManager) factory
//				.newInstance(clazz0, objects0);
//		/**
//		 * 
//		 */
//		autonomicManager.inspectSLA("C:/temp/sla.properties");
//		System.out.println(autonomicManager.getDeployableLocation());
//
//		// postContainer();
//		// postDataBase();
////		// postDatabaseLink();
////		// postDeployable();
////		// postEnvironment();
////		// postMixedApplication();
////		// postEnvironmentLink();
////		// startApplication();
////		// postAnalyzer();
////		// postReconfigurationManager();
////		// postSubscriptionLink();
////		// // postNotificationLink();
////		// postAlertLink();
////		// postActionLink();
////		// createSubscription();
////		// System.out.println("Infrastructure established");
//	}
//	

	/**
	 * @return the maxThreshold
	 */
	public int getMaxThreshold() {
		return maxThreshold;
	}

	/**
	 * @param maxThreshold the maxThreshold to set
	 */
	public void setMaxThreshold(int maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	/**
	 * @return the minThreshold
	 */
	public int getMinThreshold() {
		return minThreshold;
	}

	/**
	 * @param minThreshold the minThreshold to set
	 */
	public void setMinThreshold(int minThreshold) {
		this.minThreshold = minThreshold;
	}

	/**
	 * @return the aggregationInterval
	 */
	public int getAggregationInterval() {
		return aggregationInterval;
	}

	/**
	 * @param aggregationInterval the aggregationInterval to set
	 */
	public void setAggregationInterval(int aggregationInterval) {
		this.aggregationInterval = aggregationInterval;
	}
	

}
