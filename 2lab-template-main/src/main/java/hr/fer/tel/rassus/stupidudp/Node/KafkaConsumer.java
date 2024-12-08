package hr.fer.tel.rassus.stupidudp.Node;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumer {

    private List<String> topics;
    private Consumer<String, String> consumer;

    public KafkaConsumer(List<String> topics) {
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "Sensors");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProperties);
        consumer.subscribe(topics);
        System.out.println("Starting consumer for topics " + topics);
    }

    public ConsumerRecords<String, String> poll(int timeout) {
        return consumer.poll(Duration.ofMillis(timeout));
    }

    public void commitAsync(){
        consumer.commitAsync();

    }





}

