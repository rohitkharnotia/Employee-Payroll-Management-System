@echo off
setlocal enabledelayedexpansion

echo ========================================================
echo Compiling Employee Payroll Management System...
echo ========================================================

cd /d "%~dp0"

if not exist "bin" mkdir bin

REM Find any mysql-connector JAR in current dir or lib folder
set "CP=."
for %%f in (*mysql-connector*.jar) do (
    set "CP=.;%%f"
)
if exist "lib" (
    for %%f in (lib\*.jar) do (
        set "CP=!CP!;%%f"
    )
)

echo Classpath: %CP%
echo Compiling Java source files...

javac -d bin -cp "%CP%" @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo Compilation successful! Classes compiled into bin\
    echo You can now launch the application by running: run.bat
    echo ========================================================
) else (
    echo.
    echo [ERROR] Compilation failed. Please check the error messages above.
)

pause
