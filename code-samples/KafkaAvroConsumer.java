package com.ssaikia.kafka.debezium;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaAvroConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:29092");
        properties.put("group.id", "customer-consumer-group-v2");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        //properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(properties);
        String topic = "dbserver102.customerdb.customer";
        kafkaConsumer.subscribe(Collections.singleton(topic));
        System.out.println("Polling...");
        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = kafkaConsumer.poll(1000);
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                }
                kafkaConsumer.commitSync();
            }
        }finally {
            kafkaConsumer.close();
        }
    }
}
