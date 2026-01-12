package hms.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal CSV helper (RFC4180-ish) that supports:
 * - commas inside quoted fields
 * - escaped quotes inside quoted fields ("" -> ")
 *
 * Assumptions:
 * - No multi-line fields
 */
public final class Csv {
    private Csv() {}

    public static List<String> parseLine(String line) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }

        out.add(cur.toString());
        return out;
    }

    public static String toLine(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(escapeField(fields.get(i)));
        }
        return sb.toString();
    }

    private static String escapeField(String value) {
        if (value == null) return "";
        boolean mustQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return mustQuote ? ("\"" + escaped + "\"") : escaped;
    }
}

