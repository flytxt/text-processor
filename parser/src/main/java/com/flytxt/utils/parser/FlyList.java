package com.flytxt.utils.parser;

import java.util.LinkedList;

public class FlyList<M> {
	private LinkedList<Marker> list = new LinkedList<Marker>();
	private int usedPtr =  0;
	public void create(){}
	public void add(int start, int length){
		Marker m;
		if(usedPtr < list.size()){
			m = list.get(usedPtr);
		}else{
			m = new Marker();
			list.add(m);
		}
		m.index = start;
		m.length = length;
	}
}
