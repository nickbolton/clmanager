package net.deuce.clmanager.domain;
// Generated Apr 11, 2008 1:02:39 PM by Hibernate Tools 3.2.0.CR1



/**
 * City generated by hbm2java
 */
public class CitySubscription implements java.io.Serializable {

    private Long id;
    private UserPreferences userPreferences;
    private boolean subscribed;
    private boolean filtered;
    private City city;
    
    public CitySubscription() {
    }

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
    
    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof CitySubscription) {
            CitySubscription that = (CitySubscription)obj;
            b = userPreferences.equals(that.getUserPreferences()) && city.equals(that.getCity());
        }
        return b;
    }

    @Override
    public int hashCode() {
        return userPreferences.hashCode()+city.hashCode();
    }

}


