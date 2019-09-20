@echo off
setlocal ENABLEEXTENSIONS
call mvn package -DskipTests
pause