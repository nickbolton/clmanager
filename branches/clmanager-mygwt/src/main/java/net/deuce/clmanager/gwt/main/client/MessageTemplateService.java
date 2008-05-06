package net.deuce.clmanager.gwt.main.client;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface MessageTemplateService extends RemoteService {
    
    public MessageTemplateModel createMessageTemplate(MessageTemplateModel mtm) throws Exception;
    public void deleteMessageTemplate(Long id) throws Exception;
    public void updateMessageTemplate(MessageTemplateModel mtm) throws Exception;
    
    /**
     * @gwt.typeArgs <net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel>
     */
    public List getMessageTemplates() throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MessageTemplateServiceAsync instance;
		public static MessageTemplateServiceAsync getInstance(){
			if (instance == null) {
				instance = (MessageTemplateServiceAsync) GWT.create(MessageTemplateService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
			}
			return instance;
		}
	}
}
