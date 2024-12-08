package hr.fer.tel.rassus.stupidudp.model;

import java.util.Random;

public class SensorFactory {

    public static Sensor createSensor(String address, int port) {
        Random rand = new Random();
        int id = rand.nextInt(1000);
        return new Sensor(id, address, port);

    }
}
