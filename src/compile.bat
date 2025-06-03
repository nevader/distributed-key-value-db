@echo off

set SRC_DIR=src
set BIN_DIR=bin

echo Compiling Java files...

rem Create the bin directory if it doesn't exist
if not exist %BIN_DIR% mkdir %BIN_DIR%

rem Compile all .java files in the src directory
javac -d %BIN_DIR% %SRC_DIR%\*.java

echo Compilation completed successfully!