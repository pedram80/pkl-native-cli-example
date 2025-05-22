# PKL Example Application

A simple Java application demonstrating how to use the PKL Core library for configuration management.

## Prerequisites

- Java 17 or higher
- Gradle
- GraalVM (for native image compilation)

## Project Structure

- `src/main/java/com/example/pkl/PklExample.java`: Main application demonstrating PKL usage
- `src/main/resources/config.pkl`: Sample PKL configuration file
- `build.gradle`: Gradle build file with PKL dependencies and GraalVM configuration
- `reflection-config.json`: GraalVM reflection configuration
- `resource-config.json`: GraalVM resources configuration

## Features Demonstrated

1. **Evaluating PKL Text**: Parsing and accessing PKL content defined as a string
2. **Evaluating PKL File**: Loading and parsing a PKL configuration file
3. **Converting PKL to JSON**: Rendering PKL configuration as JSON format
4. **GraalVM Native Image**: Compiling the application to a native binary

## Running the Example (JVM mode)

```bash
# Build the project
./gradlew build

# Run the example application on JVM
./gradlew run
```

## Building Native Image

```bash
# Build native image
./gradlew nativeCompile

# The executable will be in build/native/nativeCompile/
./build/native/nativeCompile/pkl-example
```

## PKL Core Features Used

- `Evaluator`: Core component for evaluating PKL modules
- `ModuleSource`: Different ways to load PKL content (text, file)
- `PObject` and `PModule`: Java representations of PKL objects and modules
- `Resource loading`: Compatible with both JVM and GraalVM native image

## GraalVM Configuration

The project includes configurations necessary for GraalVM native image compilation:

- Reflection configuration: Allows dynamic class usage in PKL
- Resource configuration: Ensures PKL config files are included in the native binary
- URL protocol enablement: Ensures file access works in native image

## Learn More

For more information about PKL, visit the [official documentation](https://pkl-lang.org/main/current/pkl-core/index.html).

For GraalVM native image, see the [GraalVM documentation](https://www.graalvm.org/reference-manual/native-image/). 