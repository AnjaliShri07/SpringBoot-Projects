package com.java.program.scenario;

import com.java.program.scenario.LogValidator.LogValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogValidatorTest {
    @Test
    void validLogsOnly() {

        String logs =
                "2025-09-01|INFO|Started\n" +
                        "2025-09-01|WARN|Memory High";

        assertEquals(0, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void emptyFieldShouldBeInvalid() {

        String logs =
                "2025-09-01|INFO|\n" +
                        "2025-09-01||Message";

        assertEquals(2, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void extraFieldShouldBeInvalid() {

        String logs =
                "2025-09-01|INFO|Message|Extra";

        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void lessThanThreeFieldsShouldBeInvalid() {

        String logs =
                "2025-09-01|INFO";

        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void emptyTimestampShouldBeInvalid() {

        String logs =
                "|INFO|Started";

        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void trimSpacesAndValidate() {

        String logs =
                "2025-09-01|INFO|   ";

        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void spacesOnlyFieldShouldBeInvalid() {
        String logs = "ts|   |msg";
        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void nullInput() {
        assertEquals(0, LogValidator.countInvalidLogs(null));
    }

    @Test
    void emptyInput() {
        assertEquals(0, LogValidator.countInvalidLogs(""));
    }

    @Test
    void mixedValidAndInvalidLogs() {

        String logs =
                "2025-09-01|INFO|Started\n" +
                        "2025-09-01|ERROR|\n" +
                        "2025-09-01|WARN|Disk Full\n" +
                        "|INFO|Missing Timestamp\n" +
                        "2025-09-01|INFO|Message|Extra";

        assertEquals(3, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void leadingPipeShouldBeInvalid() {
        String logs = "|INFO|msg";
        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void trailingPipeShouldBeInvalid() {

        String logs =
                "2025-09-01|INFO|Message|";

        assertEquals(1, LogValidator.countInvalidLogs(logs));
    }

    @Test
    void trailingBlankLineIgnored() {
        String logs = "ts|INFO|msg\n\n";
        assertEquals(0, LogValidator.countInvalidLogs(logs));
    }
}
