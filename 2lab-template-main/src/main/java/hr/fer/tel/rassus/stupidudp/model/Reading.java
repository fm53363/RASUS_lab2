package hr.fer.tel.rassus.stupidudp.model;

public class Reading {

    private int NO2;

    public Reading(){

    }

    public Reading(int NO2) {
        this.NO2 = NO2;
    }

    public int getNO2() {
        return NO2;
    }

    public void setNO2(int NO2) {
        this.NO2 = NO2;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "NO2=" + NO2 +
                '}';
    }
}
