package com.flytxt.utils.parser;

public class TokenFactory {
	public static byte[] create(String string){
		return string.getBytes();
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
}
