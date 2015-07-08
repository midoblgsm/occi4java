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
package telecom.sudparis.occi.autonomic.impl;

import com.berniecode.mixin4j.MixinAware;
import com.sun.jersey.api.client.WebResource;

import telecom.sudparis.occi.autonomic.ActionLink;
import telecom.sudparis.occi.autonomic.interfaces.ActionToolMixinInterface;
import telecom.sudparis.occi.autonomic.tools.ReconfigurationAction;
import occi.application.Application;
import occi.application.coaps.client.CoapsCaller;

public class ActionToolMixinImpl extends Thread implements
		ActionToolMixinInterface, MixinAware<ActionLink> {

	private ActionLink mixinBase;
	private int instances;

	@Override
	public void applyAction(ReconfigurationAction action) {

		try {

			System.err.println("####### Action Execution #########");
			Application app = (Application) mixinBase.getTarget();
			if (action.getMethodeName().equals("scaleup")) {

				instances = app.getInstances() + (Integer) action.getArgs()[0];

				((Thread) this).run();
			} else if (action.getMethodeName().equals("scaledown")) {

				if ((app.getInstances() - (Integer) action.getArgs()[0]) >= 1) {
					instances = app.getInstances()
							- (Integer) action.getArgs()[0];

					((Thread) this).run();
				}
			}
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void setMixinBase(ActionLink mixinBase) {
		this.mixinBase = mixinBase;

	}

	public void run() {
		WebResource client = CoapsCaller.createClient();

		Application app = (Application) mixinBase.getTarget();
		try {
			CoapsCaller.updateApplicationInstances(client, app.getCoapsID(),
					instances);
			app.setInstances(instances);
		} catch (Exception e) {
			System.err.println("actionToolMixin error: "+e.getMessage());
		}

	}
}
