package com.lzc.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class FlumeLog4jAppender extends AppenderSkeleton{

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		// TODO Auto-generated method stub
		
	}


}
