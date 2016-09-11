package com.flytxt.utils.processor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.flytxt.utils.parser.p.Parser;
import com.flytxt.utils.parser.p.ScriptReader;

public class Main {
	private List<Worker> workers = new ArrayList<Worker>();
	private ExecutorService executor;
	private static WatchService watcher ;
	public static void main(String[] args) throws Exception {
		watcher = FileSystems.getDefault().newWatchService();
		Main main = new Main();
		main.loadFromClasspath();
		watcher  = FileSystems.getDefault().newWatchService();
	}
	
	public LineProcessor compileNLoad(String scriptName) throws Exception{
		ScriptReader reader = new ScriptReader();
		Parser p = new Parser();
		reader.read(scriptName, p);

		
		Path root = Paths.get("scripts");
		Files.createDirectories(root);
		String file = scriptName.substring(0, scriptName.length()-3);
		File sourceFile = new File(root.toString(), "com/flytxt/utils/parser/"+file+".java");
		sourceFile.getParentFile().mkdirs();
		System.out.println("loc: "+sourceFile.getAbsolutePath());
		Files.write(sourceFile.toPath(), p.createProcessClass().getBytes(StandardCharsets.UTF_8));

		// Compile source file.
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, null, null, sourceFile.getPath());

		// Load and instantiate compiled class.
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toUri().toURL() });
		Class<?> cls = Class.forName("com.flytxt.utils.parser."+file, true, classLoader); // Should print "hello".
		return (LineProcessor)cls.newInstance();
		
	}
	
	public void loadFromClasspath() throws Exception{
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
				    System.out.println("found :"+name);
				    if(name.contains("lp"))
				    	lineProcessors.add(name);
				}
			}
		}
		System.out.println("total processors: " +lineProcessors.size());
		startProcessing(lineProcessors);
	}
	private HashMap<String, LineProcessor> map = new HashMap<String, LineProcessor>();
	public static Set<String> wokerAvailablity = new HashSet<String>();
	public void startProcessing(ArrayList<String> lineProcessors ) throws Exception{
		executor = Executors.newFixedThreadPool(lineProcessors.size());
        for (int i = 0; i < lineProcessors.size(); i++) {
        	String lpStr = lineProcessors.get(i);
        	LineProcessor lp = compileNLoad(lpStr);
        	Path dir = Paths.get(lp.getFolder());
        	try{
        		dir.register(watcher, java.nio.file.StandardWatchEventKinds.ENTRY_CREATE);
        	}catch(java.nio.file.NoSuchFileException e){
        		//TODO log this
        		continue;
        	}
        	map.put(lp.getFolder(), lp);
            Runnable worker = new Worker(lp);
            executor.execute(worker);
          }
        while (!executor.isTerminated()) {
        }
	}
	
	public void init6(){
		for(Worker aWorker: workers){
			aWorker.stop();
		}
		executor.shutdown();
	}
	
	private void handleEvents(){
		while (true) {
		    WatchKey key;
		    try {
		        // wait for a key to be available
		        key = watcher.take();
		    } catch (InterruptedException ex) {
		        return;
		    }
		 
		    for (WatchEvent<?> event : key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();
		        @SuppressWarnings("unchecked")
		        WatchEvent<Path> ev = (WatchEvent<Path>) event;
		        Path fileName = ev.context();
		        System.out.println(kind.name() + ": " + fileName);
		 
		        if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE) {
		        	String folder = fileName.getParent().toString();
		        	if(! Main.wokerAvailablity.contains(folder)){
		        		executor.execute((Runnable) map.get(folder));
		        		Main.wokerAvailablity.add(folder);
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
