package net.deuce.clmanager.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ClBoostrapServlet extends HttpServlet {
    
    private Worker worker;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        worker = (Worker)applicationContext.getBean("worker", Worker.class);
        new Thread(worker).start();
    }

    @Override
    public void destroy() {
        super.destroy();
        worker.stop();
    }
}
