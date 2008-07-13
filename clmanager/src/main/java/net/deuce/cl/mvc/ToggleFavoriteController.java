package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.domain.dao.PostDao;

import org.springframework.web.servlet.ModelAndView;

public class ToggleFavoriteController extends BaseController {

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
                toggleFavorite(prefs, id);
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

    private void toggleFavorite(UserPreferences prefs, String id) {
        String name = "fav:"+id;
        Preference p = prefs.getPreference(name);
        if (p == null) {
            setPreference(prefs, name, id);
        } else {
            removePreference(prefs, name);
        }
        getCache().remove(prefs);
        PostDao postDao = getPostDao();
        Post post = postDao.read(Long.parseLong(id));
        PostActivities pa = getPostActivities(prefs, post);
        pa.setFavorite(!pa.isFavorite());
        getPostActivitiesDao().update(pa);
    }
    
}

