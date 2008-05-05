package net.deuce.clmanager.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

public class DefaultSpamDetector implements SpamDetector {
    
    private Properties blacklist;
    private Log log = LogFactory.getLog(getClass());
    private Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
    private Pattern tagPattern1 = Pattern.compile("<([a-zA-Z0-9]+)/?>");
    private Pattern tagPattern2 = Pattern.compile("<([a-zA-Z0-9]+) ");
    
    public DefaultSpamDetector() {
        ClassPathResource cpr = new ClassPathResource("/properties/spam-blacklist.properties");
        try {
            blacklist = new RefreshingProperties(cpr.getFile());
        } catch (IOException e) {
            log.error(e);
        }
    }
    
    private Pattern getPattern(String exp) {
        Pattern p = patternMap.get(exp.toLowerCase());
        if (p == null) {
            p = Pattern.compile(exp.toLowerCase());
        }
        return p;
    }
    
    public boolean isSuspicious(String raw) {
        if (blacklist == null) {
            log.warn("blacklist not initialized. check /properties/spam-blacklist.properties.");
        }
        String lowerCase = raw.toLowerCase();
        Iterator itr = blacklist.keySet().iterator();
        boolean suspicious = false;
        while (!suspicious && itr.hasNext()) {
            String key = (String)itr.next();
            if ("maxConsecutiveCapitalWords".equals(key) || "validTags".equals(key)) continue;
            
            String exp = blacklist.getProperty(key);
            String[] split = exp.split(",");
            for (int i=0; !suspicious && i<split.length; i++) {
                Pattern p = getPattern(split[i].trim());
                Matcher m = p.matcher(lowerCase);
                suspicious = m.find();
            }
        }
        if (suspicious) return true;
        
        String[] validTags = blacklist.getProperty("validTags").split(",");
        Matcher m = tagPattern1.matcher(lowerCase);
        while (!suspicious && m.find()) {
            String tag = m.group(1).toLowerCase();
            boolean foundValid = false;
            for (int i=0; !foundValid && i<validTags.length; i++) {
                foundValid = validTags[i].toLowerCase().equals(tag);
            }
            suspicious = !foundValid;
        }
        if (suspicious) return true;
        
        m = tagPattern2.matcher(lowerCase);
        while (!suspicious && m.find()) {
            String tag = m.group(1).toLowerCase();
            boolean foundValid = false;
            for (int i=0; !foundValid && i<validTags.length; i++) {
                foundValid = validTags[i].toLowerCase().equals(tag);
            }
            suspicious = !foundValid;
        }
        if (suspicious) return true;
        
        String[] split = raw.split(" ");
        int consecutive = 0;
        int maxConsecutiveCapitalWords = Integer.valueOf(blacklist.getProperty("maxConsecutiveCapitalWords"));
        for (int i=0; !suspicious && i<split.length; i++) {
            String trimTok = split[i].trim();
            if (trimTok.length() == 0) {
                consecutive = 0;
                continue;
            }
            if (trimTok.toUpperCase().equals(trimTok)) {
                consecutive++;
                suspicious = consecutive > maxConsecutiveCapitalWords;
            } else {
                consecutive = 0;
            }
        }
        
        return suspicious;
    }

}
