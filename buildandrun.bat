@echo off
rem Build the project
call build.bat

rem Navigate to the output directory
cd out

rem Execute the JAR file
java -jar Rubrica.jar