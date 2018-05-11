package com.lzc.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MyKafkaConsumer {

	public void getMessage() {
		System.out.println("start listen....");
		Properties props = new Properties();
		props.put("bootstrap.servers", "mycentos:9092");
		props.put("group.id", "kafkatopic1");
		props.put("enable.auto.commit", true);
		props.put("auto.commit.interval.ms", 100);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		//props.put("metadata.broker.list", "10.112.1.91:2181");
		props.put("auto.offset.reset", "earliest");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		//System.out.println(consumer.listTopics());
		String topic = "kafkatopic1";
		consumer.subscribe(Arrays.asList(topic));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(0);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyKafkaConsumer myKafkaConsumer = new MyKafkaConsumer();
		myKafkaConsumer.getMessage();
	}

}
