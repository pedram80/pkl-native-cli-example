@echo off

REM Check if GRAALVM_HOME is set
if "%GRAALVM_HOME%"=="" (
    echo GRAALVM_HOME environment variable is not set.
    echo Please set it to the root of your GraalVM installation.
    exit /b 1
)

REM Build the project with Gradle
call .\gradlew.bat build

REM Create a directory for native build
if not exist build\native-build mkdir build\native-build

REM Build classpath
set CLASSPATH=build\libs\pkl_example-1.0-SNAPSHOT.jar
for /R build\dependencies %%i in (*.jar) do (
    call set CLASSPATH=%%CLASSPATH%%;%%i
)

echo Building native image...
call "%GRAALVM_HOME%\bin\native-image.cmd" ^
  --no-fallback ^
  --initialize-at-build-time=org.pkl.core ^
  -H:+ReportExceptionStackTraces ^
  -H:ReflectionConfigurationFiles=reflection-config.json ^
  -H:ResourceConfigurationFiles=resource-config.json ^
  --enable-url-protocols=file ^
  -cp "%CLASSPATH%" ^
  -o build\native-build\pkl-example.exe ^
  com.example.pkl.PklExample

if %ERRORLEVEL% EQU 0 (
    echo Native image built at build\native-build\pkl-example.exe
) else (
    echo Failed to build native image
    exit /b 1
) 