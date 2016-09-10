package com.flytxt.utils.processor;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private List<Worker> workers = new ArrayList<Worker>();
	public static void main(String[] args) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> roots = classLoader.getResources("");
		ArrayList<String> lineProcessors = new ArrayList<String>();
		while(roots.hasMoreElements()){
			URL url = (URL) roots.nextElement();
			System.out.println("url:"+url);
			File root = new File(url.getPath());
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
				    // Loop through its listFiles() recursively.
				} else {
				    String name = file.getName();
				    System.out.println(name);
				    if(name.contains("FlyLineProcessor"))
				    	lineProcessors.add(name);
				}
			}
		}
        ExecutorService executor = Executors.newFixedThreadPool(lineProcessors.size());
        for (int i = 0; i < lineProcessors.size(); i++) {
            Runnable worker = new Worker(lineProcessors.get(i));
            executor.execute(worker);
          }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		
	}
	
	public void init6(){
		for(Worker aWorker: workers){
			aWorker.stop();
		}
	}

}
