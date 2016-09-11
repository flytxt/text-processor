package com.flytxt.utils.parser.marker;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;
import com.flytxt.utils.parser.TokenFactory;

public class MarkerSplitTest {
	MarkerFactory mf = new MarkerFactory();
	@Test
	public void test1() {
		String strb = "a,b,c,d"; 
		byte[] b = strb.getBytes();
		
		String str = ",";
		Marker line = mf.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		ArrayList <Marker> ms = line.splitAndGetMarkers(b, token,mf);
		String splits[] = strb.split(str);
		if(splits.length != ms.size()){
			assertEquals(splits.length, ms.size());
		}
		int k = 0;
		for(Marker m: ms){
			if(!splits[k].equals(m.toString(b))){
				assertEquals(splits[k], m.toString(b));
			}
			k++;
		}
	}
	@Test
	public void test2() {
		String strb = ",a,b,c,d"; 
		byte[] b = strb.getBytes();
		
		String str = ",";
		Marker line = mf.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		ArrayList <Marker> ms = line.splitAndGetMarkers(b, token, mf);
		String splits[] = strb.split(str);
		if(splits.length != ms.size()){
			assertEquals(splits.length, ms.size());
		}
		System.out.println("length:"+ms.size());
		int k = 0;
		for(Marker m: ms){
			if(!splits[k].equals(m.toString(b))){
				assertEquals(splits[k], m.toString(b));
			}
			k++;
		}
	}
	@Test
	public void test3() {
		String strb = "a,b,c,d,"; 
		byte[] b = strb.getBytes();
		
		String str = ",";
		Marker line = mf.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		ArrayList <Marker> ms = line.splitAndGetMarkers(b, token,mf);
		String splits[] = strb.split(str);
		if(splits.length != ms.size()){
			assertEquals(splits.length, ms.size());
		}
		System.out.println("length:"+ms.size());
		int k = 0;
		for(Marker m: ms){
			if(!splits[k].equals(m.toString(b))){
				assertEquals(splits[k], m.toString(b));
			}
			k++;
		}
	}
	@Test
	public void test4() {
		String strb = "a,,b,,c,,d,,"; 
		byte[] b = strb.getBytes();
		
		String str = ",,";
		Marker line = mf.create(0, b.length-1);
		byte[] token = TokenFactory.create(str);
		ArrayList <Marker> ms = line.splitAndGetMarkers(b, token,mf);
		String splits[] = strb.split(str);
		if(splits.length != ms.size()){
			assertEquals(splits.length, ms.size());
		}
		System.out.println("length:"+ms.size());
		int k = 0;
		for(Marker m: ms){
			if(!splits[k].equals(m.toString(b))){
				assertEquals(splits[k], m.toString(b));
			}
			k++;
		}
	}
}