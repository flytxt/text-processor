package com.flytxt.parser.compiler;

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
}
