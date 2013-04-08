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

import griffon.util.ApplicationHolder
import griffon.plugins.flyway.FlywayConnector
import griffon.test.mock.MockGriffonApplication

/**
 * @author Peter Kofler
 * @author Andres Almiray
 */

includeTargets << griffonScript('_GriffonCompile')
includeTargets << griffonScript('Package')

target(name: 'flywayInit',
    description: "Initializes a Flyway instance for a given datasource",
    prehook: null, posthook: null) {
    depends(prepackage)

    if (resourcesDir?.exists()) {
        addUrlIfNotPresent rootLoader, resourcesDir
        addUrlIfNotPresent classLoader, resourcesDir
    }

    def mockApplication = new MockGriffonApplication()
    // MockApplication has mock config, but for DataSourceHolder we need proper application config
    mockApplication.appConfigClass = 'Application' as Class
    mockApplication.configClass = 'Config' as Class
    mockApplication.initialize()

    ApplicationHolder.application = mockApplication
    flyway = FlywayConnector.instance.createFlyway(mockApplication, argsMap.datasource)
}

