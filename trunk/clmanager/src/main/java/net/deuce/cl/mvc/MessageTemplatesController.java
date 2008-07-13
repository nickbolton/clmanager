package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class MessageTemplatesController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        openSession();
            
        try {
            Map model = new HashMap();
            String now = (new java.util.Date()).toString(); 
            getLog().info("returning message templates view with " + now);
    
            model.put("now", now);
            model.put("foobar", "mt");
    
            return new ModelAndView("mt", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return new ModelAndView();
    }
}

