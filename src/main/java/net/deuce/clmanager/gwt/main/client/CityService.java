package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface CityService extends RemoteService {
    
    public String getCityUrl(String name) throws Exception;
    public void subscribe(String username, String name, boolean state) throws Exception;
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.CityModel>
     */
    public List getCities(String username) throws Exception;
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.CityModel>
     */
    public List getSubscribedCities(String username) throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CityServiceAsync instance;
		public static CityServiceAsync getInstance(){
			if (instance == null) {
				instance = (CityServiceAsync) GWT.create(CityService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CityService");
			}
			return instance;
		}
	}
}
