package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.UserPreferences;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.web.servlet.ModelAndView;

public class FlagController extends BaseController {

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
                flagPost(prefs, id);
            }
            return OK;
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
            return ERROR;
        } finally {
            closeSession();
        }
    }

    private void flagPost(UserPreferences prefs, String id) throws HttpException, IOException {
        Post p = getPostDao().read(Long.parseLong(id));
        if (p == null) {
            getLog().error("No post exists with id: " + id);
            return;
        }
        StringBuffer url = new StringBuffer(p.getCity().getUrl());
        if (!url.toString().endsWith("/")) {
            url.append('/');
        }
        url.append("flag/?flagCode=15&postingID=").append(p.getClId());
        if (getLog().isDebugEnabled()) {
            getLog().debug("sending flag post: " + url.toString());
        }
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url.toString());
        method.setFollowRedirects(true);
        client.executeMethod(method);
        if (getLog().isDebugEnabled()) {
            getLog().debug("sending flag post: " + url.toString() + " - statusCode: " + method.getStatusCode());
        }
        if (method.getStatusCode()!=200) {
            getLog().error("Response code (" + method.getStatusCode() + ") for url: " + url.toString());
        }
    }
    
}

