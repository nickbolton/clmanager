package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SetViewedController extends BaseController {

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
                setViewed(prefs, id);
            }
            return OK;
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }

    private void setViewed(UserPreferences prefs, String id) {
        Post p = getPostDao().read(Long.parseLong(id));
        if (p == null) {
            getLog().error("No post exists with id: " + id);
            return;
        }
        PostActivities pa = getPostActivities(prefs, p);
        pa.setViewed(true);
        getPostActivitiesDao().update(pa);
    }
    
}

