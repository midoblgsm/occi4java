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

//import com.berniecode.mixin4j.MixinAware;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.berniecode.mixin4j.MixinAware;

import telecom.sudparis.occi.autonomic.AutonomicManager;
import telecom.sudparis.occi.autonomic.interfaces.SpecificSLAMixinInterface;

//public class SpecificSLAMixinToolImpl implements SpecificSLAMixinInterface,
//		MixinAware {
public class SpecificSLAMixinToolImpl implements SpecificSLAMixinInterface,
		MixinAware<AutonomicManager> {
	private AutonomicManager mixinBase;
	private String slaLocation;
	Properties properties;

	private void getDescriptor() {
		try {
			properties = new Properties();
			InputStream inputStream = new FileInputStream(
					(new File(slaLocation)));

			properties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void inspectSLA(String slaLocation) {
		// this.setSla(sla);
		/**
		 * Container infos
		 */
		this.slaLocation = slaLocation;
		getDescriptor();

		mixinBase.setContainerName(properties.getProperty("containerName"));
		mixinBase.setContainerVersion(properties
				.getProperty("containerVersion"));
		mixinBase.setContainerArchitecture(properties
				.getProperty("containerArchitecture"));

		/**
		 * Database infos
		 */

		mixinBase.setDatabaseName(properties.getProperty("databaseName"));
		mixinBase.setDatabaseVersion(properties.getProperty("databaseVersion"));
		mixinBase.setDatabaseArchitecture(properties
				.getProperty("databaseArchitecture"));
		mixinBase.setDatabaseType(properties.getProperty("databaseType"));

		/**
		 * databaseLink infos
		 */

		mixinBase.setDatabaseLinkName(properties
				.getProperty("databaseLinkName"));

		/**
		 * Environment infos
		 */

		mixinBase.setEnvironmentMemory(properties
				.getProperty("environmentName"));
		mixinBase.setEnvironmentMemory(properties
				.getProperty("environmentMemory"));
		mixinBase.setEnvironmentVariables(properties
				.getProperty("environmentVariables"));// "var|value";

		/**
		 * Deployable infos
		 */

		mixinBase.setDeployableName(properties.getProperty("deployableName"));
		mixinBase.setDeployableType(properties.getProperty("deployableType"));
		mixinBase.setDeployableLocation(properties
				.getProperty("deployableLocation"));

		/**
		 * Application infos
		 */
		mixinBase.setApplicationName(properties.getProperty("applicationName"));
		mixinBase.setApplicationDescription(properties
				.getProperty("applicationDescription"));
		mixinBase.setApplicationInstances(properties
				.getProperty("applicationInstances"));

		/**
		 * Analyzer infos
		 */

		mixinBase.setAnalyzerName(properties.getProperty("analyzerName"));
		mixinBase.setAnalyzerVersion(properties.getProperty("analyzerVersion"));
		mixinBase.setAggregationInterval(Integer.parseInt(properties
				.getProperty("aggregationInterval")));
		mixinBase.setMaxThreshold(Integer.parseInt(properties
				.getProperty("maxThreshold")));
		mixinBase.setMinThreshold(Integer.parseInt(properties
				.getProperty("minThreshold")));
		/**
		 * Subscription infos
		 */

		mixinBase.setSubscriptionDuration(properties
				.getProperty("subscriptionDuration"));
		mixinBase.setSubscriptionInterval(properties
				.getProperty("subscriptionInterval"));

		/**
		 * reconfigManager infos
		 */
		mixinBase.setReconfigurationManagerName(properties
				.getProperty("reconfigurationManagerName"));
		mixinBase.setReconfigurationManagerVersion(properties
				.getProperty("reconfigurationManagerVersion"));

		/**
		 * SubLink infos
		 */
		mixinBase.setSubscriptionLinkName(properties
				.getProperty("subscriptionLinkName"));
		mixinBase.setSubscriptionLinkVersion(properties
				.getProperty("subscriptionLinkVersion"));

		/**
		 * NotifLink infos
		 */
		mixinBase.setNotificationLinkName(properties
				.getProperty("notificationLinkName"));
		mixinBase.setNotificationLinkVersion(properties
				.getProperty("notificationLinkVersion"));
		mixinBase.setNotificationRefreshRate(properties
				.getProperty("notificationRefreshRate"));
		/**
		 * AlertLink Infos
		 */

		mixinBase.setAlertLinkName(properties.getProperty("alertLinkName"));
		mixinBase.setAlertLinkVersion(properties
				.getProperty("alertLinkVersion"));

		/**
		 * ActionLink infos
		 */
		mixinBase.setActionLinkName(properties.getProperty("actionLinkName"));
		mixinBase.setActionLinkVersion(properties
				.getProperty("actionLinkVersion"));
	}

	/**
	 * @return the sla
	 */
	public String getSla() {
		return slaLocation;
	}

	/**
	 * @param sla
	 *            the sla to set
	 */
	public void setSla(String sla) {
		this.slaLocation = sla;
	}

	/**
	 * @param mixinBase
	 *            the mixinBase to set
	 */
	@Override
	public void setMixinBase(AutonomicManager mixinBase) {
		this.mixinBase = mixinBase;

	}

	/**
	 * @return the mixinBase
	 */
	public Object getMixinBase() {
		return mixinBase;
	}

	// @Override
	// public void setMixinBase(Object mixinBase) {
	// this.mixinBase = mixinBase;
	// // TODO Auto-generated method stub
	//
	// }

}
