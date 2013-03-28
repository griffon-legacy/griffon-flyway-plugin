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

/**
 * @author Andres Almiray
 */
class FlywayGriffonPlugin {
    // the plugin version
    String version = '0.1'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '1.2.0 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [datasource: '1.1.0']
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-datasource-plugin'
    // Map of Bnd directives and/or Manifest entries
    // see http://www.aqute.biz/Bnd/Bnd for reference
    Map manifest = [
        'Bundle-Description': 'Plugin summary/headline'
    ]
    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Flyway powered database migrations'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
This plugin enables the usage of [Flyway][1] for database migrations. It can be
used with any plugin that depends on [datasource][2].

Usage
-----
The plugin automatically runs migrations for a target datasource right after a
link to said datasource is established; this happens as a reaction to the
`DataSourceConnectEnd`event. Note that for the [hibernate3][3] and [hibernate4][4]
plugins this happens after the `Hibernate3SessionFactoryCreated` and
`Hibernate4SessionFactoryCreated` events respectively.

Flyway supports two types of migration formats: SQL and Java. SQL migrations
should be placed inside `griffon-app/resources/flyway/migrations/<datasource>`
where as Java migrations go in `src/main/flyway/migrations/<datasource>`. In both
cases <datasource> stands for the name of the datasource that's being targeted
by the migration. The default datasource required a slightly modified name, it
should be `defaultds` because `default` is a reserved keyword in Java, thus
preventing its usage in Java based migrations.

More information about SQL and Java migrations can be found in [Flyway's documentation][5].

Configuration
-------------
Upon installation the plugin will generate the following artifacts in
$appdir/griffon-app/conf`:

 * FlywayConfig.groovy - per datasource configuration for Flyway instances.

This configuration file defines properties that may be applied to a
`com.googlecode.flyway.core.Flyway` instance that will be used to migrate a
datasource. This configuration file supports multiple datasource configurations
as well as per environment configuration. For example, configuring a different
name for the migration table for a datasource named 'internal' can be done in the
following way

    dataSources {
        internal {
            table = 'my_migrations_table'
        }
    }

### Events

The following events will be triggered by this addon

 * FlywayMigration[config, dataSourceName, dataSource] - after migrations have
   run on the specified datasource

[1]: http://flywaydb.org
[2]: /plugin/datasource
[3]: /plugin/hibernate3
[4]: /plugin/hibernate4
[5]: http://flywaydb.org/documentation/migration
'''
}
