package com.flytxt.parser.store;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.flytxt.parser.marker.Marker;
import com.flytxt.parser.marker.MarkerFactory;
import com.flytxt.parser.marker.TokenFactory;


public class StoreUnitTest {
	MarkerFactory mf = new MarkerFactory();
	@Test
	public void test() {
		String aon = "aon";
		String age = "age";
		Store store = new Store("/tmp/out/my.csv", aon, age);
		String str = "10,twenty";
		byte[] strB = str.getBytes();
		Marker line = mf.create(0,  strB.length-1);
		List<Marker> ms = line.splitAndGetMarkers(strB, TokenFactory.create(","),mf);
		Marker aonM = ms.get(0);
		Marker ageM = ms.get(1);
		try {
			store.save(strB, aonM,ageM);
			store.done();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}

}
