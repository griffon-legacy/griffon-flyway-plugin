
Flyway powered database migrations
----------------------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/flyway](http://artifacts.griffon-framework.org/plugin/flyway)


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
by the migration. The default datasource requires a slightly modified name, it
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

### Additional Configuration Flags

The following properties are not standard to `com.googlecode.flyway.core.Flyway`
but can be configured in `FlywayConfig.groovy`

 * **enabled** - Whether Flyway operations are enabled or not for a particular
   datasource. Default is `true`.

### Events

The following events will be triggered by this addon

 * FlywayCommand[command, dataSourceName] - after a Flyway command has been
   successfully executed. Command may be one of `init`, `validate`, `migrate`,
   `clean` or `repair`.

Scripts
-------
 * **flyway-clean** - Drops all (tables, views, procedures, triggers, ...) in the
   configured schemas
 * **flyway-info** - Retrieve the complete information about the migrations
   including applied, pending and current migrations with details and status.
 * **flyway-init** - Initializes the metadata table in an existing schema.
 * **flyway-migrate** - Triggers the migration of the configured database to the
   latest version
 * **flyway-repair** - Repairs the metadata table after a failed migration on a
   database without DDL transactions
 * **flyway-validate** - Validate the applied migrations in the database against
   the available classpath migrations in order to detect accidental migration changes.

[1]: http://flywaydb.org
[2]: /plugin/datasource
[3]: /plugin/hibernate3
[4]: /plugin/hibernate4
[5]: http://flywaydb.org/documentation/migration

