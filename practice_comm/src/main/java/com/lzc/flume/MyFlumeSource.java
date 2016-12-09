package com.lzc.flume;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;

/**
 * 自定义Flueme数据源
 * 
 * @author LeiZhicheng
 * 
 * @date：2016-12-8
 */
public class MyFlumeSource extends AbstractSource implements Configurable, PollableSource {

	private static Log log = LogFactory.getLog(MyFlumeSource.class);

	private String command = null;

	private BufferedReader reader = null;

	public void outputLog(String msg) {
		log.warn(msg);

	}

	/**
	 * flume命令：
	 * bin/flume-ng agent --conf conf --conf-file
	 * conf/MyFlumeSource.conf --name a1 -Dflume.root.logger=INFO,console
	 * 
	 * 读取MyFlumeSource.conf里面的参数
	 */
	public void configure(Context context) {
		command = context.getString("command");
		String logpath = context.getString("logpath");
		outputLog("获取到参数 Command: " + command + ", logpath:" + logpath);

		if (command == null) {
			return;
		}
		String[] commandArgs = command.split("\\s+");
		try {
			Process process = new ProcessBuilder(commandArgs).start();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		} catch (Exception e) {

		}
	}

	/**
	 * 循环不断处理，放到chanel中
	 */
	public Status process() throws EventDeliveryException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (command == null) {
			return Status.BACKOFF;
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				outputLog("----[MyFlumeSource发送日志]---：" + line);
				if (line != null) {
					byte[] body = line.toString().getBytes();
					Event event = EventBuilder.withBody(body);
					getChannelProcessor().processEvent(event);
					return Status.READY;
				}
			}
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return Status.BACKOFF;
	}

	public long getBackOffSleepIncrement() {
		return 0;
	}

	public long getMaxBackOffSleepInterval() {
		return 0;
	}

}
