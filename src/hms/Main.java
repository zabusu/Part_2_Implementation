package hms;

import hms.data.CsvFileConfig;
import hms.GUI.MainFrame;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Path projectRoot = Path.of(System.getProperty("user.dir"));
        CsvFileConfig config = CsvFileConfig.fromProjectRoot(projectRoot);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(config);
            frame.setVisible(true);
        });
    }
}

