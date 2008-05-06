package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class ServiceResponse extends Model {
    
    public ServiceResponse() {
        setMinAge(0);
        setMaxAge(99);
        setNoFlagged(false);
        setPhotosOnly(false);
        setSearchTerm("");
    }
    
    public ServiceResponse(int minAge, int maxAge, boolean noFlagged, boolean photosOnly, String searchTerm) {
        setMinAge(minAge);
        setMaxAge(maxAge);
        setNoFlagged(noFlagged);
        setPhotosOnly(photosOnly);
        setSearchTerm(searchTerm);
    }
    
    public String toString() {
        return "PostFilter(minAge="+getMinAge()+", maxAge="+getMaxAge()+", "+
            "noFlagged="+getNoFlagged()+", photosOnly="+getPhotosOnly()+
            ", searchTerm="+getSearchTerm();
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
    
    public String getSearchTerm() {
        return (String)get("searchTerm");
    }
    
    public void setSearchTerm(String searchTerm) {
        set("searchTerm", searchTerm);
    }
    
}
