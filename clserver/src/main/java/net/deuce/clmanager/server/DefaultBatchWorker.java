package net.deuce.clmanager.server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultBatchWorker implements BatchWorker {
    
    private int numConcurrentWorkers;
    private List<Worker> queue = Collections.synchronizedList(new LinkedList<Worker>());
    private List<Worker> running = new LinkedList<Worker>();
    
    public void flush() {
    }
    
    private void startWorker(Worker w) {
        running.add(w);
        new Thread(w).start();
    }

    public void queue(Worker w) {
        queue.add(w);
    }

    public int getNumConcurrentWorkers() {
        return numConcurrentWorkers;
    }

    public void setNumConcurrentWorkers(int numConcurrentWorkers) {
        this.numConcurrentWorkers = numConcurrentWorkers;
    }

    private class Runner implements Runnable {
        public void run() {
            
        }
    }
}
