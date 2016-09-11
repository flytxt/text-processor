package com.flytxt.parser.processor;

public class Worker implements Runnable {
	private FlyReader reader;
	public enum Status {RUNNING, TERMINATED, SHUTTINGDOWN}
	private Status status;
	public Worker(LineProcessor lp) throws Exception {
		reader = new FlyReader(lp.getFolder(), lp);
	}

	public void run() {
		status = Status.RUNNING;
		reader.start();
		status = Status.TERMINATED;
	}

	public void stop() {
		status = Status.SHUTTINGDOWN;
		reader.stop();
		status = Status.TERMINATED;
	}

	public final Status getStatus(){
		return status;
	}
}
