package com.flytxt.parser.processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
@Component
public class Main {
	public static ApplicationContext ctx; 
	public static void main(String[] args) throws Exception {
		ctx = SpringApplication.run(Main.class, args);
	}
}
