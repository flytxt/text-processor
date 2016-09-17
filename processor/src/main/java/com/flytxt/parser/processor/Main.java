package com.flytxt.parser.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
@Component
public class Main {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
