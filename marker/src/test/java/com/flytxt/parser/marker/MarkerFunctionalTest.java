package com.flytxt.parser.marker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarkerFunctionalTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		logger.debug(m1.toString(d));
		Marker m2 = m1.splitAndGetMarker(d, t2, 1, mf);
		logger.debug(m2.toString(d));
		Marker m3 = m2.splitAndGetMarker(d, t3, 1, mf);
		logger.debug(m3.toString(d));
		if(!m3.toString(d).equals("b")){
			assertEquals(m3.toString(d), "b");
		}
	}

}
