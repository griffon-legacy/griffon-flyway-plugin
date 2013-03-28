/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.plugins.datasource.DataSourceHolder
import griffon.plugins.flyway.FlywayMigrator

import javax.sql.DataSource

/**
 * @author Andres Almiray
 */
class FlywayGriffonAddon {
    Map events = [
        'DataSourceConnectEnd': { String dataSourceName, DataSource dataSource ->
            if (app.addonManager.findAddon('hibernate3') || app.addonManager.findAddon('hibernate4')) return
            FlywayMigrator.instance.runDatabaseMigration(app, dataSourceName, dataSource)
        },
        'Hibernate3SessionFactoryCreated': { ConfigObject config, String dataSourceName, sessionFactory ->
            DataSource dataSource = DataSourceHolder.instance.fetchDataSource(dataSourceName)
            FlywayMigrator.instance.runDatabaseMigration(app, dataSourceName, dataSource)
        },
        'Hibernate4SessionFactoryCreated': { ConfigObject config, String dataSourceName, sessionFactory ->
            DataSource dataSource = DataSourceHolder.instance.fetchDataSource(dataSourceName)
            FlywayMigrator.instance.runDatabaseMigration(app, dataSourceName, dataSource)
        }
    ]
}
