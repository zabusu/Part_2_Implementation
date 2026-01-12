package hms.util;

import java.time.LocalDate;
import java.time.LocalTime;

public final class DateTimes {
    private DateTimes() {}

    public static LocalDate parseDate(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        return LocalDate.parse(t);
    }

    public static LocalTime parseTime(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        return LocalTime.parse(t);
    }
}

