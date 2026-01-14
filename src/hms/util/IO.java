package hms.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class IO {
    private IO() {}

    public static List<String> readAllLines(Path path) throws IOException {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public static void writeAllLines(Path path, List<String> lines) throws IOException {
        ensureParentDir(path);
        Files.write(path, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Append a line to a text file.
     * Ensures the file ends with a newline before appending, so we don't concatenate records.
     */
    public static void appendLine(Path path, String line) throws IOException {
        ensureParentDir(path);

        if (Files.exists(path)) {
            long size = Files.size(path);
            if (size > 0) {
                byte[] bytes = Files.readAllBytes(path);
                byte last = bytes[bytes.length - 1];
                if (last != (byte) '\n') {
                    Files.writeString(path, System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            }
        }

        Files.writeString(
                path,
                line + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public static void ensureDir(Path dir) throws IOException {
        if (dir == null) return;
        Files.createDirectories(dir);
    }

    private static void ensureParentDir(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
