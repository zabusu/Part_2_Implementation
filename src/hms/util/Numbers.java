package hms.util;

public final class Numbers {
    private Numbers() {}

    public static int parseIntOrDefault(String s, int defaultValue) {
        if (s == null) return defaultValue;
        String t = s.trim();
        if (t.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

