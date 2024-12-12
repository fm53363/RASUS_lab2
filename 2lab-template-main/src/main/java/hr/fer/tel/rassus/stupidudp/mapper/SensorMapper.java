package hr.fer.tel.rassus.stupidudp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.tel.rassus.stupidudp.model.Sensor;

public class SensorMapper {

    // Jackson ObjectMapper instance
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // From JSON string to Sensor object
    public static Sensor toSensor(String jsonString) {
        // Deserialize JSON string to Sensor object
        try {
            return objectMapper.readValue(jsonString, Sensor.class);
        } catch (JsonProcessingException e) {
            System.err.println(jsonString);
            throw new RuntimeException(e);
        }
    }

    // From Sensor object to JSON string
    public static String toJson(Sensor sensor) {
        // Serialize Sensor object to JSON string
        try {
            return objectMapper.writeValueAsString(sensor);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Main method for testing the functionality
    public static void main(String[] args) {
        // Example of Sensor to JSON conversion
        Sensor sensor = new Sensor(1, "192.168.0.1", 8080);
        String json = toJson(sensor);
        System.out.println("Sensor to JSON: " + json);

        // Example of JSON to Sensor conversion
        String jsonString = "{\"id\":1,\"address\":\"192.168.0.1\",\"port\":8080}";
        Sensor sensorFromJson = toSensor(jsonString);
        System.out.println("JSON to Sensor: " + sensorFromJson);

    }
}
