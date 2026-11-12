package payroll.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import payroll.db.SalaryDAO;

public class DepartmentReportPanel extends JPanel implements ActionListener {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;

    public DepartmentReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Color headerBg = new Color(44, 62, 80);

        // ---- Header ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBg);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel headerLabel = new JLabel("Department-Wise Salary Report");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // ---- Table ----
        String[] columns = {"Department Name", "Total Employees", "Total Salary", "Average Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(headerBg);
        table.getTableHeader().setForeground(Color.WHITE);

        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(headerBg, 2),
                "Report Data",
                TitledBorder.LEFT, TitledBorder.TOP,
                labelFont, headerBg));

        // ---- Button Panel ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRefresh = new JButton("Refresh Report");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.setForeground(new Color(15, 23, 42));
        btnRefresh.setBackground(new Color(241, 245, 249));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(this);
        buttonPanel.add(btnRefresh);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadReport();
    }

    public void refresh() {
        loadReport();
    }

    private void loadReport() {
        tableModel.setRowCount(0);
        try {
            SalaryDAO dao = new SalaryDAO();
            ResultSet rs = dao.getDepartmentWiseReport();
            while (rs.next()) {
                String deptName = rs.getString("dept_name");
                int totalEmployees = rs.getInt("emp_count");
                double totalSalary = rs.getDouble("total_salary");
                double avgSalary = rs.getDouble("avg_salary");
                tableModel.addRow(new Object[]{
                        deptName,
                        totalEmployees,
                        String.format("%.2f", totalSalary),
                        String.format("%.2f", avgSalary)
                });
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadReport();
        }
    }
}
