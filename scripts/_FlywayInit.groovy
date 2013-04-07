/*
 * Copyright 2008-2013 the original author or authors.
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
import griffon.plugins.flyway.FlywayConnector
import griffon.test.mock.MockGriffonApplication

/**
 * @author Peter Kofler
 * @author Andres Almiray
 */

includeTargets << griffonScript('_GriffonCompile')

target(name: 'flywayInit',
    description: "Initializes a Flyway instance for a given datasource",
    prehook: null, posthook: null) {

    depends compile // we need to compile to make sure the current configs are written

    def mockApplication = new MockGriffonApplication()
    // MockApplication has mock config, but for DataSourceHolder we need proper application config
    mockApplication.appConfigClass = 'Application' as Class
    mockApplication.configClass = 'Config' as Class
    mockApplication.initialize()

    ApplicationHolder.application = mockApplication

    def dataSourceName = argsMap.datasource ?: 'default'
    def dataSource = DataSourceHolder.instance.fetchDataSource(dataSourceName)

    def flywayConfig = FlywayConnector.instance.createConfig(mockApplication, dataSourceName)
    flyway = FlywayConnector.instance.configureFlyway(flywayConfig, dataSourceName, dataSource)
}

