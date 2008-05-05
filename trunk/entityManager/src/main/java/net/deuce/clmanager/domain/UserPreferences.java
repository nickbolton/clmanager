package net.deuce.clmanager.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserPreferences {

    private Long id;
    private String username;
    private Set<CitySubscription> citySubscriptions = new HashSet<CitySubscription>(0);
    private Set<CategorySubscription> categorySubscriptions = new HashSet<CategorySubscription>(0);
    private Set<Preference> preferences = new HashSet<Preference>(0);
    private transient Map<String, Preference> preferenceMap = null;
    private transient Map<City, CitySubscription> citySubscriptionMap = null;
    private transient Map<Category, CategorySubscription> categorySubscriptionMap = null;
    
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
    }

    public void addPreference(Preference p) {
        preferences.add(p);
        if (preferenceMap != null) {
            preferenceMap.put(p.getName(), p);
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

}
