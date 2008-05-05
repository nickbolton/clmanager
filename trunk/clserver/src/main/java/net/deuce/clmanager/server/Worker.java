package net.deuce.clmanager.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker implements Runnable {
    
    private boolean running = true;
    private long refreshTime;
    private Log log = LogFactory.getLog(getClass());
    private Synchronizer synchronizer;
    
    public Worker() {
    }

    public void run() {
        while (running) {
            try {
                System.out.println("ZZZ working...");
                synchronizer.synchronize();
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {
                log.error(e);
                running = false;
            }
        }
        synchronizer.shutdown();
    }
    
    public void stop() {
        running = false;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    public void setSynchronizer(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

}
