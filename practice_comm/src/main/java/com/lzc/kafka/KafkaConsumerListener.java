package com.lzc.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class KafkaConsumerListener implements Runnable {

	private String topic;

	private String groupId;
	
	private String seq;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
	
	

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public KafkaConsumerListener(String topic, String groupId) {
		super();
		this.topic = topic;
		this.groupId = groupId;
	}

	public void getMessage() {
		//String flagName = "[" + topic + "--" + groupId +"--"+seq+"]";
		String flagName=seq;
		System.out.println(flagName + "start listen....");
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.112.1.188:9092");
		props.put("group.id", groupId);
		props.put("enable.auto.commit", true);
		props.put("auto.commit.interval.ms", 100);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// props.put("metadata.broker.list", "10.112.1.91:2181");
		//props.put("auto.offset.reset", "latest");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		
		TopicPartition  tp = new TopicPartition(topic, 1);
		consumer.assign(Arrays.asList(tp));
		//consumer.subscribe(Arrays.asList(topic));
		consumer.seek(tp, 1);
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(0);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf(flagName+"partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),record.offset(), record.key(), record.value());

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		KafkaConsumerListener myKafkaConsumer1 = new KafkaConsumerListener("topic-05", "G101");
		myKafkaConsumer1.setSeq("0000");
		new Thread(myKafkaConsumer1).start();

		/*KafkaConsumerListener myKafkaConsumer2 = new KafkaConsumerListener("topic-05", "G17");
		myKafkaConsumer2.setSeq("2222");
		new Thread(myKafkaConsumer2).start();*/
	}

	public void run() {
		getMessage();

	}

}
