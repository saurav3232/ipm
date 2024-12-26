package com.interactive_pom.ipm.Service;

import com.interactive_pom.ipm.Model.DependencyExtract;
import com.interactive_pom.ipm.Model.DependencyManagementPom;
import com.interactive_pom.ipm.Model.Plugins;
import com.interactive_pom.ipm.Model.PomDependencies;
import com.interactive_pom.ipm.Service.impl.ExtractDependenciesImpl;
import com.interactive_pom.ipm.Service.impl.ExtractDependencyManagementImpl;
import com.interactive_pom.ipm.Service.impl.ExtractPluginsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.maven.api.model.Model;
import org.apache.maven.model.v4.MavenStaxReader;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static com.interactive_pom.ipm.Constants.CommonConstants.INPUT_PATH;
import static com.interactive_pom.ipm.Constants.CommonConstants.OUTPUT_PATH;

@Component
@RequiredArgsConstructor
public class DependencyExtractorServiceImpl {
   
    private final ExtractDependenciesImpl extractDependenciesService;
    private final ExtractPluginsImpl extractPluginsService;
    private final ExtractDependencyManagementImpl extractDependencyManagementService;

    public void extractAllDependencies() throws FileNotFoundException, XMLStreamException {

        MavenStaxReader reader = new MavenStaxReader();
        Model model = reader.read(new FileReader(INPUT_PATH));
        Map<String, String> properties = extractProperties(model);
        DependencyExtract dependencyExtract = extractDependenciesService.extractDependencies(INPUT_PATH, OUTPUT_PATH, properties);
        List<Plugins> pluginsList = extractPluginsService.extractPlugins(model, properties);
        DependencyManagementPom dependencyManagementPom = extractDependencyManagementService.extractDependencyManagement(model, properties);

        PomDependencies pomDependencies = new PomDependencies();
        pomDependencies.setDependencyExtract(dependencyExtract);
        pomDependencies.setPluginList(pluginsList);
        pomDependencies.setDependencyManagement(dependencyManagementPom);

        printDependencies(dependencyExtract.getChildren(), "Direct and transitive dependencies");
        printPlugins(pluginsList);
        printDependencies(dependencyManagementPom.getDependencies(), "Dependency Management");
    }

    private void printDependencies(List<DependencyExtract> dependencies, String tag) {
        if (Objects.isNull(dependencies) || dependencies.isEmpty()) {
            return;
        }
        System.out.println("............................Printing Dependencies from tag: " + tag + "  ............................");
        for (DependencyExtract dependency : dependencies) {
            System.out.println("GroupId:  " + dependency.getGroupId());
            System.out.println("ArtifactId:  " + dependency.getArtifactId());
            System.out.println("Version:  " + dependency.getVersion());
            System.out.println("Scope:  " + dependency.getScope());
            System.out.println("---------------------------");
        }
    }

    private void printPlugins(List<Plugins> plugins) {
        if (Objects.isNull(plugins) || plugins.isEmpty()) {
            return;
        }
        System.out.println("............................Printing Plugins............................");
        for (Plugins plugin : plugins) {
            System.out.println("GroupId: " + plugin.getGroupId());
            System.out.println("ArtifactId: " + plugin.getArtifactId());
            System.out.println("Version: " + plugin.getVersion());
            System.out.println("---------------------------");
        }
    }

    private Map<String, String> extractProperties(Model model) {
        // Extract properties from the model
        return new HashMap<>(model.getProperties());
    }
}