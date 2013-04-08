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

import com.googlecode.flyway.core.Flyway
import com.googlecode.flyway.core.api.FlywayException
import griffon.plugins.datasource.DataSourceHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.sql.DataSource

import griffon.core.GriffonApplication

import static griffon.util.ConfigUtils.loadConfigWithI18n
import static griffon.util.GriffonExceptionHandler.sanitize
import static griffon.util.GriffonNameUtils.getSetterName
import static griffon.util.GriffonNameUtils.isBlank

/**
 * @author Andres Almiray
 */
@Singleton
class FlywayConnector {
    private static final Logger LOG = LoggerFactory.getLogger(FlywayConnector)
    private static final String DEFAULT = 'default'

    ConfigObject createConfig(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        if (!app.config.pluginConfig.flyway) {
            app.config.pluginConfig.flyway = loadConfigWithI18n('FlywayConfig')
        }
        narrowConfig(app.config.pluginConfig.flyway, dataSourceName)
    }

    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        return dataSourceName == DEFAULT ? config.dataSource : config.dataSources[dataSourceName]
    }

    void migrate(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        Flyway flyway = createFlyway(app, dataSourceName)
        forceInit(flyway)
        if (LOG.infoEnabled) {
            LOG.info("Running 'migrate' for datasource ${dataSourceName}")
        }
        flyway.migrate()
        app.event('FlywayCommand', ['migrate', dataSourceName])
    }

    void clean(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        Flyway flyway = createFlyway(app, dataSourceName)
        if (LOG.infoEnabled) {
            LOG.info("Running 'clean' for datasource ${dataSourceName}")
        }
        flyway.clean()
        app.event('FlywayCommand', ['init', dataSourceName])
    }

    void init(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        Flyway flyway = createFlyway(app, dataSourceName)
        if (LOG.infoEnabled) {
            LOG.info("Running 'init' for datasource ${dataSourceName}")
        }
        flyway.init()
        app.event('FlywayCommand', ['init', dataSourceName])
    }

    void repair(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        Flyway flyway = createFlyway(app, dataSourceName)
        forceInit(flyway)
        if (LOG.infoEnabled) {
            LOG.info("Running 'repair' for datasource ${dataSourceName}")
        }
        flyway.repair()
        app.event('FlywayCommand', ['repair', dataSourceName])
    }

    void validate(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        Flyway flyway = createFlyway(app, dataSourceName)
        forceInit(flyway)
        if (LOG.infoEnabled) {
            LOG.info("Running 'validate' for datasource ${dataSourceName}")
        }
        flyway.validate()
        app.event('FlywayCommand', ['validate', dataSourceName])
    }

    Flyway createFlyway(GriffonApplication app, String dataSourceName) {
        dataSourceName = isBlank(dataSourceName) ? DEFAULT : dataSourceName
        ConfigObject config = createConfig(app, dataSourceName)
        DataSource dataSource = DataSourceHolder.instance.fetchDataSource(dataSourceName)
        configureFlyway(config, dataSourceName, dataSource)
    }

    private Flyway configureFlyway(ConfigObject config, String dataSourceName, DataSource dataSource) {
        Flyway flyway = new Flyway()

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
        if (!config.locations) {
            flyway.setLocations('flyway/migrations/' + (dataSourceName == DEFAULT ? DEFAULT + 'ds' : dataSourceName))
        }
        flyway
    }

    private void forceInit(Flyway flyway) {
        try {
            flyway.init()
        } catch (FlywayException e) {
            // ignore. If this fails it could be because the metadata table
            // already exists. Any other failure will be reported by the actual
            // command being invoked
        }
    }
}
