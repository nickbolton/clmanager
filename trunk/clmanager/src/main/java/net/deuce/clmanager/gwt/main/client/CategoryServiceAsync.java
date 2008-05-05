package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CategoryServiceAsync {
    
    /**
     */
    public void getCategories(String username, AsyncCallback callback);
    /**
     */
    public void getSubscribedCategories(String username, AsyncCallback callback);
    public void subscribe(String username, String name, boolean state, AsyncCallback callback);
}
