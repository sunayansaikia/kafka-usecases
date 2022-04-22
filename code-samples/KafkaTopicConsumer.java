package com.ssaikia.kafka.debezium;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaTopicConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-consumer-group-v4");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // avro part (deserializer)
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //properties.setProperty("schema.registry.url", "http://localhost:8081");
        //properties.setProperty("specific.avro.reader", "true");

        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(properties);
        Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics();
        for (Map.Entry<String, List<PartitionInfo>> entry : topics.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }


        String topic = "mysql-20-customer";
        kafkaConsumer.subscribe(Collections.singleton(topic));
        System.out.println("Waiting for data...");

        kafkaConsumer.poll(0);  // without this, the assignment will be empty.
        kafkaConsumer.assignment().forEach(t -> {
            System.out.printf("Set %s to offset 0%n", t.toString());
            kafkaConsumer.seek(t, 0);
        });

        try {
            while (true) {
                //System.out.println("Polling");
                Duration duration = Duration.ofSeconds(2);
                ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
                System.out.println("records: " + records.count());
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                }
                //kafkaConsumer.commitSync();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            kafkaConsumer.close();
        }
    }
}
