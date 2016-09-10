package com.flytxt.utils.parser;

public class MarkerFactory {

	public static Marker create(int lastIndex, int i) {
		Marker m = new Marker();
		m.index=lastIndex;
		m.length = i;
		System.out.println("marker:"+lastIndex+ ":"+i);
		return m;
	}

}
