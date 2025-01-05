package com.interactive_pom.ipm.Service;

import org.apache.maven.api.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public interface ModifyOSSPlugin {

    void addOssPluginConfig(Model model, File pomFile) throws XMLStreamException, IOException, ParserConfigurationException, XmlPullParserException;
    void removeOssPluginConfig(Model model, File pomFile) throws IOException;

}
