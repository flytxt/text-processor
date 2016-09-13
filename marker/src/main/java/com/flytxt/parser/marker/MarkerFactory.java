package com.flytxt.parser.marker;

import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkerFactory {
	private ArrayDeque<Marker> home = new ArrayDeque<Marker>();
	private  ArrayDeque<Marker> roam = new ArrayDeque<Marker>();
	private int reused;
	private int created;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Marker create(int lastIndex, int i) {
		Marker m = null;
		try{
			m= home.pop();
			m.index=lastIndex;
			m.length = i;
			reused ++;
			roam.push(m);
		}catch (Exception e) {
			m = new Marker();
			m.index=lastIndex;
			m.length = i;
			created++;
			roam.push(m);
		}
		return m;
	}
	public void reclaim(){
		ArrayDeque<Marker> tmp =home;
		home = roam;
		roam = tmp;
	}
	
	public void printStat(){
		logger.debug("Reused cnt:" + reused + "created :"+ created);
	}
}