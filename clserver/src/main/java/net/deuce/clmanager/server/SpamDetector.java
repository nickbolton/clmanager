package net.deuce.clmanager.server;

public interface SpamDetector {
    public boolean isSuspicious(String s);
}
