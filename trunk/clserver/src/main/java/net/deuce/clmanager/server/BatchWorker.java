package net.deuce.clmanager.server;

public interface BatchWorker {
    public void queue(Worker w);
    public void flush();
}
