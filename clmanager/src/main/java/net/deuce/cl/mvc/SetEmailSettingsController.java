package net.deuce.cl.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.UserPreferences;

import org.springframework.web.servlet.ModelAndView;

public class SetEmailSettingsController extends BaseController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        boolean fromEmailSettingsPage = Boolean.parseBoolean(request.getParameter("fromEmailSettingsPage"));
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            session.removeAttribute("settingsSaved");
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            
            if (prefs == null) {
                if (fromEmailSettingsPage) {
                    response.sendRedirect("login");
                    return new ModelAndView();
                }
                return UNAUTHENTICATED_ERROR;
            }
            
            String smtpPassword = request.getParameter("smtpPassword");
            if (smtpPassword == null) {
                if (fromEmailSettingsPage) {
                    response.sendRedirect("email");
                    return new ModelAndView();
                }
                return ERROR;
            }
            
            String smtpUser = request.getParameter("smtpUser");
            if (smtpUser == null) {
                if (fromEmailSettingsPage) {
                    response.sendRedirect("email");
                    return new ModelAndView();
                }
                return ERROR;
            }
            
            String smtpServer = request.getParameter("smtpServer");
            if (smtpServer == null) {
                if (fromEmailSettingsPage) {
                    response.sendRedirect("email");
                    return new ModelAndView();
                }
                return ERROR;
            }
            
            String smtpAuth = request.getParameter("smtpAuth");
            String smtpPort = request.getParameter("smtpPort");
            String fromEmailAddress = request.getParameter("fromEmailAddress");
            String bccEmailAddress = request.getParameter("bccEmailAddress");
            
            setEmailSettings(prefs, smtpAuth, smtpServer, smtpUser, 
                smtpPassword, smtpPort, fromEmailAddress, bccEmailAddress);
            removePreference(prefs, "emailSettingsError");
            
            if (fromEmailSettingsPage) {
                session.setAttribute("settingsSaved", "true");
                response.sendRedirect("email");
                return new ModelAndView();
            }
            return OK;
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        
        if (fromEmailSettingsPage) {
            response.sendRedirect("email");
            return new ModelAndView();
        }

        return ERROR;
    }

    private void setEmailSettings(UserPreferences prefs, String smtpAuth,
        String smtpServer, String smtpUser, String smtpPassword, 
        String smtpPort, String fromEmailAddress, String bccEmailAddress) {
        
        setPreference(prefs, "smtpAuth", smtpAuth);
        setPreference(prefs, "smtpServer", smtpServer);
        setPreference(prefs, "smtpUser", smtpUser);
        setPreference(prefs, "smtpPassword", smtpPassword);
        if (smtpPort != null) {
            setPreference(prefs, "smtpPort", smtpPort);
        }
        if (fromEmailAddress != null) {
            setPreference(prefs, "fromEmailAddress", fromEmailAddress);
        }
        if (bccEmailAddress != null) {
            setPreference(prefs, "bccEmailAddress", bccEmailAddress);
        }
    }
}

