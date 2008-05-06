package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessageTemplateServiceAsync {
    
    public void createMessageTemplate(MessageTemplateModel mtm, AsyncCallback callback);
    public void deleteMessageTemplate(Long id, AsyncCallback callback);
    public void updateMessageTemplate(MessageTemplateModel mtm, AsyncCallback callback);
    
    /**
     */
    public void getMessageTemplates(AsyncCallback callback);
}
