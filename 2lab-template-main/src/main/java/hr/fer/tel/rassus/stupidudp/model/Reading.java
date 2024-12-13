package hr.fer.tel.rassus.stupidudp.model;

import java.util.Objects;

public class Reading {

    private int NO2;

    public Reading() {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reading reading)) return false;
        return NO2 == reading.NO2;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(NO2);
    }

    @Override
    public String toString() {
        return "reading:{" +
                "no2=" + NO2 +
                '}';
    }
}
