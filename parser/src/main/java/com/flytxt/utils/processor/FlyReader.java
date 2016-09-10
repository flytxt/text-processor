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
	            	  FileLock lock=null;
	            	  try {
	            	      lock = processFile(data, path, file, channel);
	            	      if(stopRequested){
	            	    	  break;
	            	      }
	            	   } catch (OverlappingFileLockException e) {
	            		   e.printStackTrace();;
	            	   } finally {
	            	    lock.release();
	            	   }
	            	  }catch (Exception e) {
	            		  e.printStackTrace();;
	            		  
	            	  }
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		System.out.println("--done--");
	}

	private FileLock processFile(byte[] data, Path path, RandomAccessFile file, FileChannel channel)
			throws IOException {
		FileLock lock;
		lock = channel.lock();
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
		int j = 0;
	      do{
	    	  readCnt = file.read();
	    	  data[i] = (byte) readCnt;
	    	  if(readCnt == 10){
	    		  System.out.println("match");
	    	  }
	    	  if(j < eol.length && readCnt != -1 
	    			  && (byte)readCnt==eol[j] ){
	    		match = true;
	    		j++;
	    		  continue;
	    	  }
	    	  if(eol.length== j && match){
	    		  lp.process(data, readCnt);
	    		  i = 0;
	    		  j=0;
	    	  }
	    	  i++;
	      }while(readCnt != -1);		
	}

	public void stop(){
		stopRequested = true;
	}
}