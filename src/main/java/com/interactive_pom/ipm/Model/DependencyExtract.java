package com.interactive_pom.ipm.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DependencyExtract {
    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String scope;
    private String classifier;
    private boolean optional;
    private List<DependencyExtract> children;
    private List<Exclusions> exclusions;
    private boolean hasPropertyVersion = false;
}

