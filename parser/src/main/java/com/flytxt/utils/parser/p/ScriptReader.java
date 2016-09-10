package com.flytxt.utils.parser.p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScriptReader {

	public void read(String location, Parser lp) {
		BufferedReader br = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(location).getFile());
			lp.setName(file.getName().substring(0,file.getName().indexOf('.')));
			String sCurrentLine;
			br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().length()==0) continue;
				if(sCurrentLine.startsWith("#")) continue;
				lp.process(sCurrentLine.replace("\\s+",""));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		lp.done();

	}
}