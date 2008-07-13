package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SelectCityController extends BaseController {

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
                } else if (city != null && city.trim().length() > 0) {
                    clearArea(prefs);
                    if ("all".equals(city)) {
                        clearCity(prefs);
                    } else {
                        setCity(prefs, city);
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

    private void setCity(UserPreferences prefs, String city) {
        String name = "selectedCity";
        Preference p = prefs.getPreference(name);
        
        boolean update = p == null || !p.getValue().equals(city);
        setPreference(prefs, name, city);
        
        if (update) {
            getCache().remove(prefs);
        }
    }
    
    private void setArea(UserPreferences prefs, String city, String area) {
        setCity(prefs, city);
        
        String name = "selectedArea";
        Preference p = prefs.getPreference(name);
        
        boolean update = p == null || !p.getValue().equals(area);
        setPreference(prefs, name, area);
        
        if (update) {
            getCache().remove(prefs);
        }
    }
    
    private void clearCity(UserPreferences prefs) {
        String name = "selectedCity";
        Preference p = prefs.getPreference(name);
        if (p != null) {
            removePreference(prefs, name);
            getCache().remove(prefs);
        }
        clearArea(prefs);
    }
    
    private void clearArea(UserPreferences prefs) {
        String name = "selectedArea";
        Preference p = prefs.getPreference(name);
        if (p != null) {
            removePreference(prefs, name);
            getCache().remove(prefs);
        }
    }

}

