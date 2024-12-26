package com.interactive_pom.ipm.Service;

import com.interactive_pom.ipm.Model.Plugins;
import org.apache.maven.api.model.Model;

import java.util.List;
import java.util.Map;

public interface ExtractPlugins {
    List<Plugins> extractPlugins(Model model, Map<String, String> properties);
}
