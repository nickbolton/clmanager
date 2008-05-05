package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.PostModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface PostService extends RemoteService {
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.PostModel>
     */
    public List getPosts(String username, List postingGroups) throws Exception;
    public void flagPost(Long id) throws Exception;
    public void setViewed(String username, Long id) throws Exception;
    public void setReplied(String username, Long id) throws Exception;
    public void setAllViewed(String username, List postingGroups) throws Exception;
    public List getNewPosts(String username, List postingGroups) throws Exception;
    public Integer getNewPostCount(String username, List postingGroups) throws Exception;
    public PostModel getPost(String username, Long id) throws Exception;
    public PostModel getPostByClId(String username, long id) throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static PostServiceAsync instance;
		public static PostServiceAsync getInstance(){
			if (instance == null) {
				instance = (PostServiceAsync) GWT.create(PostService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
			}
			return instance;
		}
	}
}
