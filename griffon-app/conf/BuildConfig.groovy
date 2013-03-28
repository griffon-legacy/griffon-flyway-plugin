griffon.project.dependency.resolution = {
    inherits "global"
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonHome()
        mavenCentral()
    }
    dependencies {
        compile 'com.googlecode.flyway:flyway-core:2.1.1'
    }
}

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%d [%t] %-5p %c - %m%n')
    }

    error 'org.codehaus.griffon',
          'org.springframework',
          'org.apache.karaf',
          'groovyx.net'
    warn  'griffon'
}
