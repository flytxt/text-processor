package com.flytxt.utils.processor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.flytxt.utils.parser.MarkerFactory;

public class FlyReader {
	private String folder;
	private LineProcessor lp;
	private boolean stopRequested;
	byte[]eol = System.lineSeparator().getBytes();
	
	public FlyReader(String folder, LineProcessor lp) {
		this.lp = lp;
		this.folder = folder;
		System.out.println("file reader @ "+ folder);
	}

	public void start() {
		System.out.println("Startring file reader @ "+ folder);
		
		byte[] data = new byte[1024];
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder))) {
            for (Path path : directoryStream) {
            	RandomAccessFile file= new RandomAccessFile(path.toString(), "rw");
            	  try(FileChannel channel = file.getChannel()){
	            	  // Get an exclusive lock on the whole file
	            	  FileLock lock = channel.lock();
	            	  try {
	            	      lock = processFile(data, path, file, channel);
	            	      if(stopRequested){
	            	    	  break;
	            	      }
	            	   } catch (OverlappingFileLockException e) {
	            	    // File is open by someone else
	            	   } finally {
	            	    lock.release();
	            	   }
	            	  }catch (Exception e) {
	            		  
	            	  }
            }
        } catch (Exception ex) {}
		System.out.println("--done--");
	}

	private FileLock processFile(byte[] data, Path path, RandomAccessFile file, FileChannel channel)
			throws IOException {
		FileLock lock;
		lock = channel.tryLock();
		  readLines(file, data);
		  lp.done();
		  file.close();
		  Files.delete(path);
		return lock;
	}
	
	private final void readLines(RandomAccessFile file, byte[]data) throws IOException {
		boolean match= false;
		int i = 0;
		int readCnt;
	      do{
	    	  readCnt = file.read();
	    	  if(i<eol.length
	    			  && readCnt != -1 
	    			  && (byte)readCnt==eol[i++] ){
	    		match = true;
	    		  continue;
	    	  }
	    	  if(eol.length== i&& match){
	    		  lp.process(data, readCnt);
	    		  i = 0;
	    	  }
	      }while(readCnt != -1);		
	}

	public void stop(){
		stopRequested = true;
	}
}