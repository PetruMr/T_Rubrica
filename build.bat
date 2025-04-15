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

REM Find all Java source files recursively and compile
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt

REM Check if compilation was successful
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

REM Copy the images folder into "out" so they end up in the JAR
xcopy /s /i /y src\images out\images

REM Merge dependencies: unpack all jars in "lib" into "out"
pushd out
for %%f in (..\lib\*.jar) do (
    echo Unpacking "%%f"...
    jar xf "%%f"
)

REM Create the JAR file using the manifest file.
REM Note: We're instructing jar (with options cfm) to use manifest.txt.
cd out
jar cfm Rubrica.jar ..\manifest.txt -C . .
cd ..

REM Clean up the manifest file and class files (if desired)
del manifest.txt
del sources.txt
for /r out %%i in (*.class) do del %%i
REM Clean up the folders which are created by the jar command
for /d %%i in (out\*) do rmdir /s /q %%i
REM Remove "LICENSE", "README", "INFO_SRC" and "INFO_BIN"
del out\LICENSE 2>nul
del out\README 2>nul
del out\INFO_SRC 2>nul
del out\INFO_BIN 2>nul


echo Build completed successfully. The JAR file is located in the "out" folder.
