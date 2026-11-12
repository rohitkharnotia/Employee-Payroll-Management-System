package payroll;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import payroll.db.DBConnection;
import payroll.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Attempt automatic database schema & demo data seeding if MySQL is running
        try {
            DBConnection.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Database auto-init note: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
