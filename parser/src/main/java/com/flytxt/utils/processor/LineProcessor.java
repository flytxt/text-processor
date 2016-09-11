package com.flytxt.utils.processor;

import java.io.IOException;

import com.flytxt.utils.parser.MarkerFactory;

public interface LineProcessor {
	String getFolder();
	
	void setInputFileName(String currentFileName);
	
	void process(byte[] data, int readCnt, MarkerFactory mf) throws IOException;

	void done() throws IOException;
	
}
