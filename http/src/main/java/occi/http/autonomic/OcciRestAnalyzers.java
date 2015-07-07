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
package occi.http.autonomic;

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
import occi.infrastructure.Network;
import occi.infrastructure.links.NetworkInterface;


import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import telecom.sudparis.occi.autonomic.Analyzer;

/**
 * Interface for all container resources. Only HTTP GET Request is possible. A GET
 * request returns all available container resources.
 * 
 * @author Sebastian Heckmann
 * @author Sebastian Laag
 */
public class OcciRestAnalyzers extends ServerResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OcciRestAnalyzers.class);

	/**
	 * Returns all analyzer resources.
	 * 
	 * @return Analyzer Resource String
	 */
	@Get
	public String getOCCIRequest(Representation representation) {
		// set occi version info
		getServerInfo().setAgent(
				OcciConfig.getInstance().config.getString("occi.version"));

		// initialize analyzer list
		Map<UUID, Analyzer> analyzerList = Analyzer.getAnalyzerList();
		// initialize buffer
		StringBuffer buffer = new StringBuffer();
		Analyzer analyzer = null;

		// Access the request Headers
		Form requestHeaders = (Form) getRequest().getAttributes().get(
				"org.restlet.http.headers");
		LOGGER.debug("Raw Request Headers: " + requestHeaders);

		LOGGER.debug("Size of analyzer list: " + analyzerList.size());
		// iterate through all available container resources
		int i = 1;
		for (UUID id : analyzerList.keySet()) {
			analyzer = analyzerList.get(id);
			buffer.append(getReference());
			buffer.append(analyzer.getId());
			if (i < analyzerList.size()) {
				buffer.append(",");
			}
			i++;
		}

		representation = OcciCheck.checkContentType(requestHeaders, buffer,
				getResponse());
		getResponse().setEntity(representation);
		if (analyzerList.size() <= 0) {
			// return http status code
			getResponse().setStatus(Status.SUCCESS_NO_CONTENT,
					buffer.toString());
			System.out.println("There are no analyzer resources");
			return "There are no analyzer resources";
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
