package com.flytxt.parser.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class Utils {
	public boolean createFile(String loc, String content, String fileName){
		Path folder = Paths.get(loc+"com/flytxt/utils/parser");
			try {
				if(! Files.exists(folder)){
					Files.createDirectories(folder);
				}
				Path file = Paths.get(folder.toString()+"/"+fileName);
				OpenOption[] options = { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
				Files.write(file, content.getBytes(), options);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
	}
	public String complie(String src, String dest){
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager sjfm = javaCompiler.getStandardFileManager(null, null, null); 
		  DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		String[] options = new String[] { "-d", dest };
		File[] javaFiles = new File[] { new File(src) };

		StringWriter bos = new StringWriter();
		CompilationTask compilationTask = javaCompiler.getTask(bos, null, null,
		        Arrays.asList(options),
		        null,
		        sjfm.getJavaFileObjects(javaFiles)
		);
		if(!compilationTask.call()){
			Locale myLocale = Locale.getDefault();
		      StringBuilder msg = new StringBuilder();
		      msg.append("Cannot compile to Java bytecode:");
		      for (Diagnostic<? extends JavaFileObject> err : diagnostics.getDiagnostics()) {
		        msg.append('\n');
		        msg.append(err.getKind());
		        msg.append(": ");
		        if (err.getSource() != null) {
		          msg.append(err.getSource().getName());
		        }
		        msg.append(':');
		        msg.append(err.getLineNumber());
		        msg.append(": ");
		        msg.append(err.getMessage(myLocale));
		      }
		      return msg.toString() + "\n" + bos.toString();
		    }
		return null;
	}
	
	public void createJar(String loc, String dest) throws IOException{
		Path destP = Paths.get(dest);
		if(!Files.exists(destP.getParent())){
			Files.createDirectories(destP);
		}
		FileOutputStream fout = new FileOutputStream(dest);
		JarOutputStream jarOut = new JarOutputStream(fout);
		listFiles(Paths.get(loc), jarOut, loc.length()+1, true);
	}
	public void listFiles(Path path, JarOutputStream jarOut, int root, boolean isParent) throws IOException {
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
	        for (Path entry : stream) {
	        	boolean isDirectory = Files.isDirectory(entry);
	            if (isDirectory) {
	                listFiles(entry, jarOut, root, false);
	                if(isParent)
	                	return;
	            }
	            String folderName = entry.getParent().toString().substring(root);
	            if(isDirectory){
	            	//System.out.println("zip ; "+folderName);
	            	jarOut.putNextEntry(new ZipEntry(folderName+"/"));
	            }else{
	            	jarOut.putNextEntry(new ZipEntry(entry.toString().substring(root)));
	            	jarOut.write(Files.readAllBytes(entry));
	            	jarOut.closeEntry();
	            	//System.out.println("\t"+entry.toString().substring(root));
	            }
	        }
	    }
	}
}
