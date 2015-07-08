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
package telecom.sudparis.occi.autonomic.testresources;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import telecom.sudparis.occi.autonomic.tools.Subscription;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class AutomaticResourceCreation {

	// private final static String newRelicURI =
	// "https://api.newrelic.com/v2/applications.xml";
	// private final static String analyzer =
	// "http://localhost:8182/analyzer/notify/";
	// private final static String api_key =
	// "37a10b0b241d2c0182ab81d5a1b9f4e5ae620276c4fe849";
	//private final static String subscriptionLinURI = "http://localhost:8182/subscriptionlink/subscribe/";
	//private final static String analyzerUuid = "c0413307-27e6-4d9d-99a5-e83463c6c8de";
	//private final static String subLinkuuid = "4999b245-db0a-4d45-a622-6453f468d7f6";
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

	public static void main(String[] args) {
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
		//postNotificationLink();
		postAlertLink();
		postActionLink();
		createSubscription();
		System.out.println("Infrastructure established");
	}

	public static void postContainer() {

		ClientResource resource = new ClientResource(host + "/container");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"container; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.container.name=tomcat");

				headers.add("X-OCCI-Attribute", "occi.container.version=2");
				headers.add("X-OCCI-Attribute",
						"occi.container.architecture=x64");

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

	public static void postDataBase() {

		ClientResource resource = new ClientResource(host + "/database");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"database; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.database.name=mysql");

				headers.add("X-OCCI-Attribute", "occi.database.version=2.3");
				headers.add("X-OCCI-Attribute",
						"occi.database.architecture=x64");
				headers.add("X-OCCI-Attribute", "occi.database.type=relational");
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

	public static void postDatabaseLink() {
		ClientResource resource = new ClientResource(host + "/databaselink");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"databaselink; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.database.name=MyDatabaseLink");

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

	public static void postEnvironment() {
		ClientResource resource = new ClientResource(host + "/environment");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"environment; scheme='http://schemas.ogf.org/occi/platform#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.environment.name=myenv");

				headers.add("X-OCCI-Attribute", "occi.environment.memory=256");
				headers.add("X-OCCI-Attribute",
						"occi.environment.containersList=" + containerURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.databasesList=" + databaseURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.databasesLink=" + databaseLinkURI);
				headers.add("X-OCCI-Attribute",
						"occi.environment.variables=var|value");

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

	public static void postDeployable() {
		ClientResource resource = new ClientResource(host + "/deployable");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"deployable; scheme='http://schemas.ogf.org/occi/application#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.deployables.name=ExperimentsServlet.war");

				headers.add("X-OCCI-Attribute",
						"occi.deployables.content_type=artifact");
				headers.add(
						"X-OCCI-Attribute",
						"occi.deployables.location=C:\\Users\\mohame_m\\git\\EASICLOUDS\\COAPS\\test-resources\\sampleApplication");

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

	public static void postMixedApplication() {
		ClientResource resource = new ClientResource(host + "/mixedapplication");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"application; scheme='http://schemas.ogf.org/occi/application#'; class='kind'");
				headers.add("X-OCCI-Attribute",
						"occi.application.name=ExperimentsTspApp");
				headers.add("X-OCCI-Attribute",
						"occi.application.description=sample");
				headers.add("X-OCCI-Attribute", "occi.application.instances=1");
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

	public static void postEnvironmentLink() {
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

	public static void postAnalyzer() {
		ClientResource resource = new ClientResource(host + "/analyzer");

		Form headers = (Form) resource.getRequestAttributes().get(
				"org.restlet.http.headers");
		try {
			if (headers == null) {
				headers = new Form();
				headers.add("Category",
						"analyzer; scheme='http://schemas.ogf.org/occi/autonomic#'; class='kind'");
				headers.add("X-OCCI-Attribute", "occi.analyzer.name=MyAnalyzer");
				headers.add("X-OCCI-Attribute", "occi.analyzer.version=2");
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
	public static void postSubscription() {
	ClientResource resource=	new ClientResource(subscriptionLinkURI);
	
	
	try {

		 Subscription subscription=new Subscription(1000000, getLastSegment(analyzerURI), -1);

		;
		resource.post(XMLTools.getSubscriptionXML(subscription)).write(System.out);
	} catch (ResourceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
   
	
	}
	
	public static void postReconfigurationManager() {
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
						"occi.reconfigurationmanager.name=MyreconfigManager");
				headers.add("X-OCCI-Attribute",
						"occi.reconfigurationmanager.version=2");
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

		} catch (Exception e) {e.printStackTrace();
		}

	}

	public static void postSubscriptionLink() {
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
						"occi.subscriptionlink.name=sublink");
				headers.add("X-OCCI-Attribute",
						"occi.subscriptionlink.version=1");

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			subscriptionLinkURI = subscriptionLinkURI+getLastSegment(headersResponse.getFirstValue("Location"));

			System.out.println("\n subscriptionLinkURI Location \n"
					+ subscriptionLinkURI);

		} catch (Exception e) {e.printStackTrace();
		}

	}

	public static void postNotificationLink() {

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
						"occi.notificationlink.name=notificationlinkM");
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.version=1");
				headers.add("X-OCCI-Attribute",
						"occi.notificationlink.refreshrate=" + 100);
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

	public static void postAlertLink() {
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
						"occi.actionlink.name=alertlinkM");
				headers.add("X-OCCI-Attribute", "occi.actionlink.version=1");

				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			String alertLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n alertLinkURI Location" + alertLinkURI);

		} catch (Exception e) {e.printStackTrace();
		}

	}

	public static void postActionLink() {
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
						"occi.actionlink.name=actionlinkM");
				headers.add("X-OCCI-Attribute", "occi.actionlink.version=1");
				headers.add("Content-Type", "text/occi");
				resource.getRequestAttributes().put("org.restlet.http.headers",
						headers);
			}
			resource.post(headers).write(System.out);

			Form headersResponse = (Form) resource.getResponseAttributes().get(
					"org.restlet.http.headers");

			String actionLinkURI = headersResponse.getFirstValue("Location");

			System.out.println("\n actionLinkURI Location" + actionLinkURI);

		} catch (Exception e) {e.printStackTrace();
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
		} catch (Exception e) {e.printStackTrace();
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
	public static void setApplicationId(String applicationId) {
		AutomaticResourceCreation.applicationId = applicationId;
	}

	public static void createSubscription() {
		ClientResource resource = new ClientResource(subscriptionLinkURI);

		try {

			subscription = new Subscription(1000000,getLastSegment( analyzerURI), -1);

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
	public static String getLastSegment(String string)
	{
		return string.substring(string.lastIndexOf("/")+1);
	}
	
	
}
