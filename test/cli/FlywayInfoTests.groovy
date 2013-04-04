/**
 * Test case for the "flyway-info" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayInfoTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-info"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayInfo script not found.", output.contains("Script not found:")
    }
}
