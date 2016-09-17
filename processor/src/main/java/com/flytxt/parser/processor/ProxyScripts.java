package com.flytxt.parser.processor;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
	
	public LineProcessor[] getLPInstance() throws Exception{
//		String[] scriptNames =getScipts();
//		URL url = new URL(remoteHost+getJar+"?host="+hostName); 
		URL url = new URL("file:///tmp/jar/demo/demo.jar"); 
		try(URLClassLoader loader = new URLClassLoader(new URL[] { url })){
			LineProcessor[] lpA = new LineProcessor[1];
			//TODO load classes from scriptName, now its hard coded
			@SuppressWarnings("unchecked")
			Class<LineProcessor> loadClass = (Class<LineProcessor>) loader.loadClass("com.flytxt.utils.parser.Script");
			lpA[0] = loadClass.newInstance();
			return lpA;
		}catch (Exception e) {
			throw e;
		}
	}
	
	public String[] getScipts(){
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(remoteHost+getScript+"?host="+hostName, String.class);
	    return result.split(",");
	}
}