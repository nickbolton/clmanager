package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class FavoritesController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                response.sendRedirect("login");
            } else {
                toggleFavorites(prefs);
                response.sendRedirect("main");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return new ModelAndView();
    }

    private void toggleFavorites(UserPreferences prefs) {
        String name = "favorites";
        Preference p = prefs.getPreference(name);
        if (p == null || !"true".equals(p.getValue())) {
            setPreference(prefs, name, "true");
        } else {
            removePreference(prefs, name);
        }
        getCache().remove(prefs);
        getUserPreferencesDao().update(prefs);
    }
    
}

