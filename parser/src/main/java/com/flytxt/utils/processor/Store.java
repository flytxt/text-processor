package com.flytxt.utils.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.flytxt.utils.parser.Marker;

public class Store {
	private RandomAccessFile channel;
	private final byte csv = (byte)',';
	private int status;
	public Store(String file, String...headers){
		int index = file.toString().lastIndexOf("/");
		if(index ==-1){
			status = -1;
			System.out.println("some thing wrong expected a file and fileName");
			return;
		}
		String fileName = file.toString();
		String folder = fileName.substring(0, index);
		Path root = Paths.get(folder);
		 
		try {
			if(! Files.exists(root))
				Files.createDirectories(root);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		System.out.println("file created @ "+file.toString());
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
		if(status == -1){
			throw new IOException("store location expects a folder and a file name");
		}
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
