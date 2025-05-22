package com.example.pkl;

import org.pkl.core.Evaluator;
import org.pkl.core.ModuleSource;
import org.pkl.core.PModule;
import org.pkl.core.PObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * A simplified example of using PKL that avoids problematic collection classes
 * that cause issues when building a native image.
 */
public class SimplePklExample {

    public static void main(String[] args) {
        System.out.println("Simple PKL Example");
        System.out.println("=================\n");

        // Example 1: Evaluate PKL text directly
        evaluateText();
        
        // Example 2: Evaluate PKL from a file
        evaluateFile();
    }

    private static void evaluateText() {
        System.out.println("Example 1: Evaluating PKL text");
        System.out.println("-----------------------------");
        
        try (var evaluator = Evaluator.preconfigured()) {
            // Evaluate a simple PKL text snippet
            var moduleSource = ModuleSource.text(
                "person { name = \"Alice\"; age = 30; }"
            );
            
            var module = evaluator.evaluate(moduleSource);
            
            // Access the module's content
            var person = (PObject) module.get("person");
            var name = (String) person.get("name");
            var age = (Long) person.get("age");
            
            System.out.println("Parsed person object:");
            System.out.println("- Class: " + person.getClassInfo().getQualifiedName());
            System.out.println("- Name: " + name);
            System.out.println("- Age: " + age);
            System.out.println();
        }
    }
    
    private static void evaluateFile() {
        System.out.println("Example 2: Evaluating PKL from file");
        System.out.println("--------------------------------");
        
        try (var evaluator = Evaluator.preconfigured()) {
            // Create a simple config file in memory
            String pklContent = "application {\n" +
                "  name = \"MyApp\"\n" +
                "  version = \"1.0.0\"\n" +
                "  database {\n" +
                "    host = \"localhost\"\n" +
                "    port = 5432\n" +
                "  }\n" +
                "}";
            
            // Evaluate the PKL content directly
            var moduleSource = ModuleSource.text(pklContent);
            var module = evaluator.evaluate(moduleSource);
            
            // Access the application configuration
            var app = (PObject) module.get("application");
            var name = (String) app.get("name");
            var version = (String) app.get("version");
            
            // Access nested objects
            var database = (PObject) app.get("database");
            
            // Print the configuration
            System.out.println("Application Configuration:");
            System.out.println("- Name: " + name);
            System.out.println("- Version: " + version);
            
            System.out.println("\nDatabase Configuration:");
            System.out.println("- Host: " + database.get("host"));
            System.out.println("- Port: " + database.get("port"));
            
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error reading PKL: " + e.getMessage());
        }
    }
} 