package com.interactive_pom.ipm.Controller;

import com.interactive_pom.ipm.Service.DependencyExtractorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExtractDependenciesController {

    private final DependencyExtractorServiceImpl dependencyExtractorService;

    @PostMapping("/extract-pom-dependencies")
    public ResponseEntity<String> extractPomDependencies(@RequestParam("file") MultipartFile file) {
        try {
            // Get the original file name and generate a unique name for the new file
            String originalFileName = file.getOriginalFilename() == null? "pom.xml": file.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + originalFileName;

            // Define the directory where the file will be saved
            Path targetLocation = Paths.get("uploaded-files").resolve(newFileName);

            // Create the directory if it doesn't exist
            File directory = new File("uploaded-files");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputDirectory = new File("output-trees");
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            String outputFileName = newFileName.replace(originalFileName, "dependency-tree.json");
            File outputFile = new File(outputDirectory, outputFileName);

            // Write the file to the server's file system
            Files.copy(file.getInputStream(), targetLocation);

            // Pass the path of the new file to the service
            dependencyExtractorService.extractAllDependencies(targetLocation.toAbsolutePath().toString(), outputFile.getAbsolutePath());

            return ResponseEntity.ok("Dependencies extracted successfully from " + newFileName);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error occurred while storing the file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while extracting dependencies: " + e.getMessage());
        }
    }

}
