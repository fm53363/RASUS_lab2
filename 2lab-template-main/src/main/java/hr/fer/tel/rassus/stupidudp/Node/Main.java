package hr.fer.tel.rassus.stupidudp.Node;

import hr.fer.tel.rassus.stupidudp.client.StupidUDPClient;
import hr.fer.tel.rassus.stupidudp.kafka.KafkaConsumer;
import hr.fer.tel.rassus.stupidudp.kafka.KafkaProducer;
import hr.fer.tel.rassus.stupidudp.mapper.SensorMapper;
import hr.fer.tel.rassus.stupidudp.mapper.SensorPacketMapper;
import hr.fer.tel.rassus.stupidudp.model.Reading;
import hr.fer.tel.rassus.stupidudp.model.Sensor;
import hr.fer.tel.rassus.stupidudp.model.SensorPacket;
import hr.fer.tel.rassus.stupidudp.model.VectorClock;
import hr.fer.tel.rassus.stupidudp.network.EmulatedSystemClock;
import hr.fer.tel.rassus.stupidudp.repo.MySensorRepo;
import hr.fer.tel.rassus.stupidudp.server.StupidUDPServer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class Main {
    // kafka
    private static final List<String> CONSUMER_TOPICS = List.of("Command", "Register");
    private static final String PRODUCER_TOPICS = "Register";
    private static final double LOSS_RATE = 0.3;
    private static final int AVERAGE_DELAY = 1000;
    private static final long SEND_INTERVAL_MILLIS = 1000;
    private static final long SORT_INTERVAL_MILLIS = 5000;
    private static final Set<StupidUDPClient> clients = Collections.synchronizedSet(new HashSet<>());


    private static KafkaConsumer consumer;
    private static KafkaProducer producer;
    private static Sensor currentSensor;
    private static StupidUDPServer UDPServer;
    private static MySensorRepo repo = MySensorRepo.getInstance();
    private static boolean FINSIHED = false;
    private static EmulatedSystemClock scalarClock;
    private static VectorClock vectorClock;
    private static List<SensorPacket> packets = Collections.synchronizedList(new LinkedList<>());
    private static Set<SensorPacket> intervalPackets = Collections.synchronizedSet(new HashSet<>());


    private static Thread udpServerThread() {
        return new Thread(() -> {
            try {
                UDPServer.startServer(packets);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Thread kafkaThread() {
        return new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
                for (var record : consumerRecords) { //  obrada poruke ovisno o temi
                    if (record.topic().equals("Command")) {
                        String value = record.value();
                        if (value.equals("Start")) {
                            producer.sendData(SensorMapper.toJson(currentSensor));
                        } else if (value.equals("Stop")) {
                            // todo implement stopping mechanism
                            FINSIHED = true;
                        }
                    } else if (record.topic().equals("Register")) {
                        Sensor tmp = null;
                        tmp = SensorMapper.toSensor(record.value());
                        if (!currentSensor.equals(tmp)) {
                            try {
                                clients.add(new StupidUDPClient(tmp, LOSS_RATE, AVERAGE_DELAY));
                                System.err.println("Imam postavljeno klienta: " + clients.size());
                                vectorClock.resizeVector(tmp.id() + 1);
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
                int currentReadingId = (int) elapsedTime % repo.getSize();


                synchronized (clients) {
                    if (!clients.isEmpty()) {
                        Reading r = repo.getReading(currentReadingId);
                        // create Sensor Packet
                        vectorClock.updateBeforeSending();
                        SensorPacket packet = new SensorPacket(r, vectorClock, scalarClock.currentTimeMillis());
                        for (var client : clients) {
                            String msg = SensorPacketMapper.toJson(packet);
                            try {
                                client.send1(msg);
                                while (!client.send1(msg)) {
                                    System.out.println("RETRANSMISIJA " + msg);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(SEND_INTERVAL_MILLIS); // Sleep for 1000 milliseconds (1 second)
                } catch (InterruptedException e) {
                    System.out.println("Thread was interrupted.");
                    break;
                }
            }
        });
    }

    private static Thread sortingAndAveragingThread() {
        return new Thread(() -> {
            while (true) {
                synchronized (intervalPackets) {
                    System.out.println("\n INTERVAL PACKETS");
                    intervalPackets.forEach(System.out::println);
                    System.out.println("\n");
                }


                try {
                    Thread.sleep(SORT_INTERVAL_MILLIS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter sensor id:");
            int id = scanner.nextInt();

            scalarClock = new EmulatedSystemClock();
            UDPServer = new StupidUDPServer(0, LOSS_RATE, AVERAGE_DELAY);// stvori udp server
            currentSensor = new Sensor(id, "localhost", UDPServer.getPort());// stvori trenutni senzor
            vectorClock = new VectorClock(currentSensor.id());
            UDPServer.setVectorClock(vectorClock);
            UDPServer.setIntervalPackets(intervalPackets);

            consumer = new KafkaConsumer(CONSUMER_TOPICS, currentSensor.id());
            producer = new KafkaProducer(PRODUCER_TOPICS);

            System.out.println(currentSensor);

            udpServerThread().start();
            kafkaThread().start();
            udpClientThread().start();
            sortingAndAveragingThread().start();

            // thread
            udpServerThread().join();
            kafkaThread().join();
            udpClientThread().join();
            sortingAndAveragingThread().join();


        } catch (SocketException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
