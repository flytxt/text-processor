package com.flytxt.parser.compiler.parser;

public class ParserUtils {
	protected StringBuilder code = new StringBuilder();
	protected int maxVal =0;
	
	protected String  getDelim(String s){
		return s.substring(s.indexOf('(')+1, s.length()-1);
	}
	protected String  getValue(String s){

		String val = s.substring(s.indexOf('(')+1, s.length()-1);
		int valI = Integer.parseInt(val);
		if (valI>maxVal)
			maxVal = valI;
		return s.substring(s.indexOf('(')+1, s.length()-1);
	}
}
