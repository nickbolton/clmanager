package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SubscribeCategoryController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            String id = request.getParameter("id");
            if (id != null && id.trim().length() > 0) {
                subscribe(prefs, id);
            }
            return new ModelAndView("msg", "msg", id);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }

    private void subscribe(UserPreferences prefs, String id) {
        Category cat = getCategoryDao().read(Long.parseLong(id));
        if (cat == null) {
            throw new RuntimeException("No category with id: " + id);
        }
        CategorySubscription cs = prefs.getCategorySubscription(cat);
        if (cs != null) {
            cs.setSubscribed(true);
            getCategorySubscriptionDao().update(cs);
        } else {
            cs = new CategorySubscription();
            cs.setCategory(cat);
            cs.setSubscribed(true);
            cs.setUserPreferences(prefs);
            getCategorySubscriptionDao().create(cs);
            prefs.getCategorySubscriptions().add(cs);
            getUserPreferencesDao().update(prefs);
        }
        getCache().remove(prefs);
    }
    
}

