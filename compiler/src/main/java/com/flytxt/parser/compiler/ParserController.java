package com.flytxt.parser.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class ParserController {

	@Autowired
	private LocationSettings loc;
	
	@Autowired
	private Utils utils;
	
	@RequestMapping(path= "/", method = RequestMethod.GET)
	public @ResponseBody String getScripts(@RequestParam("host") String host){
		return "Script.pl";
	}

	@RequestMapping(path= "/submit", method = RequestMethod.GET)
	public @ResponseBody String submitScript(
			@RequestParam("host") String host,
			@RequestParam("script") String script,
			@RequestParam("scriptName") String scriptName
			){

		String scriptLoc= loc.scriptHame+host;
		utils.createFile(scriptLoc, script, scriptName);
		String javaContent = utils.createJavaContent(scriptLoc+"/"+scriptName);
		String srcFolder = loc.javaHame+host+"/com/flytxt/utils/parser";
		String javaFile = utils.createFile(srcFolder, javaContent, scriptName.replaceAll(".pl", ".java"));
		utils.complie(javaFile, loc.classHame+host+"/");
		try {
			utils.createJar(loc.classHame+host, loc.jarHome+host+"/"+host+".jar");
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}
	
	
	@RequestMapping(
			path= "/getJar", 
			method = RequestMethod.GET,
			produces ={"application/java-archive"})
	public @ResponseBody ResponseEntity<InputStreamResource> getJar(@RequestParam("host") String host){

		File jar = new File(loc.jarHome+host+"/"+host+".jar");
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename="+host+".jar");
	    
	    try {
			return ResponseEntity
			        .ok()
			        .headers(headers)
			        .contentLength(jar.length())
			        .contentType(MediaType.parseMediaType("application/octet-stream"))
			        .body(new InputStreamResource(new FileInputStream(jar)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;

	}

}