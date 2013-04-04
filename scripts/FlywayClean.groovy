// TODO add Apache license header

includePluginScript('flyway', '_FlywayInit')

target(name: 'flywayclean',
        description: "The description of the script goes here!",
        prehook: null, posthook: null) {
        depends flywayInit

        flyway.clean()
}

setDefaultTarget('flywayclean')
