package hr.fer.tel.rassus.coordinator;


import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;


public class Coordinator {
    private static String TOPIC = "Command";

    public static void main(String[] args){
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(producerProperties)) {

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.err.println("Write a message to send to consumer on topic " + TOPIC);
                String command = sc.nextLine();

                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, null, command);

                producer.send(record);
                producer.flush();

                if(command.equals("Stop")){
                    producer.close();
                    break;

                }
            }
        }
    }

}
