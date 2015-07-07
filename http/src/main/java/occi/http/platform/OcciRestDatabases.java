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
package occi.http.platform;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.naming.directory.SchemaViolationException;

import occi.config.OcciConfig;
import occi.core.Kind;
import occi.http.check.OcciCheck;
import occi.platform.Database;
import occi.infrastructure.Network;
import occi.infrastructure.links.NetworkInterface;
import occi.platform.Database.Architecture;
import occi.platform.Database.State;
import occi.platform.Database.Type;
import occi.platform.database.actions.RestartAction.Restart;
import occi.platform.database.actions.StartAction.Start;
import occi.platform.database.actions.StopAction.Stop;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nguyen_n
 *
 */
public class OcciRestDatabases extends ServerResource{
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(OcciRestDatabases.class);

	@Get
	public String treatingGETRequest(Representation representation) {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));

		// initialize database list
		Map<UUID, Database> databaseList = Database.getDatabaseList();
		// initialize buffer
		StringBuffer buffer = new StringBuffer();
		Database database = null;

		// Access the request Headers
		Form requestHeaders = (Form) getRequest().getAttributes().get(
				"org.restlet.http.headers");
		LOGGER.debug("Raw Request Headers: " + requestHeaders);

		LOGGER.debug("Size of database list: " + databaseList.size());
		// iterate through all available database resources
		int i = 1;
		for (UUID id : databaseList.keySet()) {
			database = databaseList.get(id);
			buffer.append(getReference());
			buffer.append(database.getId());
			if (i < databaseList.size()) {
				buffer.append(",");
			}
			i++;
		}

		representation = OcciCheck.checkContentType(requestHeaders, buffer,
				getResponse());
		getResponse().setEntity(representation);
		if (databaseList.size() <= 0) {
			// return http status code
			getResponse().setStatus(Status.SUCCESS_NO_CONTENT,
					buffer.toString());
			return "There are no database resources";
		} else if (representation.getMediaType().toString().equals("text/occi")) {
			// Set Location Attribute
			setLocationRef(buffer.toString());
			// return http status code
			getResponse().setStatus(Status.SUCCESS_OK, " ");
		} else {
			// return http status code
			getResponse().setStatus(Status.SUCCESS_OK);
		}
		return " ";
	}	
}
