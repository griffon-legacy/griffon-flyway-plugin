// TODO header

// Include core griffon scripts with the following call
//
//     includeTargets << griffonScript('_GriffonCompile')
//
// Include plugin scripts with the following call
//
//    includeTargets << griffonPluginScript('some-plugin-name', 'ScriptName')
//

import griffon.test.mock.MockGriffonApplication
import griffon.util.ApplicationHolder
import griffon.plugins.datasource.DataSourceHolder
import griffon.plugins.flyway.FlywayMigrator

includeTargets << griffonScript('_GriffonCompile')

target(name: 'flywayInit',
        description: "TODO The description of the script goes here!",
        prehook: null, posthook: null) {

        depends compile // we need to compile to make sure the current configs are written

        def mockApplication = new MockGriffonApplication()
        // MockApplication has mock config, but for DataSourceHolder we need proper application config
        mockApplication.appConfigClass = 'Application' as Class
        mockApplication.configClass = 'Config' as Class
        mockApplication.initialize()

        ApplicationHolder.application = mockApplication

        def dataSourceName = argsMap.datasource ?: 'default' // TODO datasource into help
        def dataSource = DataSourceHolder.instance.fetchDataSource(dataSourceName)

        def flywayConfig = FlywayMigrator.instance.createConfig(mockApplication, dataSourceName)
        flyway = FlywayMigrator.instance.createFlyway(flywayConfig, dataSourceName, dataSource)
}

