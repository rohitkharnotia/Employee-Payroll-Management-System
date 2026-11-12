package payroll.model;

/**
 * Represents a department in the organization.
 */
public class Department {

    private int deptId;
    private String deptName;

    /**
     * Default no-arg constructor.
     */
    public Department() {
    }

    /**
     * Parameterized constructor.
     *
     * @param deptId   the department ID
     * @param deptName the department name
     */
    public Department(int deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    // -------------------- Getters & Setters --------------------

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    // -------------------- Static Utility --------------------

    /**
     * Returns an array of sample department names.
     *
     * @return array of department name strings
     */
    public static String[] getAllDepartmentNames() {
        return new String[] {
            "Human Resources",
            "Finance",
            "Engineering",
            "Marketing",
            "Sales",
            "Administration",
            "IT Support"
        };
    }

    @Override
    public String toString() {
        return "Department{deptId=" + deptId + ", deptName='" + deptName + "'}";
    }
}
