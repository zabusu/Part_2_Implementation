package hms.GUI;

import javax.swing.JOptionPane;
import java.awt.Component;

public final class SwingDialogs {
    private SwingDialogs() {}

    public static void showError(Component parent, String message, Exception e) {
        String text = message;
        if (e != null && e.getMessage() != null && !e.getMessage().isBlank()) {
            text = message + "\n\nDetails: " + e.getMessage();
        }
        JOptionPane.showMessageDialog(parent, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        int res = JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return res == JOptionPane.YES_OPTION;
    }
}

