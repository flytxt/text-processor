package com.flytxt.utils.parser.p;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;

public  class Parser {
	
	protected String[] keywords = {"folder","line","split", "element","currentFile"};
	
	private LineParser lp = new LineParser();
	private FolderParser fp = new FolderParser();
	private StorageParser sp = new StorageParser();
	private String processorName ="";
	
	public void setName(String n){
		this.processorName = n;
	}
	
	public void checkSyntax(String line){
		String [] res = line.replace("\\s+","").split("=>");	
	}
	public void process(String sCurrentLine) {
		if(fp.check(sCurrentLine))
			fp.process(sCurrentLine);
		else if (sp.check(sCurrentLine))
			sp.process(sCurrentLine);
		else 
			lp.process(sCurrentLine);
	}

	public void done() {
		fp.done();
		lp.done();
		sp.done();
		
		System.out.println(createProcessClass());
	}
	public String createProcessClass() {
		return 
				"package com.flytxt.utils.parser;\n"
				+"import java.util.ArrayList;\n"
				+ "import com.flytxt.utils.processor.Store;\n"
				+ "import com.flytxt.utils.parser.Marker;\n"
				+ "import java.io.IOException;\n"
		+ "public  class "+ processorName+" implements com.flytxt.utils.processor.LineProcessor{\n"
		+ sp.getMembers() +"\n"
		+ lp.getMemberVar()
		+ fp.getInput()
		+ "public final void done() throws IOException{" + sp.getDoneCode() +"}\n"
		+ "public final void process(byte[] data, int lineSize, MarkerFactory mf) throws IOException{\n"
		+ lp.getCode() +"\n"
		+ sp.getCode()+"\n"
		+ "}"
		+ "}";
		
	}
}