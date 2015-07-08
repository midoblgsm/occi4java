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
// G�n�r� le : 2013.04.17 � 04:43:35 PM CEST 
//

package xml.environment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import xml.LinksListType;

/**
 * <p>
 * Classe Java pour environmentType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="environmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="configuration" type="{}configurationType"/>
 *         &lt;element name="linksList" type="{}linksListType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="envId" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="envName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="envMemory" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="envDesc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "environment")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentType", propOrder = { "configuration", "linksList" })
public class EnvironmentType {

	@XmlElement(required = true)
	protected ConfigurationType configuration;
	@XmlElement(required = true)
	protected LinksListType linksList;
	@XmlAttribute(name = "envId")
	protected Integer envId;
	@XmlAttribute(name = "envName")
	protected String envName;
	@XmlAttribute(name = "envMemory")
	protected Integer envMemory;
	@XmlAttribute(name = "envDesc")
	protected String envDesc;

	/**
	 * Obtient la valeur de la propri�t� configuration.
	 * 
	 * @return possible object is {@link ConfigurationType }
	 * 
	 */
	public ConfigurationType getConfiguration() {
		return configuration;
	}

	/**
	 * D�finit la valeur de la propri�t� configuration.
	 * 
	 * @param value
	 *            allowed object is {@link ConfigurationType }
	 * 
	 */
	public void setConfiguration(ConfigurationType value) {
		this.configuration = value;
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

	/**
	 * Obtient la valeur de la propri�t� envName.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnvName() {
		return envName;
	}

	/**
	 * D�finit la valeur de la propri�t� envName.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnvName(String value) {
		this.envName = value;
	}

	/**
	 * Obtient la valeur de la propri�t� envMemory.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getEnvMemory() {
		return envMemory;
	}

	/**
	 * D�finit la valeur de la propri�t� envMemory.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setEnvMemory(Integer value) {
		this.envMemory = value;
	}

	/**
	 * Obtient la valeur de la propri�t� envDesc.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnvDesc() {
		return envDesc;
	}

	/**
	 * D�finit la valeur de la propri�t� envDesc.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnvDesc(String value) {
		this.envDesc = value;
	}

}
