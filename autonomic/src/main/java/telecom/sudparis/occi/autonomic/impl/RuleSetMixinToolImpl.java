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
package telecom.sudparis.occi.autonomic.impl;

import java.util.Map;
import java.util.UUID;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.berniecode.mixin4j.MixinAware;

import telecom.sudparis.occi.autonomic.AlertLink;
import telecom.sudparis.occi.autonomic.Analyzer;
import telecom.sudparis.occi.autonomic.interfaces.RuleSetMixinToolInterface;
import telecom.sudparis.occi.autonomic.tools.Alert;
import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

public class RuleSetMixinToolImpl implements RuleSetMixinToolInterface,
		MixinAware<Analyzer> {
	private static int counter = 0;
	private static double notifTable[];
	private String alertLinkURI = "http://157.159.110.227:8182/alertlink/alert/";
	private int maxThreshold;
	private int minThreshold;
	private int aggregationInterval;

	/**
	 * @return the maxThreshold
	 */
	public int getMaxThreshold() {
		return maxThreshold;
	}

	@Override
	public void setMaxThreshold(int maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	/**
	 * @return the minThreshold
	 */
	public int getMinThreshold() {
		return minThreshold;
	}

	@Override
	/**
	 * @param minThreshold the minThreshold to set
	 */
	public void setMinThreshold(int minThreshold) {
		this.minThreshold = minThreshold;
	}

	/**
	 * @return the aggregationInterval
	 */
	public int getAggregationInterval() {
		return aggregationInterval;
	}

	@Override
	/**
	 * @param aggregationInterval the aggregationInterval to set
	 */
	public void setAggregationInterval(int aggregationInterval) {
		this.aggregationInterval = aggregationInterval;
		notifTable = new double[aggregationInterval];
	}

	private Analyzer mixinBase;
	private ClientResource resource;

	@Override
	public void analyze(Notification notification) {

		/**
		 * TODO use a real condition threshold
		 */
		System.out.println("RuleSetMixin:: Analyzing ");
		double value = Double.parseDouble(notification.getValue());
		notifTable[counter] = value;
		counter++;
		System.out.println("RuleSet:: Counter==" + counter);
		if (counter >= aggregationInterval) {
			double average = getAvg(notifTable);
			counter = 0;
			if (average > maxThreshold)

			{
				System.out.println("RuleSet:: ScaleUp Alert");
				Alert alert = new Alert("scaleup", "Description");
				alert(alert);
			} else if (average < minThreshold) {
				System.out.println("RuleSet:: ScaleDown Alert");
				Alert alert = new Alert("scaledown", "Description");
				alert(alert);
			}
		}
	}

	private double getAvg(double[] notifTable2) {
		double avg = 0;
		for (int i = 0; i < notifTable2.length; i++) {
			avg = avg + notifTable2[i];
		}
		return avg / notifTable2.length;
	}

	@Override
	public void alert(Alert alert) {
//		 Client client = new Client(Protocol.HTTP);
//		 client.setConnectTimeout(100000);
		try {
			resource = new ClientResource(getAlertLinkURI());
			//resource.setNext(client);
			resource.post(XMLTools.getAlertXML(alert));
			resource.release();
		} catch (ResourceException e) {
			System.err.println("RuleSetMixin error: " +e.getMessage());

		}

	}

	/**
	 * @return the alertLinkURI
	 */
	public String getAlertLinkURI() {
		Map<UUID, AlertLink> alertLinkList = AlertLink.getAlertLinkList();
		for (UUID id : alertLinkList.keySet()) {
			AlertLink alertLink = alertLinkList.get(id);
			if (alertLink.getLink().getId().toString()
					.equals(mixinBase.getId().toString()))// .equals(mixinBase.getId())
			{
				// alertLinkURI = alertLink.getId().toString();
				String uri = (alertLinkURI + alertLink.getId().toString())
						.substring(alertLink.getId().toString()
								.lastIndexOf("/") + 1);
				System.out.println("RuleSetMixin" + uri);
				return   uri;
			}
		}
		System.out.println("Probably alertLink null");
		return alertLinkURI;
	}

	@Override
	public void setMixinBase(Analyzer mixinBase) {
		this.mixinBase = (Analyzer) mixinBase;

	}

}
