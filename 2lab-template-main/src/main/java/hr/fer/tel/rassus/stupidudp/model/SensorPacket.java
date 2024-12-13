package hr.fer.tel.rassus.stupidudp.model;


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
}
