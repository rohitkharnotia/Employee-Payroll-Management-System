package payroll.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private EmployeePanel employeePanel;
    private SalaryStructurePanel salaryStructurePanel;
    private SalaryCalculationPanel salaryCalculationPanel;
    private SalarySlipPanel salarySlipPanel;
    private DepartmentReportPanel departmentReportPanel;

    public MainFrame() {
        setTitle("Employee Payroll Management System — Group 10");
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        employeePanel = new EmployeePanel();
        salaryStructurePanel = new SalaryStructurePanel();
        salaryCalculationPanel = new SalaryCalculationPanel();
        salarySlipPanel = new SalarySlipPanel();
        departmentReportPanel = new DepartmentReportPanel();

        tabbedPane.addTab("Employee Registration", employeePanel);
        tabbedPane.addTab("Salary Structure", salaryStructurePanel);
        tabbedPane.addTab("Calculate Salary", salaryCalculationPanel);
        tabbedPane.addTab("Salary Slip", salarySlipPanel);
        tabbedPane.addTab("Department Report", departmentReportPanel);

        // Auto-refresh the selected panel when switching tabs
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            switch (idx) {
                case 0: employeePanel.refresh(); break;
                case 2: salaryCalculationPanel.refresh(); break;
                case 3: salarySlipPanel.refresh(); break;
                case 4: departmentReportPanel.refresh(); break;
            }
        });

        add(tabbedPane);
    }
}
