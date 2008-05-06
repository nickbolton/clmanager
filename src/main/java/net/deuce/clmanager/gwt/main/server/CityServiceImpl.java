package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.gwt.main.client.CityService;
import net.deuce.clmanager.gwt.main.client.model.CityModel;

public class CityServiceImpl extends UserServiceImpl implements CityService {

    public CityServiceImpl() {
    }
    
    public void subscribe(String username, String name, boolean b) throws Exception {
        openSession();
        try {
            City city = getCityDao().findUniqueByName(name);
            if (city == null) {
                getLog().warn("No city found with name: " + name);
                return;
            }
            UserPreferences userPreferences = getUserPreferences(username);
            CitySubscription cs = userPreferences.getCitySubscription(city);
            if (cs == null) {
                cs = new CitySubscription();
                cs.setUserPreferences(userPreferences);
                cs.setCity(city);
                cs.setSubscribed(b);
                getCitySubscriptionDao().create(cs);
            } else {
                cs.setSubscribed(b);
                getCitySubscriptionDao().update(cs);
            }
            updateUserPreferences(userPreferences);
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public List getCities(String username) throws Exception {
        System.out.println("ZZZ fetching cities for " + username);
        List result = new ArrayList();
        UserPreferences userPreferences = getUserPreferences(username);
        for (City city : getCityDao().findAll()) {
            CitySubscription cs = userPreferences.getCitySubscription(city);
            boolean subscribed = false;
            boolean filtered = false;
            if (cs != null) {
                subscribed = cs.isSubscribed();
                filtered = cs.isFiltered();
            }
            result.add(new CityModel(city.getId(), city.getName(), city.getUrl(), subscribed, filtered));
        }
        System.out.println("ZZZ returning " + result.size() + " cities for " + username);
        return result;
    }

    public List getSubscribedCities(String username) throws Exception {
        System.out.println("ZZZ fetching subscribed cities for " + username);
        List result = new ArrayList();
        UserPreferences userPreferences = getUserPreferences(username);
        for (CitySubscription cs : userPreferences.getCitySubscriptions()) {
            if (cs.isSubscribed()) {
                City city = cs.getCity();
                result.add(new CityModel(city.getId(), city.getName(), city.getUrl(), cs.isSubscribed(), cs.isFiltered()));
            }
        }
        System.out.println("ZZZ returning " + result.size() + " subscribed cities for " + username);
        return result;
    }

    public String getCityUrl(String name) throws Exception {
        openSession();
        try {
            City city = getCityDao().findUniqueByName(name);
            return city.getUrl();
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
}
