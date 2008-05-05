package net.deuce.clmanager.gwt.main.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {
    
    private String basePath;
    
    public ImageServlet() {
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        basePath = config.getInitParameter("basePath");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        
        System.out.println("ZZZ context path: " + req.getContextPath());
        System.out.println("ZZZ path: " + req.getServletPath());
        //resp.sendRedirect(arg0)
    }
    

}
