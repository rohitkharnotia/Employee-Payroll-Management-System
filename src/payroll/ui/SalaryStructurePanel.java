package payroll.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SalaryStructurePanel extends JPanel {

    public SalaryStructurePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---- Header ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel headerLabel = new JLabel("Salary Structure Overview");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // ---- Table ----
        String[] columns = {"Employee Type", "HRA %", "DA %", "PF %", "Tax %"};
        Object[][] data = {
                {"Manager", "20%", "30%", "12%", "10% of Gross"},
                {"Clerk", "15%", "20%", "10%", "5% of Gross"},
                {"Teacher", "18%", "25%", "12%", "8% of Gross"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
                "Salary Components by Employee Type",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(44, 62, 80)));

        // ---- Info Panel ----
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
                "Salary Calculation Formula",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(44, 62, 80)));

        Font infoFont = new Font("SansSerif", Font.PLAIN, 13);
        JLabel l1 = new JLabel("  Gross Salary = Basic + HRA + DA");
        l1.setFont(infoFont);
        JLabel l2 = new JLabel("  Net Salary = Gross - PF - Tax");
        l2.setFont(infoFont);
        JLabel l3 = new JLabel("  HRA, DA, PF are calculated on Basic Salary");
        l3.setFont(infoFont);
        JLabel l4 = new JLabel("  Tax is calculated on Gross Salary");
        l4.setFont(infoFont);

        infoPanel.add(l1);
        infoPanel.add(l2);
        infoPanel.add(l3);
        infoPanel.add(l4);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }
}
