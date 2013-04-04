/**
 * Test case for the "flyway-init" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayInitTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-init"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayInit script not found.", output.contains("Script not found:")
    }
}
