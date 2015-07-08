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
 * <p>Mixin implementation classes can use this interface to be made aware of the object
 * that they are mixed with. Implementing classes should use the type parameter {@code <B>}
 * to specify a constraint on the type of class that this implementation can be applied to.
 * 
 * <p>This code creates a mixin implementation that can be mixed onto any base class:
 * 
 * <pre>{@code class MyMixinImpl implements MyMixinType, MixinAware<? extends Object>}</pre>
 * 
 * <p>This code will ensure that the mixin implementation is only used with base classes
 * that implement the java.lang.Cloneable interface.
 * 
 * <pre>{@code class MyMixinImpl implements MyMixinType, MixinAware<? extends Cloneable>}</pre>
 * 
 * <p>Be careful not to do this:
 * 
 * <pre>{@code class MyMixinImpl implements MyMixinType, MixinAware<SomeType>}</pre>
 * 
 * <p>since because of the way generic constraints work, that would ensure that the implementation
 * can only be mixed with the actual class <code>SomeType</code>, not any of its subtypes.
 * 
 * @author Bernard Sumption
 *
 * @param <B> A type constraint on the base class
 */
//TODO: implement functionality behind this type in new demo
//TODO: check that mixin base matches constraints, and that constraint is in the form '? extends Foo'
public interface MixinAware<B> {
	
	/**
	 * <p>This method will be called by the {@link Mixer} during construction of the mixed object.
	 * This method should just be used to set a field. No members of mixinBase should be accessed
	 * because the object is still in the process of being built.
	 * 
	 * @param mixinBase the mixin base class that this mixin implementation is being mixed with
	 */
	public void setMixinBase(B mixinBase);
	
}
