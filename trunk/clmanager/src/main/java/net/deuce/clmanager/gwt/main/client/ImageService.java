package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ImageService extends RemoteService {
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.ImageModel>
     */
    public List getImages(Long postId) throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ImageServiceAsync instance;
		public static ImageServiceAsync getInstance(){
			if (instance == null) {
				instance = (ImageServiceAsync) GWT.create(ImageService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "ImageService");
			}
			return instance;
		}
	}
}
