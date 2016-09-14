package com.flytxt.parser.processor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class FolderEventListener implements Runnable{

	private WatchService watcher ;
	private boolean requestShutDown;
	private Logger logger = LoggerFactory.getLogger(FolderEventListener.class);
	
	@Autowired
	private Processor processor;

	public FolderEventListener() throws IOException{
		watcher = FileSystems.getDefault().newWatchService();

	}
	
	public void attachListener(String folder) throws IOException{
		Path dir = Paths.get(folder);
		dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
	}
	public void run(){
		logger.debug("starting folder listener");
			WatchKey key;
			while (!requestShutDown) {
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}
		 
				for (WatchEvent<?> event : key.pollEvents()) {
			        WatchEvent.Kind<?> kind = event.kind();
			        @SuppressWarnings("unchecked")
			        WatchEvent<Path> ev = (WatchEvent<Path>) event;
			        Path fileName = ev.context();
			        if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE) {
			        	String folder = fileName.getParent().toString();
			        	processor.handleEvent(folder);
			        } 
			    }
				boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			}
		}

	@PreDestroy
	public void stop() throws IOException {
		requestShutDown = true;
		watcher.close();
	}
	}