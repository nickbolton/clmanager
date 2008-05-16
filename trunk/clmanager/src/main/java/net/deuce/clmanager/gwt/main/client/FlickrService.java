package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.PhotoServiceResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface FlickrService extends RemoteService {
    public PhotoServiceResponse getPhotos(String username);
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static FlickrServiceAsync instance;
		public static FlickrServiceAsync getInstance(){
			if (instance == null) {
				instance = (FlickrServiceAsync) GWT.create(FlickrService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "FlickrService");
			}
			return instance;
		}
	}
}
