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
package telecom.sudparis.occi.autonomic.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLTools {

	public static String getResponseTime(String xmlString, String appName)
			throws XMLStreamException, SAXException, IOException,
			ParserConfigurationException {

		InputSource is = new InputSource(new StringReader(xmlString));

		SAXBuilder saxBuilder = new SAXBuilder();

		try {
			Document document = saxBuilder.build(is);
			Element rootNode = document.getRootElement();
			List<Element> applicationsList = rootNode
					.getChildren("applications");

			for (int i = 0; i < applicationsList.size(); i++) {

				Element element = (Element) applicationsList.get(i);
				List<Element> appList = element.getChildren();

				for (int j = 0; j < appList.size(); j++) {

					element = appList.get(j);

					String applicationName = element.getChildText("name");

					if (applicationName.equals(appName)) {
						element = element.getChild("application_summary");

						return element.getChildText("response_time");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Alert getAlertFromXML(String xmlString) {

		InputSource is = new InputSource(new StringReader(xmlString));

		SAXBuilder saxBuilder = new SAXBuilder();

		Document document;
		Element rootNode = null;
		try {
			document = saxBuilder.build(is);
			rootNode = document.getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Alert(rootNode.getChildText("alerttype"),
				rootNode.getChildText("description"),
				rootNode.getChildText("timestamp"));

	}

	public static String getAlertXML(Alert alert) {
		Element xmlAlert = new Element("alert");
		Document document = new Document(xmlAlert);

		StringWriter s = new StringWriter();

		xmlAlert.addContent(new Element("timestamp").setText(String
				.valueOf(alert.getTimeStamp())));
		xmlAlert.addContent(new Element("alerttype").setText(""
				+ alert.getType()));
		xmlAlert.addContent(new Element("description").setText(""
				+ alert.getDescription()));

		XMLOutputter xmlOutput = new XMLOutputter();
		try {
			xmlOutput.output(document, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// passed fileWriter to write content in specified file
		xmlOutput.setFormat(Format.getPrettyFormat());

		return s.toString();

	}

	public static Notification getNotificationFromXML(String xmlString) {

		InputSource is = new InputSource(new StringReader(xmlString));

		SAXBuilder saxBuilder = new SAXBuilder();

		Document document;
		Element rootNode = null;
		try {
			document = saxBuilder.build(is);
			rootNode = document.getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Notification notification = new Notification(
				rootNode.getChildText("notificationtype"),
				rootNode.getChildText("timestamp"),
				rootNode.getChildText("monitoredproperty"),
				rootNode.getChildText("value"),
				rootNode.getChildText("description"));
		return notification;

	}

	public static String getNotificationXML(Notification notification) {
		Element xmlAlert = new Element("notification");
		Document document = new Document(xmlAlert);

		StringWriter s = new StringWriter();

		xmlAlert.addContent(new Element("timestamp").setText(String
				.valueOf(notification.getTimeStamp())));
		xmlAlert.addContent(new Element("notificationtype").setText(""
				+ notification.getType()));
		xmlAlert.addContent(new Element("monitoredproperty")
				.setText(notification.getMonitoredProerty()));

		xmlAlert.addContent(new Element("value").setText(notification
				.getValue()));
		xmlAlert.addContent(new Element("description").setText(notification
				.getDescription()));

		XMLOutputter xmlOutput = new XMLOutputter();
		try {
			xmlOutput.output(document, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// passed fileWriter to write content in specified file
		xmlOutput.setFormat(Format.getPrettyFormat());

		return s.toString();

	}

	public static void main(String arg[]) {
		File f = new File("C:\\temp\\jar\\notification.xml");
		StringBuffer s = new StringBuffer();
		try {
			Scanner sc = new Scanner(new FileReader(f));
			while (sc.hasNext()) {
				s.append(sc.nextLine());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Notification a = getNotificationFromXML(s.toString());
		System.out.println(a.toString());
		System.out.println(getNotificationXML(a));
	}

	public static Subscription getSubscriptionFromXML(String xmlString) {

		InputSource is = new InputSource(new StringReader(xmlString));

		SAXBuilder saxBuilder = new SAXBuilder();

		Document document;
		Element rootNode = null;
		try {
			document = saxBuilder.build(is);
			rootNode = document.getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Subscription subscription = new Subscription(Long.parseLong(rootNode
				.getChildText("duration")),
				rootNode.getChildText("subscriber"), Long.parseLong(rootNode
						.getChildText("interval")));
		return subscription;

	}

	public static String getSubscriptionXML(Subscription subscription) {
		Element xmlAlert = new Element("subscription");
		Document document = new Document(xmlAlert);

		StringWriter s = new StringWriter();

		xmlAlert.addContent(new Element("duration").setText(String
				.valueOf(subscription.getDuration())));
		xmlAlert.addContent(new Element("subscriber").setText(""
				+ subscription.getSubscriber()));
		xmlAlert.addContent(new Element("interval").setText(String
				.valueOf(subscription.getInterval())));

		XMLOutputter xmlOutput = new XMLOutputter();
		try {
			xmlOutput.output(document, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// passed fileWriter to write content in specified file
		xmlOutput.setFormat(Format.getPrettyFormat());

		return s.toString();

	}

	public static ReconfigurationAction getReconfigurationActionFromXML(
			String xmlString) {

		InputSource is = new InputSource(new StringReader(xmlString));

		SAXBuilder saxBuilder = new SAXBuilder();

		Document document;
		Element rootNode = null;
		try {
			document = saxBuilder.build(is);
			rootNode = document.getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * TODO
		 */
		int arg = Integer.parseInt(rootNode.getChildText("args"));
		Object args[] = { arg };
		ReconfigurationAction reconfigurationAction = new ReconfigurationAction(
				rootNode.getChildText("methodname"), args);
		return reconfigurationAction;

	}

	public static String getReconfigurationActionXML(
			ReconfigurationAction reconfigurationAction) {
		Element xmlAlert = new Element("reconfigurationaction");
		Document document = new Document(xmlAlert);

		StringWriter s = new StringWriter();

		xmlAlert.addContent(new Element("methodname").setText(String
				.valueOf(reconfigurationAction.getMethodeName())));
		xmlAlert.addContent(new Element("args").setText(""
				+ reconfigurationAction.getArgs()[0]));

		XMLOutputter xmlOutput = new XMLOutputter();
		try {
			xmlOutput.output(document, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// passed fileWriter to write content in specified file
		xmlOutput.setFormat(Format.getPrettyFormat());

		return s.toString();

	}
}
