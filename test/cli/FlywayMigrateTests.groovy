/**
 * Test case for the "flyway-migrate" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayMigrateTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-migrate"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayMigrate script not found.", output.contains("Script not found:")
    }
}
