package com.example.pkl;

import org.pkl.core.*;
import org.pkl.core.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class PklExample {

    public static void main(String[] args) {
        System.out.println("PKL Example Application");
        System.out.println("======================\n");

        // Example 1: Evaluate PKL text directly
        evaluateText();
        
        // Example 2: Evaluate PKL from a file
        evaluateFile();
        
        // Example 3: Manually convert PKL to JSON-like format
        convertToJsonLike();
    }

    private static void evaluateText() {
        System.out.println("Example 1: Evaluating PKL text");
        System.out.println("-----------------------------");
        
        try (var evaluator = Evaluator.preconfigured()) {
            // Evaluate a simple PKL text snippet
            var moduleSource = ModuleSource.text(
                "person { name = \"Alice\"; age = 30; hobbies = List(\"reading\", \"hiking\", \"coding\") }"
            );
            
            var module = evaluator.evaluate(moduleSource);
            
            // Access the module's content
            var person = (PObject) module.get("person");
            var name = (String) person.get("name");
            var age = (Long) person.get("age");
            @SuppressWarnings("unchecked")
            var hobbies = (List<String>) person.get("hobbies");
            
            System.out.println("Parsed person object:");
            System.out.println("- Class: " + person.getClassInfo().getQualifiedName());
            System.out.println("- Name: " + name);
            System.out.println("- Age: " + age);
            System.out.println("- Hobbies: " + hobbies);
            System.out.println();
        }
    }
    
    private static void evaluateFile() {
        System.out.println("Example 2: Evaluating PKL from file");
        System.out.println("--------------------------------");
        
        try (var evaluator = Evaluator.preconfigured()) {
            // Get the config file
            Path tempConfigFile = extractResourceToTempFile("config.pkl");
            var moduleSource = ModuleSource.path(tempConfigFile);
            
            var module = evaluator.evaluate(moduleSource);
            
            // Access the application configuration
            var app = (PObject) module.get("application");
            var name = (String) app.get("name");
            var version = (String) app.get("version");
            
            // Access nested objects
            var database = (PObject) app.get("database");
            var server = (PObject) app.get("server");
            
            // Get individual feature flags
            var feature1 = (String) app.get("feature1");
            var feature2 = (String) app.get("feature2");
            var feature3 = (String) app.get("feature3");
            
            // Print the configuration
            System.out.println("Application Configuration:");
            System.out.println("- Name: " + name);
            System.out.println("- Version: " + version);
            
            System.out.println("\nDatabase Configuration:");
            System.out.println("- Host: " + database.get("host"));
            System.out.println("- Port: " + database.get("port"));
            System.out.println("- Username: " + database.get("username"));
            
            System.out.println("\nServer Configuration:");
            System.out.println("- Host: " + server.get("host"));
            System.out.println("- Port: " + server.get("port"));
            
            System.out.println("\nFeatures:");
            System.out.println("- " + feature1);
            System.out.println("- " + feature2);
            System.out.println("- " + feature3);
            
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error reading PKL file: " + e.getMessage());
        }
    }
    
    private static void convertToJsonLike() {
        System.out.println("Example 3: Converting PKL to JSON-like format");
        System.out.println("------------------------------------------");
        
        try (var evaluator = Evaluator.preconfigured()) {
            // Get the config file
            Path tempConfigFile = extractResourceToTempFile("config.pkl");
            var moduleSource = ModuleSource.path(tempConfigFile);
            
            var module = evaluator.evaluate(moduleSource);
            
            // Access the application configuration
            var app = (PObject) module.get("application");
            
            // Manually convert to JSON-like format
            String jsonLike = convertPObjectToJsonString(app, 0);
            
            System.out.println("JSON-like Output:");
            System.out.println(jsonLike);
        } catch (Exception e) {
            System.err.println("Error converting PKL to JSON-like format: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to extract a resource to a temporary file that can be read by the PKL evaluator
     */
    private static Path extractResourceToTempFile(String resourceName) throws IOException {
        ClassLoader classLoader = PklExample.class.getClassLoader();
        URL resource = classLoader.getResource(resourceName);
        
        if (resource == null) {
            throw new IOException("Resource not found: " + resourceName);
        }
        
        Path tempFile = Files.createTempFile("pkl-", "-" + resourceName);
        tempFile.toFile().deleteOnExit();
        
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return tempFile;
    }
    
    private static String convertPObjectToJsonString(PObject obj, int indent) {
        StringBuilder json = new StringBuilder();
        String indentStr = " ".repeat(indent);
        String indentNextStr = " ".repeat(indent + 2);
        
        json.append("{\n");
        
        boolean first = true;
        for (String key : obj.getProperties().keySet()) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            json.append(indentNextStr).append("\"").append(key).append("\": ");
            
            Object value = obj.get(key);
            if (value instanceof PObject) {
                json.append(convertPObjectToJsonString((PObject) value, indent + 2));
            } else if (value instanceof List) {
                json.append(convertListToJsonString((List<?>) value, indent + 2));
            } else if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
        }
        
        json.append("\n").append(indentStr).append("}");
        return json.toString();
    }
    
    private static String convertListToJsonString(List<?> list, int indent) {
        StringBuilder json = new StringBuilder();
        String indentStr = " ".repeat(indent);
        
        json.append("[\n");
        
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            json.append(indentStr);
            
            if (item instanceof PObject) {
                json.append(convertPObjectToJsonString((PObject) item, indent + 2));
            } else if (item instanceof List) {
                json.append(convertListToJsonString((List<?>) item, indent + 2));
            } else if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else {
                json.append(item);
            }
        }
        
        json.append("\n").append(" ".repeat(indent - 2)).append("]");
        return json.toString();
    }
} 