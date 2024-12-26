package com.interactive_pom.ipm.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plugins extends DependencyExtract {
    List<DependencyExtract> dependencies;
    boolean hasPluginVersionProperty = false;
}
