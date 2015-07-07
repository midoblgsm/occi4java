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
package telecom.sudparis.occi.autonomic.testresources;
import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import telecom.sudparis.occi.autonomic.tools.Notification;
import telecom.sudparis.occi.autonomic.tools.Subscription;
import telecom.sudparis.occi.autonomic.tools.XMLTools;

/**
 * Hello world!
 *
 */
public class QueryTests 
{
	
	private final static String newRelicURI ="https://api.newrelic.com/v2/applications.xml";
	private final static String analyzer ="http://localhost:8182/analyzer/notify/";
	private final static String api_key="37a10b0b241d2c0182ab81d5a1b9f4e5ae620276c4fe849";
	private final static String subscriptionLinURI="http://localhost:8182/subscriptionlink/subscribe/";
	private final static String analyzerUuid="28915c68-946d-4648-8b88-bfe61e510549";
	private final static String subLinkuuid="0c240b16-0109-422a-9346-c61933bd665b";
	public static void main( String[] args )
    {
		
		ClientResource resource=	new ClientResource(subscriptionLinURI+subLinkuuid);
		
		
		try {

			 Subscription subscription=new Subscription(1000000, analyzerUuid, 1000);

			;
			resource.post(XMLTools.getSubscriptionXML(subscription)).write(System.out);
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	   
		
		//String response=(service.header("X-Api-Key", api_key).accept(MediaType.TEXT_XML).get(String.class));
	   //System.out.println(response);
	    // Get XML for application
	   // System.out.println(service.path("rest").path("todo").accept(MediaType.APPLICATION_JSON).get(String.class));
	    // Get JSON for application
	 //   System.out.println(service.path("rest").path("todo").accept(MediaType.APPLICATION_XML).get(String.class));
 }


	

	/**
	 * @return the newRelicURI
	 */
	public String getNewRelicURI() {
		return newRelicURI;
	}
	
	



		 
//	 private static URI getBaseURI() {
//		    return UriBuilder.fromUri(newRelicURI).build();
//		  }


}
