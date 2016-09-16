package com.flytxt.parser.marker;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkerFactory {
	private ArrayDeque<Marker> home = new ArrayDeque<Marker>();
	private ArrayDeque<Marker> roam = new ArrayDeque<Marker>();
	private ArrayDeque<ArrayList<Marker>> homeList = new ArrayDeque<ArrayList<Marker>>();
	private ArrayDeque<ArrayList<Marker>> roamList = new ArrayDeque<ArrayList<Marker>>();
	private int reused;
	private int created;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Marker create(int lastIndex, int i) {
		System.out.println("index:" + lastIndex+" len:" +1);
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
		ArrayDeque<ArrayList<Marker>> tmpList =homeList;
		homeList = roamList;
		roamList = tmpList;
	}
	
	public void printStat(){
		logger.debug("Reused cnt:" + reused + "created :"+ created);
	}
	public ArrayList<Marker> getArrayList() {
		ArrayList<Marker> list;
		list = homeList.pop();
		if(list == null)
			list = new ArrayList<Marker>();
		roamList.push(list);
		return list;
	}
}