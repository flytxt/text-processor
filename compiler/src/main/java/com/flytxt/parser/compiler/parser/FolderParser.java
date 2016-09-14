package com.flytxt.parser.compiler.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderParser extends ParserUtils {
	private String inputFolder;
	private String inputFileFilter;
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
		String [] tt = line.split("->");
		if(line.startsWith("inputFolder")){
			inputFolder = "public String getFolder(){ return"
			+ getValue(tt[0]) +";}\n";
		}
		if(tt.length >= 2){
			inputFileFilter = "public String getFilter(){ return "
					+ getValue(tt[1]) +";}\n";
		}else{
			inputFileFilter = "public String getFilter(){ return null ;}\n";
		}
		
	}
	public void done(){
		logger.debug(code.toString());
	}
	public String getFileFilter() {
		return inputFileFilter;
	}

}
