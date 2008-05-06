package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface CategoryService extends RemoteService {
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.CategoryModel>
     */
    public List getCategories(String username) throws Exception;
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.CategoryModel>
     */
    public List getSubscribedCategories(String username) throws Exception;
    public void subscribe(String username, String name, boolean state) throws Exception;

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CategoryServiceAsync instance;
		public static CategoryServiceAsync getInstance(){
			if (instance == null) {
				instance = (CategoryServiceAsync) GWT.create(CategoryService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
			}
			return instance;
		}
	}
}
