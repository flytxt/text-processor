package com.flytxt.parser.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "loc")
@Data
public class LocationSettings {

    public String jarHome = "/tmp/jar/";

    public String scriptHome = "/tmp/scripts/";

    public String javaHome = "/tmp/java/";

    public String classHome = "/tmp/class/";

    @PostConstruct
    public void init() throws IOException {
        createDir(jarHome);
        createDir(scriptHome);
        createDir(javaHome);
        createDir(classHome);
    }

    private Path createDir(final String loc) throws IOException {
        final Path folder = Paths.get(loc);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        return folder;
    }

    public String getScriptDumpLoc(final String host) {
        return scriptHome.concat(host).concat("/");
    }

    public String getScriptURI(final String host, final String scriptName) {
        return getScriptDumpLoc(host).concat(scriptName).concat("/");
    }

    public String getJavaDumpLoc(final String host) {
        return javaHome + host + "/com/flytxt/utils/parser/";
    }

    public String getClassDumpLoc(final String host) {
        return classHome + host + "/";
    }

    public String getJarDumpLocatiom(final String host) {
        return jarHome + host + "/";
    }
}
