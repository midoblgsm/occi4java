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

/**
 * The StorageLink type represents a link from a Resource to a target Storage
 * instance. This enables a Storage instance be attached to a Compute instance,
 * with all the prerequisite low- level operations handled by the OCCI
 * implementation. Storage inherits the Link base type defined in the OCCI Core
 * Model.
 * 
 * The StorageLink type is assigned the Kind instance
 * http://schemas.ogf.org/occi/infrastructure#storagelink. A StorageLink
 * instance MUST use and expose this Kind. The Kind instance assigned to the
 * StorageLink type MUST be related to the http://schemas.ogf.org/occi/core#link
 * Kind. [T. Metsch, A. Edmonds - Open Cloud Computing Interface -
 * Infrastructure, http://ogf.org/documents/GFD.184.pdf, Apr. 2011]
 * 
 * @author Sebastian Laag
 * @author Sebastian Heckmann
 */
public class DeployableLink extends Link {

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
	
	private static Map<UUID, DeployableLink> deployablelinkList = new HashMap<UUID, DeployableLink>();

	public DeployableLink(State state, Resource link,
			Resource target) throws URISyntaxException {
		super(link, target);
		this.state = state;
		uuid = UUID.randomUUID();
		deployablelinkList.put(uuid, this);
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
	 * @return the deployablelinkList
	 */
	public static Map<UUID, DeployableLink> getDeployableLinkList() {
		return deployablelinkList;
	}

	/**
	 * @param deployablelinkList the deployablelinkList to set
	 */
	public static void setDeployableLinkList(Map<UUID, DeployableLink> deployablelinkList) {
		DeployableLink.deployablelinkList = deployablelinkList;
	}
	
	
}