package com.flytxt.parser.marker;

import java.io.IOException;

public interface LineProcessor {
	String getFolder();
	
	void setInputFileName(String currentFileName);
	
	void process(byte[] data, int readCnt, MarkerFactory mf) throws IOException;

	void done() throws IOException;
	
}
