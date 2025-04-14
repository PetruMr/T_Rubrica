@echo off
REM Clean previous build artifacts except specific files
if exist out\ (
    for /f "delims=" %%i in ('dir /b /s /a-d out\ ^| findstr /v /i /e "schema_database.sql credenziali_database.properties"') do del /q "%%i"
)
if not exist out\ (
    mkdir out
)

REM Create a temporary manifest file with Main-Class and Class-Path information.
echo Main-Class: Application > manifest.txt
echo Class-Path: ../lib/mysql-connector-j-8.0.32.jar >> manifest.txt

REM Compile all Java files in the src folder (adjust the path if your sources are in subdirectories)
javac -d out ./src/*.java

REM Check if compilation was successful
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

REM Create the JAR file using the manifest file.
REM Note: We're instructing jar (with options cfm) to use manifest.txt.
cd out
jar cfm Rubrica.jar ..\manifest.txt -C . .
cd ..

REM Clean up the manifest file and class files (if desired)
del manifest.txt
for /r out %%i in (*.class) do del %%i

echo Build completed successfully. The JAR file is located in the "out" folder.
