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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour paas_application_manifestType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="paas_application_manifestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paas_application" type="{}paas_applicationType"/>
 *         &lt;element name="paas_environment" type="{}paas_environmentType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement (name = "paas_application_manifest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paas_application_manifestType", propOrder = { "description",
		"paasApplication", "paasEnvironment" })
public class PaasApplicationManifestType {

	@XmlElement(required = true)
	protected String description;
	@XmlElement(name = "paas_application", required = true)
	protected PaasApplicationType paasApplication;
	@XmlElement(name = "paas_environment", required = true)
	protected PaasEnvironmentType paasEnvironment;
	@XmlAttribute(name = "name")
	protected String name;
	
	//@XmlAttribute(name = "xmlns")
	//protected String namespace = "http://www.compatibleone.fr/schemes/paasmanifest.xsd";

	/**
	 * Obtient la valeur de la propri�t� description.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * D�finit la valeur de la propri�t� description.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Obtient la valeur de la propri�t� paasApplication.
	 * 
	 * @return possible object is {@link PaasApplicationType }
	 * 
	 */
	public PaasApplicationType getPaasApplication() {
		return paasApplication;
	}

	/**
	 * D�finit la valeur de la propri�t� paasApplication.
	 * 
	 * @param value
	 *            allowed object is {@link PaasApplicationType }
	 * 
	 */
	public void setPaasApplication(PaasApplicationType value) {
		this.paasApplication = value;
	}

	/**
	 * Obtient la valeur de la propri�t� paasEnvironment.
	 * 
	 * @return possible object is {@link PaasEnvironmentType }
	 * 
	 */
	public PaasEnvironmentType getPaasEnvironment() {
		return paasEnvironment;
	}

	/**
	 * D�finit la valeur de la propri�t� paasEnvironment.
	 * 
	 * @param value
	 *            allowed object is {@link PaasEnvironmentType }
	 * 
	 */
	public void setPaasEnvironment(PaasEnvironmentType value) {
		this.paasEnvironment = value;
	}

	/**
	 * Obtient la valeur de la propri�t� name.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * D�finit la valeur de la propri�t� name.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

}
