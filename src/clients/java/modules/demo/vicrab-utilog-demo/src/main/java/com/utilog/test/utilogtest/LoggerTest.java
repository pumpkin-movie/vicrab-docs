package com.utilog.test.utilogtest;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggerTest {
	public static void main(String[] args) {
		Logger log = null;
		Logger log1 = null;
		Logger log2 = null;
		try {
			log = Logger.getLogger("LoggerTest");
			log.setLevel(Level.INFO);

			log1 = Logger.getLogger("LoggerTest");
			System.out.println(log == log1); // true

			log2 = Logger.getLogger("LoggerTest.blog");
			log2.setLevel(Level.WARNING);

			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.ALL);
			log.addHandler(consoleHandler);

			FileHandler fileHandler = new FileHandler("./testutilog%g.log");
			fileHandler.setLevel(Level.INFO);
			fileHandler.setFormatter(new MyLogHander());

			log.addHandler(fileHandler);
			log.info("aaa");
			log2.log(Level.WARNING,"this is a waring test ");

			throwException();
		} catch (Exception e) {
			log2.log(Level.SEVERE, "error infomation is : " +e.toString());
		}
	}

	public static void throwException() throws Exception {
		throw new Exception();
	}
}