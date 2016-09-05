package com.flytxt.utils.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;


public class TokenTester {
	@Test
	public void driver(){
		//testSingleDelim("sample,program,", ",");
		//testSingleDelim("sample,program,", "+");
		testSingleDelim("sample++program,", "++");
	}
	private void testSingleDelim(String str, String del){
		byte[] data = str.getBytes();
		Token t = new Token(',');
		for(int i =0; i <data.length; i++)
			t.check(data[i], i);
		int[] pos = t.getLocation();
		int count=0;
		for (int index = str.indexOf(del);
			     index >= 0;
			     index = str.indexOf(del, index + 1))
			{
			    if(pos[count++] != index){
			    	assertEquals(index, pos[count++] );
			    }
			}
	}
}
