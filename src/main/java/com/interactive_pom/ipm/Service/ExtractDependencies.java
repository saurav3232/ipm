package com.interactive_pom.ipm.Service;

import com.interactive_pom.ipm.Model.DependencyExtract;

import java.util.Map;

public interface ExtractDependencies {
    DependencyExtract extractDependencies(String pomPath, String outputPath, Map<String, String> properties);
}
