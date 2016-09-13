package com.flytxt.parser.compiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class Compiler {

	public static void main(String args[]){
		SpringApplication.run(Compiler.class, args);
	}
}
