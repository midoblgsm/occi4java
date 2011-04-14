package occi.infrastructure.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;

import occi.core.Link;
import occi.core.Resource;

/**
 * The NetworkInterface type represents an L2 client device (e.g. network
 * adapter). It can be extended using the mix-in mechanism or sub-typed to
 * support L3/L4 capabilities such as TCP/IP etc. NetworkInterface inherits the
 * Link base type defined in the OCCI Core Model.
 * 
 * The NetworkInterface type is assigned the Kind instance
 * http://schemas.ogf.org/occi/infrastructure#networkinterface. A
 * NetworkInterface instance MUST use and expose this Kind. The Kind instance
 * assigned to the Network- Interface type MUST be related to the
 * http://schemas.ogf.org/occi/core#link Kind.
 * 
 * @author Sebastian Laag
 * @author Sebastian Heckmann
 */
public class NetworkInterface extends Link {
	/**
	 * Identifier that relates the link to the link's device interface.
	 */
	private final String networkInterface;
	/**
	 * MAC address associated with the link's device interface.
	 */
	private String mac;
	/**
	 * Current status of the instance.
	 */
	private final State state;

	/**
	 * Possible status of the instance.
	 */
	public enum State {
		active, inactive
	}

	/**
	 * Random UUID of the compute resource.
	 */
	private final UUID uuid;

	public static Map<UUID, NetworkInterface> networkInterfaceList = new HashMap<UUID, NetworkInterface>();

	/**
	 * Static HashSet of all network interface attributes.
	 */
	public static HashSet<String> attributes = new HashSet<String>();

	public NetworkInterface(String networkInterface, String mac, State state,
			Resource link, Resource target) throws URISyntaxException,
			SchemaViolationException {
		super(link, target);
		this.networkInterface = networkInterface;
		this.mac = mac;
		this.state = state;
		uuid = UUID.randomUUID();
		setId(new URI(uuid.toString()));
		// put resource into networkinterface list
		networkInterfaceList.put(uuid, this);

		// setKind(new Kind(this, target.getKind().getTerm(), target.getKind()
		// .getTitle(), attributes));

		// Generate attribute list
		generateAttributeList();
	}

	/**
	 * Generate attribute List.
	 */
	public static void generateAttributeList() {
		if (attributes.isEmpty()) {
			// add all attributes to attribute list
			attributes.add("occi.networkinterface.interface");
			attributes.add("occi.networkinterface.mac");
			attributes.add("occi.networkinterface.state");
		}
	}

	/**
	 * Return the full network interface list.
	 * 
	 * @return all compute resources
	 */
	public static HashSet<String> getAttributes() {
		return attributes;
	}

	/**
	 * Returns the network interface.
	 * 
	 * @return string
	 */
	public String getNetworkInterface() {
		return networkInterface;
	}

	/**
	 * Sets the mac adress of the network interface.
	 * 
	 * @param mac
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * Returns the mac adress of the network interface.
	 * 
	 * @return string
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * Returns the state of the network interface.
	 * 
	 * @return state
	 */
	public State getState() {
		return state;
	}
}