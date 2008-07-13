package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class SignUpController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        openSession();
        
        try {
            Map model = new HashMap();
            
            if ("true".equals(request.getParameter("dupEmail"))) {
                model.put("emailTaken", "true");
            } 
            return new ModelAndView("signup", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return new ModelAndView();
    }
}

