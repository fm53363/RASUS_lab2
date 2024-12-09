package hr.fer.tel.rassus.stupidudp.Node;

import hr.fer.tel.rassus.stupidudp.client.StupidUDPClient;
import hr.fer.tel.rassus.stupidudp.kafka.KafkaConsumer;
import hr.fer.tel.rassus.stupidudp.kafka.KafkaProducer;
import hr.fer.tel.rassus.stupidudp.mapper.SensorMapper;
import hr.fer.tel.rassus.stupidudp.model.Reading;
import hr.fer.tel.rassus.stupidudp.model.Sensor;
import hr.fer.tel.rassus.stupidudp.model.SensorFactory;
import hr.fer.tel.rassus.stupidudp.repo.MySensorRepo;
import hr.fer.tel.rassus.stupidudp.server.StupidUDPServer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class Main {
    // kafka
    private static final List<String> CONSUMER_TOPICS = List.of("Command","Register");
    private static final String PRODUCER_TOPICS = "Register";
    private static KafkaConsumer consumer;
    private static KafkaProducer producer;

    //udp
    private static Sensor currentSensor;
    private static final double LOSS_RATE = 0.3;
    private static  final int AVERAGE_DELAY = 1000;
    private static StupidUDPServer UDPServer;
    private static final List<StupidUDPClient> clients = Collections.synchronizedList(new ArrayList<>());
    // synchronized collection ensures add and remove, not Iteration


    //data
    private static MySensorRepo repo = MySensorRepo.getInstance();

    private static boolean FINSIHED = false;
    private static final long SEND_INTERVAL_MILLIS = 1000;


    private static Thread udpServerThread() {
        return new Thread(() -> {
            try {
                UDPServer.startServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private  static Thread kafkaConsumerThread() {
        return new Thread(() -> {
            while (true) {
                ConsumerRecords<String,String> consumerRecords =  consumer.poll(1000);
                for(var record : consumerRecords) { //  obrada poruke ovisno o temi
                    if(record.topic().equals("Command") ) {
                        String value = record.value();
                        if(value.equals("Start")) {
                            producer.sendData(SensorMapper.toJSONString(currentSensor));
                        }else if(value.equals("Stop")) {
                            FINSIHED = true;
                        }
                    }
                    else if(record.topic().equals("Register")) {
                        Sensor tmp  = SensorMapper.toSensor(new JSONObject(record.value()));
                        if(!currentSensor.equals(tmp)) {
                            try {
                                clients.add( new StupidUDPClient(tmp,LOSS_RATE,AVERAGE_DELAY));
                            } catch (UnknownHostException | SocketException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
                consumer.commitAsync();
            }
        });
    }



    private static Thread udpClientThread() {
        return new Thread(() -> {
            long startTime = System.currentTimeMillis(); // Get the current time in milliseconds
            long elapsedTime = 0;

            while (true) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println("Elapsed time: " + elapsedTime + " seconds");

                int currentReadingId = (int) elapsedTime % repo.getSize();
                Reading r = repo.getReading(currentReadingId);

                System.out.println(r);


                try {
                    Thread.sleep(SEND_INTERVAL_MILLIS); // Sleep for 1000 milliseconds (1 second)
                } catch (InterruptedException e) {
                    System.out.println("Thread was interrupted.");
                    break;
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();


            UDPServer = new StupidUDPServer(0);  // stvori udp server
            currentSensor = SensorFactory.createSensor("localhost", UDPServer.getPort()); // stvori trenutni senzor
            consumer = new KafkaConsumer(CONSUMER_TOPICS, currentSensor.id());
            producer  = new KafkaProducer(PRODUCER_TOPICS);

            System.out.println(currentSensor);

            udpServerThread().start();
            kafkaConsumerThread().start();



            udpClientThread().start();

            // thread


            udpServerThread().join();
            kafkaConsumerThread().join();




        } catch (SocketException | InterruptedException  e) {
            throw new RuntimeException(e);
        }


    }
}
