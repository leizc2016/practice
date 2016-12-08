package com.lzc.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class MyConsoleAppender extends ConsoleAppender {

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	protected void subAppend(LoggingEvent event) {
		Date now = new Date();

		String prefix = "[LvLogS";
		String className = "CLASS:\"" + event.getLoggerName() + "\"";
		String dateTime = "TIME:\"" + now.getTime() + "\"";
		String suffix = "[LvLogE";

		qw.write(format.format(now));
		qw.write(prefix);
		qw.write("]");

		// qw.write(Layout.LINE_SEP);

		qw.write(super.layout.format(event));
		if (super.layout.ignoresThrowable()) {
			String s[] = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				for (int i = 0; i < len; i++) {
					qw.write(s[i]);
					qw.write(Layout.LINE_SEP);
				}

			}
		}

		qw.write(suffix);
		qw.write(" " + dateTime);
		qw.write(" " + className);
		qw.write("]");
		qw.write(Layout.LINE_SEP);

		if (shouldFlush(event))
			qw.flush();
	}

}
