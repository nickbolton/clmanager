package net.deuce.clmanager.server;

import java.util.LinkedList;
import java.util.List;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Post;

public class ScrapResult {
    private boolean succeeded = true;
    private Exception exception;
    private City city;
    private Category category;
    private Post post;
    private List<Post> posts = new LinkedList<Post>();
    
    public ScrapResult(City city, Category category) {
        this.city = city;
        this.category = category;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    
    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSucceeded(boolean b) {
        this.succeeded = b;
    }
    
    public boolean isSucceeded() {
        return succeeded;
    }
    
    public void setException(Exception e) {
        this.exception = e;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public City getCity() {
        return city;
    }
    
    public Category getCategory() {
        return category;
    }
}
