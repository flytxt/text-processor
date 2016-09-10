package com.flytxt.utils.parser.p;

import java.io.File;
import java.util.HashMap;

public class LineParser extends ParserUtils {
	private HashMap<String, String> map = new HashMap<String, String>();
	
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
					lineCode.append("splitAndGetMarkers( data, new byte[]").
					append(toHex(getDelim(tt[i]))).
					append(");");
					i++;
				}else if(tt[i+1].startsWith("element")){
					lineCode.append("splitAndGetMarker( data, new byte[]").
					append(toHex(getDelim(tt[i]))).
					append(",").append(getValue(tt[i+1])).
					append(");");
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
	
	private String toHex(String s){
		char[] tt = s.replaceAll("'", "").trim().toCharArray();
		byte[] h = {0x1,0x2};
		StringBuilder v = new StringBuilder("{");
		for(char c: tt){
			v.append(String.format("0x%02X", (int)c)).append(',');
		}
		v.deleteCharAt(v.length()-1);
		v.append("}");
		return v.toString();
	}	
	public boolean check(String str) {
		// TODO Auto-generated method stub
		return true;
	}
	public void done(){
		System.out.println(code.toString());
	}
	public String getCode(){
		return code.toString();
	}
}
