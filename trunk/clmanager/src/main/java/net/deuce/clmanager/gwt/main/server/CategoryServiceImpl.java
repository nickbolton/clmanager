package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;

public class CategoryServiceImpl extends UserServiceImpl implements CategoryService {
    
    public CategoryServiceImpl() {
    }
    
    public List getCategories(String username) throws Exception {
        System.out.println("ZZZ fetching categories for " + username);
        openSession();
        try {
            UserPreferences userPreferences = getUserPreferences(username);
            List result = new ArrayList();
            for (Category cat : (List<Category>)getCategoryDao().findAll()) {
                CategorySubscription cs = userPreferences.getCategorySubscription(cat);
                boolean subscribed = false;
                boolean filtered = false;
                if (cs != null) {
                    subscribed = cs.isSubscribed();
                    filtered = cs.isFiltered();
                }
                result.add(new CategoryModel(cat.getId(), cat.getName(), subscribed, filtered));
            }
            System.out.println("ZZZ returning " + result.size() + " categories for " + username);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    public void subscribe(String username, String name, boolean b)
        throws Exception {
        openSession();
        try {
            Category cat = getCategoryDao().findUniqueByName(name);
            if (cat == null) {
                getLog().warn("No category found with name: " + name);
                return;
            }
            UserPreferences userPreferences = getUserPreferences(username);
            CategorySubscription cs = userPreferences.getCategorySubscription(cat);
            if (cs == null) {
                cs = new CategorySubscription();
                cs.setUserPreferences(userPreferences);
                cs.setCategory(cat);
                cs.setSubscribed(b);
                getCategorySubscriptionDao().create(cs);
            } else {
                cs.setSubscribed(b);
                getCategorySubscriptionDao().update(cs);
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

    public List getSubscribedCategories(String username) throws Exception {
        System.out.println("ZZZ fetching categories for " + username);
        List result = new ArrayList();
        UserPreferences userPreferences = getUserPreferences(username);
        for (CategorySubscription cs : userPreferences.getCategorySubscriptions()) {
            if (cs.isSubscribed()) {
                Category cat = cs.getCategory();
                result.add(new CategoryModel(cat.getId(), cat.getName(), cs.isSubscribed(), cs.isFiltered()));
            }
        }
        System.out.println("ZZZ returning " + result.size() + " categories for " + username);
        return result;
    }

}
