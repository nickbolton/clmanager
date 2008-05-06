package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class PostingGroup extends Model {
    
    public PostingGroup() {
    }
    
    public PostingGroup(String city, String category) {
        setCity(city);
        setCategory(category);
        setActive(true);
        setLastFetched(new Long(0L));
    }
    
    public boolean isActive() {
        return ((Boolean)get("active")).booleanValue();
    }
    public void setActive(boolean b) {
        set("active", new Boolean(b));
    }
    public Long getLastFetched() {
        return (Long)get("lastFetched");
    }
    public void setLastFetched(Long lastFetched) {
        set("lastFetched", lastFetched);
    }
    public String getCity() {
        return (String)get("city");
    }
    public void setCity(String city) {
        set("city", city);
    }
    public String getCategory() {
        return (String)get("category");
    }
    public void setCategory(String category) {
        set("category", category);
    }
    
    public String toString() {
        return "("+getCity()+", "+getCategory()+", "+isActive()+", "+getLastFetched()+")";
    }
}
