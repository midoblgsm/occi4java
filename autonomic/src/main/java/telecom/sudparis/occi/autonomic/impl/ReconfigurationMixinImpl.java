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

import telecom.sudparis.occi.autonomic.interfaces.ReconfigurationMixinInterface;
import telecom.sudparis.occi.autonomic.testresources.MixedApplication;

public class ReconfigurationMixinImpl implements ReconfigurationMixinInterface,
		MixinAware<MixedApplication> {

	private Object mixinBase;

	@Override
	public void applyReconfiguration(Object action) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param mixinBase
	 *            the mixinBase to set
	 */

	@Override
	public void setMixinBase(MixedApplication mixinBase) {
		this.mixinBase = mixinBase;

	}

	/**
	 * @return the mixinBase
	 */
	public Object getMixinBase() {
		return mixinBase;
	}

}
