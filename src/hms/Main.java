package hms;

import hms.app.AppContext;
import hms.GUI.MainFrame;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Path projectRoot = Path.of(System.getProperty("user.dir"));

        SwingUtilities.invokeLater(() -> {
            try {
                AppContext ctx = AppContext.create(projectRoot);
                MainFrame frame = new MainFrame(ctx);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}

