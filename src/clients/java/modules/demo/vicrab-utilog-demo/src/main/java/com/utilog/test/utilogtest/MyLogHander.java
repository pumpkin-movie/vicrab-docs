package com.utilog.test.utilogtest;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyLogHander extends Formatter {

	  @Override 
      public String format(LogRecord record) { 
              return record.getLevel() + ":" + record.getMessage()+"\n"; 
      } 
}
