package com.flytxt.parser.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="loc")
@Data
public class LocationSettings {
	public String jarHome = "/tmp/jar/";
	public String scriptHame = "/tmp/scripts/";
	public String javaHame = "/tmp/java/";
	public String classHame = "/tmp/class/";

	@PostConstruct
	public void init() throws IOException{
		createDir(jarHome);
		createDir(scriptHame);
		createDir(javaHame);
		createDir(classHame);
	}
	private Path createDir(String loc) throws IOException{
		Path folder = Paths.get(loc);
		if(! Files.exists(folder)){
			Files.createDirectories(folder);
		}
		return folder;
	}
	public String getScriptDumpLoc(String host) {
		return scriptHame.concat(host).concat("/");
	}

	public String getScriptURI(String host, String scriptName) {
		return getScriptDumpLoc(host)
				.concat(scriptName)
				.concat("/");
	}

	public String getJavaDumpLoc(String host) {
		return javaHame+host+"/com/flytxt/utils/parser/";
	}

	public String getClassDumpLoc(String host) {
		return classHame+host+"/";
	}

	public String getJarDumpLocatiom(String host) {
		return jarHome+host+"/";
	}
}
