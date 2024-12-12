package hr.fer.tel.rassus.stupidudp.model;


import java.util.ArrayList;
import java.util.List;

public class  VectorClock {
    private List<Long> vectorClock = new ArrayList<>();

    public VectorClock() {
        this.vectorClock = new ArrayList<>();
    }

    public List<Long> getVector() {
        return vectorClock;
    }

    public VectorClock updateVector(int id) {
        // update existing vector time
        if (id >= 0 && id < vectorClock.size()) {
            long old = vectorClock.get(id) == null ? -1 : vectorClock.get(id);
            vectorClock.set(id,  old+1);  // Just an example of setting it
        }else{
            var updatedVectorClock = new ArrayList<Long>();
            for (int i = 0; i < id; i++) {
                try {
                    long old = vectorClock.get(i);
                    updatedVectorClock.add(old);
                } catch(Exception e) {
                    updatedVectorClock.add(null);
                }
            }
            updatedVectorClock.add(0L);
            vectorClock = updatedVectorClock;
        }
        return this;

    }



}
