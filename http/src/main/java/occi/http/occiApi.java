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
package occi.http;

import java.io.File;

import occi.config.OcciConfig;
import occi.http.application.OcciRestApplication;
import occi.http.application.OcciRestApplications;
import occi.http.application.OcciRestDeployable;
import occi.http.application.OcciRestDeployables;
import occi.http.application.OcciRestEnvironment;
import occi.http.application.OcciRestEnvironmentLink;
import occi.http.application.OcciRestEnvironments;
import occi.http.application.OcciRestMixedApplication;
import occi.http.autonomic.OcciRestActionLink;
import occi.http.autonomic.OcciRestActionLinks;
import occi.http.autonomic.OcciRestAlertLink;
import occi.http.autonomic.OcciRestAlertLinks;
import occi.http.autonomic.OcciRestAnalyzer;
import occi.http.autonomic.OcciRestAnalyzers;
import occi.http.autonomic.OcciRestAutonomicManager;
import occi.http.autonomic.OcciRestAutonomicManagers;
import occi.http.autonomic.OcciRestNotificationLink;
import occi.http.autonomic.OcciRestNotificationLinks;
import occi.http.autonomic.OcciRestReconfigurationManager;
import occi.http.autonomic.OcciRestReconfigurationManagers;
import occi.http.autonomic.OcciRestSubscriptionLink;
import occi.http.autonomic.OcciRestSubscriptionLinks;
import occi.http.platform.OcciRestContainer;
import occi.http.platform.OcciRestContainers;
import occi.http.platform.OcciRestDatabase;
import occi.http.platform.OcciRestDatabaseLink;
import occi.http.platform.OcciRestDatabases;

import org.apache.log4j.PropertyConfigurator;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

public class occiApi extends ServerResource {
	// initialize server component
	public static Component comp = new Component();

	
	
	
	public static void main(String[] args) throws Exception {
		
		/**
		 * Mohamed mod
		 */
		// Create a router Restlet that defines routes.
	
		Router router = new Router(comp.getContext().createChildContext());
		
		
		// load all logger properties
		if (new File("conf/log4j.properties").exists())
			PropertyConfigurator.configure("conf/log4j.properties");
		else
			PropertyConfigurator
					.configure("../core/src/main/resources/conf/log4jTest.properties");
		// Create the HTTP server and listen on port 8182

		comp.getServers().add(Protocol.HTTP,
				OcciConfig.getInstance().config.getInt("occi.server.port"));

		comp.getClients().add(Protocol.HTTPS);
		comp.getClients().add(Protocol.HTTP);

		// comp.getContext().getParameters().add("socketTimeout", "2000");
		// comp.getContext().getParameters().set("maxTotalConnections", "5000");
		// comp.getContext().getParameters().set("maxConnexionsPerHost",
		// "1000");

		comp.getDefaultHost().attach("/", OcciRestRoot.class);
		// Router for the Query/Discovery Interface.
		comp.getDefaultHost().attach("/-/", OcciRestQuery.class);

		// Returns one single instance of a compute resource
		comp.getDefaultHost().attach("/compute", OcciRestCompute.class);
		comp.getDefaultHost().attach("/compute/{uuid}", OcciRestCompute.class);

		// Returns all compute instances
		comp.getDefaultHost().attach("/compute/", OcciRestComputes.class);

		// Router for storage instances
		comp.getDefaultHost().attach("/storage", OcciRestStorage.class);

		// Returns all storage instances
		comp.getDefaultHost().attach("/storage/", OcciRestStorages.class);

		// Router for storage instances
		comp.getDefaultHost().attach("/network", OcciRestNetwork.class);
		comp.getDefaultHost().attach("/network/{uuid}", OcciRestNetwork.class);
		comp.getDefaultHost().attach("/networkinterface",
				OcciRestNetworkInterface.class);
		comp.getDefaultHost().attach("/networkinterface/{uuid}",
				OcciRestNetworkInterface.class);
		// Returns all storage instances
		comp.getDefaultHost().attach("/network/", OcciRestNetworks.class);

		/*******************/
		/** OCCI Platform **/
		/*******************/
		// Returns one single instance of a container resource
		comp.getDefaultHost().attach("/container", OcciRestContainer.class);
		comp.getDefaultHost().attach("/container/{uuid}",
				OcciRestContainer.class);

		// Returns one single instance of a database resource
		comp.getDefaultHost().attach("/database", OcciRestDatabase.class);

		comp.getDefaultHost()
				.attach("/database/{uuid}", OcciRestDatabase.class);

		// Returns all container instances
		comp.getDefaultHost().attach("/container/", OcciRestContainers.class);

		// Returns all database instances
		comp.getDefaultHost().attach("/database/", OcciRestDatabases.class);
		// Returns one single instance of a databaseLink resource
		comp.getDefaultHost().attach("/databaselink",
				OcciRestDatabaseLink.class);
		comp.getDefaultHost().attach("/databaselink/{uuid}",
				OcciRestDatabaseLink.class);

		/*******************/
		/** OCCI Application **/
		/*******************/
		// Returns one single instance of an Application resource
		comp.getDefaultHost().attach("/application", OcciRestApplication.class);
		comp.getDefaultHost().attach("/application/{uuid}",
				OcciRestApplication.class);

		/***
		 * Test Mixed App
		 */

		// Defines a route for the resource "list of items"
		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
		router.setRoutingMode(Router.MODE_RANDOM_MATCH);
		router.attach("/mixedapplication",OcciRestMixedApplication.class);
		router.attach("/mixedapplication/notify/{uuid}",OcciRestMixedApplication.class);
		router.attach("/application/notify/{uuid}",OcciRestMixedApplication.class);
		// Defines a route for the resource "item"
		
		comp.getDefaultHost().attach("/mixedapplication",
				OcciRestMixedApplication.class);
		comp.getDefaultHost().attach("/mixedapplication/{uuid}",
				OcciRestMixedApplication.class);

		comp.getDefaultHost().attach("/mixedapplication/poll/{uuid}/",
				OcciRestMixedApplication.class);
		/**
		 * End Test Mixed App
		 */
		comp.getDefaultHost().attach("/application/{uuid}/poll/",
				OcciRestApplication.class);

		// Returns all Application instances
		comp.getDefaultHost().attach("/application/",
				OcciRestApplications.class);
		// Returns one single instance of an Environment resource
		comp.getDefaultHost().attach("/environment", OcciRestEnvironment.class);
		comp.getDefaultHost().attach("/environment/{uuid}",
				OcciRestEnvironment.class);

		// Returns all Environment instances
		comp.getDefaultHost().attach("/environment/",
				OcciRestEnvironments.class);

		// Returns one single instance of a Deployables resource
		comp.getDefaultHost().attach("/deployable", OcciRestDeployable.class);
		comp.getDefaultHost().attach("/deployable/{uuid}",
				OcciRestDeployable.class);

		// Returns all Deployables instances
		comp.getDefaultHost().attach("/deployable/", OcciRestDeployables.class);

		// Returns one single instance of a environmentLink resource
		comp.getDefaultHost().attach("/environmentlink",
				OcciRestEnvironmentLink.class);
		comp.getDefaultHost().attach("/environmentlink/{uuid}",
				OcciRestEnvironmentLink.class);

		/*******************/
		/** OCCI Autonomic **/
		/*******************/

		// Returns one single instance of a autonomicmanager resource
		comp.getDefaultHost().attach("/autonomicmanager",
				OcciRestAutonomicManager.class);
		comp.getDefaultHost().attach("/autonomicmanager/{uuid}",
				OcciRestAutonomicManager.class);

		// Returns one single instance of a reconfigurationmanager resource
		comp.getDefaultHost().attach("/reconfigurationmanager",
				OcciRestReconfigurationManager.class);
		comp.getDefaultHost().attach("/reconfigurationmanager/{uuid}",
				OcciRestReconfigurationManager.class);

		// Returns one single instance of a analyzer resource
		comp.getDefaultHost().attach("/analyzer", OcciRestAnalyzer.class);
		comp.getDefaultHost()
				.attach("/analyzer/{uuid}", OcciRestAnalyzer.class);
		// Returns all analyzer instances
		comp.getDefaultHost().attach("/analyzer/", OcciRestAnalyzers.class);

		// Returns all reconfigurationmanager instances
		comp.getDefaultHost().attach("/reconfigurationmanager/",
				OcciRestReconfigurationManagers.class);

		// Returns all autonomicmanager instances
		comp.getDefaultHost().attach("/autonomicmanager/",
				OcciRestAutonomicManagers.class);

		// Returns one single instance of a subscriptionlink
		comp.getDefaultHost().attach("/subscriptionlink",
				OcciRestSubscriptionLink.class);
		comp.getDefaultHost().attach("/subscriptionlink/{uuid}",
				OcciRestSubscriptionLink.class);
		// Returns all subscriptionlink instances
		comp.getDefaultHost().attach("/subscriptionlink/",
				OcciRestSubscriptionLinks.class);

		// Returns one single instance of a notificationlink
		comp.getDefaultHost().attach("/notificationlink",
				OcciRestNotificationLink.class);
		comp.getDefaultHost().attach("/notificationlink/{uuid}",
				OcciRestNotificationLink.class);

		// Returns all notificationlink instances
		comp.getDefaultHost().attach("/notificationlink/",
				OcciRestNotificationLinks.class);

		// Returns one single instance of a alertlink
		comp.getDefaultHost().attach("/alertlink", OcciRestAlertLink.class);
		comp.getDefaultHost().attach("/alertlink/{uuid}",
				OcciRestAlertLink.class);
		// Returns all alertlink instances
		comp.getDefaultHost().attach("/alertlink/", OcciRestAlertLinks.class);

		// Returns one single instance of a actionlink
		comp.getDefaultHost().attach("/actionlink", OcciRestActionLink.class);
		comp.getDefaultHost().attach("/actionlink/{uuid}",
				OcciRestActionLink.class);
		// Returns all actionlink instances
		comp.getDefaultHost().attach("/actionlink/", OcciRestActionLinks.class);

		// // Returns one single instance of a polling mixin
		// comp.getDefaultHost().attach("/pollingmixin",
		// OcciRestPollingMixin.class);
		// comp.getDefaultHost().attach("/pollingmixin/{uuid}",
		// OcciRestPollingMixin.class);
		// // Returns all polling mixin instances
		// comp.getDefaultHost().attach("/pollingmixin/",
		// OcciRestPollingMixins.class);
		//
		// // Returns one single instance of a push mixin
		// comp.getDefaultHost().attach("/pushmixin", OcciRestPushMixin.class);
		// comp.getDefaultHost().attach("/pushmixin/{uuid}",
		// OcciRestPushMixin.class);
		// // Returns all push mixin instances
		// comp.getDefaultHost().attach("/pushmixin/",
		// OcciRestPushMixins.class);
		//
		// // Returns one single instance of a reconfiguration mixin
		// comp.getDefaultHost().attach("/reconfigurationmixin",
		// OcciRestReconfigurationMixin.class);
		// comp.getDefaultHost().attach("/reconfigurationmixin/{uuid}",
		// OcciRestReconfigurationMixin.class);
		// // Returns all reconfiguration mixin instances
		// comp.getDefaultHost().attach("/reconfigurationmixin/",
		// OcciRestReconfigurationMixins.class);
		//
		// // Returns one single instance of a ruleset mixin
		// comp.getDefaultHost().attach("/rulesetmixin",
		// OcciRestRuleSetMixin.class);
		// comp.getDefaultHost().attach("/rulesetmixin/{uuid}",
		// OcciRestRuleSetMixin.class);
		// // Returns all ruleset mixin instances
		// comp.getDefaultHost().attach("/rulesetmixin/",
		// OcciRestRuleSetMixins.class);
		//
		// // Returns one single instance of a subscriptiontool mixin
		// comp.getDefaultHost().attach("/subscriptiontoolmixin",
		// OcciRestSubscriptionToolMixin.class);
		// comp.getDefaultHost().attach("/subscriptiontoolmixin/{uuid}",
		// OcciRestSubscriptionToolMixin.class);
		// // Returns all subscriptiontool mixin instances
		// comp.getDefaultHost().attach("/subscriptiontoolmixin/",
		// OcciRestSubscriptionToolMixins.class);
		//
		// // Returns one single instance of a notificationtool mixin
		// comp.getDefaultHost().attach("/notificationtoolmixin",
		// OcciRestNotificationToolMixin.class);
		// comp.getDefaultHost().attach("/notificationtoolmixin/{uuid}",
		// OcciRestNotificationToolMixin.class);
		// // Returns all notificationtool mixin instances
		// comp.getDefaultHost().attach("/notificationtoolmixin/",
		// OcciRestNotificationToolMixins.class);
		//
		// // Returns one single instance of a alerttool mixin
		// comp.getDefaultHost().attach("/alerttoolmixin",
		// OcciRestAlertToolMixin.class);
		// comp.getDefaultHost().attach("/alerttoolmixin/{uuid}",
		// OcciRestAlertToolMixin.class);
		// // Returns all alerttool mixin instances
		// comp.getDefaultHost().attach("/alerttoolmixin/",
		// OcciRestAlertToolMixins.class);
		//
		// // Returns one single instance of a strategyset mixin
		// comp.getDefaultHost().attach("/strategysetmixin",
		// OcciRestStrategySetMixin.class);
		// comp.getDefaultHost().attach("/strategysetmixin/{uuid}",
		// OcciRestStrategySetMixin.class);
		// // Returns all strategyset mixin instances
		// comp.getDefaultHost().attach("/strategysetmixin/",
		// OcciRestStrategySetMixins.class);
		//
		// // Returns one single instance of a actiontool mixin
		// comp.getDefaultHost().attach("/actiontoolmixin",
		// OcciRestActionToolMixin.class);
		// comp.getDefaultHost().attach("/actiontoolmixin/{uuid}",
		// OcciRestActionToolMixin.class);
		// // Returns all actiontool mixin instances
		// comp.getDefaultHost().attach("/actiontoolmixin/",
		// OcciRestActionToolMixins.class);

		/*******************/
		/** OCCI Application--> COAPS **/
		/*******************/

		// TODO: OCCI Application--> COAPS
		// Router for all available mixin instances. Returns mixin information.

		comp.getDefaultHost().attach("/{mixin}", OcciRestMixin.class);

		// start occi api
		comp.start();
	}
}