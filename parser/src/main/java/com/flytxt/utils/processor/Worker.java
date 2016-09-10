package com.flytxt.utils.processor;

public class Worker implements Runnable {
	private FlyReader reader;
	private String folder;
	public Worker(LineProcessor lp) throws Exception {
		folder = lp.getFolder();
		reader = new FlyReader(lp.getFolder(), lp);
	}

	public void run() {
		reader.start();
		Main.wokerAvailablity.remove(folder);
	}

	public void stop() {
		reader.stop();
	}

}
