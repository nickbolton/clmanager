package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SelectCategoryController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");

            if (prefs == null) {
                response.sendRedirect("login");
            } else {
                String cat = request.getParameter("cat");
                if (cat != null && cat.trim().length() > 0) {
                    if ("all".equals(cat)) {
                        clearCategory(prefs);
                    } else {
                        setCategory(prefs, cat);
                    }
                    clearFavorites(prefs);
                }

                response.sendRedirect("main");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }

    private void setCategory(UserPreferences prefs, String cat) {
        String name = "selectedCategory";
        Preference p = prefs.getPreference(name);
        
        boolean update = p == null || !p.getValue().equals(cat);
        setPreference(prefs, name, cat);
        
        if (update) {
            getCache().remove(prefs);
        }
    }
    
    private void clearCategory(UserPreferences prefs) {
        String name = "selectedCategory";
        Preference p = prefs.getPreference(name);
        if (p != null) {
            removePreference(prefs, name);
            getCache().remove(prefs);
        }
    }
    
}

