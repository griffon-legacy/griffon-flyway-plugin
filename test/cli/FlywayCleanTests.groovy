/**
 * Test case for the "flyway-clean" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayCleanTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-clean"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayClean script not found.", output.contains("Script not found:")
    }
}
