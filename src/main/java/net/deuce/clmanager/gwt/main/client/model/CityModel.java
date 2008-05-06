package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class CityModel extends Model {
    
    public CityModel() {
    }
    
    public CityModel(Long id, String name, String url, Boolean subscribed, Boolean filtered) {
        setId(id);
        setName(name);
        setUrl(url);
        setSubscribed(subscribed);
        setFiltered(filtered);
    }
    
    public Long getId() {
        return (Long)get("id");
    }
    public void setId(Long id) {
        set("id", id);
    }
    public String getName() {
        return (String)get("name");
    }
    public void setName(String name) {
        set("name", name);
    }
    public String getUrl() {
        return (String)get("url");
    }
    public void setUrl(String url) {
        set("url", url);
    }
    public Boolean isSubscribed() {
        return (Boolean)get("subscribed");
    }
    public void setSubscribed(Boolean subscribed) {
        set("subscribed", subscribed);
    }
    public Boolean isFiltered() {
        return (Boolean)get("filtered");
    }
    public void setFiltered(Boolean filtered) {
        set("filtered", filtered);
    }
 
}
