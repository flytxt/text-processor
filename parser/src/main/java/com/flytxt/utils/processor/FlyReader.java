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
	public FlyReader(String folder, LineProcessor lp) {
		this.lp = lp;
		this.folder = folder;
	}

	public void start() {
		byte[]eol = "\n\r".getBytes();
		byte[] data = new byte[1024];
		int readCnt = 0;
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder))) {
            for (Path path : directoryStream) {
            	RandomAccessFile file= new RandomAccessFile(path.toString(), "rw");
            	  try(FileChannel channel = file.getChannel()){
	            	  // Get an exclusive lock on the whole file
	            	  FileLock lock = channel.lock();
	            	  try {
	            	      lock = channel.tryLock();
	            	      int i = 0;
	            	      boolean match= false;
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
	            	      lp.done();
	            	      Files.delete(path);
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
	}
	public void stop(){
		stopRequested = true;
	}

}
