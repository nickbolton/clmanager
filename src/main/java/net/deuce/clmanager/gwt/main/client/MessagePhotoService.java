package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface MessagePhotoService extends RemoteService {
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.ImageModel>
     */
    public List getMessagePhotos() throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MessagePhotoServiceAsync instance;
		public static MessagePhotoServiceAsync getInstance(){
			if (instance == null) {
				instance = (MessagePhotoServiceAsync) GWT.create(MessagePhotoService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessagePhotoSerivce");
			}
			return instance;
		}
	}
}
