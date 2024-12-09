package hr.fer.tel.rassus.stupidudp.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducer {
    private  String topic;
    private Producer<String, String> producer;


    public KafkaProducer(String topic) {
        this.topic = topic;
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(producerProperties);
        System.err.println("Starting kafka producer...");
    }

    public void sendData(String data) {
        System.err.println("Sending data (kafka producer):: " + data);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, data);
        producer.send(record);
        producer.flush();
    }

}
