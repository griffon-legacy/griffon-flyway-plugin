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

package griffon.plugins.flyway

import griffon.core.GriffonApplication

import com.googlecode.flyway.core.Flyway
import com.googlecode.flyway.core.api.FlywayException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.sql.DataSource

import static griffon.util.ConfigUtils.loadConfigWithI18n
import static griffon.util.GriffonExceptionHandler.sanitize
import static griffon.util.GriffonNameUtils.getSetterName

/**
 * @author Andres Almiray
 */
@Singleton
class FlywayMigrator {
    private static final Logger LOG = LoggerFactory.getLogger(FlywayMigrator)
    private static final String DEFAULT = 'default'


    ConfigObject createConfig(GriffonApplication app) {
        if (!app.config.pluginConfig.flyway) {
            app.config.pluginConfig.flyway = loadConfigWithI18n('FlywayConfig')
        }
        app.config.pluginConfig.flyway
    }

    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {
        return dataSourceName == DEFAULT ? config.dataSource : config.dataSources[dataSourceName]
    }

    void runDatabaseMigration(GriffonApplication app, String dataSourceName, DataSource dataSource) {
        Flyway flyway = new Flyway()

        ConfigObject config = narrowConfig(createConfig(app), dataSourceName)
        for (entry in config) {
            if (entry.key == 'class' || entry.key == 'metaClass') continue
            try {
                String setter = getSetterName(entry.key)
                flyway."${setter}"(entry.value)
            } catch (Exception e) {
                if (LOG.traceEnabled) {
                    LOG.trace("Cant' set property ${entry.key} on Flyway instance for dataSource ${dataSourceName}", sanitize(e))
                }
            }
        }

        flyway.setDataSource(dataSource)
        flyway.setLocations('flyway/migrations/' + (dataSourceName == DEFAULT ? DEFAULT + 'ds' : dataSourceName))
        try {
            // force init
            flyway.init()
        } catch (FlywayException fe) {
            // ignore it as we could be calling init() on a non-empty schema
            // if init() really failed for the first time then migrate() will fail too
            // in which case we let the application crash as the DB would be in
            // an inconsistent state
        }
        if (LOG.infoEnabled) {
            LOG.info("Running migrations for datasource ${dataSourceName}")
        }
        flyway.migrate()
        app.event('FlywayMigration', [config, dataSourceName, dataSource])
    }
}
