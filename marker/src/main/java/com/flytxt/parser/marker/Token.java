package com.flytxt.parser.marker;

public class Token implements Delim {
	public final byte[] token;
	private int[] ptrs;
	private int indexPtr=0;
	private int currentPos;
	
	public Token(byte[]d){
		this.token = d;
	}
	public Token(char c){
		token = new byte[1];
		token[0]=(byte)c;
		ptrs = new int[100];
	}
	
	public void check(byte datum, int index){
		if(token[currentPos++] == datum){
			if(currentPos == token.length){
				ptrs[indexPtr++] = index - token.length+1;
				currentPos = 0;
			}
		}else{
			currentPos = 0;
		}
	}
	public int[] getLocation(){
			return ptrs;
	}
	
	public int getCount(){
		return indexPtr;
	}
	
	public void reset(){
		indexPtr=0;
		currentPos = 0;
			
	}
}
