package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "flyway",
        name = "flyway-migrate",
        description = "Single line description goes here",
        detailedDescription = "classpath:flyway-migrate.txt")
public class FlywayMigrateCommand extends AbstractGriffonCommand {
    /*
    @Option(name = "--foo",
            description = "Description of foo option.",
            required = false)
    private String foo;

    @Argument(index = 0,
            name = "bar",
            description = "Description of first argument.",
            required = false)
    private String bar;
    */
}