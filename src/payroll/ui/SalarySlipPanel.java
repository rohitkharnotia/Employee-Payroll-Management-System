package payroll.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;

import payroll.model.*;
import payroll.db.EmployeeDAO;
import payroll.db.DepartmentDAO;

public class SalarySlipPanel extends JPanel implements ActionListener {

    private JComboBox<String> cmbEmployee;
    private JButton btnGenerate, btnPrint, btnRefresh;
    private JTextArea txtSlip;

    public SalarySlipPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Color headerBg = new Color(44, 62, 80);

        // ---- Header ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBg);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel headerLabel = new JLabel("Salary Slip Generator");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // ---- Top Controls ----
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(headerBg, 2),
                "Select Employee",
                TitledBorder.LEFT, TitledBorder.TOP,
                labelFont, headerBg));

        cmbEmployee = new JComboBox<>();
        loadEmployees();

        btnGenerate = new JButton("Generate Slip");
        btnGenerate.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGenerate.setForeground(new Color(15, 23, 42));
        btnGenerate.setBackground(new Color(241, 245, 249));
        btnGenerate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        btnGenerate.setFocusPainted(false);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerate.addActionListener(this);

        btnPrint = new JButton("Print");
        btnPrint.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnPrint.setForeground(new Color(15, 23, 42));
        btnPrint.setBackground(new Color(220, 252, 231));
        btnPrint.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 197, 94), 2),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        btnPrint.setFocusPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrint.addActionListener(this);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRefresh.setForeground(new Color(15, 23, 42));
        btnRefresh.setBackground(new Color(241, 245, 249));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(this);

        controlPanel.add(cmbEmployee);
        controlPanel.add(btnGenerate);
        controlPanel.add(btnPrint);
        controlPanel.add(btnRefresh);

        // ---- Slip Area ----
        txtSlip = new JTextArea();
        txtSlip.setEditable(false);
        txtSlip.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtSlip.setMargin(new Insets(15, 15, 15, 15));
        txtSlip.setBackground(new Color(253, 254, 254));

        JScrollPane scrollPane = new JScrollPane(txtSlip);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(headerBg, 2),
                "Salary Slip",
                TitledBorder.LEFT, TitledBorder.TOP,
                labelFont, headerBg));

        // ---- Layout ----
        JPanel topSection = new JPanel(new BorderLayout(5, 5));
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(controlPanel, BorderLayout.SOUTH);

        add(topSection, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        loadEmployees();
    }

    private void loadEmployees() {
        cmbEmployee.removeAllItems();
        try {
            EmployeeDAO dao = new EmployeeDAO();
            ResultSet rs = dao.getAllEmployees();
            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String name = rs.getString("name");
                cmbEmployee.addItem(empId + " - " + name);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSelectedEmpId() {
        String selected = (String) cmbEmployee.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            return Integer.parseInt(selected.split(" - ")[0].trim());
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadEmployees();
            return;
        }

        if (e.getSource() == btnGenerate) {
            int empId = getSelectedEmpId();
            if (empId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                EmployeeDAO dao = new EmployeeDAO();
                DepartmentDAO deptDao = new DepartmentDAO();
                ResultSet rs = dao.getEmployeeById(empId);
                if (rs.next()) {
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int deptId = rs.getInt("dept_id");
                    double basicSalary = rs.getDouble("basic_salary");
                    rs.close();

                    String deptName = deptDao.getDepartmentName(deptId);

                    Employee emp;
                    switch (type) {
                        case "Manager":
                            emp = new Manager();
                            break;
                        case "Clerk":
                            emp = new Clerk();
                            break;
                        case "Teacher":
                            emp = new Teacher();
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown employee type: " + type);
                            return;
                    }
                    emp.setBasicSalary(basicSalary);
                    emp.calculateSalary();

                    // Build slip using generatePaySlip() and add extra details
                    StringBuilder slip = new StringBuilder();
                    slip.append("============================================================\n");
                    slip.append("           EMPLOYEE PAYROLL MANAGEMENT SYSTEM\n");
                    slip.append("                     SALARY SLIP\n");
                    slip.append("============================================================\n\n");
                    slip.append(String.format("  Employee ID    : %d\n", empId));
                    slip.append(String.format("  Employee Name  : %s\n", name));
                    slip.append(String.format("  Employee Type  : %s\n", type));
                    slip.append(String.format("  Department     : %s\n", deptName));
                    slip.append("\n------------------------------------------------------------\n");
                    slip.append(emp.generatePaySlip());
                    slip.append("\n------------------------------------------------------------\n");
                    slip.append("============================================================\n");

                    txtSlip.setText(slip.toString());
                } else {
                    rs.close();
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } else if (e.getSource() == btnPrint) {
            try {
                txtSlip.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Print error: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
