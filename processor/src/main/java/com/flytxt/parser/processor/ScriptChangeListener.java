package com.flytxt.parser.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ScriptChangeListener {
	@Autowired
	private Processor processor;

	@RequestMapping(method = RequestMethod.GET, path = "/newJars")
	public @ResponseBody String reload(){
		processor.stopFileReads();
		processor.startFileReaders();
		return "ok";
	}

}
