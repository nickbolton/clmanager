package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CityServiceAsync {
    
    public void getCityUrl(String name, AsyncCallback callback);
    public void subscribe(String username, String name, boolean state, AsyncCallback callback);
    
    /**
     */
    public void getCities(String username, AsyncCallback callback);
    
    /**
     */
    public void getSubscribedCities(String username, AsyncCallback callback);
}
