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
package telecom.sudparis.occi.autonomic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;






import com.berniecode.mixin4j.MixinBase;

import occi.config.OcciConfig;
import occi.core.Kind;
import occi.core.Link;
import occi.core.Resource;
import telecom.sudparis.occi.autonomic.interfaces.SubscriptionToolMixinInterface;

@MixinBase
public abstract class SubscriptionLink extends Link implements
		SubscriptionToolMixinInterface {
	/**
	 * State of the compute resource
	 */
	private State state;

	/**
	 * Current State of the instance
	 */
	public enum State {
		unavailable, available
	}
	
	/**
	 * Static Hashmap of all Analyzer Resources. The Key is a UUID, the Value a
	 * Analyzer Object.
	 */
	private static Map<UUID, SubscriptionLink> subscriptionLinkList = new HashMap<UUID, SubscriptionLink>();

	/**
	 * Static HashSet of all analyzer attributes.
	 */
	
	private static HashSet<String> attributes = new HashSet<String>();

	/**
	 * Random UUID of the Actionlink resource.
	 */
	private final UUID uuid;
	
	/**
	 * Name of the ActionLink.
	 */
	private String name;
	/**
	 * Version of the ActionLink.
	 */
	private String version;

	public SubscriptionLink(Resource source, Resource target, String name, String version, State state, Set<String>set)
			throws URISyntaxException, SchemaViolationException {
		super(source, target);
		this.name=name;
		this.version=version;
		this.state=state;
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// TODO expected error: incompatible variable attributes

		setKind(new Kind(null, null, null, null, "subscriptionlink", "SubscriptionLink",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/autonomic#",attributes));
		

		setId(new URI(uuid.toString()));
		// put resource into compute list
		subscriptionLinkList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();

	}

	public static Map<UUID, SubscriptionLink> getActionLinkList() {
		// TODO Auto-generated method stub
		return subscriptionLinkList;
	}
	
	/**
	 * Generate attribute List.
	 */
	public static final void generateAttributeList() {
		if (attributes.isEmpty()) {
			// add all attributes to attribute list
			attributes.add("occi.subscriptionlink.name");
			attributes.add("occi.subscriptionlink.version");

		}
	}
	public final UUID getUuid() {
		return uuid;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @param subscriptionLinkList the subscriptionLinkList to set
	 */
	public static void setSubscriptionLinkList(
			Map<UUID, SubscriptionLink> subscriptionLinkList) {
		SubscriptionLink.subscriptionLinkList = subscriptionLinkList;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public static void setAttributes(HashSet<String> attributes) {
		SubscriptionLink.attributes = attributes;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return the subscriptionLinkList
	 */
	public static Map<UUID, SubscriptionLink> getSubscriptionLinkList() {
		return subscriptionLinkList;
	}

	/**
	 * @return the attributes
	 */
	public static HashSet<String> getAttributes() {
		return attributes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
}
