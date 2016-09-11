package com.flytxt.parser.processor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.web.client.RestTemplate;

public class ProxyScripts {
	final String remoteCompiler = System.getenv("remoteCompiler");
	public Object getInstanceFor(String script) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		URL url = new URL(remoteCompiler+"/"+InetAddress.getLocalHost().getHostName()); 
		URLClassLoader loader = new URLClassLoader(new URL[] { url });
		 Class c = loader.loadClass (script);
	      Object o = c.newInstance();
	      return o;
	}
	
	public String[] getScipts(){
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(remoteCompiler, String.class);
	    return result.split(",");
	}
}
