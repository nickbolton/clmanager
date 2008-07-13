package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.UserPreferences;

import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;

public class CategoriesController extends BaseController {
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Session session = openSession();
        
        try {
            HttpSession httpSession = request.getSession();
            UserPreferences prefs = (UserPreferences)httpSession.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("cats", getCategories(prefs));
            
            return new ModelAndView("cats", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }
    
    private List<Category> getCategories(UserPreferences prefs) {
        List<Category> cats = new LinkedList<Category>();
        for (Category c : getCategoryDao().findAll()) {
            CategorySubscription cs = prefs.getCategorySubscription(c);
            if (cs == null || !cs.isSubscribed()) {
                cats.add(c);
            }
        }
        return cats;
    }

}

