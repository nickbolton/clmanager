package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.MailResponse;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface MailService extends RemoteService {
    
    public MailResponse sendMail(String username, Long postId, String to, String subject, String body, PhotoWrapper[] pics) throws Exception;
    
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MailServiceAsync instance;
		public static MailServiceAsync getInstance(){
			if (instance == null) {
				instance = (MailServiceAsync) GWT.create(MailService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MailService");
			}
			return instance;
		}
	}
}
