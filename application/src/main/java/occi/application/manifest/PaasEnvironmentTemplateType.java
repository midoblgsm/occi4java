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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour paas_environment_templateType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="paas_environment_templateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paas_environment_node" type="{}paas_environment_nodeType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="memory" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paas_environment_templateType", propOrder = { "description",
		"paasEnvironmentNode", "passEnvironmentRelation", "passEnvironmentConfig" })
public class PaasEnvironmentTemplateType {

	@XmlElement(required = true)
	protected String description;
	@XmlElement(name = "paas_environment_node", required = true)
	protected ArrayList<PaasEnvironmentNodeType> paasEnvironmentNode;
	@XmlElement (name = "paas_environment_relation")
	protected PaasEnvironmentRelationType passEnvironmentRelation;
	@XmlElement (name = "paas_environment_configuration")
	protected PaasEnvironmentConfigurationType passEnvironmentConfig;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "memory")
	protected Integer memory;	

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
	 * Gets the value of the paasEnvironmentNode property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the paasEnvironmentNode property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPaasEnvironmentNode().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PaasEnvironmentNodeType }
	 * 
	 * 
	 */
	public ArrayList<PaasEnvironmentNodeType> getPaasEnvironmentNode() {
		if (paasEnvironmentNode == null) {
			paasEnvironmentNode = new ArrayList<PaasEnvironmentNodeType>();
		}
		return this.paasEnvironmentNode;
	}
	
	public void setPaasEnvironmentNode (ArrayList<PaasEnvironmentNodeType> listNode){
		this.paasEnvironmentNode = listNode;
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

	/**
	 * Obtient la valeur de la propri�t� memory.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMemory() {
		return memory;
	}

	/**
	 * D�finit la valeur de la propri�t� memory.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMemory(Integer value) {
		this.memory = value;
	}
	
	public PaasEnvironmentRelationType getPassEnvironmentRelation() {
		return passEnvironmentRelation;
	}

	public void setPassEnvironmentRelation(
			PaasEnvironmentRelationType passEnvironmentRelation) {
		this.passEnvironmentRelation = passEnvironmentRelation;
	}

	public PaasEnvironmentConfigurationType getPassEnvironmentConfig() {
		return passEnvironmentConfig;
	}

	public void setPassEnvironmentConfig(
			PaasEnvironmentConfigurationType passEnvironmentConfig) {
		this.passEnvironmentConfig = passEnvironmentConfig;
	}

}
