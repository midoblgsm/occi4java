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
package com.berniecode.mixin4j;

import java.lang.reflect.Modifier;


/**
 * <p>Represents a bit of functionality that is added to a class at runtime. It consists of a
 * mixin type interface and associated concrete class that implements the type interface.
 * 
 * @param <B> the type of the mixin base class
 * 
 * @author Bernard Sumption
 */
public class Mixin<B> {
	
	private Class<?> mixinType;
	private Class<?> mixinImpl;

	public Mixin(Class<?> mixinType, Class<?> mixinImpl, Class<B> mixinBase) {
		requireConcreteImplementationOfType(mixinType, mixinImpl, "mixin implementation", false);
		requireConcreteImplementationOfType(mixinType, mixinBase, "mixin base", true);
		this.mixinType = mixinType;
		this.mixinImpl = mixinImpl;
	}

	private void requireConcreteImplementationOfType(Class<?> type, Class<?> impl, String implDesc, boolean isAbstract) {
		String failReason = null;
		if (!type.isAssignableFrom(impl)) {
			failReason = "does not implement the mixin type as an interface";
		} else if (impl.isInterface()) {
			failReason = "is an interface, not a class";
		} else if (Modifier.isAbstract(impl.getModifiers()) != isAbstract) {
			failReason = isAbstract ? "is not abstract" : "is abstract";
		}
		if (failReason != null) {
			throw new MixinException("Can't apply mixin type " + type.getCanonicalName()
					+ " because the " + implDesc + " class " + impl.getCanonicalName()
					+ " " + failReason + ".");
		}
	}
	
	/**
	 * Get the type interface for this Mixin
	 */
	public Class<?> getMixinType() {
		return this.mixinType;
	}

	/**
	 * Get the concrete class that implements the type interface
	 */
	public Class<?> getMixinImpl() {
		return this.mixinImpl;
	}


}