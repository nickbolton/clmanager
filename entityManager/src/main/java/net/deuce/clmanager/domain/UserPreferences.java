package net.deuce.clmanager.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String username;
    private String password;
    private Set<CitySubscription> citySubscriptions = new HashSet<CitySubscription>(0);
    private Set<CategorySubscription> categorySubscriptions = new HashSet<CategorySubscription>(0);
    private Set<Preference> preferences = new HashSet<Preference>(0);
    private Set<SavedFile> savedFiles = new HashSet<SavedFile>(0);
    private Set<MessageTemplate> messageTemplates = new HashSet<MessageTemplate>(0);
    private transient Map<String, Preference> preferenceMap = null;
    private transient Map<City, CitySubscription> citySubscriptionMap = null;
    private transient Map<Category, CategorySubscription> categorySubscriptionMap = null;
    private transient List<Long> favorites = null;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<CitySubscription> getCitySubscriptions() {
        return citySubscriptions;
    }
    public void setCitySubscriptions(Set<CitySubscription> citySubscriptions) {
        this.citySubscriptions = citySubscriptions;
        this.citySubscriptionMap = null;
    }
    public Set<CategorySubscription> getCategorySubscriptions() {
        return categorySubscriptions;
    }
    public void setCategorySubscriptions(Set<CategorySubscription> categorySubscriptions) {
        this.categorySubscriptions = categorySubscriptions;
        categorySubscriptionMap = null;

    }
    
    public Set<Preference> getPreferences() {
        return preferences;
    }
    
    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
        preferenceMap = null;
        favorites = null;
    }

    public void addPreference(Preference p) {
        preferences.add(p);
        if (preferenceMap != null) {
            preferenceMap.put(p.getName(), p);
        }
        favorites = null;
    }
    
    public void removePreference(String name) {
        Preference removedPreference = null;
        for (Preference p : preferences) {
            if (p.getName().equals(name)) {
                removedPreference = p;
                break;
            }
        }
        if (removedPreference != null) {
            preferences.remove(removedPreference);
            if (preferenceMap != null) {
                preferenceMap.remove(name);
            }
            favorites = null;
        }
    }
    
    public CitySubscription getCitySubscription(City city) {
        if (citySubscriptionMap == null) {
            citySubscriptionMap = new HashMap<City, CitySubscription>(citySubscriptions.size());
            for (CitySubscription cs : citySubscriptions) {
                citySubscriptionMap.put(cs.getCity(), cs);
            }
        }
        return citySubscriptionMap.get(city);
    }
    
    public CategorySubscription getCategorySubscription(Category category) {
        if (categorySubscriptionMap == null) {
            categorySubscriptionMap = new HashMap<Category, CategorySubscription>(categorySubscriptions.size());
            for (CategorySubscription cs : categorySubscriptions) {
                categorySubscriptionMap.put(cs.getCategory(), cs);
            }
        }
        return categorySubscriptionMap.get(category);
    }
    
    private Map<String, Preference> getPreferenceMap() {
        if (preferenceMap == null) {
            preferenceMap = new HashMap<String, Preference>(preferences.size());
            for (Preference p : preferences) {
                preferenceMap.put(p.getName(), p);
            }
        }
        return preferenceMap;
    }
    
    public Preference getPreference(String name) {
        return getPreferenceMap().get(name);
    }
    
    public String getPreferenceValue(String name) {
        Preference p = getPreferenceMap().get(name);
        if (p == null) return null;
        return p.getValue();
    }
    
    public int getFavoriteCount() {
        return getFavorites().size();
    }
    
    public List<Long> getFavorites() {
        if (favorites == null) {
            favorites = new LinkedList<Long>();
            for (Preference p : preferences) {
                if (p.getName().startsWith("fav:")) {
                    favorites.add(Long.parseLong(p.getValue()));
                }
            }
        }
        return favorites;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof UserPreferences) {
            b = username.equals(((UserPreferences)obj).getUsername());
        }
        return b;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
    public Set<SavedFile> getSavedFiles() {
        return savedFiles;
    }
    public void setSavedFiles(Set<SavedFile> savedFiles) {
        this.savedFiles = savedFiles;
    }
    public Set<MessageTemplate> getMessageTemplates() {
        return messageTemplates;
    }
    public void setMessageTemplates(Set<MessageTemplate> messageTemplates) {
        this.messageTemplates = messageTemplates;
    }

}
