package com.flytxt.utils.parser;

import java.util.ArrayDeque;

public class MarkerFactory {
	private static ArrayDeque<Marker> home = new ArrayDeque<Marker>();
	private static  ArrayDeque<Marker> roam = new ArrayDeque<Marker>();
	
	public static Marker create(int lastIndex, int i) {
		Marker m = null;
		try{
		m= home.pop();
		}catch (Exception e) {
		}
		if(m == null){
			m = new Marker();
			m.index=lastIndex;
			m.length = i;
			//System.out.println("marker:"+lastIndex+ ":"+i);
			roam.push(m);
		}
		return m;
	}
	public static void reclaim(){
		ArrayDeque<Marker> tmp =home;
		home = roam;
		roam = tmp;
	}

}
