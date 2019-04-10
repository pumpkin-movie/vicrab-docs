package com.log.vicrab;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@SpringBootApplication
public class ExampleLog4j {

	private final static Logger logger = LoggerFactory.getLogger(ExampleLog4j.class);

	public static void main(String[] args) throws Exception {

		SpringApplication.run(ExampleLog4j.class, args);
	}

	@RequestMapping("/hello")
	String vicrabhomne() {
		String dateStr = "";
		try {
			logger.info("logback 访问hello");
			logger.error("logback 访问hello");
			dateStr = new Date().toString();
			Integer[] i = new Integer[2];
			int a = i[3];
			logger.error("this is a error test ");//the sample error message not be sent to the server
		} catch (Exception e) {
			logger.error("this is a error test ",e);//must be with param 'e' 
		}
		return "Hello World! now time is :" + dateStr;

	}
}