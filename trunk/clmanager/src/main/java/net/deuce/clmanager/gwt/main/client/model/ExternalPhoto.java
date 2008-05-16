package net.deuce.clmanager.gwt.main.client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExternalPhoto implements IsSerializable {
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
        this);
    private String id;
    private String thumbnail;
    private String standard;
    private String title;
    private String service;
    
    public ExternalPhoto() {
        
    }
    
    public ExternalPhoto(String id, String service) {
        setId(id);
        setService(service);
    }
    
    public ExternalPhoto(String id, String service, String title, String standard, String thumbnail) {
        setId(id);
        setService(service);
        setTitle(title);
        setStandard(standard);
        setThumbnail(thumbnail);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(
        String propertyName, PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, l);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(
        String propertyName, PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(
            propertyName, l);
    }
    
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.propertyChangeSupport.firePropertyChange(
            "thumbnail", this.thumbnail, this.thumbnail = thumbnail);
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.propertyChangeSupport.firePropertyChange(
            "standard", this.standard, this.standard = standard);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.propertyChangeSupport.firePropertyChange(
            "title", this.title, this.title = title);
    }
    
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.propertyChangeSupport.firePropertyChange(
            "service", this.service, this.service = service);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.propertyChangeSupport.firePropertyChange(
            "id", this.id, this.id = id);
    }
}