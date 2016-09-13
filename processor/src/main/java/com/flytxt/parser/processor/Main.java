package com.flytxt.parser.processor;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class Main {
	private String[] scripts;
	private ProxyScripts proxy= new ProxyScripts();
	private HashMap<String, Worker> map = new HashMap<String, Worker>();
	private List<Worker> workers;
	private ExecutorService executor;
	private static WatchService watcher ;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@RequestMapping(method = RequestMethod.GET, path = "/scripts")
	public @ResponseBody String getitem(@RequestParam("scripts") String scripts){
		this.scripts = scripts.split(","); 
		restartWorkers();
		return "ok";
	}
	
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.scripts = main.proxy.getScipts();
		main.restartWorkers();
		watcher = FileSystems.getDefault().newWatchService();
		SpringApplication.run(Main.class, args);
	}
	
	private void restartWorkers(){
		for(Worker aWorker: workers){
			aWorker.stop();
		}
		try {
			if(! executor.isShutdown())
				executor.awaitTermination(15, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workers = new ArrayList<Worker>(scripts.length);
		executor = Executors.newFixedThreadPool(scripts.length +1);
		executor.execute(new FolderEventHandler(this));
		for(String aScript: scripts){
        	try{
				LineProcessor lp =	(LineProcessor)proxy.getInstanceFor(aScript);
        		Path dir = Paths.get(lp.getFolder());
        		dir.register(watcher, java.nio.file.StandardWatchEventKinds.ENTRY_CREATE);
				Worker worker = new Worker(lp);
				executor.execute(worker);
			} catch ( Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}
	
	
	public void init6(){
		for(Worker aWorker: workers){
			aWorker.stop();
		}
		executor.shutdown();
	}
	
	private class FolderEventHandler implements Runnable{
		private Main main;
		public FolderEventHandler(Main main) {
			this.main = main;
		}
		public void run(){
			WatchKey key;
			while (true) {
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
			        logger.debug(kind.name() + ": " + fileName);
			 
			        if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE) {
			        	String folder = fileName.getParent().toString();
			        	if(map.get(folder).getStatus() == Worker.Status.TERMINATED) {
			        		executor.execute( map.get(folder));
			        	}
			        } 
			    }
				boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			}
		}
	}
}
