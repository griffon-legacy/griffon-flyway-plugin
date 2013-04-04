// Include core griffon scripts with the following call
//
//     includeTargets << griffonScript('_GriffonCompile')
//
// Include plugin scripts with the following call
//
//    includeTargets << griffonPluginScript('some-plugin-name', 'ScriptName')
//

target(name: 'flywayhistory',
        description: "The description of the script goes here!",
        prehook: null, posthook: null) {
    // TODO: Implement script here
}

setDefaultTarget('flywayhistory')
