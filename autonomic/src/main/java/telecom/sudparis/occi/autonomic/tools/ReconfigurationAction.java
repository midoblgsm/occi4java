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
package telecom.sudparis.occi.autonomic.tools;

import java.net.URI;
import java.util.Arrays;

import occi.core.Action;
import occi.core.Method;

public class ReconfigurationAction extends Action {

	private String methodeName;
	private Object[] args;

	
	
	public ReconfigurationAction(String methodeName, Object[] args) {
		super();
		this.methodeName = methodeName;
		this.args = args;
	}

	@Override
	public void execute(URI uri, Method method) {

	}

	/**
	 * @return the methodeName
	 */
	public String getMethodeName() {
		return methodeName;
	}

	/**
	 * @param methodeName
	 *            the methodeName to set
	 */
	public void setMethodeName(String methodeName) {
		this.methodeName = methodeName;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}
	@Override
	public String toString() {
		return "ReconfigurationAction [methodeName=" + methodeName + ", args="
				+ Arrays.toString(args) + "]";
	}

}
