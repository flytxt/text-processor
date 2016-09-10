package com.flytxt.utils.parser.marker;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;
import com.flytxt.utils.parser.TokenFactory;

public class MarkerSplitAndGetTest {

	@Test
	public void test() {
		String strb = "a,bb,c,d"; 
		byte[] b = strb.getBytes();
		int get = 1;
		int javaIndex = get -1;
		
		String str = ",";
		Marker line = MarkerFactory.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		Marker ms = line.splitAndGetMarker(b, token,get);
		String splits[] = strb.split(str);
		//for user index starts @ 1
		if(!splits[javaIndex].equals(ms.toString(b))){
			assertEquals(splits[javaIndex],ms.toString(b));
		}
	}
	@Test
	public void test1() {
		String strb = "c,d"; 
		byte[] b = strb.getBytes();
		String str = ",";
		String splits[] = strb.split(str);
		int get = splits.length;
		int javaIndex = get -1;
		
		
		Marker line = MarkerFactory.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		Marker ms = line.splitAndGetMarker(b, token,get);
		
		//for user index starts @ 1
		if(!splits[javaIndex].equals(ms.toString(b))){
			assertEquals(splits[javaIndex],ms.toString(b));
		}
	}
	@Test
	public void test2() {
		String strb = ",c,d"; 
		byte[] b = strb.getBytes();
		String str = ",";
		String splits[] = strb.split(str);
		int get = 1;
		int javaIndex = get -1;
		
		
		Marker line = MarkerFactory.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		Marker ms = line.splitAndGetMarker(b, token,get);
		
		//for user index starts @ 1
		if(!splits[javaIndex].equals(ms.toString(b))){
			assertEquals(splits[javaIndex],ms.toString(b));
		}
	}
	@Test
	public void test3() {
		String strb = ",c,d,"; 
		byte[] b = strb.getBytes();
		String str = ",";
		String splits[] = strb.split(str);
		int get = splits.length;
		int javaIndex = get -1;
		
		
		Marker line = MarkerFactory.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		Marker ms = line.splitAndGetMarker(b, token,get);
		
		//for user index starts @ 1
		if(!splits[javaIndex].equals(ms.toString(b))){
			assertEquals(splits[javaIndex],ms.toString(b));
		}
	}
}
