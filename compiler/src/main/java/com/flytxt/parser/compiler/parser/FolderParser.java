package com.flytxt.parser.compiler.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderParser extends ParserUtils {
	private String inputFolder;
	private String doneAction;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public boolean check(String line) {
		try{
		if( line.startsWith("inputFolder") ||
			line.startsWith("folder") ||
			line.startsWith("currentFile")){
					return true;
		}else{
			return false;
		}
		}catch (ArrayIndexOutOfBoundsException e) {
			logger.debug("error @: "+line);
			throw new RuntimeException(line);
		}
	}
	public String getInput(){
		return inputFolder.replace("'", "\"");
	}
	public  void process(String line){
		if(line.startsWith("inputFolder")){
			inputFolder = "public String getFolder(){ return"
			+ getValue(line) +";}\n";
			
		}
	}
	public void done(){
		logger.debug(code.toString());
	}

}
