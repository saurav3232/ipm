package com.interactive_pom.ipm.Service.impl;

import com.interactive_pom.ipm.Model.DependencyExtract;
import com.interactive_pom.ipm.Model.DependencyManagementPom;
import com.interactive_pom.ipm.Service.ExtractDependencyManagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.DependencyManagement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ExtractDependencyManagementImpl implements ExtractDependencyManagement {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DependencyManagementPom extractDependencyManagement(Model model, Map<String, String> properties) {

        DependencyManagement dependencyManagement = getDependencyManagementFromPomFile(model);
        DependencyManagementPom dependencyManagementPom = new DependencyManagementPom();
        if(Objects.isNull(dependencyManagement)){
            return dependencyManagementPom;
        }
        dependencyManagementPom.setDependencies(dependencyManagement.getDependencies().stream().map(dependency -> objectMapper.convertValue(dependency, DependencyExtract.class)).toList());

        if (Objects.nonNull(dependencyManagementPom.getDependencies())) {
            setVersionFromPropertyPom(properties, dependencyManagementPom.getDependencies());
        }
        return dependencyManagementPom;
    }

    private DependencyManagement getDependencyManagementFromPomFile(Model model) {
        return model.getDependencyManagement();
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
