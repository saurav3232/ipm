package com.interactive_pom.ipm.Service;

import com.interactive_pom.ipm.Model.DependencyManagementPom;
import org.apache.maven.api.model.Model;

import java.util.Map;

public interface ExtractDependencyManagement {

    DependencyManagementPom extractDependencyManagement(Model model, Map<String, String> properties);
}
