package com.flytxt.parser.marker;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkerSplitTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
		logger.debug("length:"+ms.size());
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
		logger.debug("length:"+ms.size());
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
		logger.debug("length:"+ms.size());
		int k = 0;
		for(Marker m: ms){
			if(!splits[k].equals(m.toString(b))){
				assertEquals(splits[k], m.toString(b));
			}
			k++;
		}
	}
}