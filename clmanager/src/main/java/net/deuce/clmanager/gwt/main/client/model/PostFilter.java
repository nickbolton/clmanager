package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class PostFilter extends Model {
    
    public PostFilter() {
        setMinAge(0);
        setMaxAge(99);
        setNoFlagged(false);
        setPhotosOnly(false);
        setUnreadOnly(false);
        setSearchTerm("");
    }
    
    public PostFilter(int minAge, int maxAge, boolean unreadOnly, boolean noFlagged, boolean photosOnly, String searchTerm) {
        setMinAge(minAge);
        setMaxAge(maxAge);
        setNoFlagged(noFlagged);
        setPhotosOnly(photosOnly);
        setUnreadOnly(unreadOnly);
        setSearchTerm(searchTerm);
    }
    
    public String toString() {
        return "PostFilter(minAge="+getMinAge()+", maxAge="+getMaxAge()+", "+
            "noFlagged="+getNoFlagged()+", photosOnly="+getPhotosOnly()+
            "unreadOnly="+getUnreadOnly()+", searchTerm="+getSearchTerm();
    }
    
    public int getMinAge() {
        return ((Integer)get("minAge")).intValue();
    }
    
    public void setMinAge(int minAge) {
        set("minAge", new Integer(minAge));
    }
    
    public int getMaxAge() {
        return ((Integer)get("maxAge")).intValue();
    }
    
    public void setMaxAge(int maxAge) {
        set("maxAge", new Integer(maxAge));
    }
    
    public boolean getNoFlagged() {
        return ((Boolean)get("noFlagged")).booleanValue();
    }
    
    public void setNoFlagged(boolean noFlagged) {
        set("noFlagged", new Boolean(noFlagged));
    }
    
    public boolean getPhotosOnly() {
        return ((Boolean)get("photosOnly")).booleanValue();
    }
    
    public void setPhotosOnly(boolean photosOnly) {
        set("photosOnly", new Boolean(photosOnly));
    }
    
    public boolean getUnreadOnly() {
        return ((Boolean)get("unreadOnly")).booleanValue();
    }
    
    public void setUnreadOnly(boolean unreadOnly) {
        set("unreadOnly", new Boolean(unreadOnly));
    }
    
    public String getSearchTerm() {
        return (String)get("searchTerm");
    }
    
    public void setSearchTerm(String searchTerm) {
        set("searchTerm", searchTerm);
    }
    
}
