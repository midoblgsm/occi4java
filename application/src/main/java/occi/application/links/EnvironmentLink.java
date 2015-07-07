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
package occi.application.links;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import occi.core.Link;
import occi.core.Resource;


public class EnvironmentLink extends Link {

	/**
	 * Current status of the instance.
	 */
	private State state;

	/**
	 * Possible status of the instance.
	 */
	public enum State {
		active, inactive
	}

	/**
	 * Static HashSet of all storage link attributes.
	 */
	public static HashSet<String> attributes = new HashSet<String>();
	
	/**
	 * Random UUID of the compute resource.
	 */
	private final UUID uuid;
	
	private static Map<UUID, EnvironmentLink> environmentlinkList = new HashMap<UUID, EnvironmentLink>();

	public EnvironmentLink(State state, Resource link,
			Resource target) throws URISyntaxException {
		super(link, target);
		this.state = state;
		uuid = UUID.randomUUID();
		environmentlinkList.put(uuid, this);
	}

	/**
	 * Generate attribute List.
	 */
	public static void generateAttributeList() {
		if (attributes.isEmpty()) {
			// add all attributes to attribute list
			attributes.add("occi.storagelink.state");
		}
	}

	
	/**
	 * Returns state of the storage link.
	 * 
	 * @return state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return the environmentlinkList
	 */
	public static Map<UUID, EnvironmentLink> getEnvironmentLinkList() {
		return environmentlinkList;
	}

	/**
	 * @param environmentlinkList the environmentlinkList to set
	 */
	public static void setEnvironmentLinkList(Map<UUID, EnvironmentLink> environmentlinkList) {
		EnvironmentLink.environmentlinkList = environmentlinkList;
	}
	
	
}