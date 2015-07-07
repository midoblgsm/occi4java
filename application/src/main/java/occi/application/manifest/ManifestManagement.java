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
package occi.application.manifest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import occi.application.Application;
import occi.application.Deployable;
import occi.application.Environment;
import occi.platform.Container;
import occi.platform.Database;
import occi.platform.links.DatabaseLink;

/**
 * @author nguyen_n
 * 
 */
public class ManifestManagement {

	private static String manifestName = "";
	private static String manifestDesc = "";

	private static String appName = "";
	private static String appDesc = "";
	private static String appStartCommand = "";
	private static String appVersionName = "";
	private static String appVersionLabel = "";

	private static ArrayList<PaasApplicationDeployableType> listDeployables = new ArrayList<PaasApplicationDeployableType>();
	private static ArrayList<PaasApplicationVersionInstanceType> listInstances = new ArrayList<PaasApplicationVersionInstanceType>();

	private static String envName = "";
	private static String envTemplateName = "";
	private static Integer envTemplateMemory = 128;
	private static String envDesc = "";
	private static ArrayList<PaasEnvironmentNodeType> envNodes = new ArrayList<PaasEnvironmentNodeType>();
	private static ArrayList<PaasEnvironmentLinkType> envLinks = new ArrayList<PaasEnvironmentLinkType>();
	private static ArrayList<PaasEnvironmentVariableType> envVariables = new ArrayList<PaasEnvironmentVariableType>();

	public ManifestManagement() {
	}

	public ManifestManagement(String manifestName, String manifestDesc,
			String appName, String appDesc, String appStartCommand,
			String appVersionName, String appVersionLabel, 
			ArrayList<PaasApplicationDeployableType> listDeployables,
			ArrayList<PaasApplicationVersionInstanceType> listInstances,
			String envName, String envTemplateName, int envTemplateMemory,
			String envDesc, ArrayList<PaasEnvironmentNodeType> envNodes,
			ArrayList<PaasEnvironmentLinkType> envLinks,
			ArrayList<PaasEnvironmentVariableType> envVariables

	) {
		ManifestManagement.manifestName = manifestName;
		ManifestManagement.manifestDesc = manifestDesc;
		ManifestManagement.appName = appName;
		ManifestManagement.appDesc = appDesc;
		ManifestManagement.appStartCommand = appStartCommand;
		ManifestManagement.appVersionName = appVersionName;
		ManifestManagement.appVersionLabel = appVersionLabel;
		ManifestManagement.listDeployables = listDeployables;
		ManifestManagement.listInstances = listInstances;
		ManifestManagement.envName = envName;
		ManifestManagement.envTemplateName = envTemplateName;
		ManifestManagement.envTemplateMemory = envTemplateMemory;
		ManifestManagement.envDesc = envDesc;
		ManifestManagement.envNodes = envNodes;
		ManifestManagement.envLinks = envLinks;
		ManifestManagement.envVariables = envVariables;
	}

	public ManifestManagement(String appName, String appDesc,
			String appStartCommand, ArrayList<PaasApplicationDeployableType> listDeployables,
			ArrayList<PaasApplicationVersionInstanceType> listInstances,
			String envName, String envDesc,
			ArrayList<PaasEnvironmentNodeType> envNodes

	) {

		ManifestManagement.appName = appName;
		ManifestManagement.appDesc = appDesc;
		ManifestManagement.appStartCommand = appStartCommand;

		ManifestManagement.listDeployables = listDeployables;
		ManifestManagement.listInstances = listInstances;

		ManifestManagement.envName = envName;
		ManifestManagement.envDesc = envDesc;
		ManifestManagement.envNodes = envNodes;
	}

	public static String createApplicationManifest(Application app,
			ArrayList<Deployable> depList) {
		appName = app.getName();
		appDesc = app.getDescription();
		appStartCommand = app.getStart_command();

		listInstances = new ArrayList<PaasApplicationVersionInstanceType>();
		for (int i = 0; i < app.getInstances(); i++) {
			PaasApplicationVersionInstanceType ins = new PaasApplicationVersionInstanceType();
			ins.setName("Instance" + (i + 1));
			ins.setInitialState(1);

			listInstances.add(ins);
		}

		listDeployables = new ArrayList<PaasApplicationDeployableType>();
		for (Deployable dep:depList) {
			PaasApplicationDeployableType depVariable = new PaasApplicationDeployableType();
			depVariable.name = dep.getName();
			depVariable.contentType = dep.getContent_type().toString();
			depVariable.location = dep.getLocation();
			
			listDeployables.add(depVariable);
		}
		
		return getApplicationOnly();
	}
	
	public static String createEnvironmentManifest(Environment env) {
		
		ManifestManagement.envName = env.getName();
		ManifestManagement.envDesc = env.getDescription();
		ManifestManagement.envTemplateName = env.getName();
		ManifestManagement.envTemplateMemory = env.getMemory();
		
		envNodes = new ArrayList<PaasEnvironmentNodeType>();
		
		Set<UUID> containerKeys = env.getContainersList().keySet();
		for (UUID uuid:containerKeys) {
			Container con = env.getContainersList().get(uuid);
			
			PaasEnvironmentNodeType envNode = new PaasEnvironmentNodeType();
			envNode.setName(con.getName());
			envNode.setContentType("container");
			envNode.setVersion(con.getVersion());
			//envNode.setProvider(con.getProvider());
			
			envNodes.add(envNode);
		}
		
		Set<UUID> databaseKeys = env.getDatabasesList().keySet();
		for (UUID uuid:databaseKeys){
			Database db = env.getDatabasesList().get(uuid);
			PaasEnvironmentNodeType envNode = new PaasEnvironmentNodeType();
			envNode.setName(db.getName());
			envNode.setContentType("database");
			envNode.setVersion(db.getVersion());
			//envNode.setProvider(con.getProvider());
			
			envNodes.add(envNode);
		}
		
		envLinks = new ArrayList<PaasEnvironmentLinkType>();
		
		Set<UUID> linkKeys = env.getDatabasesLink().keySet();
		for (UUID uuid:linkKeys){
			DatabaseLink dblink = env.getDatabasesLink().get(uuid);
			
			PaasEnvironmentLinkType envLink = new PaasEnvironmentLinkType();
			Container con = (Container) dblink.getLink();
			Database db = (Database) dblink.getTarget();
			
			envLink.setSource(con.getName());
			envLink.setTarget(db.getName());
			
			envLinks.add(envLink);
			
		}
		
		envVariables = new ArrayList<PaasEnvironmentVariableType>();
		Set<String> variables = env.getVariables().keySet();
		for (String varName:variables){
			String varValue = env.getVariables().get(varName);
			
			PaasEnvironmentVariableType envVar = new PaasEnvironmentVariableType();
			envVar.setName(varName); 
			envVar.setValue(varValue);
			
			envVariables.add(envVar);			
		}
		
		return getEnvironmentOnly();
	}

	public static String getManifest() {
		// create JAXB context and instantiate marshaller
		JAXBContext context;

		// System.out.println(fileToStoreDB);
		try {
			context = JAXBContext.newInstance("occi.application.manifest");

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			PaasApplicationManifestType manifest = new PaasApplicationManifestType();

			manifest.setName(manifestName);
			manifest.setDescription(manifestDesc);

			// create environment
			PaasEnvironmentType eType = new PaasEnvironmentType();

			eType.setName(envName);
			eType.setTemplate(envTemplateName);

			PaasEnvironmentTemplateType eTemp = new PaasEnvironmentTemplateType();
			eTemp.setName(envTemplateName);
			eTemp.setMemory(envTemplateMemory<=0 ? 128 : envTemplateMemory);
			eTemp.setDescription(envDesc);
			eTemp.setPaasEnvironmentNode(envNodes);

			PaasEnvironmentRelationType envRelation = new PaasEnvironmentRelationType();
			envRelation.setPaasEnvironmentLinks(envLinks);
			eTemp.setPassEnvironmentRelation(envRelation);

			PaasEnvironmentConfigurationType envConfig = new PaasEnvironmentConfigurationType();
			envConfig.setPaasEnvironmentVariables(envVariables);
			eTemp.setPassEnvironmentConfig(envConfig);

			eType.setPaasEnvironmentTemplate(eTemp);
			manifest.setPaasEnvironment(eType);

			// create application
			PaasApplicationType aType = new PaasApplicationType();

			aType.setDescription(appDesc);
			aType.setName(appName);
			aType.setEnvironment(envName);
			aType.setStartCommand(appStartCommand);

			PaasApplicationVersionType aVersion = new PaasApplicationVersionType();
			aVersion.setLabel(appVersionLabel);
			aVersion.setName(appVersionName);

			aVersion.setPaasApplicationDeployable(listDeployables);
			aVersion.setPaasApplicationVersionInstance(listInstances);
			aType.setPaasApplicationVersion(aVersion);

			manifest.setPaasApplication(aType);

			// generate XML manifest string
			StringWriter sw = new StringWriter();
			// Write to System.out
			m.marshal(manifest, sw);

			return sw.toString();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getApplicationOnly() {
		// create JAXB context and instantiate marshaller
		JAXBContext context;

		// System.out.println(fileToStoreDB);
		try {
			context = JAXBContext.newInstance("occi.application.manifest");

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			PaasApplicationManifestType manifest = new PaasApplicationManifestType();

			manifest.setName(manifestName);
			manifest.setDescription(manifestDesc);

			// create application
			PaasApplicationType aType = new PaasApplicationType();

			aType.setDescription(appDesc);
			aType.setName(appName);
			aType.setEnvironment(envName);
			aType.setStartCommand(appStartCommand);

			PaasApplicationVersionType aVersion = new PaasApplicationVersionType();
			aVersion.setLabel(appVersionLabel);
			aVersion.setName(appVersionName);

			aVersion.setPaasApplicationDeployable(listDeployables);
			aVersion.setPaasApplicationVersionInstance(listInstances);
			aType.setPaasApplicationVersion(aVersion);

			manifest.setPaasApplication(aType);

			// generate XML manifest string
			StringWriter sw = new StringWriter();
			// Write to System.out
			m.marshal(manifest, sw);

			return sw.toString();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getEnvironmentOnly() {
		// create JAXB context and instantiate marshaller
		JAXBContext context;

		// System.out.println(fileToStoreDB);
		try {
			context = JAXBContext
					.newInstance("occi.application.manifest");

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			PaasApplicationManifestType manifest = new PaasApplicationManifestType();

			manifest.setName(manifestName);
			manifest.setDescription(manifestDesc);

			// create environment
			PaasEnvironmentType eType = new PaasEnvironmentType();

			eType.setName(envName);
			eType.setTemplate(envTemplateName);

			PaasEnvironmentTemplateType eTemp = new PaasEnvironmentTemplateType();
			eTemp.setName(envTemplateName);
			eTemp.setMemory(envTemplateMemory<=0 ? 128 :envTemplateMemory);
			eTemp.setDescription(envDesc);
			eTemp.setPaasEnvironmentNode(envNodes);

			PaasEnvironmentRelationType envRelation = new PaasEnvironmentRelationType();
			envRelation.setPaasEnvironmentLinks(envLinks);
			eTemp.setPassEnvironmentRelation(envRelation);

			PaasEnvironmentConfigurationType envConfig = new PaasEnvironmentConfigurationType();
			envConfig.setPaasEnvironmentVariables(envVariables);
			eTemp.setPassEnvironmentConfig(envConfig);

			eType.setPaasEnvironmentTemplate(eTemp);
			manifest.setPaasEnvironment(eType);

			// generate XML manifest string
			StringWriter sw = new StringWriter();
			// Write to System.out
			m.marshal(manifest, sw);

			return sw.toString();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void parseManifest(String manifestDescription) {
		try {
			if (manifestDescription != null) {
				InputStream is = new ByteArrayInputStream(
						manifestDescription.getBytes());

				JAXBContext jaxbContext;
				PaasApplicationManifestType manifest = new PaasApplicationManifestType();
				try {
					jaxbContext = JAXBContext
							.newInstance("occi.application.manifest");
					Unmarshaller jaxbUnmarshaller = jaxbContext
							.createUnmarshaller();

					JAXBElement<PaasApplicationManifestType> root = jaxbUnmarshaller
							.unmarshal(new StreamSource(is),
									PaasApplicationManifestType.class);

					manifest = root.getValue();

				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				manifestName = manifest.getName();
				manifestDesc = manifest.getDescription();

				if (manifest.getPaasApplication() != null) {
					appName = manifest.getPaasApplication().getName();
					appDesc = manifest.getPaasApplication().getDescription();
					appStartCommand = manifest.getPaasApplication()
							.getStartCommand();
					if (manifest.getPaasApplication()
							.getPaasApplicationVersion() != null) {
						appVersionName = manifest.getPaasApplication()
								.getPaasApplicationVersion().getName();
						if (manifest.getPaasApplication()
								.getPaasApplicationVersion().getLabel() != null)
							appVersionLabel = manifest.getPaasApplication()
									.getPaasApplicationVersion().getLabel()
									.toString();
						listDeployables = manifest.getPaasApplication()
								.getPaasApplicationVersion()
								.getPaasApplicationDeployable();
						listInstances = manifest.getPaasApplication()
								.getPaasApplicationVersion()
								.getPaasApplicationVersionInstance();
					}
				}

				if (manifest.getPaasEnvironment() != null) {
					envName = manifest.getPaasEnvironment().getName();
					envTemplateName = manifest.getPaasEnvironment()
							.getTemplate();
					if (manifest.getPaasEnvironment()
							.getPaasEnvironmentTemplate() != null) {
						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate().getMemory() != null)
							envTemplateMemory = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate().getMemory();

						envDesc = manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate().getDescription();
						envNodes = manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPaasEnvironmentNode();

						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPassEnvironmentRelation() != null)
							envLinks = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate()
									.getPassEnvironmentRelation()
									.getPaasEnvironmentLinks();
						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPassEnvironmentConfig() != null)
							envVariables = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate()
									.getPassEnvironmentConfig()
									.getPaasEnvironmentVariables();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to create the environment: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void parseApplicationOnly(String description) {
		try {
			if (description != null) {
				InputStream is = new ByteArrayInputStream(
						description.getBytes());

				JAXBContext jaxbContext;
				PaasApplicationManifestType manifest = new PaasApplicationManifestType();
				try {
					jaxbContext = JAXBContext
							.newInstance("occi.application.manifest");
					Unmarshaller jaxbUnmarshaller = jaxbContext
							.createUnmarshaller();

					JAXBElement<PaasApplicationManifestType> root = jaxbUnmarshaller
							.unmarshal(new StreamSource(is),
									PaasApplicationManifestType.class);

					manifest = root.getValue();

				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				manifestName = manifest.getName();
				manifestDesc = manifest.getDescription();

				if (manifest.getPaasApplication() != null) {
					appName = manifest.getPaasApplication().getName();
					appDesc = manifest.getPaasApplication().getDescription();
					appStartCommand = manifest.getPaasApplication()
							.getStartCommand();
					if (manifest.getPaasApplication()
							.getPaasApplicationVersion() != null) {
						appVersionName = manifest.getPaasApplication()
								.getPaasApplicationVersion().getName();
						if (manifest.getPaasApplication()
								.getPaasApplicationVersion().getLabel() != null)
							appVersionLabel = manifest.getPaasApplication()
									.getPaasApplicationVersion().getLabel()
									.toString();
						listDeployables = manifest.getPaasApplication()
							.getPaasApplicationVersion()
							.getPaasApplicationDeployable();
						listInstances = manifest.getPaasApplication()
								.getPaasApplicationVersion()
								.getPaasApplicationVersionInstance();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to create the environment: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void parseEnvironmentOnly(String description) {
		try {
			if (description != null) {
				InputStream is = new ByteArrayInputStream(
						description.getBytes());

				JAXBContext jaxbContext;
				PaasApplicationManifestType manifest = new PaasApplicationManifestType();
				try {
					jaxbContext = JAXBContext
							.newInstance("occi.application.manifest");
					Unmarshaller jaxbUnmarshaller = jaxbContext
							.createUnmarshaller();

					JAXBElement<PaasApplicationManifestType> root = jaxbUnmarshaller
							.unmarshal(new StreamSource(is),
									PaasApplicationManifestType.class);

					manifest = root.getValue();

				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				manifestName = manifest.getName();
				manifestDesc = manifest.getDescription();

				if (manifest.getPaasEnvironment() != null) {
					envName = manifest.getPaasEnvironment().getName();
					envTemplateName = manifest.getPaasEnvironment()
							.getTemplate();
					if (manifest.getPaasEnvironment()
							.getPaasEnvironmentTemplate() != null) {
						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate().getMemory() != null)
							envTemplateMemory = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate().getMemory();

						envDesc = manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate().getDescription();
						envNodes = manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPaasEnvironmentNode();

						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPassEnvironmentRelation() != null)
							envLinks = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate()
									.getPassEnvironmentRelation()
									.getPaasEnvironmentLinks();
						if (manifest.getPaasEnvironment()
								.getPaasEnvironmentTemplate()
								.getPassEnvironmentConfig() != null)
							envVariables = manifest.getPaasEnvironment()
									.getPaasEnvironmentTemplate()
									.getPassEnvironmentConfig()
									.getPaasEnvironmentVariables();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to create the environment: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManifestManagement mm = new ManifestManagement();

		String s = "<?xml version=\"1.0\" encoding=\"UTF8\"?>"
				+ "<paas_application_manifest name=\"ServletSampleApplicationManifest\">"
				+ "<description>This manifest describes a Sample Servlet.</description>"
				+ "	<paas_application name=\"ServletSampleApplication\" environement=\"JavaWebEnv\">"
				+ "		<description>Sample Servlet application description.</description>"
				+ "		<paas_application_version name=\"version1.0\" label=\"1.0\">"
				+ "			<paas_application_deployable name=\"SampleServlet.war\" content_type=\"artifact\" location=\"[location]\" multitenancy_level=\"SharedInstance\"/>"
				+ "			<paas_application_version_instance name=\"Instance1\" initial_state=\"1\" default_instance=\"true\"/>"
				+ "		</paas_application_version>"
				+ "	</paas_application>"
				+ "	<paas_environment name=\"JavaWebEnv\" template=\"TomcatEnvTemp\">"
				+ "		<paas_environment_template name=\"TomcatEnvTemp\" memory=\"65\">"
				+ "  		  <description>TomcatServerEnvironmentTemplate</description>"
				+ "		  <paas_environment_node content_type=\"container\" name=\"tomcat\" version=\"\" provider=\"CF\"/>"
				+ "		</paas_environment_template>" + "	</paas_environment>"
				+ "</paas_application_manifest>";

		// System.out.print(mm.getApplicationOnly());
		mm.parseManifest(s);
	}

}
