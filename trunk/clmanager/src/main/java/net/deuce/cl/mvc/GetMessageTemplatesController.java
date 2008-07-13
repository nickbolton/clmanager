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

public class GetMessageTemplatesController extends BaseController {
    
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("mt", prefs.getMessageTemplates());
            return new ModelAndView("getmt", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }
    
}

