package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class UnsubscribeCategoryController extends BaseController {

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
                unsubscribe(prefs, id);
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

    private void unsubscribe(UserPreferences prefs, String id) {
        Category cat = getCategoryDao().read(Long.parseLong(id));
        if (cat == null) {
            throw new RuntimeException("No category with id: " + id);
        }
        CategorySubscription cs = prefs.getCategorySubscription(cat);
        if (cs != null) {
            cs.setSubscribed(false);
            getCategorySubscriptionDao().update(cs);
            getCache().remove(prefs);
            Preference p = prefs.getPreference("selectedCategory");
            if (p != null && cat.getId().equals(Long.parseLong(p.getValue()))) {
                removePreference(prefs, "selectedCategory");
            }
        }
    }
    
}

