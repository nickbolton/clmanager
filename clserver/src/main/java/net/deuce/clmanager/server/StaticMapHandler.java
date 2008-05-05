package net.deuce.clmanager.server;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import Scrapeforge.ScrapeforgeOutputHandler;

public class StaticMapHandler implements ScrapeforgeOutputHandler {
    
    private static Map m = new HashMap();
    
    public static Map getMap() {
        return m;
    }
    
    public void outputValues(Hashtable ht) {
        m.clear();
        m.putAll(ht);
    }

    public void setup(Properties p) {
    }
    
}
