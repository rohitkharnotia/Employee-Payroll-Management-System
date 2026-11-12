package payroll.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import payroll.model.*;
import payroll.db.EmployeeDAO;
import payroll.db.SalaryDAO;

public class SalaryCalculationPanel extends JPanel implements ActionListener {

    private JComboBox<String> cmbEmployee;
    private JTextField txtMonth, txtYear;
    private JButton btnCalculate, btnSave, btnRefresh;
    private JLabel lblBasic, lblHRA, lblDA, lblGross, lblPF, lblTax, lblNet;
    private double[] currentBreakdown;
    private int currentEmpId = -1;

    public SalaryCalculationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Color headerBg = new Color(44, 62, 80);

        // ---- Header ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBg);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel headerLabel = new JLabel("Salary Calculation");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // ---- Input Panel ----
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(headerBg, 2),
                "Select Employee & Period",
                TitledBorder.LEFT, TitledBorder.TOP,
                labelFont, headerBg));

        JLabel lblEmp = new JLabel("Select Employee:");
        lblEmp.setFont(labelFont);
        cmbEmployee = new JComboBox<>();
        loadEmployees();

        JLabel lblMonth = new JLabel("Month:");
        lblMonth.setFont(labelFont);
        txtMonth = new JTextField("November");

        JLabel lblYear = new JLabel("Year:");
        lblYear.setFont(labelFont);
        txtYear = new JTextField("2026");

        btnRefresh = new JButton("Refresh List");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRefresh.setForeground(new Color(15, 23, 42));
        btnRefresh.setBackground(new Color(241, 245, 249));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(this);

        inputPanel.add(lblEmp);
        inputPanel.add(cmbEmployee);
        inputPanel.add(lblMonth);
        inputPanel.add(txtMonth);
        inputPanel.add(lblYear);
        inputPanel.add(txtYear);
        inputPanel.add(new JLabel());
        inputPanel.add(btnRefresh);

        // ---- Result Panel ----
        JPanel resultPanel = new JPanel(new GridLayout(7, 2, 10, 8));
        resultPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(headerBg, 2),
                "Salary Breakdown",
                TitledBorder.LEFT, TitledBorder.TOP,
                labelFont, headerBg));

        Font valueFont = new Font("SansSerif", Font.PLAIN, 14);

        lblBasic = new JLabel("0.00");
        lblBasic.setFont(valueFont);
        lblHRA = new JLabel("0.00");
        lblHRA.setFont(valueFont);
        lblDA = new JLabel("0.00");
        lblDA.setFont(valueFont);
        lblGross = new JLabel("0.00");
        lblGross.setFont(valueFont);
        lblPF = new JLabel("0.00");
        lblPF.setFont(valueFont);
        lblTax = new JLabel("0.00");
        lblTax.setFont(valueFont);
        lblNet = new JLabel("0.00");
        lblNet.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNet.setForeground(new Color(39, 174, 96));

        JLabel l1 = new JLabel("Basic Salary:");  l1.setFont(labelFont);
        JLabel l2 = new JLabel("HRA:");            l2.setFont(labelFont);
        JLabel l3 = new JLabel("DA:");             l3.setFont(labelFont);
        JLabel l4 = new JLabel("Gross Salary:");   l4.setFont(labelFont);
        JLabel l5 = new JLabel("PF:");             l5.setFont(labelFont);
        JLabel l6 = new JLabel("Tax:");            l6.setFont(labelFont);
        JLabel l7 = new JLabel("Net Salary:");     l7.setFont(labelFont);

        resultPanel.add(l1); resultPanel.add(lblBasic);
        resultPanel.add(l2); resultPanel.add(lblHRA);
        resultPanel.add(l3); resultPanel.add(lblDA);
        resultPanel.add(l4); resultPanel.add(lblGross);
        resultPanel.add(l5); resultPanel.add(lblPF);
        resultPanel.add(l6); resultPanel.add(lblTax);
        resultPanel.add(l7); resultPanel.add(lblNet);

        // ---- Button Panel ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnCalculate = new JButton("Calculate Salary");
        btnCalculate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCalculate.setForeground(new Color(15, 23, 42));
        btnCalculate.setBackground(new Color(241, 245, 249));
        btnCalculate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnCalculate.setFocusPainted(false);
        btnCalculate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalculate.addActionListener(this);

        btnSave = new JButton("Save Salary");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSave.setForeground(new Color(15, 23, 42));
        btnSave.setBackground(new Color(220, 252, 231));
        btnSave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 197, 94), 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setEnabled(false);
        btnSave.addActionListener(this);

        buttonPanel.add(btnCalculate);
        buttonPanel.add(btnSave);

        // ---- Layout ----
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(inputPanel);
        centerPanel.add(resultPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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

        if (e.getSource() == btnCalculate) {
            int empId = getSelectedEmpId();
            if (empId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String month = txtMonth.getText().trim();
            String year = txtYear.getText().trim();
            if (month.isEmpty() || year.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter month and year.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                EmployeeDAO dao = new EmployeeDAO();
                ResultSet rs = dao.getEmployeeById(empId);
                if (rs.next()) {
                    String type = rs.getString("type");
                    double basicSalary = rs.getDouble("basic_salary");
                    rs.close();

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
                    currentBreakdown = emp.getSalaryBreakdown();
                    currentEmpId = empId;

                    lblBasic.setText(String.format("%.2f", currentBreakdown[0]));
                    lblHRA.setText(String.format("%.2f", currentBreakdown[1]));
                    lblDA.setText(String.format("%.2f", currentBreakdown[2]));
                    lblGross.setText(String.format("%.2f", currentBreakdown[3]));
                    lblPF.setText(String.format("%.2f", currentBreakdown[4]));
                    lblTax.setText(String.format("%.2f", currentBreakdown[5]));
                    lblNet.setText(String.format("%.2f", currentBreakdown[6]));

                    btnSave.setEnabled(true);
                } else {
                    rs.close();
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } else if (e.getSource() == btnSave) {
            if (currentBreakdown == null || currentEmpId == -1) {
                JOptionPane.showMessageDialog(this, "Calculate salary first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String month = txtMonth.getText().trim();
            int year;
            try {
                year = Integer.parseInt(txtYear.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid year format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                SalaryDAO salaryDAO = new SalaryDAO();
                salaryDAO.saveSalary(currentEmpId, month, year,
                        currentBreakdown[0], currentBreakdown[1], currentBreakdown[2],
                        currentBreakdown[3], currentBreakdown[4], currentBreakdown[5],
                        currentBreakdown[6]);
                JOptionPane.showMessageDialog(this, "Salary saved successfully!");
                btnSave.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving salary: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
