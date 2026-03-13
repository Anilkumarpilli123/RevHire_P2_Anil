@echo off
set PORT=8081
echo Checking for processes on port %PORT%...

for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%PORT% ^| findstr LISTENING') do (
    echo Found process with PID %%a on port %PORT%. Attempting to kill...
    taskkill /F /PID %%a
    echo Process %%a terminated.
)

if errorlevel 1 (
    echo No process found listening on port %PORT%.
) else (
    echo Port %PORT% should now be free.
)
pause
