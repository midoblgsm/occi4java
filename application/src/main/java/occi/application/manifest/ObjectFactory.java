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
// G�n�r� le : 2013.04.18 � 05:02:15 PM CEST 
//

package occi.application.manifest;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * telecom.sudparis.eu.paas.core.server.xml.manifest package.
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

	private final static QName _PaasApplicationManifest_QNAME = new QName("",
			"paas_application_manifest");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * telecom.sudparis.eu.paas.core.server.xml.manifest
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link PaasApplicationManifestType }
	 * 
	 */
	public PaasApplicationManifestType createPaasApplicationManifestType() {
		return new PaasApplicationManifestType();
	}

	/**
	 * Create an instance of {@link PaasApplicationDeployableType }
	 * 
	 */
	public PaasApplicationDeployableType createPaasApplicationDeployableType() {
		return new PaasApplicationDeployableType();
	}

	/**
	 * Create an instance of {@link PaasApplicationVersionType }
	 * 
	 */
	public PaasApplicationVersionType createPaasApplicationVersionType() {
		return new PaasApplicationVersionType();
	}

	/**
	 * Create an instance of {@link PaasApplicationVersionInstanceType }
	 * 
	 */
	public PaasApplicationVersionInstanceType createPaasApplicationVersionInstanceType() {
		return new PaasApplicationVersionInstanceType();
	}

	/**
	 * Create an instance of {@link PaasApplicationType }
	 * 
	 */
	public PaasApplicationType createPaasApplicationType() {
		return new PaasApplicationType();
	}

	/**
	 * Create an instance of {@link PaasEnvironmentType }
	 * 
	 */
	public PaasEnvironmentType createPaasEnvironmentType() {
		return new PaasEnvironmentType();
	}

	/**
	 * Create an instance of {@link PaasEnvironmentNodeType }
	 * 
	 */
	public PaasEnvironmentNodeType createPaasEnvironmentNodeType() {
		return new PaasEnvironmentNodeType();
	}

	/**
	 * Create an instance of {@link PaasEnvironmentTemplateType }
	 * 
	 */
	public PaasEnvironmentTemplateType createPaasEnvironmentTemplateType() {
		return new PaasEnvironmentTemplateType();
	}
	
	/**
	 * Create an instance of {@link PaasEnvironmentConfigurationType }
	 * 
	 */
	public PaasEnvironmentConfigurationType createPaasEnvironmentConfigurationType() {
		return new PaasEnvironmentConfigurationType();
	}
	
	/**
	 * Create an instance of {@link PaasEnvironmentVariableType }
	 * 
	 */
	public PaasEnvironmentVariableType createPaasEnvironmentVariableType() {
		return new PaasEnvironmentVariableType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link PaasApplicationManifestType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "paas_application_manifest")
	public JAXBElement<PaasApplicationManifestType> createPaasApplicationManifest(
			PaasApplicationManifestType value) {
		return new JAXBElement<PaasApplicationManifestType>(
				_PaasApplicationManifest_QNAME,
				PaasApplicationManifestType.class, null, value);
	}

}
