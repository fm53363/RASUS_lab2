package hr.fer.tel.rassus.stupidudp.model;


import java.util.List;

public class SensorPacket {
    private Reading reading;
    private List<Long> vectorTime;
    private long scalarTime;

    private SensorPacket(Reading reading, List<Long> vectorTime, long scalarTime) {
        this.reading = reading;
        this.vectorTime = vectorTime;
        this.scalarTime = scalarTime;
    }

    public SensorPacket() {

    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public List<Long> getVectorTime() {
        return vectorTime;
    }

    public void setVectorTime(List<Long> vectorTime) {
        this.vectorTime = vectorTime;
    }

    public long getScalarTime() {
        return scalarTime;
    }

    public void setScalarTime(long scalarTime) {
        this.scalarTime = scalarTime;
    }

    public static class Builder {
        private Reading reading;
        private List<Long> vectorTime;
        private long scalarTime;

        public Builder reading(Reading reading) {
            this.reading = reading;
            return this;
        }

        public Builder vectorTime(List<Long> vectorTime) {
            this.vectorTime = vectorTime;
            return this;
        }

        public Builder scalarTime(long scalarTime) {
            this.scalarTime = scalarTime;
            return this;
        }

        public SensorPacket build() {
            SensorPacket sensorPacket = new SensorPacket(reading, vectorTime, scalarTime);
            return sensorPacket;

        }

    }


}
