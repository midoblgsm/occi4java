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
 * <p>A mixin consists of an interface (the mixin type) and a class that implements
 * that interface (the mixin implementation).
 * 
 * <p>The mixin type is specified directly by the programmer by implementing the 
 * interface on the class that she wants to be mixed (the mixin base).
 * 
 * <p>An ImplementationSource is used by the author of the mixin type to control how
 * the mixin implementation is chosen, by specifying an ImplementationSource in the @MixinType
 * annotation.
 * 
 * <p>Writing your own ImplementationSource is a very advanced degree of customisation:
 * the vast majority of mixin authors will find the provided sources adequate.
 * 
 * @author Bernard Sumption
 */
public interface ImplementationSource {
	
	/**
	 * <p>Source an implementation for a mixin type.
	 * 
	 * <p>For example, if the interface signature of the mixin base class is:
	 * 
	 * <pre>@MixinBase class Foo implements SomeMixin</pre>
	 * 
	 * <p>Then this method will be called as:
	 * 
	 * <pre>getImplementation(SomeMixin.class, Foo.class);</pre>
	 * 
	 * @return a class that implements the declared interface
	 */
	public Class<?> getImplementation(Class<?> declaredInteface, Class<?> declaringClass);

}
