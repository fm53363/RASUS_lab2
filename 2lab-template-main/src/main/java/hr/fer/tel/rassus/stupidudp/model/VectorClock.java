package hr.fer.tel.rassus.stupidudp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class VectorClock {
    private List<Long> vectorClock = new ArrayList<>();

    @JsonIgnore
    private int id;

    public VectorClock() {
    }

    public VectorClock(int id) {
        this.vectorClock = new ArrayList<>(id + 1);
        for (int i = 0; i <= id + 1; i++) {
            vectorClock.add(0L);
        }
        this.id = id;
    }

    public List<Long> getVector() {
        return vectorClock;
    }

    public void updateBeforeSending() {
        this.vectorClock.set(id, vectorClock.get(id) + 1);
    }

    public void updateAfterReceiving(VectorClock other) {
        if (other.vectorClock.size() > this.vectorClock.size()) {
            resizeVector(other.vectorClock.size());
        }
        for (int i = 0; i < this.vectorClock.size(); i++) {
            long old = (i < other.getVector().size()) ? other.getVector().get(i) : 0L;
            long newValue = Math.max(this.vectorClock.get(i), old);
            this.vectorClock.set(i, newValue);
        }
        this.updateBeforeSending();
    }


    public void resizeVector(int newSize) {
        // no resizing
        if (id >= 0 && id < vectorClock.size()) {
            return;
        }

        int start = vectorClock.size();
        for (int i = start; i < start + newSize; i++) {
            vectorClock.add(0L);
        }
    }


}
