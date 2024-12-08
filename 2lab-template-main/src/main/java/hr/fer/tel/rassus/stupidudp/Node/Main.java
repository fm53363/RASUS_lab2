package hr.fer.tel.rassus.stupidudp.Node;

import hr.fer.tel.rassus.stupidudp.kafka.KafkaConsumer;
import hr.fer.tel.rassus.stupidudp.kafka.KafkaProducer;
import hr.fer.tel.rassus.stupidudp.mapper.SensorMapper;
import hr.fer.tel.rassus.stupidudp.model.Sensor;
import hr.fer.tel.rassus.stupidudp.model.SensorFactory;
import hr.fer.tel.rassus.stupidudp.server.StupidUDPServer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    static final List<String> CONSUMER_TOPICS = List.of("Command","Register");
    static final String PRODUCER_TOPICS = "Register";

    static StupidUDPServer UDPServer;

    static Sensor currentSensor;

    static KafkaConsumer consumer;
    static KafkaProducer producer;

    static final List<Sensor>  neighbours = Collections.synchronizedList(new ArrayList<>());
    static boolean FINSIHED = false;

    public static Thread udpServerThread() {
        return new Thread(() -> {
            try {
                UDPServer.startServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Thread kafkaConsumerThread() {
        return new Thread(() -> {
            while (true) {
                ConsumerRecords<String,String> consumerRecords =  consumer.poll(1000);

                for(var record : consumerRecords) { //  obrada porkuek ovisno o temi
                    System.out.println(record.value());
                    if(record.topic().equals("Command") ) {
                        String value = record.value();
                        if(value.equals("Start")) {
                            producer.sendData(SensorMapper.toString(currentSensor));

                        }else if(value.equals("Stop")) {
                            FINSIHED = true;
                        }
                    }
                    else if(record.topic().equals("Register")) {
                        Sensor tmp  = SensorMapper.toSensor(new JSONObject(record.value()));
                        if(currentSensor.equals(tmp)) {
                            synchronized (neighbours) {
                                neighbours.add(tmp);
                            }
                        }
                    }
                }
                consumer.commitAsync();
            }

        });
    }

    public static void main(String[] args) {
        try {
            UDPServer = new StupidUDPServer(0);  // stvori udp server
            currentSensor = SensorFactory.createSensor("localhost", UDPServer.getPort()); // stvori trenutni senzor
            consumer = new KafkaConsumer(CONSUMER_TOPICS, currentSensor.id());
            producer  = new KafkaProducer(PRODUCER_TOPICS);

            System.out.println(currentSensor);

            udpServerThread().start();
            kafkaConsumerThread().start();










        } catch (SocketException e) {
            throw new RuntimeException(e);
        }







    }
}
