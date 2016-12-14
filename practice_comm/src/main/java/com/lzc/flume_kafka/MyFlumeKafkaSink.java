package com.lzc.flume_kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

/**
 * 自定义Flume目的地
 * 
 * @author LeiZhicheng
 * 
 * @date：2016-12-8
 */
public class MyFlumeKafkaSink extends AbstractSink implements Configurable {

	private static Log log = LogFactory.getLog(MyFlumeKafkaSink.class);

	public void outputLog(String msg) {
		log.warn(msg);

	}

	public Status process() throws EventDeliveryException {

		getContentWithTx();

		return Status.READY;
	}

	public void getContentWithTx() {
		Channel chanel = getChannel();
		System.out.println("Chanel class():" + chanel.getClass().getName());
		Transaction tx = chanel.getTransaction();
		int i = 2;
		try {
			tx.begin();
			Event event = chanel.take();
			if (event != null) {
				String content = new String(event.getBody());

				outputLog("----[MyFlumeKafkaSink获取到的日志]---：" + content);
			}
			/*
			 * if (i > 0) { throw new Exception("故意异常"); }
			 */
			Thread.sleep(1000 * 5);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}

	public void configure(Context context) {

	}

}
