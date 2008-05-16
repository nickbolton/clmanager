package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class ImageModel extends Model {
    
    public ImageModel() {
    }
    
    public ImageModel(Long id, String url, String path) {
        setId(id);
        setUrl(url);
        setPath(path);
    }
    
    public Long getId() {
        return (Long)get("id");
    }
    public void setId(Long id) {
        set("id", id);
    }
    public String getPath() {
        return (String)get("path");
    }
    public void setPath(String path) {
        set("path", path);
    }
    public String getUrl() {
        return (String)get("url");
    }
    public void setUrl(String url) {
        set("url", url);
    }
    public String getThumbnail() {
        return (String)get("thumbnail");
    }
    public void setThumbnail(String thumbnail) {
        set("thumbnail", thumbnail);
    }
}
