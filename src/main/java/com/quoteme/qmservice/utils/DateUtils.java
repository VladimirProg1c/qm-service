package com.quoteme.qmservice.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtils {

    public static Long getCurrentTime() {
        return ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
