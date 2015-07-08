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

import telecom.sudparis.occi.autonomic.interfaces.NotificationToolMixinInterface;

public  class Notification {

	public void setNotifier(NotificationToolMixinInterface notifier) {
		this.notifier = notifier;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * 
	 */
	private String value;
	
	/**
	 * 
	 */
	private String type;

	/**
	 * 
	 */
	private NotificationToolMixinInterface notifier;

	/**
	 * 
	 */
	private String description;

	

	/**
	 * 
	 */
	private long timeStamp;
	
	/**
	 * 
	 */
	private String monitoredProperty;

	/**
	 * 
	 * @param type
	 * @param notifier
	 * @param description
	 */
	public Notification(String notificationType,
			NotificationToolMixinInterface notifier, String description) {

		this.type = notificationType;
		this.notifier = notifier;
		this.description = description;
		this.timeStamp = System.nanoTime();
	}
	/**
	 * 
	 * @param type
	 * @param notifier
	 * @param description
	 */
	public Notification(String notificationType,String timeStamp,
			String monitoredProperty,String value,String description) {

		this.type = notificationType;
		this.notifier = null;
		this.timeStamp=Long.parseLong(timeStamp);
		this.monitoredProperty=monitoredProperty;
		this.value=value;
		this.description = description;
		
	}

	/**
	 * 
	 * @param type
	 * @param notifier
	 */
	public Notification(String notificationType,
			NotificationToolMixinInterface notifier) {

		this(notificationType, notifier, "");
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public NotificationToolMixinInterface getNotifier() {
		return this.notifier;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}



	/**
	 * 
	 * @return
	 */
	public long getTimeStamp() {
		return this.timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuilder res = new StringBuilder();
		res.append("\n description       = ");
		res.append(this.description);
		res.append("\n monitoredProperty       = ");
		res.append(this.monitoredProperty);
		res.append("\n value       = ");
		res.append(this.value);
		res.append("\n Notification type = ");
		res.append(this.getType());
		res.append("\n timeStamp         = ");
		res.append(this.getTimeStamp());
		return res.toString();
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the monitoredProerty
	 */
	public String getMonitoredProerty() {
		return monitoredProperty;
	}

	/**
	 * @param monitoredProerty the monitoredProerty to set
	 */
	public void setMonitoredProperty(String monitoredProerty) {
		this.monitoredProperty = monitoredProerty;
	}

}
