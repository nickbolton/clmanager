package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.domain.dao.UserPreferencesDao;

import org.springframework.web.servlet.ModelAndView;

public class AuthenticationController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        openSession();
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            UserPreferencesDao dao = getUserPreferencesDao();
            UserPreferences up = dao.findUniqueByUsername(username);
            if (up == null) {
                response.sendRedirect("login?username="+username+"&invalidEmail=true");
            } else if (up.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                if (session != null) {
                    session.invalidate();
                }
                session = request.getSession(true);
                session.setAttribute("userPreferences", up);
                response.sendRedirect("main");
            } else {
                response.sendRedirect("login?username="+username+"&invalidEmail=true");
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

