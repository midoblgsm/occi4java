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
// G�n�r� le : 2013.04.18 � 04:42:34 PM CEST 
//

package occi.application.manifest;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour paas_environment_configurationType complex type.
 * 
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette
 * classe.
 * 
 * <pre>
 * &lt;complexType name="paas_environment_configurationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paas_environment_variable" type="{}paas_environment_variableType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paas_environment_configurationType", propOrder = { "paasEnvironmentVariables" })
public class PaasEnvironmentConfigurationType {

	@XmlElement(name = "paas_environment_variable", required = true)
	protected ArrayList<PaasEnvironmentVariableType> paasEnvironmentVariables;

	/**
	 * Obtient la valeur de la propri�t� paasEnvironmentVariable.
	 * 
	 * @return possible object is {@link PaasEnvironmentVariableType }
	 * 
	 */
	public ArrayList<PaasEnvironmentVariableType> getPaasEnvironmentVariables() {
		return paasEnvironmentVariables;
	}

	/**
	 * D�finit la valeur de la propri�t� paasEnvironmentVariable.
	 * 
	 * @param value
	 *            allowed object is {@link PaasEnvironmentVariableType }
	 * 
	 */
	public void setPaasEnvironmentVariables(ArrayList<PaasEnvironmentVariableType> value) {
		this.paasEnvironmentVariables = value;
	}

}
