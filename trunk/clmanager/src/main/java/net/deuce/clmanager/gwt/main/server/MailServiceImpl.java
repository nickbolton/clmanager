package net.deuce.clmanager.gwt.main.server;

import java.net.URL;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.clmanager.gwt.main.client.MailService;
import net.deuce.clmanager.gwt.main.client.model.MailResponse;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailServiceImpl extends UserServiceImpl implements MailService {
    
    private Log log = LogFactory.getLog(getClass());
    private String imageBasePath;
    private Properties defaultProps = System.getProperties();
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        imageBasePath = config.getInitParameter("imageBasePath");
        if (imageBasePath == null) {
            log.error("No imageBasePath specified in web.xml for servlet: " + getClass().getName());
            imageBasePath = "/Users/deuce/dev/src/personal/clmanager/tomcat/webapps/clserver";
        }
        
        defaultProps.setProperty("mail.transport.protocol", "smtp");
        
    }
    
    private Properties fillProperties(Preference smtpAuth, Preference smtpServer, Preference smtpPort) {
        Properties p = new Properties(defaultProps);
        if (Boolean.valueOf(smtpAuth.getValue())) {
            p.setProperty("mail.smtp.auth", "true");
            Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            p.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            p.setProperty("mail.smtp.socketFactory.fallback", "false");
            if (smtpPort != null && smtpPort.getValue().trim().length() > 0) {
                p.setProperty("mail.smtp.socketFactory.port", smtpPort.getValue());
            }
        } else {
            if (smtpPort != null && smtpPort.getValue().trim().length() > 0) {
                p.setProperty("mail.smtp.port", smtpPort.getValue());
            }
        }
        p.setProperty("mail.smtp.host", smtpServer.getValue());
        return p;
    }

    public MailResponse sendMail(String username, Long id, String to, String subject, String body, PhotoWrapper[] pics) throws Exception {
        
        UserPreferences up = getUserPreferences(username);
        Preference smtpAuth = up.getPreference("smtpAuth");
        if (smtpAuth == null || smtpAuth.getValue().trim().length() == 0) {
            return new MailResponse(id, "Email Setting missing: smtpAuth", Boolean.FALSE);
        }
        Preference smtpServer = up.getPreference("smtpServer");
        if (smtpServer == null || smtpServer.getValue().trim().length() == 0) {
            return new MailResponse(id, "Email Setting missing: smtpServer", Boolean.FALSE);
        }
        
        final Preference smtpUser = up.getPreference("smtpUser");
        if (smtpUser == null || smtpUser.getValue().trim().length() == 0) {
            return new MailResponse(id, "Email Setting missing: smtpUser", Boolean.FALSE);
        }
        final Preference smtpPassword = up.getPreference("smtpPassword");
        if (smtpPassword == null || smtpPassword.getValue().trim().length() == 0) {
            return new MailResponse(id, "Email Setting missing: smtpPassword", Boolean.FALSE);
        }
        Preference smtpPort = up.getPreference("smtpPort");
        Preference fromEmailAddress = up.getPreference("fromEmailAddress");
        Preference bccEmailAddress = up.getPreference("bccEmailAddress");
        
        
        
        try {
            Session session = Session.getDefaultInstance(fillProperties(smtpAuth, smtpServer, smtpPort), new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser.getValue(), smtpPassword.getValue());
                }
            });
            MimeMessage msg = new MimeMessage(session);
            if (fromEmailAddress != null && fromEmailAddress.getValue().trim().length() > 0) {
                msg.setFrom(new InternetAddress(fromEmailAddress.getValue()));
            } else {
                msg.setFrom(new InternetAddress(smtpUser.getValue()));
            }
            msg.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(to, false));
            if (bccEmailAddress != null && bccEmailAddress.getValue().trim().length() > 0) {
                msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bccEmailAddress.getValue(), false));
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);
            
            for (int i=0; pics != null && i<pics.length; i++) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new URLDataSource(new URL(pics[i].getUrl()));
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("photo-"+(i+1));
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
            
            /*
            StringBuffer htmlText = new StringBuffer();
            htmlText.append("<p>").append(body).append("</p><br/><br/>");
            for (int i=0; i<pics.length; i++) {
                htmlText.append("<img src=\""+pics[i].getUrl()+"\"/><br/>");
            }
            
            StringBuffer plainText = new StringBuffer(body);
            plainText.append('\n');
            for (int i=0; i<pics.length; i++) {
                plainText.append('\n').append(pics[i].getUrl());
            }
            
            Multipart multipart = new MimeMultipart("alternative");
            
            BodyPart plainMessageBodyPart = new MimeBodyPart();
            plainMessageBodyPart.setContent(plainText.toString(),"text/plain");
            
            multipart.addBodyPart(plainMessageBodyPart);   

            BodyPart htmlMessageBodyPart = new MimeBodyPart();
            htmlMessageBodyPart.setContent(htmlText.toString(), "text/html");
            
            multipart.addBodyPart(htmlMessageBodyPart);
            
            msg.setContent(multipart);
            */
            
            Transport.send(msg);
            return new MailResponse(id, "Success", Boolean.TRUE);
        } catch (Throwable t) {
            return new MailResponse(id, t.getMessage(), Boolean.FALSE);
        }
    }

}
