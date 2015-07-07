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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xml.LinksListType;
import xml.environment.EnvironmentType;

/**
 * <p>
 * Classe Java pour applicationType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="applicationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uris" type="{}urisType"/>
 *         &lt;element name="deployable" type="{}deployableType"/>
 *         &lt;element name="Instances" type="{}InstancesType"/>
 *         &lt;element name="environment" type="{}environmentType"/>
 *         &lt;element name="linksList" type="{}linksListType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="appName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="appId" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="memory" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="checkExists" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="nbInstances" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="envId" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "application")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicationType", propOrder = { "description", "uris", "deployable",
		"instances", "environment", "linksList" })
public class ApplicationType {

	@XmlElement(required = false)
	protected  String description;
	@XmlElement(required = true)
	protected UrisType uris;
	@XmlElement(required = true)
	protected DeployableType deployable;
	@XmlElement(name = "Instances", required = true)
	protected InstancesType instances;
	@XmlElement(required = true)
	protected EnvironmentType environment;
	@XmlElement(required = true)
	protected LinksListType linksList;
	@XmlAttribute(name = "appName")
	protected String appName;
	@XmlAttribute(name = "appId")
	protected Integer appId;
	@XmlAttribute(name = "status")
	protected String status;
	//@XmlAttribute(name = "memory")
	//protected Integer memory;
	//@XmlAttribute(name = "checkExists")
	//protected Boolean checkExists;
	@XmlAttribute(name = "nbInstances")
	protected Integer nbInstances;
	@XmlAttribute(name = "envId")
	protected Integer envId;

	/**
	 * Obtient la valeur de la propri�t� uris.
	 * 
	 * @return possible object is {@link UrisType }
	 * 
	 */
	public UrisType getUris() {
		return uris;
	}

	/**
	 * D�finit la valeur de la propri�t� uris.
	 * 
	 * @param value
	 *            allowed object is {@link UrisType }
	 * 
	 */
	public void setUris(UrisType value) {
		this.uris = value;
	}

	/**
	 * Obtient la valeur de la propri�t� deployable.
	 * 
	 * @return possible object is {@link DeployableType }
	 * 
	 */
	public DeployableType getDeployable() {
		return deployable;
	}

	/**
	 * D�finit la valeur de la propri�t� deployable.
	 * 
	 * @param value
	 *            allowed object is {@link DeployableType }
	 * 
	 */
	public void setDeployable(DeployableType value) {
		this.deployable = value;
	}

	/**
	 * Obtient la valeur de la propri�t� instances.
	 * 
	 * @return possible object is {@link InstancesType }
	 * 
	 */
	public InstancesType getInstances() {
		return instances;
	}

	/**
	 * D�finit la valeur de la propri�t� instances.
	 * 
	 * @param value
	 *            allowed object is {@link InstancesType }
	 * 
	 */
	public void setInstances(InstancesType value) {
		this.instances = value;
	}

	/**
	 * Obtient la valeur de la propri�t� environment.
	 * 
	 * @return possible object is {@link EnvironmentType }
	 * 
	 */
	public EnvironmentType getEnvironment() {
		return environment;
	}

	/**
	 * D�finit la valeur de la propri�t� environment.
	 * 
	 * @param value
	 *            allowed object is {@link EnvironmentType }
	 * 
	 */
	public void setEnvironment(EnvironmentType value) {
		this.environment = value;
	}

	/**
	 * Obtient la valeur de la propri�t� linksList.
	 * 
	 * @return possible object is {@link LinksListType }
	 * 
	 */
	public LinksListType getLinksList() {
		return linksList;
	}

	/**
	 * D�finit la valeur de la propri�t� linksList.
	 * 
	 * @param value
	 *            allowed object is {@link LinksListType }
	 * 
	 */
	public void setLinksList(LinksListType value) {
		this.linksList = value;
	}

	/**
	 * Obtient la valeur de la propri�t� appName.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * D�finit la valeur de la propri�t� appName.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAppName(String value) {
		this.appName = value;
	}

	/**
	 * Obtient la valeur de la propri�t� appId.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getAppId() {
		return appId;
	}

	/**
	 * D�finit la valeur de la propri�t� appId.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setAppId(Integer value) {
		this.appId = value;
	}

	/**
	 * Obtient la valeur de la propri�t� status.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * D�finit la valeur de la propri�t� status.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStatus(String value) {
		this.status = value;
	}
//
//	/**
//	 * Obtient la valeur de la propri�t� memory.
//	 * 
//	 * @return possible object is {@link Integer }
//	 * 
//	 */
//	public Integer getMemory() {
//		return memory;
//	}
//
//	/**
//	 * D�finit la valeur de la propri�t� memory.
//	 * 
//	 * @param value
//	 *            allowed object is {@link Integer }
//	 * 
//	 */
//	public void setMemory(Integer value) {
//		this.memory = value;
//	}
//
//	/**
//	 * Obtient la valeur de la propri�t� checkExists.
//	 * 
//	 * @return possible object is {@link Boolean }
//	 * 
//	 */
//	public Boolean isCheckExists() {
//		return checkExists;
//	}
//
//	/**
//	 * D�finit la valeur de la propri�t� checkExists.
//	 * 
//	 * @param value
//	 *            allowed object is {@link Boolean }
//	 * 
//	 */
//	public void setCheckExists(Boolean value) {
//		this.checkExists = value;
//	}

	/**
	 * Obtient la valeur de la propri�t� nbInstances.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getNbInstances() {
		return nbInstances;
	}

	/**
	 * D�finit la valeur de la propri�t� nbInstances.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setNbInstances(Integer value) {
		this.nbInstances = value;
	}

	/**
	 * Obtient la valeur de la propri�t� envId.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getEnvId() {
		return envId;
	}

	/**
	 * D�finit la valeur de la propri�t� envId.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setEnvId(Integer value) {
		this.envId = value;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
