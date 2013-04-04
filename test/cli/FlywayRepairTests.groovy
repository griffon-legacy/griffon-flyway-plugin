/**
 * Test case for the "flyway-repair" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayRepairTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-repair"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayRepair script not found.", output.contains("Script not found:")
    }
}
