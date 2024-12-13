package hr.fer.tel.rassus.stupidudp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VectorClock {
    private List<Long> vectorClock = new ArrayList<>();

    @JsonIgnore
    private int id;

    public VectorClock() {
    }

    public VectorClock(int id) {
        this.vectorClock = new ArrayList<>(id + 1);
        for (int i = 0; i <= id; i++) {
            vectorClock.add(0L);
        }
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Long> getVector() {
        return vectorClock;
    }

    public void updateBeforeSending() {
        System.out.println("    before sending:" + this.getVector());
        this.vectorClock.set(id, vectorClock.get(id) + 1);
        System.out.println("    after sending:" + this.getVector());
    }

    public void updateAfterReceiving(VectorClock other) {
        System.out.println("    before receiving:" + this.getVector());
        if (other.vectorClock.size() > this.vectorClock.size()) {
            this.resizeVector(other.vectorClock.size());
        }
        for (int i = 0; i < this.vectorClock.size(); i++) {
            long old = (i < other.getVector().size()) ? other.getVector().get(i) : 0L;
            long newValue = Math.max(this.vectorClock.get(i), old);
            this.vectorClock.set(i, newValue);
        }
        this.vectorClock.set(id, vectorClock.get(id) + 1);

        System.out.println("    after receiving:" + this.getVector());
    }


    public void resizeVector(int newSize) {
        // no resizing
        if (newSize >= 0 && newSize <= vectorClock.size()) {
            return;
        }
        int start = vectorClock.size();
        for (int i = start; i < newSize; i++) {
            vectorClock.add(0L);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VectorClock clock)) return false;
        return Objects.equals(vectorClock, clock.vectorClock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vectorClock);
    }

    @Override
    public String toString() {
        return "VectorClock{" +
                "vectorClock=" + vectorClock +
                '}';
    }
}
