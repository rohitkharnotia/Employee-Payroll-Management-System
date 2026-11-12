# Employee Payroll Management System

**Group 10** — A system to maintain employee data and compute salary slips with allowances and deductions.

## Team Members

| Role     | Name              | UID        |
|----------|-------------------|------------|
| Leader   | Rohit Kharnotia   | 2510001265 |
| Member 1 | Sankeit Sharma    | 2510001308 |
| Member 2 | Aryan             | 2510001228 |
| Member 3 | Gaganpreet Singh  | 2510001277 |

## Modules

1. **Employee Registration** — Add, update, delete, and search employees
2. **Salary Structure Setup** — View salary components (HRA, DA, PF, Tax) per employee type
3. **Monthly Salary Calculation** — Calculate salary with breakdown for any employee
4. **Salary Slip Generation** — Generate and print formatted salary slips
5. **Department-wise Report** — View aggregated salary report by department

## Syllabus Concepts Applied

| Unit   | Concepts                                                                                      |
|--------|-----------------------------------------------------------------------------------------------|
| Unit 1 | Classes `Employee`, `Department`; constructors; `Math.round()`; arrays (`getSalaryBreakdown`) |
| Unit 2 | Inheritance `Employee → Manager/Clerk/Teacher`; overriding `calculateSalary()`; interface `Payable`; package `payroll.model` |
| Unit 3 | `InvalidSalaryException` (custom checked exception); JavaBean class for `Employee`            |
| Unit 4 | JDBC CRUD operations (`PreparedStatement`); `Statement.executeQuery()` for reports             |
| Unit 5 | Swing `JTextField`, `JLabel`, `JButton`; `GridLayout` forms; `ActionListener` event handling   |

## Project Structure

```
Employee Payroll Management System/
├── database/
│   └── schema.sql                          # MySQL database schema
├── src/
│   └── payroll/
│       ├── Main.java                       # Entry point
│       ├── model/
│       │   ├── Payable.java                # Interface (Unit 2)
│       │   ├── Employee.java               # Abstract base class (Unit 1, 2, 3)
│       │   ├── Manager.java                # Extends Employee (Unit 2)
│       │   ├── Clerk.java                  # Extends Employee (Unit 2)
│       │   ├── Teacher.java                # Extends Employee (Unit 2)
│       │   └── Department.java             # Department class (Unit 1)
│       ├── exception/
│       │   └── InvalidSalaryException.java # Custom exception (Unit 3)
│       ├── db/
│       │   ├── DBConnection.java           # JDBC Singleton (Unit 4)
│       │   ├── EmployeeDAO.java            # Employee CRUD (Unit 4)
│       │   ├── DepartmentDAO.java          # Department DAO (Unit 4)
│       │   └── SalaryDAO.java              # Salary DAO + Reports (Unit 4)
│       └── ui/
│           ├── MainFrame.java              # JFrame + JTabbedPane (Unit 5)
│           ├── EmployeePanel.java          # Employee registration form (Unit 5)
│           ├── SalaryStructurePanel.java   # Salary structure display (Unit 5)
│           ├── SalaryCalculationPanel.java # Salary calculator (Unit 5)
│           ├── SalarySlipPanel.java        # Salary slip generator (Unit 5)
│           └── DepartmentReportPanel.java  # Department report (Unit 5)
├── compile.bat                             # Compile script
├── run.bat                                 # Run script
└── README.md
```

## Database Tables

- **departments** — `dept_id` (PK), `dept_name`
- **employees** — `emp_id` (PK), `name`, `type`, `dept_id` (FK), `basic_salary`
- **salary** — `salary_id` (PK), `emp_id` (FK), `month`, `year`, `basic`, `hra`, `da`, `gross`, `pf`, `tax`, `net_salary`

## Prerequisites

1. **Java JDK 8+** — [Download](https://www.oracle.com/java/technologies/downloads/)
2. **MySQL Server 5.7+** — [Download](https://dev.mysql.com/downloads/mysql/)
3. **MySQL Connector/J** — [Download](https://dev.mysql.com/downloads/connector/j/) (Place `mysql-connector-j-x.x.x.jar` in the project root)

## Deploying on Vercel

This project includes a web client in [index.html](file:///c:/Users/rohit/OneDrive/Desktop/Employee%20Payroll%20Management%20System/index.html) and [app.js](file:///c:/Users/rohit/OneDrive/Desktop/Employee%20Payroll%20Management%20System/app.js) configured with [vercel.json](file:///c:/Users/rohit/OneDrive/Desktop/Employee%20Payroll%20Management%20System/vercel.json) for instant 1-click cloud deployment on Vercel.

### Method 1: Deploy with Git & GitHub (Recommended)
1. Push this folder to a GitHub repository:
   ```bash
   git init
   git add .
   git commit -m "Initial commit of Employee Payroll Management System"
   git branch -M main
   git remote add origin https://github.com/<your-username>/<repo-name>.git
   git push -u origin main
   ```
2. Log into [Vercel](https://vercel.com).
3. Click **"Add New"** &rarr; **"Project"**.
4. Import your GitHub repository and click **Deploy**.
5. Your live URL will be ready in seconds (e.g., `https://employee-payroll-group10.vercel.app`)!

### Method 2: Deploy with Vercel CLI
If you have Node.js and Vercel CLI installed:
```bash
npx vercel
```
Follow the prompts (accept default settings) and your site will be live instantly.

---

## Desktop Java Application (Swing + JDBC)

### Step 1: Create the Database
```bash
mysql -u root -p < database/schema.sql
```

### Step 2: Configure Database Connection
Edit `src/payroll/db/DBConnection.java` and update if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/payroll_db";
private static final String USER = "root";
private static final String PASSWORD = "";  // ← set your MySQL password
```

### Step 3: Compile
```bash
compile.bat
```

### Step 4: Run
```bash
run.bat
```

## Salary Calculation

| Type    | HRA         | DA          | PF          | Tax            |
|---------|-------------|-------------|-------------|----------------|
| Manager | 20% of Basic| 30% of Basic| 12% of Basic| 10% of Gross   |
| Clerk   | 15% of Basic| 20% of Basic| 10% of Basic|  5% of Gross   |
| Teacher | 18% of Basic| 25% of Basic| 12% of Basic|  8% of Gross   |

**Formulas:**
- Gross Salary = Basic + HRA + DA
- Net Salary = Gross − PF − Tax

