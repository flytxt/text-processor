package com.flytxt.parser.compiler.parser;

import java.util.HashMap;
import java.util.Iterator;

public class LineParser extends ParserUtils {
	private HashMap<String, String> tokenMemVarMap = new HashMap<String, String>();
	private HashMap<String, String> tokenFucVarMap = new HashMap<String, String>();

	public LineParser(){
		code.append("line.index = 0;\nline.length = lineSize;\n");
	}
	public  void process(String line){
		StringBuilder lineCode = new StringBuilder();
		String [] res = line.replace(" ","").split("=>");
		String[] tt = res[0].split("->");
		lineCode.append(tt[0]).append('.');
		for(int i = 1; i < tt.length; i++){
			if(tt[i].startsWith("element")){
				lineCode.append("get(").append(getValue(tt[i])).append(")");
				if(i == tt.length-1){
					lineCode.append(';');
				}
				else{
					lineCode.append('.');
				}
			}
			if(tt[i].startsWith("split")){
				if(i == tt.length-1){
					lineCode.append("splitAndGetMarkers( data, ").
					append(getFunVar(getDelim(tt[i]))).
					append(", mf);");
					i++;
				}else if(tt[i+1].startsWith("element")){
					lineCode.append("splitAndGetMarker( data, ").
					append(getFunVar(getDelim(tt[i]))).
					append(",").append(getValue(tt[i+1])).
					append(", mf);");
					i++;
				}else{
					throw new RuntimeException("could not parse line:"+line);
				}
			}
		}
		if(res.length == 2){
			if(tt[tt.length-1].startsWith("element")){
				 lineCode.insert(0, "Marker " + res[1] + " = ");
			}
			else lineCode.insert(0, "ArrayList<Marker> "+res[1]+" = ");
		}
		lineCode.append(System.getProperty("line.separator"));
		code.append(lineCode.toString());
	}
	
	private String getFunVar(String s){
		String token = s.replaceAll("'", "").trim();
		String str =tokenFucVarMap.get(token);
		if(str == null){
			char[] tt = token.toCharArray();
			StringBuilder v = new StringBuilder("{");
			StringBuilder varName = new StringBuilder("token");
			for(char c: tt){
				String fmtName= String.format("0x%02X", (int)c); 
				v.append(fmtName).append(',');
				varName.append(fmtName);
			}
			v.deleteCharAt(v.length()-1);
			v.append("}");
			tokenMemVarMap.put(token, "private final byte[] "+varName.toString()+" = new byte[]" + v.toString() +";");
			tokenFucVarMap.put(token, varName.toString());
			str = varName.toString();
		}
		return str;
	}	
	public boolean check(String str) {
		// TODO Auto-generated method stub
		return true;
	}
	public void done(){
	}

	public String getCode(){
		return code.toString();
	}
	
	public String getMemberVar(){
		StringBuilder sb = new StringBuilder();
		sb.append("private final Marker line = new Marker();\n");
		sb.append("private String currentFileName;\n");
		for (Iterator<String> iterator = tokenMemVarMap.values().iterator(); iterator.hasNext();) {
			sb.append( iterator.next()).append("\n");
		}
		return sb.toString();
	}
}