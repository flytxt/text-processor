package com.flytxt.utils.parser;

public class Tests {
	public void test1(){
		Token[] tokens = TokenFactory.create(",","*","|");
		FileReader reader = new FileReader(tokens, "file.txt");
		Dictionary d = reader.readLine();
	}
}
