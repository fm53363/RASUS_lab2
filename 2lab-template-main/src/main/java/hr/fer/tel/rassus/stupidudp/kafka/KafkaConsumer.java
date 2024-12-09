package hr.fer.tel.rassus.stupidudp.kafka;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumer {

    private List<String> topics;
    private Consumer<String, String> consumer;
    private String groupId;

    public KafkaConsumer(List<String> topics, int groupId) {
        this.topics = topics;
        this.groupId = "Group_" + groupId;
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProperties);
        consumer.subscribe(topics);
        System.err.println("Starting consumer for topics " + topics);
    }

    public ConsumerRecords<String, String> poll(int timeout) {

        var records = consumer.poll(Duration.ofMillis(timeout));
        if (!records.isEmpty()) {
            System.err.println("    Received " + records.count() + " records");
        }
        for (ConsumerRecord<String, String> record : records) {
            System.err.println("    "+record.value());
        }
        return records;
    }

    public void commitAsync(){
        consumer.commitAsync();

    }





}

