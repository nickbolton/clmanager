package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class PhotoWrapper extends Model {
    
    public PhotoWrapper() {
    }
    
    public PhotoWrapper(Long id, String service, String externalId, String url) {
        setId(id);
        setService(service);
        setExternalId(externalId);
        setUrl(url);
    }
    
    public Long getId() {
        return (Long)get("id");
    }
    public void setId(Long id) {
        set("id", id);
    }
    public String getService() {
        return (String)get("service");
    }
    public void setService(String service) {
        set("service", service);
    }
    public String getExternalId() {
        return (String)get("externalId");
    }
    public void setExternalId(String externalId) {
        set("externalId", externalId);
    }
    public String getUrl() {
        return (String)get("url");
    }
    public void setUrl(String url) {
        set("url", url);
    }
}
