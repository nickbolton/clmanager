package net.deuce.cl.mvc;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.cl.service.FileService;
import net.deuce.clmanager.domain.MessageTemplate;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.SavedFile;
import net.deuce.clmanager.domain.UserPreferences;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

public class SendMailController extends BaseController implements InitializingBean {
    
    private FileService fileService;
    private String tmpUploadDir;
    private int maxMemorySize;
    private int maxRequestSize;
    private boolean debug = false;
    private String debugToAddress = "larvell.jones@mac.com";
    
    private Properties defaultProps = System.getProperties();
    private ServletFileUpload upload;
    
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        openSession();
        
        try {
            HttpSession session = request.getSession();
            UserPreferences prefs = (UserPreferences)session.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            String id = null;
            String message = null;
            String subject = null;
            String save = null;
            String templateName = null;
            
            Map<Integer, File> attachments = new HashMap<Integer, File>();
            Set<Integer> saveFileIds = new HashSet<Integer>();
            Set<File> saveFiles = new HashSet<File>();
            Set<File> fileExisted = new HashSet<File>();
            
            Set<File> selectedFiles = new HashSet<File>();
            
            if (ServletFileUpload.isMultipartContent(request)) {
                List<FileItem> items = upload.parseRequest(request);

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        if ("id".equals(item.getFieldName())) {
                            id = item.getString();
                        } else if ("subject".equals(item.getFieldName())) {
                            subject = item.getString();
                        } else if ("message".equals(item.getFieldName())) {
                            message = item.getString();
                        } else if ("save".equals(item.getFieldName())) {
                            save = item.getString();
                        } else if ("name".equals(item.getFieldName())) {
                            templateName = item.getString();
                        } else if (item.getFieldName().startsWith("save-")) {
                            int fileId = Integer.parseInt(item.getFieldName().substring(5));
                            saveFileIds.add(fileId);
                        } else if (item.getFieldName().startsWith("sf-")) {
                            long savedFileId = Long.parseLong(item.getFieldName().substring(3));
                            SavedFile sf = getSavedFileDao().read(savedFileId);
                            if (sf != null) {
                                selectedFiles.add(fileService.getFile(prefs, sf.getName()));
                            }
                        }
                    } else if (item.getFieldName().startsWith("file-")) {
                        int fileId = Integer.parseInt(item.getFieldName().substring(5));
                        File dir = fileService.getRepository(prefs);
                        dir.mkdirs();
                        File f = new File(dir, item.getName());
                        if (f.exists()) {
                            fileExisted.add(f);
                        }
                        item.write(f);
                        attachments.put(fileId, f);
                        if (saveFileIds.contains(fileId)) {
                            saveFiles.add(f);
                        }
                    }
                }
            } else {
                id = request.getParameter("id");
                message = request.getParameter("message");
                subject = request.getParameter("subject");
                save = request.getParameter("save");
                templateName = request.getParameter("name");
                Enumeration en = request.getParameterNames();
                String name;
                while (en.hasMoreElements()) {
                    name = (String)en.nextElement();
                    if (name.startsWith("sf-")) {
                        long savedFileId = Long.parseLong(name.substring(3));
                        SavedFile sf = getSavedFileDao().read(savedFileId);
                        if (sf != null) {
                            selectedFiles.add(fileService.getFile(prefs, sf.getName()));
                        }
                    }
                }
            }
            
            if (id != null && id.trim().length() > 0) {
                Post p = getPostDao().read(Long.parseLong(id));
                if (p == null) {
                    return new ModelAndView("msg", "msg", "error:no post with id " + id);
                }
                selectedFiles.addAll(attachments.values());
                if ("true".equals(save)) {
                    saveTemplate(prefs, templateName, subject, message, selectedFiles);
                }
                subject = replaceTokens(p, subject);
                message = replaceTokens(p, message);
                String error = sendMessage(prefs, p, subject, message, selectedFiles);
                
                for (File f : attachments.values()) {
                    if (saveFiles.contains(f)) {
                        saveFile(prefs, f);
                    } else if (!fileExisted.contains(f)){
                        f.delete();
                    }
                }
                if (error != null && error.length() > 0) {
                    return new ModelAndView("msg", "msg", error);
                }
                
                PostActivities pa = getPostActivities(prefs, p);
                if (!pa.isResponded()) {
                    pa.setResponded(true);
                    getPostActivitiesDao().update(pa);
                }
            }
            return new ModelAndView("msg", "msg", id);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
            return new ModelAndView("msg", "msg", e.getMessage());
        } finally {
            closeSession();
        }
        
    }
    
    private void saveTemplate(UserPreferences prefs, String name, String subject, String message, Collection<File> files) {
        Set<MessageTemplate> templates = prefs.getMessageTemplates();
        MessageTemplate mt = null;
        for (MessageTemplate m : templates) {
            if (m.getName().equals(name)) {
                mt = m;
                break;
            }
        }
        if (mt == null) {
            mt = new MessageTemplate();
            mt.setName(name);
            mt.setSubject(subject);
            mt.setMessage(message);
            for (File f : files) {
                mt.addFile(f.getName());
            }
            getMessageTemplateDao().create(mt);
            templates.add(mt);
            getUserPreferencesDao().update(prefs);
        } else {
            mt.setFiles("");
            mt.setSubject(subject);
            mt.setMessage(message);
            for (File f : files) {
                mt.addFile(f.getName());
            }
            getMessageTemplateDao().update(mt);
        }
    }
    
    private String replaceTokens(Post p, String s) {
        return s.replaceAll("%TITLE%", p.getTitle())
            .replaceAll("%CLID%", p.getClId().toString())
            .replaceAll("%AGE%", p.getAge())
            .replaceAll("%LOC%", p.getLocation())
            .replaceAll("%REPLY_ADDR%", p.getReplyAddress())
            .replaceAll("%URL%", p.getUrl())
            .replaceAll("%CAT_EXT%", p.getCategory().getExternalName())
            .replaceAll("%CAT%", p.getCategory().getName())
            .replaceAll("%CITY_PROPER%", p.getCity().getProperName())
            .replaceAll("%CITY%", p.getCity().getName());
    }
    
    private void saveFile(UserPreferences prefs, File f) {
        SavedFile sf = null;
        for (SavedFile s : prefs.getSavedFiles()) {
            if (s.getName().equals(f.getName())) {
                sf = s;
                break;
            }
        }
        if (sf == null) {
            sf = new SavedFile();
            sf.setName(f.getName());
            sf.setPath(f.getAbsolutePath().substring(fileService.getRepository(prefs).getAbsolutePath().length()+1));
            sf.setType(new MimetypesFileTypeMap().getContentType(f));
            sf.setUserPreferences(prefs);
            getSavedFileDao().create(sf);
            prefs.getSavedFiles().add(sf);
            getUserPreferencesDao().update(prefs);
        }
    }
    
    public static boolean isValidString(String s) {
        return s != null && s.trim().length() > 0;
    }

    private String sendMessage(UserPreferences up, Post p, String subject, String message, Collection<File> attachments)
    throws HttpException, IOException {
        Preference smtpServer = up.getPreference("smtpServer");
        if (smtpServer == null || !isValidString(smtpServer.getValue())) {
            return "Email Setting missing: smtpServer";
        }

        final Preference smtpUser = up.getPreference("smtpUser");
        if (smtpUser == null || !isValidString(smtpUser.getValue())) {
            return "Email Setting missing: smtpUser";
        }
        final Preference smtpPassword = up.getPreference("smtpPassword");
        if (smtpPassword == null || !isValidString(smtpPassword.getValue())) {
            return "Email Setting missing: smtpPassword";
        }
        Preference smtpAuth = up.getPreference("smtpAuth");
        Preference smtpPort = up.getPreference("smtpPort");
        Preference fromEmailAddress = up.getPreference("fromEmailAddress");
        Preference bccEmailAddress = up.getPreference("bccEmailAddress");
        
        try {
            Session session = Session.getDefaultInstance(fillProperties(smtpAuth, smtpServer, smtpPort), new javax.mail.Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(smtpUser.getValue(), smtpPassword.getValue());
                }
            });
            MimeMessage msg = new MimeMessage(session);
            if (fromEmailAddress != null && isValidString(fromEmailAddress.getValue())) {
                msg.setFrom(new InternetAddress(fromEmailAddress.getValue()));
            } else {
                msg.setFrom(new InternetAddress(smtpUser.getValue()));
            }
            String to = p.getReplyAddress();
            if (debug && debugToAddress != null) {
                to = debugToAddress;
            }
            msg.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(to, false));
            if (bccEmailAddress != null && isValidString(bccEmailAddress.getValue())) {
                msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bccEmailAddress.getValue(), false));
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(message);
            multipart.addBodyPart(messageBodyPart);
            
            int i=1;
            for (File f : attachments) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(f);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(f.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
            
            Transport.send(msg);
            return "";
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            String error = "error:Authentication Failed";
            setPreference(up, "emailSettingsError", error);
            return error;
        } catch (Throwable t) {
            t.printStackTrace();
            String error = "error:"+t.getMessage();
            setPreference(up, "emailSettingsError", error);
            return error;
        }
    }
    
    private Properties fillProperties(Preference smtpAuth, Preference smtpServer, Preference smtpPort) {
        Properties p = new Properties(defaultProps);
        if (smtpAuth != null && Boolean.valueOf(smtpAuth.getValue())) {
            p.setProperty("mail.smtp.auth", "true");
            Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            p.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            p.setProperty("mail.smtp.socketFactory.fallback", "false");
            if (smtpPort != null && isValidString(smtpPort.getValue())) {
                p.setProperty("mail.smtp.socketFactory.port", smtpPort.getValue());
            }
        } else {
            if (smtpPort != null && isValidString(smtpPort.getValue())) {
                p.setProperty("mail.smtp.port", smtpPort.getValue());
            }
        }
        p.setProperty("mail.smtp.host", smtpServer.getValue());
        return p;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public int getMaxMemorySize() {
        return maxMemorySize;
    }

    public void setMaxMemorySize(int maxMemorySize) {
        this.maxMemorySize = maxMemorySize;
    }

    public int getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getTmpUploadDir() {
        return tmpUploadDir;
    }

    public void setTmpUploadDir(String tmpUploadDir) {
        this.tmpUploadDir = tmpUploadDir;
    }

    public void afterPropertiesSet() throws Exception {
        if (tmpUploadDir == null) {
            tmpUploadDir = System.getProperty("java.io.tmpdir");
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(maxMemorySize);
        File f = new File(tmpUploadDir);
        f.mkdirs();
        factory.setRepository(f);
        upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxRequestSize);
        defaultProps.setProperty("mail.transport.protocol", "smtp");
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getDebugToAddress() {
        return debugToAddress;
    }

    public void setDebugToAddress(String debugToAddress) {
        this.debugToAddress = debugToAddress;
    }
    
}

