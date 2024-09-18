@echo off
setlocal ENABLEEXTENSIONS
call mvn clean package -DskipTests
if %errorlevel% == 0 exit 0
pause