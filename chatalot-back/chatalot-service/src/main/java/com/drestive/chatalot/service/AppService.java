package com.drestive.chatalot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppService {

	private static final Logger log = LoggerFactory.getLogger(AppService.class);

	public static void main(String[] args) {
		SpringApplication.run(AppService.class);
	}

}
