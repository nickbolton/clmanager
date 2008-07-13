package net.deuce.clmanager.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RefreshingProperties extends Properties {
    
    private static final long serialVersionUID = 1L;
    
    private File file;
    private Log log = LogFactory.getLog(getClass());
    private Worker worker;

    public RefreshingProperties(File file) {
        this.file = file;
        refresh();
        worker = new Worker();
        new Thread(worker).start();
    }
    
    private void refresh() {
        try {
            FileInputStream fis = new FileInputStream(file);
            load(fis);
            if (log.isDebugEnabled()) {
                StringBuffer sb = new StringBuffer("\nrefreshing properties:\n");
                Enumeration en = keys();
                while (en.hasMoreElements()) {
                    String key = (String)en.nextElement();
                    sb.append("\t"+key+'='+getProperty(key)+'\n');
                }
                log.debug(sb.toString());
            }
        } catch (IOException ioe) {
            log.error(ioe);
        }
    }
    
    private class Worker implements Runnable {
        private boolean running = true;
        public void run() {
            while (running) {
                try {
                    Thread.sleep(60000);
                    refresh();
                } catch (InterruptedException e) {
                    log.error(e);
                    running = false;
                }
            }
        }
        
        public void stop() {
            running = false;
        }
    }
}
