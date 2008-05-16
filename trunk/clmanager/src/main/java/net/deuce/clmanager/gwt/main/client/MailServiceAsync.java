package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.MailResponse;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync {
    
    public void sendMail(String username, Long postId, String to, String subject, String body, PhotoWrapper[] pics, AsyncCallback callback);
}
