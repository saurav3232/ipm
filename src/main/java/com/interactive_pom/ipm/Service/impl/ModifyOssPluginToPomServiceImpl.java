package com.interactive_pom.ipm.Service.impl;

import com.interactive_pom.ipm.Service.ModifyOSSPlugin;
import com.interactive_pom.ipm.Utils.PomCleanserService;
import lombok.RequiredArgsConstructor;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Plugin;
import org.apache.maven.api.xml.XmlNode;
import org.apache.maven.internal.xml.XmlNodeBuilder;
import org.apache.maven.model.v4.MavenStaxReader;
import org.apache.maven.model.v4.MavenStaxWriter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ModifyOssPluginToPomServiceImpl implements ModifyOSSPlugin {

    private static final String ossConfigurationXml = "<configuration><fail>false</fail><transitive>true</transitive><reportFile>../vulnerability-reports/${project.artifactId}-report.json</reportFile></configuration>";

    private final PomCleanserService pomCleanserService;

    @Override
    public void addOssPluginConfig(Model model, File pomFile) throws XMLStreamException, IOException, XmlPullParserException {

        List<Plugin> newPluginList = new ArrayList<>(model.getBuild().getPlugins());
        newPluginList.add(Plugin.newBuilder().artifactId("ossindex-maven-plugin").groupId("org.sonatype.ossindex.maven").configuration(createConfigurationNode()).build());
        Model modifiedModel = model.withBuild(model.getBuild().withPlugins(newPluginList));

        try (FileWriter writer = new FileWriter(pomFile)) {
            MavenStaxWriter mavenWriter = new MavenStaxWriter();
            mavenWriter.write(writer, modifiedModel);
        }

        pomCleanserService.cleanPom(pomFile);

        System.out.println("OSS plugin added successfully and pom.xml updated.");
    }

    @Override
    public void removeOssPluginConfig(Model model, File pomFile) throws IOException {
        List<Plugin> newPluginList = new ArrayList<>(model.getBuild().getPlugins());
        newPluginList.removeIf(plugin -> plugin.getArtifactId().equals("ossindex-maven-plugin"));
        Model modifiedModel = model.withBuild(model.getBuild().withPlugins(newPluginList));

        try (FileWriter writer = new FileWriter(pomFile)) {
            MavenStaxWriter mavenWriter = new MavenStaxWriter();
            mavenWriter.write(writer, modifiedModel);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

        pomCleanserService.cleanPom(pomFile);
        System.out.println("OSS plugin removed successfully from pom.xml");
    }

    private XmlNode createConfigurationNode() throws IOException, XmlPullParserException {
        return XmlNodeBuilder.build(new StringReader(ossConfigurationXml));
    }
}
