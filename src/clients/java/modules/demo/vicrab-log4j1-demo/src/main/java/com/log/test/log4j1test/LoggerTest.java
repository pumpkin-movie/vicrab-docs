package com.log.test.log4j1test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;

public class LoggerTest {
	private static Logger logger = Logger.getLogger(LoggerTest.class);
	static {
//		logger.setAdditivity(additive);
//		String configPath = System.getProperty("user.dir") + File.separator + "config" + File.separator
//				+ "log4j.properties";
//		PropertyConfigurator.configure(configPath);
		LoggerRepository loggerRepository = logger.getLoggerRepository();
		Level level = loggerRepository.getRootLogger().getLevel();
		System.out.println(level.toInt());
		loggerRepository.getRootLogger().setLevel(Level.DEBUG);
//		Level level2 = loggerRepository.getRootLogger().getLevel();
//		System.out.println(level2.toInt());
//		logger.setLevel(Level.INFO);
	}

	public void testLoggerDebug() {
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
		logger.fatal("fatal");
		try {
			throwException();
		} catch (Exception e) {
			logger.error("出现的错误：", e);
		}
	}

	public void throwException() throws Exception {
		throw new Exception();
	}

	public static void main(String[] args) {
		LoggerTest loggerTest = new LoggerTest();
		loggerTest.testLoggerDebug();
	}
}