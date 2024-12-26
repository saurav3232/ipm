package com.interactive_pom.ipm.Model;

import lombok.Data;

import java.util.List;

@Data
public class DependencyManagementPom {
    List<DependencyExtract> dependencies;
}
