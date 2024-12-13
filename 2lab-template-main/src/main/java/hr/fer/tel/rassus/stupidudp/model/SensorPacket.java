package hr.fer.tel.rassus.stupidudp.model;


import java.util.Objects;

public class SensorPacket {
    private Reading reading;
    private VectorClock vectorClock;
    private long scalarClock;

    public SensorPacket() {
    }

    public SensorPacket(Reading reading, VectorClock vectorClock, long scalarClock) {
        this.reading = reading;
        this.vectorClock = vectorClock;
        this.scalarClock = scalarClock;
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public void setVectorClock(VectorClock vectorClock) {
        this.vectorClock = vectorClock;
    }

    public long getScalarClock() {
        return scalarClock;
    }

    public void setScalarClock(long scalarClock) {
        this.scalarClock = scalarClock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorPacket that)) return false;
        return scalarClock == that.scalarClock && Objects.equals(reading, that.reading) && Objects.equals(vectorClock, that.vectorClock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reading, vectorClock, scalarClock);
    }

    @Override
    public String toString() {
        return "SensorPacket{" +
                "reading=" + reading +
                ", vectorClock=" + vectorClock +
                ", scalarClock=" + scalarClock +
                '}';
    }
}
