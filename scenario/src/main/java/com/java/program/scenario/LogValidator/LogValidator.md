Coding: Count Invalid Log Entries in a Logfile
Problem Statement
Given a logfile where each line has the following format:

Timestamp|LogLevel|LogMessage

Each line should contain exactly 3 non-empty fields separated by the | character.

A log entry is valid only if:
It contains exactly 3 fields separated by |.

None of the fields are empty. Otherwise, the entry is invalid.

You don't have to read the file. The content is provided in a string, each log entry is separated by '\n'.

Task: Count the number of invalid log entries.

1.1 Explain Your Approach
How would you solve this problem?

1.2 Unit Testing
What unit test cases would you write for this program?

1.3. Processing Large Logfiles
How would you process a large logfile?
15 MB logfile
10 GB logfile

1.4. Thread Count
How would you determine the optimal number of threads?

1.5. Factors Affecting Thread Count
What factors influence the number of threads?

1.6. Thread Safety
Is your code thread-safe? Why or why not?