package com.inde.indytrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class IndytrackApplication {

	private static final Logger LOG = LoggerFactory.getLogger(IndytrackApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IndytrackApplication.class, args);
		LOG.info("Indytrack application started successfully.");
	}

}
