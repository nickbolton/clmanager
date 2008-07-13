package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SubscribeCityController extends BaseController {

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
        City city = getCityDao().read(Long.parseLong(id));
        if (city == null) {
            throw new RuntimeException("No city with id: " + id);
        }
        CitySubscription cs = prefs.getCitySubscription(city);
        if (cs != null) {
            cs.setSubscribed(true);
            getCitySubscriptionDao().update(cs);
        } else {
            cs = new CitySubscription();
            cs.setCity(city);
            cs.setSubscribed(true);
            cs.setUserPreferences(prefs);
            getCitySubscriptionDao().create(cs);
            prefs.getCitySubscriptions().add(cs);
            getUserPreferencesDao().update(prefs);
        }
        getCache().remove(prefs);
    }
    
}

