package net.deuce.clmanager.domain;




/**
 * MessagePhoto generated by hbm2java
 */
public class MessagePhoto  implements java.io.Serializable {


     private Long id;
     private String service;
     private String externalId;
     private String url;
     private MessageTemplate messageTemplate;

    public MessagePhoto() {
    }

    public MessagePhoto(String service, String externalId) {
       this.service = service;
       this.externalId = externalId;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
    
    public String getService() {
        return this.service;
    }
    
    public void setService(String service) {
        this.service = service;
    }

    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getExternalId() {
        return this.externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof MessagePhoto) {
            b = externalId.equals(((MessagePhoto)obj).getExternalId()) &&
                service.equals(((MessagePhoto)obj).getService());
        }
        return b;
    }

    @Override
    public int hashCode() {
        return externalId.hashCode();
    }
}


