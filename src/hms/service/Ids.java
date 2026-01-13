package hms.service;

import java.util.List;

public final class Ids {
    private Ids() {}

    public static String nextId(String prefix, int width, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            if (id == null) continue;
            String t = id.trim();
            if (!t.startsWith(prefix)) continue;
            String numPart = t.substring(prefix.length());
            try {
                int n = Integer.parseInt(numPart);
                if (n > max) max = n;
            } catch (NumberFormatException ignored) {
            }
        }
        int next = max + 1;
        String fmt = "%0" + width + "d";
        return prefix + String.format(fmt, next);
    }
}

