package com.flytxt.utils.parser.p;

import java.util.HashSet;
import java.util.Set;

public class StorageParser extends ParserUtils {
	
	private Set<String> stores = new HashSet<String>();
	private StringBuilder memberVar = new StringBuilder();
	public  void process(String line){
		String[] tt = line.split("->");
		String name = getStoreName(tt[1]);
		if(stores.add(name+"Store")){
			String str = "private Store "+ name +"Store = new Store(" +getValue(tt[1]).replaceAll("'", "\"") +");";
			memberVar.append(str).append(System.lineSeparator());
		}
		String str = tt[0].replace("]", "").trim();
		String vars = str.substring(1, str.length());
		code.append( name +"Store.save( data, "+ appendMarkers(vars)+");").append(System.lineSeparator());
	}
	private String appendMarkers(String vars){
		String[] tt = vars.split(",");
		StringBuilder sb = new StringBuilder();
		for(String att: tt){
			sb.append(att).append(',');
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	public String getDoneCode(){
		StringBuilder sb = new StringBuilder();
		for(String aStore: stores){
			sb.append("\n");
			sb.append(aStore+".done();\n");
		}
		return sb.toString();
	}

	 private String  getStoreName(String s){
		 String []tt = s.split("'")[1].split("/");
		 String str = tt[tt.length-1];
		 int index = str.indexOf('.');
		 return index > -1? str.substring(0, index):str;
		}
	
	public boolean check(String line) {
		if(line.startsWith("[")){
			return true;
		}else{
			return false;
		}
	}
	public void done(){
		System.out.println(code.toString());
		System.out.println(memberVar.toString());
	}
	public String getMembers(){
		return memberVar.toString();
	}
	
	public String getCode(){
		return code.toString();
	}
}
