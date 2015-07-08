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

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour paas_application_versionType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="paas_application_versionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paas_application_deployable" type="{}paas_application_deployableType"/>
 *         &lt;element name="paas_application_version_instance" type="{}paas_application_version_instanceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paas_application_versionType", propOrder = {
		"paasApplicationDeployable", "paasApplicationVersionInstance" })
public class PaasApplicationVersionType {

	@XmlElement(name = "paas_application_deployable", required = true)
	protected ArrayList<PaasApplicationDeployableType> paasApplicationDeployable;
	@XmlElement(name = "paas_application_version_instance", required = true)
	protected ArrayList<PaasApplicationVersionInstanceType> paasApplicationVersionInstance;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "label")
	protected String label;

	/**
	 * Obtient la valeur de la propri�t� paasApplicationDeployable.
	 * 
	 * @return possible object is {@link PaasApplicationDeployableType }
	 * 
	 */
	public ArrayList<PaasApplicationDeployableType> getPaasApplicationDeployable() {
		return paasApplicationDeployable;
	}

	/**
	 * D�finit la valeur de la propri�t� paasApplicationDeployable.
	 * 
	 * @param value
	 *            allowed object is {@link PaasApplicationDeployableType }
	 * 
	 */
	public void setPaasApplicationDeployable(ArrayList<PaasApplicationDeployableType> value) {
		this.paasApplicationDeployable = value;
	}

	/**
	 * Gets the value of the paasApplicationVersionInstance property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the paasApplicationVersionInstance property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPaasApplicationVersionInstance().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PaasApplicationVersionInstanceType }
	 * 
	 * 
	 */
	public ArrayList<PaasApplicationVersionInstanceType> getPaasApplicationVersionInstance() {
		if (paasApplicationVersionInstance == null) {
			paasApplicationVersionInstance = new ArrayList<PaasApplicationVersionInstanceType>();
		}
		return this.paasApplicationVersionInstance;
	}
	
	public void setPaasApplicationVersionInstance (ArrayList<PaasApplicationVersionInstanceType> listInstances){
		this.paasApplicationVersionInstance = listInstances;
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
	 * Obtient la valeur de la propri�t� label.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * D�finit la valeur de la propri�t� label.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setLabel(String value) {
		this.label = value;
	}

}
