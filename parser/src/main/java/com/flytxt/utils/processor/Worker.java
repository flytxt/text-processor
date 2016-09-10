package com.flytxt.utils.processor;

public class Worker implements Runnable {
	private LineProcessor lp;
	private FlyReader reader;

	public Worker(String string) throws Exception {
		Class<?> clazz = Class.forName(string);
		lp = (LineProcessor) clazz.newInstance();
		reader = new FlyReader(lp.getFolder(), lp);
	}

	public void run() {
		reader.start();
	}

	public void stop() {
		reader.stop();
	}

}
