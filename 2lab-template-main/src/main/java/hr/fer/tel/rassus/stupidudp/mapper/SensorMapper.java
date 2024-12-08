package hr.fer.tel.rassus.stupidudp.mapper;

import hr.fer.tel.rassus.stupidudp.model.Sensor;
import org.json.JSONObject;


public class SensorMapper {

    // From JSON to Sensor
    public static Sensor toSensor(JSONObject json) {
        int id = json.getInt("id");
        String address = json.getString("address");
        int port = json.getInt("port");

        return new Sensor(id, address, port);
    }

    // From Sensor to JSON
    public static JSONObject toJson(Sensor sensor) {
        JSONObject json = new JSONObject();
        json.put("id", sensor.id());
        json.put("address", sensor.address());
        json.put("port", sensor.port());

        return json;
    }
    public static String toString(Sensor sensor) {
        return toJson(sensor).toString();
    }

    public static void main(String[] args) {
        // Example of Sensor to JSON conversion
        Sensor sensor = new Sensor(1, "192.168.0.1", 8080);
        JSONObject json = toJson(sensor);
        System.out.println("Sensor to JSON: " + json.toString());

        // Example of JSON to Sensor conversion
        String jsonString = "{\"id\":1,\"address\":\"192.168.0.1\",\"port\":\"8080\"}";
        JSONObject jsonFromString = new JSONObject(jsonString);
        Sensor sensorFromJson = toSensor(jsonFromString);
        System.out.println("JSON to Sensor: " + sensorFromJson);
    }
}
