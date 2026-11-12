@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo Starting Employee Payroll Management System...
echo ========================================================

cd /d "%~dp0"

if not exist "bin\payroll\Main.class" (
    echo [WARNING] Project is not compiled yet.
    echo Running compile.bat first...
    call compile.bat
    if not exist "bin\payroll\Main.class" (
        echo [ERROR] Compilation failed. Please resolve compile errors before running.
        pause
        exit /b 1
    )
)

REM Build classpath with bin and any mysql-connector jar
set "CP=bin"
for %%f in (*mysql-connector*.jar) do (
    set "CP=bin;%%f"
)
if exist "lib" (
    for %%f in (lib\*.jar) do (
        set "CP=!CP!;%%f"
    )
)

echo Using Classpath: %CP%
echo Launching GUI...

java -cp "%CP%" payroll.Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ====================================================================
    echo [NOTE] If you encounter a ClassNotFoundException or Connection error:
    echo 1. Ensure MySQL Server is running (e.g. via XAMPP or MySQL Service).
    echo 2. Execute database\schema.sql to create payroll_db.
    echo 3. Verify password in src\payroll\db\DBConnection.java.
    echo 4. Place mysql-connector-j-8.x.x.jar in this folder or in lib\
    echo ====================================================================
    pause
)
