package net.deuce.clmanager.domain;

// Generated Apr 11, 2008 1:02:39 PM by Hibernate Tools 3.2.0.CR1

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * MessageTemplate generated by hbm2java
 */
public class MessageTemplate implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private UserPreferences userPreferences;
    private String name;
    private String subject;
    private String message;
    private Category defaultCategory;
    private String files;
    private transient List<String> fileList = new LinkedList<String>();

    public MessageTemplate() {
    }

    public MessageTemplate(String name, String subject, String message, String files, Category defaultCategory) {
        this.name = name;
        this.subject = subject;
        this.message = message;
        this.defaultCategory = defaultCategory;
        this.files = files;
        this.fileList = Arrays.asList(files.split("|"));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Category getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Category defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    @Override
    public boolean equals(Object obj) {
        if (name != null && userPreferences != null) {
            boolean b = false;
            if (obj != null && obj instanceof MessageTemplate) {
                MessageTemplate that = (MessageTemplate)obj;
                b = name.equals(that.getName()) &&
                    userPreferences.equals(that.getUserPreferences());
            }
            return b;
        } 
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (name != null && userPreferences != null) {
            return name.hashCode()+userPreferences.hashCode();
        }
        return super.hashCode();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public List<String> getFileList() {
        return fileList;
    }
    
    public void addFile(String name) {
        fileList.add(name);
        serializeFileList();
    }
    
    public void removeFile(String name) {
        fileList.remove(name);
        serializeFileList();
    }
    
    private void serializeFileList() {
        if (fileList.size() == 0) {
            files = "";
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(fileList.get(0));
        for (int i=1; i<fileList.size(); i++) {
            sb.append('|').append(fileList.get(i));
        }
        files = sb.toString();
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
        this.fileList = Arrays.asList(files.split("|"));
    }
}