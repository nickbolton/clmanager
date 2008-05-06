package net.deuce.clmanager.gwt.main.client.model;

import java.util.Date;

import net.mygwt.ui.client.data.Model;

public class PostModel extends Model {
    
    public PostModel() {
    }
    
    public Long getId() {
        return (Long)get("id");
    }
    
    public void setId(Long id) {
        set("id", id);
    }
    public Long getClId() {
        return (Long)get("clId");
    }
    
    public void setClId(Long clId) {
        set("clId", clId);
    }
    public String getTitle() {
        return (String)get("title");
    }
    
    public void setTitle(String title) {
        set("title", title);
    }
    public String getUrl() {
        return (String)get("url");
    }
    
    public void setUrl(String url) {
        set("url", url);
    }
    public String getAge() {
        return (String)get("age");
    }
    
    public void setAge(String age) {
        set("age", age);
    }
    public String getContent() {
        return (String)get("content");
    }
    
    public void setContent(String content) {
        set("content", content);
    }
    public String getLocation() {
        return (String)get("location");
    }
    
    public void setLocation(String location) {
        set("location", location);
    }
    public String getReplyAddress() {
        return (String)get("replyAddress");
    }
    
    public void setReplyAddress(String replyAddress) {
        set("replyAddress", replyAddress);
    }
    public Date getDate() {
        return (Date)get("date");
    }
    
    public void setDate(Date date) {
        if (date != null) {
            set("date", new Date(date.getTime()));
        }
    }
    public Boolean isFlagged() {
        return (Boolean)get("flagged");
    }
    
    public void setFlagged(Boolean flagged) {
        set("flagged", flagged);
    }
    public Boolean isPic() {
        return (Boolean)get("pic");
    }
    
    public void setPic(Boolean pic) {
        set("pic", pic);
    }
    public Boolean isFetched() {
        return (Boolean)get("fetched");
    }
    
    public void setFetched(Boolean fetched) {
        set("fetched", fetched);
    }
    public Boolean isResponded() {
        return (Boolean)get("responded");
    }
    
    public void setResponded(Boolean responded) {
        set("responded", responded);
    }
    public Boolean isViewed() {
        return (Boolean)get("viewed");
    }
    
    public void setViewed(Boolean viewed) {
        set("viewed", viewed);
    }
    public Boolean isSpam() {
        return (Boolean)get("spam");
    }
    
    public void setSpam(Boolean spam) {
        set("spam", spam);
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
}
