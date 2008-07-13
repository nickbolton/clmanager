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

import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class CitiesController extends BaseController {
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession httpSession = request.getSession();
            UserPreferences prefs = (UserPreferences)httpSession.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            
            Map<String, Object> model = new HashMap<String, Object>();
            
            model.put("cities", getCities(prefs));
            
            return new ModelAndView("cities", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }
    
    private List<City> getCities(UserPreferences prefs) {
        List<City> cities = new LinkedList<City>();
        for (City c : getCityDao().findAll()) {
            CitySubscription cs = prefs.getCitySubscription(c);
            if (cs == null || !cs.isSubscribed()) {
                cities.add(c);
            }
        }
        return cities;
    }
}

