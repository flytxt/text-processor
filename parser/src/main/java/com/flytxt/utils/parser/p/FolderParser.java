package com.flytxt.utils.parser.p;

public class FolderParser extends ParserUtils {
	private String inputFolder;
	private String doneAction;
	
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
			System.out.println("error @: "+line);
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
		System.out.println(code.toString());
	}

}
