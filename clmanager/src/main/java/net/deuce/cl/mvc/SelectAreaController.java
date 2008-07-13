package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SelectAreaController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                response.sendRedirect("login");
            } else {
                String city = request.getParameter("city");
                String area = request.getParameter("area");
                if (city != null && city.trim().length() > 0 &&
                    area != null && area.trim().length() > 0) {
                    setArea(prefs, city, area);
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

    private void setArea(UserPreferences prefs, String city, String area) {
        String name = "selectedCity";
        Preference p = prefs.getPreference(name);
        
        boolean update = p == null || !p.getValue().equals(city);
        setPreference(prefs, name, city);
        
        name = "selectedArea";
        p = prefs.getPreference(name);
        update = update || p == null || !p.getValue().equals(area);
        setPreference(prefs, name, area);
        
        if (update) {
            getCache().remove(prefs);
        }
    }
    
}

