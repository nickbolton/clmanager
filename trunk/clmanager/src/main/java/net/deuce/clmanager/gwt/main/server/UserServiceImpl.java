package net.deuce.clmanager.gwt.main.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.model.UserModel;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    public UserServiceImpl() {
    }

    protected void updateUserPreferences(UserPreferences userPreferences)
        throws Exception {
        getUserPreferencesDao().update(userPreferences);
    }

    protected UserPreferences getUserPreferences(String username)
        throws Exception {
        UserPreferences userPreferences = getUserPreferencesDao()
            .findUniqueByUsername(username);
        if (userPreferences == null) {
            userPreferences = new UserPreferences();
            userPreferences.setUsername(username);
            getUserPreferencesDao().create(userPreferences);
        }
        return userPreferences;
    }

    public UserModel getUser(String username) throws Exception {
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            UserModel model = new UserModel(userPreferences.getId(),
                userPreferences.getUsername());
            Set<String> s = new HashSet<String>();
            for (CitySubscription cs : userPreferences.getCitySubscriptions()) {
                if (cs.isFiltered()) {
                    s.add(cs.getCity().getName());
                }
            }
            model.setCitySubscriptionFilter(s);
            s = new HashSet<String>();
            for (CategorySubscription cs : userPreferences
                .getCategorySubscriptions()) {
                if (cs.isFiltered()) {
                    s.add(cs.getCategory().getName());
                }
            }
            model.setCategorySubscriptionFilter(s);
            Map<String, String> prefs = new HashMap<String, String>(
                userPreferences.getPreferences().size());
            for (Preference p : userPreferences.getPreferences()) {
                prefs.put(p.getName(), p.getValue());
            }
            model.setPreferences(prefs);
            return model;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void addCategoryFilter(String username, String categoryName)
        throws Exception {
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            Category category = getCategoryDao().findUniqueByName(categoryName);
            if (category == null) {
                getLog().error("No category named: " + categoryName);
                return;
            }
            CategorySubscription cs = userPreferences
                .getCategorySubscription(category);
            if (cs != null && !cs.isFiltered()) {
                cs.setFiltered(true);
                getCategorySubscriptionDao().update(cs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void addCityFilter(String username, String cityName)
        throws Exception {
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            City city = getCityDao().findUniqueByName(cityName);
            if (city == null) {
                getLog().error("No city named: " + cityName);
                return;
            }
            CitySubscription cs = userPreferences.getCitySubscription(city);
            boolean update = false;
            if (cs != null && !cs.isFiltered()) {
                cs.setFiltered(true);
                getCitySubscriptionDao().update(cs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void deletePreference(String username, String name) throws Exception {
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            Preference p = userPreferences.getPreference(name);
            if (p != null) {
                userPreferences.getPreferences().remove(p);
                getPreferenceDao().delete(p);
                getUserPreferencesDao().update(userPreferences);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void removeCategoryFilter(String username, String categoryName)
        throws Exception {

        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            Category category = getCategoryDao().findUniqueByName(categoryName);
            if (category == null) {
                getLog().error("No category named: " + categoryName);
                return;
            }
            CategorySubscription cs = userPreferences
                .getCategorySubscription(category);
            if (cs != null && cs.isFiltered()) {
                cs.setFiltered(false);
                getCategorySubscriptionDao().update(cs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void removeCityFilter(String username, String cityName)
        throws Exception {

        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            City city = getCityDao().findUniqueByName(cityName);
            if (city == null) {
                getLog().error("No city named: " + cityName);
                return;
            }
            CitySubscription cs = userPreferences.getCitySubscription(city);
            if (cs != null && cs.isFiltered()) {
                cs.setFiltered(false);
                getCitySubscriptionDao().update(cs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }

    }
    
    private boolean setPreference(UserPreferences userPreferences, String name, String value) {
        Preference p = userPreferences.getPreference(name);
        boolean update = false;
        if (p != null) {
            if (!p.getValue().equals(value)) {
                p.setValue(value);
                getPreferenceDao().update(p);
            }
        } else {
            p = new Preference();
            p.setName(name);
            p.setUserPreferences(userPreferences);
            p.setValue(value);
            getPreferenceDao().create(p);
            userPreferences.addPreference(p);
            update = true;
        }
        return update;
    }
    
    public void setPreferences(String username, Map preferences) throws Exception {
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            
            String name;
            Iterator itr = preferences.keySet().iterator();
            boolean update = false;
            while (itr.hasNext()) {
                name = (String)itr.next();
                if (setPreference(userPreferences, name, (String)preferences.get(name))) {
                    update = true;
                }
            }
            if (update) {
                getUserPreferencesDao().update(userPreferences);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void setPreference(String username, String name, String value)
        throws Exception {

        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            if (userPreferences == null) {
                getLog().error("No user named: " + username);
                return;
            }
            if (setPreference(userPreferences, name, value)) {
                getUserPreferencesDao().update(userPreferences);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
}
