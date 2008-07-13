package net.deuce.cl.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.UserPreferences;

public class FileServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    
    private FileService fileService;

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse res)
        throws Exception {

        HttpSession session = req.getSession();
        UserPreferences prefs = (UserPreferences) session.getAttribute("userPreferences");
        if (prefs == null) {
            return;
        }
        
        Long id = Long.parseLong(req.getParameter("id"));
        if (!prefs.getId().equals(id)) return;
        
        
    }
    
    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
