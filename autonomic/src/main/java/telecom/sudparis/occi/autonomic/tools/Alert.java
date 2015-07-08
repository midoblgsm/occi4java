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

import telecom.sudparis.occi.autonomic.ReconfigurationManager;

public class Alert {

	private String type;

	/**
	 * 
	 */
	//private ReconfigurationManager reconfigurationManager;

	/**
	 * 
	 */
	private String description;

	/**
	 * 
	 */
	private double timeStamp;

	public Alert(String alertType, String description) {
		this.type = alertType;
		this.description = description;
		this.timeStamp = System.nanoTime();
	}

	/**
	 * 
	 * @param type
	 * @param notifier
	 */
//	protected Alert(String alertType,
//			ReconfigurationManager reconfigurationManager) {
//
//		this(alertType, reconfigurationManager, "");
//	}

	public Alert(String alertType, String description, String timeStamp) {
		this.type = alertType;
		this.description = description;
		this.timeStamp = Double.parseDouble(timeStamp);
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
//	public ReconfigurationManager getReconfigurationManager() {
//		return this.reconfigurationManager;
//	}

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
	public double getTimeStamp() {
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
		res.append("\n alert type = ");
		res.append(this.getType());
		res.append("\n timeStamp         = ");
		res.append(this.getTimeStamp());
		return res.toString();
	}

}
