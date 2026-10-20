package com.dynamodb_springboot.dynamodb.commons;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:'Z'");


    public static String getCurrentTimestamp(){
        return ZonedDateTime.now(ZoneOffset.UTC).format(ISO_DATE_TIME_FORMATTER);
    }
}
