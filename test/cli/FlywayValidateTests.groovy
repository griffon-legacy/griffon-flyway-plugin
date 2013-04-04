/**
 * Test case for the "flyway-validate" Griffon command.
 */

import griffon.test.AbstractCliTestCase

class FlywayValidateTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["flyway-validate"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "FlywayValidate script not found.", output.contains("Script not found:")
    }
}
