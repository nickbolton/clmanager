package net.deuce.clmanager.gwt.main.client;

import java.util.Map;

import net.deuce.clmanager.gwt.main.client.model.UserModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface UserService extends RemoteService {
    
    public UserModel getUser(String username) throws Exception;
    public void addCityFilter(String username, String cityName) throws Exception;
    public void removeCityFilter(String username, String cityName) throws Exception;
    public void addCategoryFilter(String username, String categoryName) throws Exception;
    public void removeCategoryFilter(String username, String categoryName) throws Exception;
    public void setPreference(String username, String name, String value) throws Exception;
    public void deletePreference(String username, String name) throws Exception;
    public void setPreferences(String username, Map m) throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static UserServiceAsync instance;
		public static UserServiceAsync getInstance(){
			if (instance == null) {
				instance = (UserServiceAsync) GWT.create(UserService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
			}
			return instance;
		}
	}
}
