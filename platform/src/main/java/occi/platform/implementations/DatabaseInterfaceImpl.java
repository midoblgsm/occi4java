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
package occi.platform.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import occi.platform.Database;
import occi.platform.interfaces.DatabaseInterface;

/**
 * @author nguyen_n
 *
 */
public class DatabaseInterfaceImpl implements DatabaseInterface {
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(DatabaseInterfaceImpl.class);

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#createDatabase(occi.platform.Database)
	 */
	@Override
	public Database createDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Create database: "+database.getName());
		return null;
	}

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#startDatabase(occi.platform.Database)
	 */
	@Override
	public Database startDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Start database: "+database.getName());
		return null;
	}

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#stopDatabase(occi.platform.Database, occi.core.Method)
	 */
	@Override
	public Database stopDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Stop database: "+database.getName());
		return null;
	}

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#backupDatabase(occi.platform.Database, occi.core.Method)
	 */
	@Override
	public Database backupDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Backup database: "+database.getName());
		return null;
	}

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#restartdatabase(occi.platform.Database, occi.core.Method)
	 */
	@Override
	public Database restartDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Restart database: "+database.getName());
		return null;
	}

	/* (non-Javadoc)
	 * @see occi.platform.interfaces.DatabaseInterface#deletedatabase(occi.platform.Database)
	 */
	@Override
	public Database deleteDatabase(Database database) {
		// TODO Auto-generated method stub
		LOGGER.debug("Delete database: "+database.getName());
		return null;
	}

}
