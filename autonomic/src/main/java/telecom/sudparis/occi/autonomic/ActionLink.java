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

import telecom.sudparis.occi.autonomic.interfaces.ActionToolMixinInterface;

import com.berniecode.mixin4j.MixinBase;

import occi.config.OcciConfig;
import occi.core.Action;
import occi.core.Kind;
import occi.core.Link;
import occi.core.Resource;

@MixinBase
public abstract class ActionLink extends Link implements
		ActionToolMixinInterface {
	public State getState() {
		return state;
	}

	public static HashSet<String> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
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
	private static Map<UUID, ActionLink> actionLinkList = new HashMap<UUID, ActionLink>();

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

	public ActionLink(Resource source, Resource target,String name,String version, State state,Set<String> set )
			throws URISyntaxException, SchemaViolationException {
		super(source, target);
		
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// TODO expected error: incompatible variable attributes

		setKind(new Kind(null, null, null, null, "actionLink", "ActionLink",
				OcciConfig.getInstance().config.getString("occi.scheme")
						+ "/autonomic#",attributes));
		

		setId(new URI(uuid.toString()));
		// put resource into compute list
		actionLinkList.put(uuid, this);
		// Generate attribute list
		generateAttributeList();

	}

	public static Map<UUID, ActionLink> getActionLinkList() {
		// TODO Auto-generated method stub
		return actionLinkList;
	}
	
	/**
	 * Generate attribute List.
	 */
	public static final void generateAttributeList() {
		if (attributes.isEmpty()) {
			// add all attributes to attribute list
			attributes.add("occi.actionlink.name");
			attributes.add("occi.actionlink.version");

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
	 * @param actionLinkList the actionLinkList to set
	 */
	public static void setActionLinkList(Map<UUID, ActionLink> actionLinkList) {
		ActionLink.actionLinkList = actionLinkList;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public static void setAttributes(HashSet<String> attributes) {
		ActionLink.attributes = attributes;
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

	public static Action getReconfigureAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
