package com.iscas.smarthome;

import com.iscas.smarthome.test.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主程序，run此main方法开启server
 */
@SpringBootApplication
public class SmarthomeApplication {

	public static void main(String[] args) {
		Test.init();
		SpringApplication.run(SmarthomeApplication.class, args);
	}

}
