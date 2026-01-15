package hms.data;

import hms.util.Csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class CsvTable {
    private final List<String> header;
    private final Map<String, Integer> indexByName;
    private final List<List<String>> rows;

    public CsvTable(List<String> header, List<List<String>> rows) {
        this.header = new ArrayList<>(header);
        this.rows = new ArrayList<>(rows);
        this.indexByName = new HashMap<>();
        for (int i = 0; i < header.size(); i++) {
            indexByName.put(header.get(i), i);
        }
    }

    public List<String> header() {
        return new ArrayList<>(header);
    }

    public int rowCount() {
        return rows.size();
    }

    public List<List<String>> rows() {
        return new ArrayList<>(rows);
    }

    public String get(List<String> row, String colName) {
        Integer idx = indexByName.get(colName);
        if (idx == null) return "";
        if (idx < 0 || idx >= row.size()) return "";
        return row.get(idx);
    }

    public static CsvTable fromLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return new CsvTable(List.of(), List.of());
        }
        List<String> header = Csv.parseLine(lines.get(0));
        List<List<String>> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.isBlank()) continue;
            rows.add(Csv.parseLine(line));
        }
        return new CsvTable(header, rows);
    }
}

