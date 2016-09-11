package com.flytxt.utils.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.flytxt.parser.marker.Marker;


public class Store {
	private RandomAccessFile channel;
	private final byte csv = (byte)',';
	private final byte[] newLine = System.lineSeparator().getBytes();
	private int status;
	
	public Store(String file, String...headers){
		Path fileName = Paths.get(file);
		if( ! Files.exists(fileName)){
			Path folder = fileName.getParent();
			try {
				Files.createDirectories(folder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				status = -1;
				return;
			}
			System.out.println("file created @ "+file.toString());
			try{
				channel = new RandomAccessFile(file.toString(), "rw");
				for(String aheader: headers){
					channel.write(aheader.getBytes());
					channel.write(csv);
				}
				channel.write(newLine);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try{
				channel = new RandomAccessFile(file.toString(), "rw");
				channel.seek(channel.length());
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void save(byte[] data, Marker... markers) throws IOException{
		if(status == -1){
			throw new IOException("store location expects a folder and a file name");
		}
		for(Marker aMarker: markers){
			channel.write(data, aMarker.index, aMarker.length);
			channel.write(csv);
		}
		channel.write(newLine);
	}
	
	public void done() throws IOException{
		channel.close();
	}
}