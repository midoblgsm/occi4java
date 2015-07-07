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
//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.6 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2013.04.17 � 07:21:53 PM CEST 
//

package xml.application;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import xml.LinkType;
import xml.LinksListType;
import xml.environment.ConfigurationType;
import xml.environment.EntryType;
import xml.environment.EnvironmentType;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * xml.application package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _Application_QNAME = new QName("", "application");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * xml.application
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ApplicationType }
	 * 
	 */
	public ApplicationType createApplicationType() {
		return new ApplicationType();
	}

	/**
	 * Create an instance of {@link InstancesType }
	 * 
	 */
	public InstancesType createInstancesType() {
		return new InstancesType();
	}

	/**
	 * Create an instance of {@link ConfigurationType }
	 * 
	 */
	public ConfigurationType createConfigurationType() {
		return new ConfigurationType();
	}

	/**
	 * Create an instance of {@link UrisType }
	 * 
	 */
	public UrisType createUrisType() {
		return new UrisType();
	}

	/**
	 * Create an instance of {@link EntryType }
	 * 
	 */
	public EntryType createEntryType() {
		return new EntryType();
	}

	/**
	 * Create an instance of {@link DeployableType }
	 * 
	 */
	public DeployableType createDeployableType() {
		return new DeployableType();
	}

	/**
	 * Create an instance of {@link LinkType }
	 * 
	 */
	public LinkType createLinkType() {
		return new LinkType();
	}

	/**
	 * Create an instance of {@link LinksListType }
	 * 
	 */
	public LinksListType createLinksListType() {
		return new LinksListType();
	}

	/**
	 * Create an instance of {@link InstanceType }
	 * 
	 */
	public InstanceType createInstanceType() {
		return new InstanceType();
	}

	/**
	 * Create an instance of {@link EnvironmentType }
	 * 
	 */
	public EnvironmentType createEnvironmentType() {
		return new EnvironmentType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationType }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "application")
	public JAXBElement<ApplicationType> createApplication(ApplicationType value) {
		return new JAXBElement<ApplicationType>(_Application_QNAME,
				ApplicationType.class, null, value);
	}

}
