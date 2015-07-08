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

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.naming.directory.SchemaViolationException;

import com.berniecode.mixin4j.MixinBase;

import occi.application.Application;
import occi.application.Deployable;
import telecom.sudparis.occi.autonomic.interfaces.PollingMixinInterface;
import telecom.sudparis.occi.autonomic.interfaces.SubscriptionMixinInterface;

@MixinBase
public abstract class MixedApplication extends Application  implements
		PollingMixinInterface, SubscriptionMixinInterface {

	public MixedApplication(String name, String description, Integer instances,
			String url, String start_command, State state,
			ArrayList<Deployable> depList) throws URISyntaxException,
			SchemaViolationException {
		super(name, description, instances, url, start_command, state, depList);
		// TODO Auto-generated constructor stub
	}


}
