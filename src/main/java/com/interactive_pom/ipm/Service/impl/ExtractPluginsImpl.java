package com.interactive_pom.ipm.Service.impl;

import com.interactive_pom.ipm.Model.DependencyExtract;
import com.interactive_pom.ipm.Model.Plugins;
import com.interactive_pom.ipm.Service.ExtractPlugins;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Plugin;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExtractPluginsImpl implements ExtractPlugins {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Plugins> extractPlugins(Model model, Map<String, String> properties) {
        List<Plugin> pluginList = getPluginDependenciesFromPomFile(model);
        if(Objects.isNull(pluginList) || pluginList.isEmpty()){
            return new ArrayList<>();
        }
        return pluginList.stream().map(plugin -> {
            Plugins pluginFromPom = objectMapper.convertValue(plugin, Plugins.class);
            if (Objects.nonNull(pluginFromPom) && Objects.nonNull(pluginFromPom.getDependencies())) {
                setPluginVersionFromProperty(properties, pluginFromPom);
                setVersionFromPropertyPom(properties, pluginFromPom.getDependencies());
            }
            return pluginFromPom;
        }).toList();
    }

    private List<Plugin> getPluginDependenciesFromPomFile(Model model) {
        if(Objects.isNull(model.getBuild())){
            return Collections.emptyList();
        }
        return model.getBuild().getPlugins();
    }

    private void setPluginVersionFromProperty(Map<String,String> property, Plugins plugin) {

        String version = plugin.getVersion();
        if (version != null && version.startsWith("${") && version.endsWith("}")) {
            // Extract property name from placeholder
            String propertyName = version.substring(2, version.length() - 1);
            String resolvedVersion = property.get(propertyName);

            // If resolved version is found, create a new Dependency with the resolved version
            if (resolvedVersion != null) {
                plugin.setVersion(resolvedVersion);
                plugin.setHasPluginVersionProperty(true);
            }
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
