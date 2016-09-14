package com.flytxt.parser.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.flytxt.parser.marker.LineProcessor;

@Component
public class Processor {
	@Autowired
	private ProxyScripts proxy;

	@Autowired
	private ApplicationContext ctx;
	
	private List<FlyReader> fileReaders = new ArrayList<FlyReader>();
	private ThreadPoolExecutor executor;
	
	public void stopFileReads(){
		for(FlyReader aReader: fileReaders){
			aReader.stop();
		}
	}
	
	@PostConstruct
	public void startFileReaders(){
		try {
			LineProcessor[] lpInstance = proxy.getLPInstance();
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(lpInstance.length);
			String folder; 
			for(LineProcessor lP: lpInstance){
				FlyReader reader = (FlyReader)ctx.getBean("flyReader");
				folder = lP.getFolder();
				reader.set(folder, lP);
				fileReaders.add(reader);
				executor.submit(reader);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		//	((AbstractApplicationContext) ctx).close();
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
