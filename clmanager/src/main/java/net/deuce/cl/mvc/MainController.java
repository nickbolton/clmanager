package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class MainController extends BaseController {
    
    private static List<Post> EMPTY_RESULT = new ArrayList<Post>(0);
    private static String selectByCityAndCategoryPrefix =
        "select p from net.deuce.clmanager.domain.Post p " +
        "where p.spam = ? and (";

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                response.sendRedirect("login");
                return new ModelAndView();
            }
        
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("prefs", prefs);
            model.put("area", prefs.getPreferenceValue("selectedArea"));
            model.put("city", prefs.getPreferenceValue("selectedCity"));
            model.put("cat", prefs.getPreferenceValue("selectedCategory"));
            model.put("fav", prefs.getPreferenceValue("favorites"));
            model.put("emailSettingsSet", Boolean.valueOf(prefs.getPreferenceValue("smtpUser") != null));
            model.put("emailSettingsError", prefs.getPreferenceValue("emailSettingsError"));
    
            return new ModelAndView("main", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return new ModelAndView();
    }
    
}

