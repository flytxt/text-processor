package com.flytxt.utils.parser;

import java.util.ArrayDeque;

public class MarkerFactory {
	private ArrayDeque<Marker> home = new ArrayDeque<Marker>();
	private  ArrayDeque<Marker> roam = new ArrayDeque<Marker>();
	
	public Marker create(int lastIndex, int i) {
		Marker m = null;
		try{
			m= home.pop();
			m.index=lastIndex;
			m.length = i;
		}catch (Exception e) {
			m = new Marker();
			m.index=lastIndex;
			m.length = i;
			roam.push(m);
		}
		return m;
	}
	public void reclaim(){
		ArrayDeque<Marker> tmp =home;
		home = roam;
		roam = tmp;
	}

}
