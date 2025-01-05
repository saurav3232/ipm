package com.interactive_pom.ipm.Controller;

import com.interactive_pom.ipm.Model.PomDependencies;
import com.interactive_pom.ipm.Service.impl.DependencyExtractorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExtractDependenciesController {

    private final DependencyExtractorServiceImpl dependencyExtractorService;

    @PostMapping("/extract-pom-dependencies")
    public ResponseEntity<?> extractPomDependencies(@RequestParam("pomFileName") String pomFileName) {
        try {

            // Define the directory where the file will be saved
            Path targetLocation = Paths.get("uploaded-files").resolve(pomFileName);

            File outputDirectory = new File("output-files");
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            String outputFileName = pomFileName.replace("pom.xml", "dependency-tree.json");
            File outputFile = new File(outputDirectory, outputFileName);


            // Pass the path of the new file to the service
            PomDependencies pomDependencies = dependencyExtractorService.extractAllDependencies(targetLocation.toAbsolutePath().toString(), outputFile.getAbsolutePath());

            return ResponseEntity.ok(pomDependencies);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error occurred while retrieving the file:" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while extracting dependencies: " + e.getMessage());
        }
    }

}
