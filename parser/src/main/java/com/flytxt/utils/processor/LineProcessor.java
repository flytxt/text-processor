package com.flytxt.utils.processor;

import java.io.IOException;

public interface LineProcessor {
	String getFolder();

	void process(byte[] data, int readCnt) throws IOException;

	void done() throws IOException;
	
}
