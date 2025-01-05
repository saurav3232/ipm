package com.interactive_pom.ipm.Controller;

import com.interactive_pom.ipm.Model.PomDependencies;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
public class UploadPomController {

    @PostMapping("/upload-pom")
    public ResponseEntity<?> uploadPomFile(@RequestParam("file") MultipartFile file) {
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

            // Write the file to the server's file system
            Files.copy(file.getInputStream(), targetLocation);

            return ResponseEntity.ok("Pom file uploaded successfully: " + newFileName);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error occurred while storing the file: " + e.getMessage());
        }
    }
}
