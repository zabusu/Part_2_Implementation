package hms.data;

import hms.util.IO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class CsvTables {
    private CsvTables() {}

    public static CsvTable load(Path csvPath) throws IOException {
        List<String> lines = IO.readAllLines(csvPath);
        return CsvTable.fromLines(lines);
    }
}

