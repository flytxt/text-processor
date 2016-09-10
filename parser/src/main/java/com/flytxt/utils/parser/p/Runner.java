package com.flytxt.utils.parser.p;

public class Runner {
	public static void main(String[] args){
		
		ScriptReader reader = new ScriptReader();
		reader.read("script.txt", new Parser());
	}
}
