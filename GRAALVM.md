# GraalVM Setup Instructions

This document provides instructions for setting up GraalVM and building the native image for the PKL Example application.

## Prerequisites

- Java 17 or higher
- GraalVM Community Edition or Enterprise Edition

## Installing GraalVM

### macOS / Linux

1. Download the latest GraalVM from [GraalVM Releases](https://github.com/graalvm/graalvm-ce-builds/releases) for your platform
2. Extract the archive to a location of your choice (e.g., `/opt/graalvm`)
3. Set the `GRAALVM_HOME` environment variable to the GraalVM installation directory:
   ```bash
   export GRAALVM_HOME=/path/to/graalvm
   export PATH=$GRAALVM_HOME/bin:$PATH
   ```
4. Install the Native Image component:
   ```bash
   $GRAALVM_HOME/bin/gu install native-image
   ```

### Windows

1. Download the latest GraalVM from [GraalVM Releases](https://github.com/graalvm/graalvm-ce-builds/releases) for Windows
2. Extract the archive to a location of your choice (e.g., `C:\Program Files\GraalVM`)
3. Set the `GRAALVM_HOME` environment variable to the GraalVM installation directory:
   - Open System Properties → Advanced → Environment Variables
   - Add a new system variable:
     - Name: `GRAALVM_HOME`
     - Value: `C:\path\to\graalvm`
   - Add `%GRAALVM_HOME%\bin` to your PATH variable
4. Install the Native Image component:
   ```
   %GRAALVM_HOME%\bin\gu.cmd install native-image
   ```
5. Install Visual Studio Build Tools:
   - Download and install the [Visual Studio Build Tools](https://visualstudio.microsoft.com/visual-cpp-build-tools/)
   - Select "C++ build tools" during installation
6. Run the Native Image build in a Developer Command Prompt:
   - Start → Visual Studio 2019 → Developer Command Prompt
   - Navigate to the project directory
   - Run `build-native.bat`

## Building a Native Image

### Using Gradle (Recommended)

The project is configured with the GraalVM plugin for Gradle, which makes it easy to build a native image:

```bash
# Build native image using Gradle
./gradlew nativeCompile

# Run the native executable
./build/native/nativeCompile/pkl-example
```

### Using Manual Scripts

For more control over the native image build process, you can use the provided scripts:

#### Linux/macOS
```bash
# Make the script executable
chmod +x build-native.sh

# Run the build script
./build-native.sh

# Run the generated executable
./build/native-build/pkl-example
```

#### Windows
```
REM Run the build script in a Developer Command Prompt
build-native.bat

REM Run the generated executable
build\native-build\pkl-example.exe
```

## Troubleshooting

1. **Missing Classes**: If the native image compilation fails due to missing classes, you may need to add more entries to the `reflection-config.json` file.

2. **Resource Not Found**: If resources cannot be found at runtime, ensure they are included in `resource-config.json`.

3. **Native Image Tool Not Found**: Make sure you have installed the Native Image component with `gu install native-image`.

4. **Windows-specific Issues**:
   - Ensure you're using a Developer Command Prompt for building on Windows
   - Make sure the path to your GraalVM installation doesn't contain spaces

5. **Debug Native Image Build**: Add the `-H:+PrintAnalysisCallTree` option to the native-image command to see more information about the compilation process.

## Learn More

- [GraalVM Native Image Documentation](https://www.graalvm.org/reference-manual/native-image/)
- [GraalVM Build Tools for Gradle](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html) 