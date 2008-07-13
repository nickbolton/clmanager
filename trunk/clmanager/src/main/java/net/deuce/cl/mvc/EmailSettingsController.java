package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class EmailSettingsController extends BaseController {

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
            
            model.put("settingsSaved", session.getAttribute("settingsSaved"));
            model.put("smtpAuth", prefs.getPreferenceValue("smtpAuth"));
            model.put("smtpServer", prefs.getPreferenceValue("smtpServer"));
            model.put("smtpPort", prefs.getPreferenceValue("smtpPort"));
            model.put("smtpUser", prefs.getPreferenceValue("smtpUser"));
            model.put("smtpPassword", prefs.getPreferenceValue("smtpPassword"));
            model.put("fromEmailAddress", prefs.getPreferenceValue("fromEmailAddress"));
            model.put("bccEmailAddress", prefs.getPreferenceValue("bccEmailAddress"));
    
            session.removeAttribute("settingsSaved");

            return new ModelAndView("email", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return new ModelAndView();
    }
}

