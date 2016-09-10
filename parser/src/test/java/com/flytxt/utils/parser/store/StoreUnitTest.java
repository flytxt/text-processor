package com.flytxt.utils.parser.store;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;
import com.flytxt.utils.parser.TokenFactory;
import com.flytxt.utils.processor.Store;

public class StoreUnitTest {

	@Test
	public void test() {
		String aon = "aon";
		String age = "age";
		Store store = new Store("/tmp/out/my.csv", aon, age);
		String str = "10,twenty";
		byte[] strB = str.getBytes();
		Marker line = MarkerFactory.create(0,  strB.length-1);
		List<Marker> ms = line.splitAndGetMarkers(strB, TokenFactory.create(","));
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
