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
		
		byte[] data = new byte[6024];
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder))) {
			MarkerFactory mf = new MarkerFactory();
            for (Path path : directoryStream) {
            	RandomAccessFile file= new RandomAccessFile(path.toString(), "rw");
            	  try(FileChannel channel = file.getChannel()){
	            	  try {
	            		  lp.setInputFileName(path.getFileName().toString());
	            		  processFile(data, path, file, channel, mf);
	            	      if(stopRequested){
	            	    	  break;
	            	      }
	            	   } catch (OverlappingFileLockException e) {
	            		   e.printStackTrace();;
	            	   } finally {
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

	private void processFile(byte[] data, Path path, RandomAccessFile file, FileChannel channel, MarkerFactory mf)
			throws IOException {
		
		  readLines(file, data, mf);
		  lp.done();
		  file.close();
		  Files.delete(path);
	}
	
	private final void readLines(RandomAccessFile file, byte[]data, MarkerFactory mf) throws IOException {
		boolean match= false;
		
		int i = 0;
		int readCnt;
		int j = 0;
		long t1 = System.currentTimeMillis();
	      do{
	    	  readCnt = file.read();
	    	  data[i] = (byte) readCnt;
	    	  if(readCnt == 10){
	    	  }
	    	  if(j < eol.length && readCnt != -1 
	    			  && (byte)readCnt==eol[j] ){
	    		match = true;
	    		j++;
	    	  }
	    	  if(eol.length== j && match){
	    		  try{
	    			  lp.process(data, i, mf);
	    		  }catch (IndexOutOfBoundsException e) {
					System.out.println("could not process : "+ new String(data, 0, i)+ " \n cause:"+e.getMessage());
	    		  }
	    		  i = 0;
	    		  j=0;
	    		  mf.reclaim();
	    		  continue;
	    	  }
	    	  i++;
	      }while(readCnt != -1);
	      long t2 = System.currentTimeMillis();
	      System.out.println("total time taken: "+ (t2-t1)/1000);
	}

	public void stop(){
		stopRequested = true;
	}
}