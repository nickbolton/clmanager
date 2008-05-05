package net.deuce.clmanager.domain;

// Generated Apr 11, 2008 1:02:39 PM by Hibernate Tools 3.2.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * MessageTemplate generated by hbm2java
 */
public class MessageTemplate implements java.io.Serializable {

    private Long id;
    private UserPreferences userPreferences;
    private String name;
    private String message;
    private Category defaultCategory;
    private Set photos = new HashSet(0);

    public MessageTemplate() {
    }

    public MessageTemplate(String name, String message, Category defaultCategory, Set photos) {
        this.name = name;
        this.message = message;
        this.defaultCategory = defaultCategory;
        if (photos != null) {
            this.photos = photos;
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set photos) {
        this.photos = photos;
    }

    public Category getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Category defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    @Override
    public boolean equals(Object obj) {
        if (name != null && userPreferences != null) {
            boolean b = false;
            if (obj != null && obj instanceof MessageTemplate) {
                MessageTemplate that = (MessageTemplate)obj;
                b = name.equals(that.getName()) &&
                    userPreferences.equals(that.getUserPreferences());
            }
            return b;
        } 
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (name != null && userPreferences != null) {
            return name.hashCode()+userPreferences.hashCode();
        }
        return super.hashCode();
    }
}
