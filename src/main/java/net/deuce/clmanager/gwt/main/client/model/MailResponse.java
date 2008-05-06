package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class MailResponse extends Model {
    
    public MailResponse() {
    }
    
    public MailResponse(Long id, String message, Boolean status) {
        setClId(id);
        setMessage(message);
        setStatus(status);
    }
    
    public Long getClId() {
        return (Long)get("clId");
    }
    
    public void setClId(Long clId) {
        set("clId", clId);
    }
    public String getMessage() {
        return (String)get("message");
    }
    
    public void setMessage(String message) {
        set("message", message);
    }
    public Boolean getStatus() {
        return (Boolean)get("status");
    }
    
    public void setStatus(Boolean status) {
        set("status", status);
    }
}
