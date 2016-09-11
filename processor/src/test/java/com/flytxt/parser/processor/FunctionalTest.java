package com.flytxt.parser.processor;

import java.io.IOException;

import com.flytxt.utils.processor.FlyReader;
import com.flytxt.utils.processor.LineProcessor;
import com.flytxt.utils.processor.Main;

public class FunctionalTest {

	public static void main(String[] args) {
		try {
			Main main = new Main();
			LineProcessor lp  = main.compileNLoad("Script2.lp");
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