package com.intuit.developer.tutorials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@SpringBootApplication
public class Application extends RepositoryRestMvcConfiguration {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }

}
