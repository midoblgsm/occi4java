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
package occi.platform.interfaces;

import occi.core.Method;
import occi.platform.Database;

/**
 * Interface to execute operations on database resources. Necessary interface to
 * connect to the occi implementation.
 */
public interface DatabaseInterface {
	/**
	 * Creates a new definition for a database resource.
	 * 
	 * @param database
	 * @return database
	 */
	Database createDatabase(Database database);
	/**
	 * Starts a existing database resource.
	 * 
	 * @param database
	 * @return database
	 */
	Database startDatabase(Database database);

	/**
	 * Stops a existing database resource.
	 * 
	 * @param database
	 * @param stop
	 * @return database
	 */
	Database stopDatabase(Database database);
	
	/**
	 * Backup an existing database resource.
	 * 
	 * @param database
	 * @return database
	 */
	Database backupDatabase(Database database);
	
	/**
	 * Restart a started database resource.
	 * 
	 * @param database
	 * @return database
	 */
	Database restartDatabase(Database database);
	
	/**
	 * Deletes all files for a existing database resource.
	 * 
	 * @param database
	 * @return database
	 */
	Database deleteDatabase(Database database);
}