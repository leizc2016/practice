package com.lzc.flume;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;

/**
 * 自定义FlumeSource
 * 
 * @author LeiZhicheng
 * 
 * @date：2016-12-8
 */
public class MyFlumeEventDriveSource extends AbstractSource implements Configurable, EventDrivenSource {

	public void configure(Context arg0) {

	}

	public void start() {
	}

	public void stop() {
	}

}
