package com.flytxt.utils.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.flytxt.utils.parser.Marker;

public class Store {
	private RandomAccessFile channel;
	private final byte csv = (byte)',';
	public Store(String file, String...headers){
		try{
			channel = new RandomAccessFile(file.toString(), "rw");
			for(String aheader: headers){
				channel.write(aheader.getBytes());
				channel.write(csv);
			}
			channel.writeChars("\n\r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save(byte[] data, Marker... markers) throws IOException{
		for(Marker aMarker: markers){
			channel.write(data, aMarker.index, aMarker.length);
			channel.write(csv);
		}
		channel.writeChars("\n\r");
	}
	
	public void done() throws IOException{
		channel.close();
	}
}
