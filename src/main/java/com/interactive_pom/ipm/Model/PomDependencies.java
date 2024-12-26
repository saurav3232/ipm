package com.interactive_pom.ipm.Model;

import lombok.Data;

import java.util.List;

@Data
public class PomDependencies {
    DependencyExtract dependencyExtract;
    List<Plugins> pluginList;
    DependencyManagementPom dependencyManagement;
}
