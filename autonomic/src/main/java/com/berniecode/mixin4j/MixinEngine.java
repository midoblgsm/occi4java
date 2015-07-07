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

/**
 * <p>Represents a mixin system. The default mixing engine, {@link MixinSupport}, is
 * extremely configurable and is designed allow dramatic extension by swapping implementations
 * of the {@link ImplementationSource} and {@link Mixer} interfaces.
 * 
 * <p>However, it still has limitations. For example, it enforces the constraint that all
 * mixins are defined as an interface (the mixin type) and that mixin base classes implement
 * this interface in order to indicate that they want the mixin. If this constraint is unsuitable,
 * you could implement a new MixinEngine.
 * 
 * <p>For example, perhaps you want a central XML file that lists the mixins that should be applied
 * to various classes. Go ahead and implement that as a MixinEngine, you weirdo.
 * 
 * @author Bernard Sumption
 */
public interface MixinEngine {

	/**
	 * Create a new instance of a mixed class using the default no-arg constructor
	 */
	public <T> T newInstanceOf(Class<T> mixinBase);

	/**
	 * Return a new instance of a mixed class using a specific constructor.
	 * 
	 * @param mixinBase The class to create a new instance of
	 * @param constructorArgTypes argument types to look up the constructor with
	 * @param constructorArgs argument values to pass to the constructor
	 */
	public <T> T newInstanceOf(Class<T> mixinBase, Class<?>[] constructorArgTypes, Object[] constructorArgs);

	/**
	 * Return a new instance of a mixed class using a constructor with a given argument count.
	 * 
	 * This method requires the constructor to be unambiguous, i.e. there can't be more than one
	 * constructor that takes the same number of arguments as provided in constructorArgs
	 * 
	 * @param mixinBase The class to create a new instance of
	 * @param constructorArgs argument values to pass to the constructor
	 */
	public <T> T newInstanceOf(Class<T> mixinBase, Object[] constructorArgs);

}
