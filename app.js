// ========================================================
// Employee Payroll Management System - Client Application
// Group 10 (Rohit Kharnotia, Sankeit Sharma, Aryan, Gaganpreet Singh)
// ========================================================

const DEFAULT_DEPARTMENTS = [
  { deptId: 1, deptName: "Information Technology" },
  { deptId: 2, deptName: "Finance & Accounts" },
  { deptId: 3, deptName: "Human Resources" },
  { deptId: 4, deptName: "Administration" },
  { deptId: 5, deptName: "Academics" }
];

const DEFAULT_EMPLOYEES = [
  { empId: 101, name: "Rohit Kharnotia (Leader)", type: "Manager", deptId: 1, basicSalary: 75000 },
  { empId: 102, name: "Sankeit Sharma", type: "Clerk", deptId: 2, basicSalary: 35000 },
  { empId: 103, name: "Aryan", type: "Teacher", deptId: 5, basicSalary: 50000 },
  { empId: 104, name: "Gaganpreet Singh", type: "Manager", deptId: 4, basicSalary: 70000 },
  { empId: 105, name: "Dr. Neha Verma", type: "Teacher", deptId: 5, basicSalary: 55000 },
  { empId: 106, name: "Vikram Malhotra", type: "Clerk", deptId: 3, basicSalary: 32000 }
];

const DEFAULT_SALARIES = [
  { salaryId: 1, empId: 101, month: "September", year: 2026, basic: 75000, hra: 15000, da: 22500, gross: 112500, pf: 9000, tax: 11250, netSalary: 92250 },
  { salaryId: 2, empId: 102, month: "September", year: 2026, basic: 35000, hra: 5250, da: 7000, gross: 47250, pf: 3500, tax: 2363, netSalary: 41387 },
  { salaryId: 3, empId: 103, month: "September", year: 2026, basic: 50000, hra: 9000, da: 12500, gross: 71500, pf: 6000, tax: 5720, netSalary: 59780 },
  { salaryId: 4, empId: 104, month: "September", year: 2026, basic: 70000, hra: 14000, da: 21000, gross: 105000, pf: 8400, tax: 10500, netSalary: 86100 },
  { salaryId: 5, empId: 105, month: "September", year: 2026, basic: 55000, hra: 9900, da: 13750, gross: 78650, pf: 6600, tax: 6292, netSalary: 65758 },
  { salaryId: 6, empId: 106, month: "September", year: 2026, basic: 32000, hra: 4800, da: 6400, gross: 43200, pf: 3200, tax: 2160, netSalary: 37840 },
  { salaryId: 7, empId: 101, month: "October", year: 2026, basic: 75000, hra: 15000, da: 22500, gross: 112500, pf: 9000, tax: 11250, netSalary: 92250 },
  { salaryId: 8, empId: 102, month: "October", year: 2026, basic: 35000, hra: 5250, da: 7000, gross: 47250, pf: 3500, tax: 2363, netSalary: 41387 },
  { salaryId: 9, empId: 103, month: "October", year: 2026, basic: 50000, hra: 9000, da: 12500, gross: 71500, pf: 6000, tax: 5720, netSalary: 59780 },
  { salaryId: 10, empId: 104, month: "October", year: 2026, basic: 70000, hra: 14000, da: 21000, gross: 105000, pf: 8400, tax: 10500, netSalary: 86100 },
  { salaryId: 11, empId: 105, month: "October", year: 2026, basic: 55000, hra: 9900, da: 13750, gross: 78650, pf: 6600, tax: 6292, netSalary: 65758 },
  { salaryId: 12, empId: 106, month: "October", year: 2026, basic: 32000, hra: 4800, da: 6400, gross: 43200, pf: 3200, tax: 2160, netSalary: 37840 }
];

// State storage
function getDepartments() {
  const data = localStorage.getItem("payroll_departments");
  if (!data) {
    localStorage.setItem("payroll_departments", JSON.stringify(DEFAULT_DEPARTMENTS));
    return DEFAULT_DEPARTMENTS;
  }
  return JSON.parse(data);
}

function getEmployees() {
  const data = localStorage.getItem("payroll_employees");
  if (!data) {
    localStorage.setItem("payroll_employees", JSON.stringify(DEFAULT_EMPLOYEES));
    return DEFAULT_EMPLOYEES;
  }
  return JSON.parse(data);
}

function saveEmployees(emps) {
  localStorage.setItem("payroll_employees", JSON.stringify(emps));
}

function getSalaries() {
  const data = localStorage.getItem("payroll_salaries");
  if (!data) {
    localStorage.setItem("payroll_salaries", JSON.stringify(DEFAULT_SALARIES));
    return DEFAULT_SALARIES;
  }
  return JSON.parse(data);
}

function saveSalaries(sals) {
  localStorage.setItem("payroll_salaries", JSON.stringify(sals));
}

// ----------------- Tab Navigation -----------------
function switchTab(tabId) {
  document.querySelectorAll(".tab-content").forEach(el => el.classList.add("hidden"));
  const target = document.getElementById(tabId);
  if (target) target.classList.remove("hidden");

  document.querySelectorAll(".nav-tab").forEach(btn => {
    btn.classList.remove("bg-indigo-600", "text-white");
    btn.classList.add("text-slate-600", "hover:bg-slate-100");
  });

  const activeBtn = document.getElementById(`btn-${tabId}`);
  if (activeBtn) {
    activeBtn.classList.remove("text-slate-600", "hover:bg-slate-100");
    activeBtn.classList.add("bg-indigo-600", "text-white");
  }

  // Refresh data on tab switches
  if (tabId === "tab-employees") renderEmployeeTable();
  if (tabId === "tab-calculate") populateCalcDropdown();
  if (tabId === "tab-slip") populateSlipDropdown();
  if (tabId === "tab-report") renderDepartmentReport();
}

// ----------------- Salary Calculator Logic -----------------
function computeBreakdown(basic, type) {
  basic = Number(basic);
  let hraRate = 0, daRate = 0, pfRate = 0, taxRate = 0;

  if (type === "Manager") {
    hraRate = 0.20;
    daRate = 0.30;
    pfRate = 0.12;
    taxRate = 0.10;
  } else if (type === "Clerk") {
    hraRate = 0.15;
    daRate = 0.20;
    pfRate = 0.10;
    taxRate = 0.05;
  } else if (type === "Teacher") {
    hraRate = 0.18;
    daRate = 0.25;
    pfRate = 0.12;
    taxRate = 0.08;
  }

  const hra = basic * hraRate;
  const da = basic * daRate;
  const gross = basic + hra + da;
  const pf = basic * pfRate;
  const tax = gross * taxRate;
  const net = Math.round(gross - pf - tax);

  return {
    basic: basic,
    hra: hra,
    da: da,
    gross: gross,
    pf: pf,
    tax: tax,
    netSalary: net
  };
}

// ----------------- Employee CRUD -----------------
function renderDepartmentOptions() {
  const depts = getDepartments();
  const select = document.getElementById("empDeptInput");
  select.innerHTML = depts.map(d => `<option value="${d.deptId}">${d.deptName}</option>`).join("");
}

function renderEmployeeTable(filter = "") {
  const emps = getEmployees();
  const depts = getDepartments();
  const deptMap = Object.fromEntries(depts.map(d => [d.deptId, d.deptName]));
  const tbody = document.getElementById("empTableBody");

  const filtered = filter
    ? emps.filter(e => e.name.toLowerCase().includes(filter.toLowerCase()))
    : emps;

  if (filtered.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" class="p-6 text-center text-slate-400 italic">No employees found.</td></tr>`;
    return;
  }

  tbody.innerHTML = filtered.map(e => `
    <tr class="hover:bg-slate-50 transition border-b border-slate-100">
      <td class="p-3 font-semibold text-slate-800">#${e.empId}</td>
      <td class="p-3 font-medium text-slate-900">${e.name}</td>
      <td class="p-3">
        <span class="px-2 py-0.5 text-xs font-semibold rounded-full ${
          e.type === 'Manager' ? 'bg-indigo-100 text-indigo-800' :
          e.type === 'Teacher' ? 'bg-emerald-100 text-emerald-800' :
          'bg-amber-100 text-amber-800'
        }">${e.type}</span>
      </td>
      <td class="p-3 text-slate-600">${deptMap[e.deptId] || 'General'}</td>
      <td class="p-3 font-mono font-semibold text-slate-800">₹${Number(e.basicSalary).toLocaleString('en-IN', {minimumFractionDigits: 2})}</td>
      <td class="p-3 text-center space-x-1">
        <button onclick="editEmployee(${e.empId})" class="p-1.5 text-slate-600 hover:text-indigo-600 hover:bg-indigo-50 rounded" title="Edit">
          <i class="fa-solid fa-pen-to-square"></i>
        </button>
        <button onclick="deleteEmployee(${e.empId})" class="p-1.5 text-slate-600 hover:text-rose-600 hover:bg-rose-50 rounded" title="Delete">
          <i class="fa-solid fa-trash-can"></i>
        </button>
      </td>
    </tr>
  `).join("");
}

function handleSearch(val) {
  renderEmployeeTable(val);
}

function handleSaveEmployee(e) {
  e.preventDefault();
  const empIdInput = document.getElementById("empIdInput").value;
  const name = document.getElementById("empNameInput").value.trim();
  const type = document.getElementById("empTypeInput").value;
  const deptId = parseInt(document.getElementById("empDeptInput").value);
  const basicSalary = parseFloat(document.getElementById("empSalaryInput").value);

  if (!name || isNaN(basicSalary) || basicSalary <= 0) {
    alert("Please enter a valid employee name and basic salary > 0.");
    return;
  }

  const emps = getEmployees();

  if (empIdInput) {
    // Update
    const idx = emps.findIndex(x => x.empId === parseInt(empIdInput));
    if (idx !== -1) {
      emps[idx] = { empId: parseInt(empIdInput), name, type, deptId, basicSalary };
      saveEmployees(emps);
      alert("Employee updated successfully!");
    }
  } else {
    // Add new
    const newId = emps.length ? Math.max(...emps.map(x => x.empId)) + 1 : 101;
    emps.push({ empId: newId, name, type, deptId, basicSalary });
    saveEmployees(emps);
    alert("Employee added successfully!");
  }

  resetEmpForm();
  renderEmployeeTable();
}

function editEmployee(empId) {
  const emps = getEmployees();
  const emp = emps.find(x => x.empId === empId);
  if (!emp) return;

  document.getElementById("empIdInput").value = emp.empId;
  document.getElementById("empNameInput").value = emp.name;
  document.getElementById("empTypeInput").value = emp.type;
  document.getElementById("empDeptInput").value = emp.deptId;
  document.getElementById("empSalaryInput").value = emp.basicSalary;

  document.getElementById("btnSubmitEmp").innerHTML = `<i class="fa-solid fa-check"></i> Update Employee`;
}

function deleteEmployee(empId) {
  if (!confirm(`Are you sure you want to delete employee #${empId}?`)) return;
  let emps = getEmployees();
  emps = emps.filter(x => x.empId !== empId);
  saveEmployees(emps);
  renderEmployeeTable();
}

function resetEmpForm() {
  document.getElementById("empForm").reset();
  document.getElementById("empIdInput").value = "";
  document.getElementById("btnSubmitEmp").innerHTML = `<i class="fa-solid fa-plus"></i> Add Employee`;
}

// ----------------- Monthly Calculation Tab -----------------
function populateCalcDropdown() {
  const emps = getEmployees();
  const select = document.getElementById("calcEmpSelect");
  select.innerHTML = emps.map(e => `<option value="${e.empId}">${e.empId} - ${e.name} (${e.type})</option>`).join("");
  runSalaryCalculation();
}

let lastCalculated = null;

function runSalaryCalculation() {
  const emps = getEmployees();
  const empId = parseInt(document.getElementById("calcEmpSelect").value);
  const emp = emps.find(e => e.empId === empId);
  if (!emp) return;

  document.getElementById("calcBadgeType").innerText = emp.type;

  const result = computeBreakdown(emp.basicSalary, emp.type);
  lastCalculated = { emp, result };

  const fmt = val => "₹" + Number(val).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

  document.getElementById("breakdownBasic").innerText = fmt(result.basic);
  document.getElementById("breakdownHra").innerText = "+" + fmt(result.hra);
  document.getElementById("breakdownDa").innerText = "+" + fmt(result.da);
  document.getElementById("breakdownGross").innerText = fmt(result.gross);
  document.getElementById("breakdownPf").innerText = "-" + fmt(result.pf);
  document.getElementById("breakdownTax").innerText = "-" + fmt(result.tax);
  document.getElementById("breakdownNet").innerText = fmt(result.netSalary);
}

function saveCalculatedSalary() {
  if (!lastCalculated) return;
  const month = document.getElementById("calcMonthInput").value;
  const year = parseInt(document.getElementById("calcYearInput").value);

  const salaries = getSalaries();
  const newId = salaries.length ? Math.max(...salaries.map(s => s.salaryId)) + 1 : 1;

  salaries.push({
    salaryId: newId,
    empId: lastCalculated.emp.empId,
    month: month,
    year: year,
    basic: lastCalculated.result.basic,
    hra: lastCalculated.result.hra,
    da: lastCalculated.result.da,
    gross: lastCalculated.result.gross,
    pf: lastCalculated.result.pf,
    tax: lastCalculated.result.tax,
    netSalary: lastCalculated.result.netSalary
  });

  saveSalaries(salaries);
  alert(`Salary saved successfully for ${lastCalculated.emp.name} for ${month} ${year}!`);
}

// ----------------- Salary Slip Tab -----------------
function populateSlipDropdown() {
  const emps = getEmployees();
  const select = document.getElementById("slipEmpSelect");
  select.innerHTML = emps.map(e => `<option value="${e.empId}">${e.empId} - ${e.name}</option>`).join("");
}

function generateSalarySlip() {
  const emps = getEmployees();
  const depts = getDepartments();
  const deptMap = Object.fromEntries(depts.map(d => [d.deptId, d.deptName]));

  const empId = parseInt(document.getElementById("slipEmpSelect").value);
  const emp = emps.find(e => e.empId === empId);
  if (!emp) return;

  const result = computeBreakdown(emp.basicSalary, emp.type);
  const fmt = val => "₹" + Number(val).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

  const slipHtml = `
    <div class="space-y-4">
      <div class="grid grid-cols-2 gap-4 pb-4 border-b">
        <div>
          <p><span class="text-slate-500">Employee ID:</span> <strong>#${emp.empId}</strong></p>
          <p><span class="text-slate-500">Employee Name:</span> <strong>${emp.name}</strong></p>
        </div>
        <div>
          <p><span class="text-slate-500">Designation:</span> <strong>${emp.type}</strong></p>
          <p><span class="text-slate-500">Department:</span> <strong>${deptMap[emp.deptId] || 'General'}</strong></p>
        </div>
      </div>

      <div class="space-y-2">
        <div class="flex justify-between py-1 border-b border-dashed">
          <span>Basic Salary</span>
          <span>${fmt(result.basic)}</span>
        </div>
        <div class="flex justify-between py-1 border-b border-dashed text-emerald-700">
          <span>House Rent Allowance (HRA)</span>
          <span>+${fmt(result.hra)}</span>
        </div>
        <div class="flex justify-between py-1 border-b border-dashed text-emerald-700">
          <span>Dearness Allowance (DA)</span>
          <span>+${fmt(result.da)}</span>
        </div>
        <div class="flex justify-between py-1.5 font-bold border-b-2 border-slate-800">
          <span>Gross Earnings</span>
          <span>${fmt(result.gross)}</span>
        </div>
        <div class="flex justify-between py-1 border-b border-dashed text-rose-700">
          <span>Provident Fund (PF)</span>
          <span>-${fmt(result.pf)}</span>
        </div>
        <div class="flex justify-between py-1 border-b border-dashed text-rose-700">
          <span>Income Tax</span>
          <span>-${fmt(result.tax)}</span>
        </div>
        <div class="flex justify-between py-2 text-base font-bold bg-slate-100 px-3 rounded mt-3">
          <span>NET TAKE-HOME SALARY</span>
          <span>${fmt(result.netSalary)}</span>
        </div>
      </div>
    </div>
  `;

  document.getElementById("slipContent").innerHTML = slipHtml;
}

function printSalarySlip() {
  window.print();
}

// ----------------- Department Report Tab -----------------
function renderDepartmentReport() {
  const depts = getDepartments();
  const emps = getEmployees();
  const salaries = getSalaries();
  const tbody = document.getElementById("deptReportTableBody");

  const fmt = val => "₹" + Number(val).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

  tbody.innerHTML = depts.map(d => {
    const deptEmps = emps.filter(e => e.deptId === d.deptId);
    const empIds = new Set(deptEmps.map(e => e.empId));
    const deptSals = salaries.filter(s => empIds.has(s.empId));

    const totalNet = deptSals.reduce((sum, s) => sum + Number(s.netSalary), 0);
    const avgNet = deptSals.length ? totalNet / deptSals.length : 0;

    return `
      <tr class="hover:bg-slate-50 transition border-b border-slate-100">
        <td class="p-3 font-semibold text-slate-800">${d.deptName}</td>
        <td class="p-3 text-center">
          <span class="inline-block px-2.5 py-0.5 rounded-full bg-slate-100 text-slate-700 font-bold text-xs">${deptEmps.length}</span>
        </td>
        <td class="p-3 text-right font-mono font-semibold text-slate-900">${fmt(totalNet)}</td>
        <td class="p-3 text-right font-mono text-emerald-700 font-semibold">${fmt(avgNet)}</td>
      </tr>
    `;
  }).join("");
}

// ----------------- Initialization -----------------
document.addEventListener("DOMContentLoaded", () => {
  renderDepartmentOptions();
  renderEmployeeTable();
  populateCalcDropdown();
  populateSlipDropdown();
  renderDepartmentReport();
});
