package com.java.program.scenario.LogValidator;

public class LogValidator {
    public static int countInvalidLogs(String logs) {

        if (logs == null || logs.trim().isEmpty()) {
            return 0;
        }

        int invalidCount = 0;

        String[] lines = logs.split("\\n");

        for (String line : lines) {
            // Skip completely blank lines
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] fields = line.split("\\|", -1);

            if (fields.length != 3) {
                invalidCount++;
                continue;
            }

            boolean valid = true;

            for (String field : fields) {
                if (field == null || field.trim().isEmpty()) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                invalidCount++;
            }
        }

        return invalidCount;
    }

    public static void main(String[] args) {

        String logs =
                "2025-09-01|INFO|Application Started\n" +
                        "2025-09-01|ERROR|\n" +
                        "2025-09-01|WARN|Disk Space Low\n" +
                        "2025-09-01||Connection Lost\n" +
                        "2025-09-01|INFO|Message|Extra\n" +
                        "|INFO|Missing Timestamp";

        int result = countInvalidLogs(logs);

        System.out.println("Invalid Log Count: " + result);
    }
}
