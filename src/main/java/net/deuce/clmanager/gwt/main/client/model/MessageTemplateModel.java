package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class MessageTemplateModel extends Model {
    
    public MessageTemplateModel() {
    }
    
    public MessageTemplateModel(Long id, String name, String message, String categoryName) {
        setId(id);
        setName(name);
        setMessage(message);
        setCategoryName(categoryName);
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
    public String getMessage() {
        return (String)get("message");
    }
    public void setMessage(String message) {
        set("message", message);
    }
    public String getCategoryName() {
        return (String)get("categoryName");
    }
    public void setCategoryName(String categoryName) {
        set("categoryName", categoryName);
    }
}
