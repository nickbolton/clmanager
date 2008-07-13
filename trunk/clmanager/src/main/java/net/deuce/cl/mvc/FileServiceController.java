package net.deuce.cl.mvc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.cl.service.FileService;
import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class FileServiceController extends BaseController {
    
    private FileService fileService;
    
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
        if (prefs != null) {
            String name = request.getParameter("name");
            if (name != null) {
                Writer out = response.getWriter();
                char[] buf = new char[1024];
                int numRead;
                Reader reader = null;
                try {
                    File f = fileService.getFile(prefs, name);
                    reader = new FileReader(f);
                    if (reader != null) {
                        response.setContentType(new MimetypesFileTypeMap().getContentType(f));
                        while ( (numRead = reader.read(buf, 0, 1024)) >= 0 ) {
                            out.write(buf, 0, numRead);
                        }
                    }
                } finally {
                    if (reader != null) reader.close();
                }
            }
        }
        return new ModelAndView();
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    
}

