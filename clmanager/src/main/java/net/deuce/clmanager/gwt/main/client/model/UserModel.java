package net.deuce.clmanager.gwt.main.client.model;

import java.util.Map;
import java.util.Set;

import net.mygwt.ui.client.data.Model;

public class UserModel extends Model {
    
    public UserModel() {
    }
    
    public UserModel(Long id, String username) {
        setId(id);
        setUsername(username);
    }
    
    public Long getId() {
        return (Long)get("id");
    }
    public void setId(Long id) {
        set("id", id);
    }
    public String getUsername() {
        return (String)get("username");
    }
    public void setUsername(String username) {
        set("username", username);
    }
    public Set getCitySubscriptionFilter() {
        return (Set)get("citySubscriptionFilter");
    }
    public void setCitySubscriptionFilter(Set s) {
        set("citySubscriptionFilter", s);
    }
    public Set getCategorySubscriptionFilter() {
        return (Set)get("categorySubscriptionFilter");
    }
    public void setCategorySubscriptionFilter(Set s) {
        set("categorySubscriptionFilter", s);
    }
    public void addAll(Map m) {
        Map prefs = (Map)get("preferences");
        prefs.putAll(m);
    }
    public void addPreference(String name, String value) {
        Map m = (Map)get("preferences");
        m.put(name, value);
    }
    public String getPreference(String name) {
        return (String)getPreferences().get(name);
    }
    public Map getPreferences() {
        return (Map)get("preferences");
    }
    public Integer getIntegerPreference(String name) {
        String pref = getPreference(name);
        if (pref == null) return null;
        return Integer.valueOf(pref);
    }
    public void setPreferences(Map m) {
        set("preferences", m);
    }
}
