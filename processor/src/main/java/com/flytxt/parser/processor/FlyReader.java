package com.flytxt.parser.processor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flytxt.parser.marker.LineProcessor;
import com.flytxt.parser.marker.MarkerFactory;

import lombok.Getter;

@Component
@Scope("prototype")
public class FlyReader implements Callable<FlyReader> {
	private String folder;
	private LineProcessor lp;
	private boolean stopRequested;
	public enum Status {RUNNING, TERMINATED, SHUTTINGDOWN}
	@Getter
	private Status status;
	byte[]eol = System.lineSeparator().getBytes();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void set(String folder, LineProcessor lp) {
		this.lp = lp;
		this.folder = folder;
		logger.debug("file reader @ "+ folder);
	}
	
	public void run() {
		logger.debug("Startring file reader @ "+ folder);
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
	            	    	  logger.debug("shutting down Wroker id:");
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
		logger.debug("--done--");
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
					logger.debug("could not process : "+ new String(data, 0, i)+ " \n cause:"+e.getMessage());
	    		  }
	    		  i = 0;
	    		  j=0;
	    		  mf.reclaim();
	    		  continue;
	    	  }
	    	  i++;
	      }while(readCnt != -1);
	      long t2 = System.currentTimeMillis();
	      logger.debug("total time taken: "+ (t2-t1)/1000);
	}

	public void stop(){
		stopRequested = true;
	}

	@Override
	public FlyReader call() throws Exception {
		run();
		return this;
	}

	public boolean canProcess(String fileName) {
		if(lp.getFolder().equals(Paths.get(fileName).getParent().toString())){
			String regex = lp.getFilter();
			if(regex == null)
				return true;
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(fileName).find();
		}else{
			return false;
		}
	}
}