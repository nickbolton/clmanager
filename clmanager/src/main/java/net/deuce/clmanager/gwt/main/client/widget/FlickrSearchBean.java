package net.deuce.clmanager.gwt.main.client.widget;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.FlickrService;
import net.deuce.clmanager.gwt.main.client.FlickrServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.PhotoServiceResponse;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.Registry;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.totsp.gwittir.client.beans.Bindable;

public class FlickrSearchBean implements Bindable {
    private final String MODAL_ORIGINATOR = "FlickrSearchBean::getPhotos";
    private static final FlickrServiceAsync SERVICE = (FlickrServiceAsync) GWT.create( FlickrService.class );
    static {
        ((ServiceDefTarget) SERVICE).setServiceEntryPoint( GWT.getModuleBaseURL()+"FlickrService");
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String username;
    private List photos;
    private AsyncCallback callback = new AsyncCallback(){
        public void onFailure(Throwable t) {
            Utils.clearModal(MODAL_ORIGINATOR);
            Debug.println(Utils.getStacktraceAsString(t));
        }

        public void onSuccess(Object result) {
            Utils.clearModal(MODAL_ORIGINATOR);
            PhotoServiceResponse response = (PhotoServiceResponse)result;
            if (response.isOk()) {
                setPhotos(response.getPhotos());
            } else if (response.getErrorMessage().trim().length() > 0) {
                Utils.errorMessage(response.getErrorMessage().trim());
            } else {
                Utils.errorMessage("Flickr service unavailable at this time.");
            }
        }

    };

    public FlickrSearchBean(){
    }

    public String getUsername() {
        return username;
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

    public List getPhotos() {
        if (photos == null) {
            UserModel user = (UserModel)Registry.get("user");
            String flickrUsername = user.getPreference("flickrUsername");
            flickrUsername = "badshowersinger";
            if (flickrUsername != null && flickrUsername.length() > 0) {
                
                Utils.goModal(MODAL_ORIGINATOR, "Retrieving photos from Flickr...");
                SERVICE.getPhotos(flickrUsername, callback);
            }
        }
        return photos;
    }

    public void setPhotos(List photos) {
        this.propertyChangeSupport.firePropertyChange("photos", this.photos, this.photos = photos);
    }
}
