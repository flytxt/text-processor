package com.flytxt.utils.parser.marker;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;
import com.flytxt.utils.parser.TokenFactory;

public class MarkerFunctionalTest {

	@Test
	public void test() {
		MarkerFactory mf = new MarkerFactory();
		String str ="a,{b|c},d";
		byte[] d = str.getBytes();
		Marker line = mf.create(0, d.length-1);
		byte[] t1 = TokenFactory.create(",{");
		byte[] t2 = TokenFactory.create("{");
		byte[] t3 = TokenFactory.create("|");
		Marker m1 = line.splitAndGetMarker(d, t1,2,mf);
		System.out.println(m1.toString(d));
		Marker m2 = m1.splitAndGetMarker(d, t2, 1, mf);
		System.out.println(m2.toString(d));
		Marker m3 = m2.splitAndGetMarker(d, t3, 1, mf);
		System.out.println(m3.toString(d));
		if(!m3.toString(d).equals("b")){
			assertEquals(m3.toString(d), "b");
		}
	}

}
