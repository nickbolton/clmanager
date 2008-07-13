package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.domain.dao.UserPreferencesDao;

import org.springframework.web.servlet.ModelAndView;

public class CreateUserController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            UserPreferencesDao dao = getUserPreferencesDao();
            UserPreferences up = dao.findUniqueByUsername(username);
            if (up != null) {
                response.sendRedirect("signup?dupEmail=true");
            } else {
                up = new UserPreferences();
                up.setUsername(username);
                up.setPassword(password);
                dao.create(up);
                
                HttpSession session = request.getSession(true);
                session.setAttribute("userPreferences", up);
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
}

