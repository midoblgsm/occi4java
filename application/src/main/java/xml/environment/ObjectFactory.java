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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import xml.LinkType;
import xml.LinksListType;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * xml.environment package.
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

	private final static QName _Environment_QNAME = new QName("", "environment");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * xml.environment
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link EnvironmentType }
	 * 
	 */
	public EnvironmentType createEnvironmentType() {
		return new EnvironmentType();
	}

	/**
	 * Create an instance of {@link ConfigurationType }
	 * 
	 */
	public ConfigurationType createConfigurationType() {
		return new ConfigurationType();
	}

	/**
	 * Create an instance of {@link EntryType }
	 * 
	 */
	public EntryType createEntryType() {
		return new EntryType();
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
	 * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "environment")
	public JAXBElement<EnvironmentType> createEnvironment(EnvironmentType value) {
		return new JAXBElement<EnvironmentType>(_Environment_QNAME,
				EnvironmentType.class, null, value);
	}

}
