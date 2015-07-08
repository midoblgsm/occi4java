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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour deployableType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="deployableType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="deployableName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="deployableId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="deployableType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="deployableDirectory" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deployableType")
public class DeployableType {

	@XmlAttribute(name = "deployableName")
	protected String deployableName;
	@XmlAttribute(name = "deployableId")
	protected String deployableId;
	@XmlAttribute(name = "deployableType")
	protected String deployableType;
	@XmlAttribute(name = "deployableDirectory")
	protected String deployableDirectory;

	/**
	 * Obtient la valeur de la propri�t� deployableName.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeployableName() {
		return deployableName;
	}

	/**
	 * D�finit la valeur de la propri�t� deployableName.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDeployableName(String value) {
		this.deployableName = value;
	}

	/**
	 * Obtient la valeur de la propri�t� deployableId.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeployableId() {
		return deployableId;
	}

	/**
	 * D�finit la valeur de la propri�t� deployableId.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDeployableId(String value) {
		this.deployableId = value;
	}

	/**
	 * Obtient la valeur de la propri�t� deployableType.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeployableType() {
		return deployableType;
	}

	/**
	 * D�finit la valeur de la propri�t� deployableType.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDeployableType(String value) {
		this.deployableType = value;
	}

	/**
	 * Obtient la valeur de la propri�t� deployableDirectory.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeployableDirectory() {
		return deployableDirectory;
	}

	/**
	 * D�finit la valeur de la propri�t� deployableDirectory.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDeployableDirectory(String value) {
		this.deployableDirectory = value;
	}

}
