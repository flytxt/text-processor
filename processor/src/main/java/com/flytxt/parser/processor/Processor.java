package com.flytxt.parser.processor;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.flytxt.parser.marker.LineProcessor;

@Component
public class Processor {
	@Autowired
	private ProxyScripts proxy;

	@Autowired
	private ApplicationContext ctx;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<FlyReader> fileReaders = new ArrayList<FlyReader>();
	private ThreadPoolExecutor executor;
	
	public void stopFileReads(){
		for(FlyReader aReader: fileReaders){
			aReader.stop();
		}
	}
	
	@PostConstruct
	public void startFileReaders() throws Exception{
		try {
			List<LineProcessor> lpInstance = proxy.getLPInstance();
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(lpInstance.size());
			String folder; 
			for(LineProcessor lP: lpInstance){
				FlyReader reader = (FlyReader)ctx.getBean("flyReader");
				folder = lP.getFolder();
				reader.set(folder, lP);
				fileReaders.add(reader);
				executor.submit(reader);
			}
			
		} catch (Exception e) {
			logger.error("can't start readers",e);
			throw e;
		}
	}

	public void handleEvent(String folderName, String fileName) {
		for(FlyReader aReader: fileReaders){
			if(aReader.canProcess(folderName,fileName) && aReader.getStatus()!=FlyReader.Status.RUNNING){
				executor.submit(aReader);
				break;
			}
		}
	}
	@PreDestroy
	public void init0(){
		for(FlyReader aReader: fileReaders){
			aReader.stop();
		}
		executor.shutdown();
		
	}
}
