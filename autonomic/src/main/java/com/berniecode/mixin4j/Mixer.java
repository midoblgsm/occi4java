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

import java.util.List;

/**
 * <p>A mixer takes a ClassWithMixins instance and produces a subclass of mixin base class
 * that delegates methods not implemented in the base class to an instance of the
 * appropriate mixin implementation
 * 
 * <p>For anyone familiar with AOP terminology, this is the equivalent of a weaver.
 * 
 * @author Bernard Sumption
 */
public interface Mixer {
	
	/**
	 * Return a Factory that can make new instances of a mixed class
	 * 
	 * @param mixinBase the mixin base class
	 * @param mixins a list of mixins to apply to the base class
	 */
	public <T> Factory<? extends T> getFactory(Class<T> mixinBase, List<Mixin<T>> mixins);

	/**
	 * Return a Factory that can make new instances of a mixed class
	 * 
	 * @param mixinBase the mixin base class
	 * @param mixin a single mixin to apply to the base class
	 */
	public <T> Factory<? extends T> getFactory(Class<T> mixinBase, Mixin<T> mixin);
}