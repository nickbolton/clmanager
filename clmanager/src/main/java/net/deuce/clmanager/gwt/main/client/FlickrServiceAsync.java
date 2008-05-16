package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.PhotoServiceResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlickrServiceAsync {
    public void getPhotos(String username, AsyncCallback callback);
}
