package com.iscas.smarthome;

import com.iscas.smarthome.test.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmarthomeApplication {

	public static void main(String[] args) {
	    //TODO: init要改
		Test.init();
		SpringApplication.run(SmarthomeApplication.class, args);
	}

}
