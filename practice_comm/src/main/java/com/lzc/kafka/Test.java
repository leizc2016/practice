package com.lzc.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties props = new Properties();
		// props.put("zk.connect", "127.0.0.1:2181");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		props.put("bootstrap.servers", "10.112.1.91:9092");

		Producer<String, String> producer = new KafkaProducer(props);

		ProducerRecord<String, String> data1 = new ProducerRecord<String, String>("ordertopic", "key1", "test-msg1");
		producer.send(data1);

		Properties consumerProps = (Properties) props.clone();
		consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProps.put("group.id", "testid");

		Consumer<String, String> consumer = new KafkaConsumer<String,String>(consumerProps);
		consumer.subscribe(Arrays.asList("ordertopic"));
		ConsumerRecords<String, String> msg = consumer.poll(100);
		System.out.println(msg);
		// consumer.c

	}
}
