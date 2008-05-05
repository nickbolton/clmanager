package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImageServiceAsync {
    
    /**
     */
    public void getImages(Long postId, AsyncCallback callback);
}
