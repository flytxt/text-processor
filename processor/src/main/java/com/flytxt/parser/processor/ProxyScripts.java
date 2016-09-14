package com.flytxt.parser.processor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.flytxt.parser.instrumentation.InstrumentationAgent;
import com.flytxt.parser.marker.LineProcessor;

import lombok.Data;
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="compiler")
@Data
public class ProxyScripts {
	public String getScript;
	public String getJar;
	public String remoteHost;
	public String hostName;
	
	public LineProcessor[] getLPInstance() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
//		URL url = new URL(remoteHost+getJar+"?host="+hostName); 
		URL url = new URL("file:///tmp/jar/demo/demo.jar"); 
		URLClassLoader loader = new URLClassLoader(new URL[] { url });
        return null;
	}
	
	public String[] getScipts(){
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(remoteHost+getScript+"?host="+hostName, String.class);
	    return result.split(",");
	}
}