@echo off

IF "%JAVA_HOME%"=="" SET LOCAL_JAVA=java
IF NOT "%JAVA_HOME%"=="" SET LOCAL_JAVA=%JAVA_HOME%\bin\java

set basedir=%~f0
:strip
set removed=%basedir:~-1%
set basedir=%basedir:~0,-1%
if NOT "%removed%"=="\" goto strip
set LOG4J_CONVERTER_HOME=%basedir%

set TMP_CP="."

dir /b "%LOG4J_CONVERTER_HOME%\lib\*.*" > %TEMP%\log4j-converter-lib.tmp
FOR /F %%I IN (%TEMP%\log4j-converter-lib.tmp) DO CALL "%LOG4J_CONVERTER_HOME%\addpath.bat" "%LOG4J_CONVERTER_HOME%\lib\%%I"

"%LOCAL_JAVA%" -cp %TMP_CP% com.fragstealers.log4j.ui.cli.CommandLineUI %1 %2 %3 %4 %5 %6 %7 %8 %9
