package payroll.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import payroll.db.EmployeeDAO;
import payroll.db.DepartmentDAO;

public class EmployeePanel extends JPanel implements ActionListener {

    private JTextField txtName, txtBasicSalary, txtSearch;
    private JComboBox<String> cmbType, cmbDepartment;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedEmpId = -1;

    public EmployeePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Form Panel ----
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
                "Employee Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(44, 62, 80)));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        txtName = new JTextField();
        formPanel.add(lblName);
        formPanel.add(txtName);

        JLabel lblType = new JLabel("Type:");
        lblType.setFont(labelFont);
        cmbType = new JComboBox<>(new String[]{"Manager", "Clerk", "Teacher"});
        formPanel.add(lblType);
        formPanel.add(cmbType);

        JLabel lblDept = new JLabel("Department:");
        lblDept.setFont(labelFont);
        cmbDepartment = new JComboBox<>();
        loadDepartments();
        formPanel.add(lblDept);
        formPanel.add(cmbDepartment);

        JLabel lblSalary = new JLabel("Basic Salary:");
        lblSalary.setFont(labelFont);
        txtBasicSalary = new JTextField();
        formPanel.add(lblSalary);
        formPanel.add(txtBasicSalary);

        JLabel lblSearch = new JLabel("Search Name:");
        lblSearch.setFont(labelFont);
        txtSearch = new JTextField();
        formPanel.add(lblSearch);
        formPanel.add(txtSearch);

        // ---- Button Panel ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Color headerBg = new Color(44, 62, 80);

        btnAdd = createStyledButton("Add", headerBg);
        btnUpdate = createStyledButton("Update", headerBg);
        btnDelete = createStyledButton("Delete", headerBg);
        btnClear = createStyledButton("Clear", headerBg);
        btnSearch = createStyledButton("Search", headerBg);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);

        // ---- Top section ----
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---- Table ----
        String[] columns = {"Emp ID", "Name", "Type", "Department", "Basic Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(headerBg);
        table.getTableHeader().setForeground(Color.WHITE);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedEmpId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    cmbType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    selectDepartment(tableModel.getValueAt(row, 3).toString());
                    txtBasicSalary.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
                "Employee List",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(44, 62, 80)));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadEmployees();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(new Color(15, 23, 42)); // Deep dark font for crystal-clear readability
        btn.setBackground(new Color(241, 245, 249)); // Clean, light button surface
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    public void refresh() {
        loadDepartments();
        loadEmployees();
    }

    private void loadDepartments() {
        cmbDepartment.removeAllItems();
        try {
            DepartmentDAO dao = new DepartmentDAO();
            ResultSet rs = dao.getAllDepartments();
            while (rs.next()) {
                cmbDepartment.addItem(rs.getInt("dept_id") + " - " + rs.getString("dept_name"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectDepartment(String deptName) {
        for (int i = 0; i < cmbDepartment.getItemCount(); i++) {
            if (cmbDepartment.getItemAt(i).contains(deptName)) {
                cmbDepartment.setSelectedIndex(i);
                break;
            }
        }
    }

    private int getSelectedDeptId() {
        String selected = (String) cmbDepartment.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            return Integer.parseInt(selected.split(" - ")[0].trim());
        }
        return -1;
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        try {
            EmployeeDAO dao = new EmployeeDAO();
            DepartmentDAO deptDao = new DepartmentDAO();
            ResultSet rs = dao.getAllEmployees();
            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int deptId = rs.getInt("dept_id");
                double salary = rs.getDouble("basic_salary");
                String deptName = deptDao.getDepartmentName(deptId);
                tableModel.addRow(new Object[]{empId, name, type, deptName, salary});
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtBasicSalary.setText("");
        txtSearch.setText("");
        cmbType.setSelectedIndex(0);
        if (cmbDepartment.getItemCount() > 0) {
            cmbDepartment.setSelectedIndex(0);
        }
        selectedEmpId = -1;
        table.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnAdd) {
            String name = txtName.getText().trim();
            String type = (String) cmbType.getSelectedItem();
            int deptId = getSelectedDeptId();
            String salaryStr = txtBasicSalary.getText().trim();

            if (name.isEmpty() || salaryStr.isEmpty() || deptId == -1) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double basicSalary = Double.parseDouble(salaryStr);
                if (basicSalary <= 0) {
                    JOptionPane.showMessageDialog(this, "Salary must be greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                EmployeeDAO dao = new EmployeeDAO();
                dao.addEmployee(name, type, deptId, basicSalary);
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                loadEmployees();
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } else if (source == btnUpdate) {
            if (selectedEmpId == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee from the table first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String name = txtName.getText().trim();
            String type = (String) cmbType.getSelectedItem();
            int deptId = getSelectedDeptId();
            String salaryStr = txtBasicSalary.getText().trim();

            if (name.isEmpty() || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double basicSalary = Double.parseDouble(salaryStr);
                EmployeeDAO dao = new EmployeeDAO();
                dao.updateEmployee(selectedEmpId, name, type, deptId, basicSalary);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                loadEmployees();
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } else if (source == btnDelete) {
            if (selectedEmpId == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee from the table first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    EmployeeDAO dao = new EmployeeDAO();
                    dao.deleteEmployee(selectedEmpId);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                    loadEmployees();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }

        } else if (source == btnClear) {
            clearFields();

        } else if (source == btnSearch) {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadEmployees();
                return;
            }
            tableModel.setRowCount(0);
            try {
                EmployeeDAO dao = new EmployeeDAO();
                DepartmentDAO deptDao = new DepartmentDAO();
                ResultSet rs = dao.searchEmployees(keyword);
                while (rs.next()) {
                    int empId = rs.getInt("emp_id");
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int deptId = rs.getInt("dept_id");
                    double salary = rs.getDouble("basic_salary");
                    String deptName = deptDao.getDepartmentName(deptId);
                    tableModel.addRow(new Object[]{empId, name, type, deptName, salary});
                }
                rs.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Search Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
