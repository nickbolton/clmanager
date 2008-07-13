package net.deuce.cl.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

public class PostCache {

    private Map<String, List<Post>> cache = Collections.synchronizedMap(new HashMap<String, List<Post>>());
    
    public List<Post> get(UserPreferences prefs) {
        return cache.get(generateKey(prefs));
    }
    
    public void put(UserPreferences prefs, List<Post> posts) {
        cache.put(generateKey(prefs), posts);
    }
    
    public void remove(UserPreferences prefs) {
        cache.remove(generateKey(prefs));
    }
    
    private String generateKey(UserPreferences prefs) {
        Preference p = prefs.getPreference("query");
        if (p == null) {
            return prefs.getUsername();
        }
        return prefs.getUsername()+"-"+p.getValue();
    }
}
