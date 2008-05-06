package net.deuce.clmanager.gwt.main.client;

import java.util.Map;

import net.deuce.clmanager.gwt.main.client.model.UserModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
    
    public void getUser(String username, AsyncCallback callback);
    public void addCityFilter(String username, String cityName, AsyncCallback callback);
    public void removeCityFilter(String username, String cityName, AsyncCallback callback);
    public void clearCityFilters(String username, AsyncCallback callback);
    public void addCategoryFilter(String username, String categoryName, AsyncCallback callback);
    public void removeCategoryFilter(String username, String categoryName, AsyncCallback callback);
    public void clearCategoryFilters(String username, AsyncCallback callback);
    public void setPreference(String username, String name, String value, AsyncCallback callback);
    public void deletePreference(String username, String name, AsyncCallback callback);
    public void setPreferences(String username, Map m, AsyncCallback callback);
}
