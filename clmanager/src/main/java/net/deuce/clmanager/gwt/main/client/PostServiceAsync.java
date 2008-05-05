package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.PostModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PostServiceAsync {
    
    /**
     */
    public void getPosts(String username, List postingGroups, AsyncCallback callback);
    public void flagPost(Long id, AsyncCallback callback);
    public void setViewed(String username, Long id, AsyncCallback callback);
    public void setReplied(String username, Long id, AsyncCallback callback);
    public void setAllViewed(String username, List postingGroups, AsyncCallback callback);
    public void getNewPosts(String username, List postingGroups, AsyncCallback callback);
    public void getNewPostCount(String username, List postingGroups, AsyncCallback callback);
    public void getPost(String username, Long id, AsyncCallback callback);
    public void getPostByClId(String username, long id, AsyncCallback callback);
}
