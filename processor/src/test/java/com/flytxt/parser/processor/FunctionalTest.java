package com.flytxt.parser.processor;

import java.io.IOException;

public class FunctionalTest {

	public static void main(String[] args) {
		try {
			ProxyScripts ps = new ProxyScripts();
			LineProcessor lp  = (LineProcessor) ps.getInstanceFor("Script2.lp");
			FlyReader reader = new FlyReader(lp.getFolder(), lp);
			reader.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}