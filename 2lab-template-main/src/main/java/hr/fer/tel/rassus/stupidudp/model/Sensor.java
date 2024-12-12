package hr.fer.tel.rassus.stupidudp.model;

import java.util.Objects;

public record Sensor(
        int id,
        String address,
        int port
) {
    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return id == sensor.id && port == sensor.port && Objects.equals(address, sensor.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, port);
    }
}
