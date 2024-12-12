package hr.fer.tel.rassus.stupidudp.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.tel.rassus.stupidudp.model.Reading;
import hr.fer.tel.rassus.stupidudp.model.SensorPacket;

import java.util.List;

public class SensorPacketMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // From JSON string to SensorPacket object
    public static SensorPacket toSensorPacket(String jsonString) {
        // Deserialize JSON string to SensorPacket object
        try {
            return objectMapper.readValue(jsonString, SensorPacket.class);
        } catch (JsonProcessingException e) {
            System.err.println(jsonString);
            throw new RuntimeException(e);
        }
    }

    // From SensorPacket object to JSON string
    public static String toJson(SensorPacket sensorPacket) {
        // Serialize SensorPacket object to JSON string
        try {

            return objectMapper.writeValueAsString(sensorPacket);
        } catch (JsonProcessingException e) {
            System.err.println(sensorPacket.toString());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SensorPacket sensorPacket = new SensorPacket.Builder()
                .reading(new Reading(1))  // Assuming Reading has a constructor
                .vectorTime(List.of(1L, 2L, 3L))
                .scalarTime(100L)
                .build();

        String json = toJson(sensorPacket);
        System.out.println("SensorPacket to JSON: " + json);

        // Example of JSON to SensorPacket conversion
        String jsonString = "{\"reading\":{\"no2\":\"2\"},\"vectorTime\":[1,2,3],\"scalarTime\":100}";
        System.out.println(jsonString.getBytes().length);
        SensorPacket deserializedPacket = toSensorPacket(jsonString);
        System.out.println("JSON to SensorPacket: " + deserializedPacket);

    }
}
