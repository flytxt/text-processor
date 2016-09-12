package com.flytxt.parser.compiler;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.jar.JarOutputStream;

import org.junit.Test;

public class UtilsUnitTest {
	@Test
	public void testBadClass(){
		Utils utils = new Utils();
		String src = "/Users/arunsoman/git/text-processor/compiler/src/test/resources/compiler/Bad.java";
		String dest = "/tmp/";
		String res = utils.complie(src, dest);
		System.out.println(res);
		if(res == null){
			fail("sshould have produced  error text");
		}
	}
	@Test
	public void testGoodClass(){
		Utils utils = new Utils();
		String src = "/Users/arunsoman/git/text-processor/compiler/src/test/resources/compiler/Good.java";
		String dest = "/tmp/classes";
		String res =utils.complie(src, dest);
		if(res != null){
			fail("should have produced  error text");
		}
	}
	
	@Test
	public void testDirListing(){
		String loc = "/Users/arunsoman/git/text-processor/parser/target/classes";
		Utils utils = new Utils();
		try {
		FileOutputStream fout = new FileOutputStream("/tmp/test.jar");
		JarOutputStream jarOut = new JarOutputStream(fout);
			utils.listFiles(Paths.get(loc), jarOut, loc.length()+1, true);
			jarOut.close();
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
