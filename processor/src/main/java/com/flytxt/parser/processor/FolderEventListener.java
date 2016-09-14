package com.flytxt.parser.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

@Data
public class FolderEventListener implements  JNotifyListener{

	private boolean requestShutDown;
	private Logger logger = LoggerFactory.getLogger(FolderEventListener.class);
	
	private Processor processor;

	public void attachListener(String folder) throws IOException{
	    int watchID = JNotify.addWatch(folder, JNotify.FILE_CREATED , false, this);
		logger.debug("Listener added for"+folder);
	}

	@PreDestroy
	public void stop() throws IOException {
		requestShutDown = true;
		logger.debug("shutting down FolderEventListener");
	}

	@Override
	public void fileCreated(int arg0, String arg1, String arg2) {
		logger.debug("Folder"+arg1 +" fileName "+arg2);
		processor.handleEvent(arg1,arg2);
	}

	@Override
	public void fileDeleted(int arg0, String arg1, String arg2) {
	}

	@Override
	public void fileModified(int arg0, String arg1, String arg2) {
	}

	@Override
	public void fileRenamed(int arg0, String arg1, String arg2, String arg3) {
	}

}