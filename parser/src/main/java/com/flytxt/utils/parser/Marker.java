package com.flytxt.utils.parser;

import java.util.ArrayList;

public class Marker {
	public int index;
	public int length;
	
	public Marker splitAndGetMarker(byte[] data, byte[] token, int indexOf, MarkerFactory  mf){
		int counter = 0;
		int lastIndex = index;
		for(int i = index; i <= length; i++){
			int j = 0;
			boolean match = false;
			while(j<token.length && data[i]== token[j]){
				i++;
				j++;
				match = true;
			}
			if(match && j == token.length){
				if(indexOf==++counter){
					return mf.create(lastIndex,  i-(lastIndex +token.length));
				}
				lastIndex = i;
			}
		}
		if(lastIndex < length+1 && indexOf == ++counter)
			return mf.create(lastIndex,this.length-(lastIndex-1));
		return null;
	}
	
	public ArrayList<Marker> splitAndGetMarkers(byte[] data, byte[] token,MarkerFactory mf){
		int lastIndex = index;
		ArrayList<Marker> markers = new ArrayList<Marker>();
		for(int i = index; i <= length; i++){
			int j = 0;
			boolean match = false;
			while(j<token.length && data[i]== token[j]){
				i++;
				j++;
				match = true;
			}
			if(match && j == token.length){
				markers.add(mf.create(lastIndex,  i-(lastIndex +token.length)));
				lastIndex = i;
			}
		}
		if(lastIndex < length+1)
		markers.add(mf.create(lastIndex,this.length-(lastIndex-1)));
		return markers;
	}
	
	public String toString(byte[] b){
		return new String(b, index, length);
	}
}
