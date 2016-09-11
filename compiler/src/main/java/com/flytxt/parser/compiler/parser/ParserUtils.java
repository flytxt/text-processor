package com.flytxt.parser.compiler.parser;

public class ParserUtils {
	protected StringBuilder code = new StringBuilder();
	protected String  getDelim(String s){
		return s.substring(s.indexOf('(')+1, s.length()-1);
	}
	protected String  getValue(String s){
		return s.substring(s.indexOf('(')+1, s.length()-1);
	}
}
