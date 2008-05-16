package net.deuce.clmanager.gwt.main.client.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoServiceResponse extends ServiceResponse {
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.PhotoWrapper>
     */
    private List photos = new ArrayList(0);

    public PhotoServiceResponse() {
        super();
    }
    
    /**
     * @gwt.typeArgs photos <net.deuce.clmanager.gwt.main.client.model.PhotoWrapper>
     */
    public PhotoServiceResponse(List photos) {
        this();
        this.photos = photos;
    }

    public PhotoServiceResponse(String errorMessage) {
        super(errorMessage);
    }

    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.PhotoWrapper>
     */
    public List getPhotos() {
        return photos;
    }
    
    /**
     * @gwt.typeArgs photos <net.deuce.clmanager.gwt.main.client.model.PhotoWrapper>
     */
    public void setPhotos(List photos) {
        this.photos = photos;
    }
}
