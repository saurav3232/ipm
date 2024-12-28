package com.interactive_pom.ipm.Service.impl;

import com.interactive_pom.ipm.Model.DependencyExtract;
import com.interactive_pom.ipm.Service.ExtractDependencies;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ExtractDependenciesImpl implements ExtractDependencies {
    @Override
    public DependencyExtract extractDependencies(String pomPath, String outputPath, Map<String, String> properties) {

        System.out.println("input path: " + pomPath);
        System.out.println("output path: " + outputPath);

        ProcessBuilder processBuilder = new ProcessBuilder();
        System.out.println("OS: " + System.getProperty("os.name").toLowerCase());

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            processBuilder.command("cmd.exe", "/c",
                    "mvn", "-f", pomPath, "dependency:tree",
                    "-DoutputType=json", "-DoutputFile=" + outputPath);
        } else {
            processBuilder.command("mvn", "-f", pomPath, "dependency:tree",
                    "-DoutputType=json", "-DoutputFile=" + outputPath);
        }

        System.out.println("Command: " + processBuilder.command());


        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // Check if the process exited successfully
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Dependency tree written to: " + outputPath);
            } else {
                System.err.println("Error occurred. Exit code: " + exitCode);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            DependencyExtract dependencyExtract = objectMapper.readValue(new File(outputPath), DependencyExtract.class);

            if (Objects.nonNull(dependencyExtract) && Objects.nonNull(dependencyExtract.getChildren())) {
                setVersionFromPropertyPom(properties, dependencyExtract.getChildren());
            }
            return new DependencyExtract();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setVersionFromPropertyPom(Map<String, String> properties, List<DependencyExtract> dependencyExtractList) {

        for (DependencyExtract dependency : dependencyExtractList) {
            String version = dependency.getVersion();
            if (version != null && version.startsWith("${") && version.endsWith("}")) {

                String propertyName = version.substring(2, version.length() - 1);
                String resolvedVersion = properties.get(propertyName);


                if (resolvedVersion != null) {
                    dependency.setVersion(resolvedVersion);
                    dependency.setHasPropertyVersion(true);
                }
            }
        }
    }
}
