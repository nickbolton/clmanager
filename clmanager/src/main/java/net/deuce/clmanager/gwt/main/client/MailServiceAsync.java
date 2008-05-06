package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.ImageModel;
import net.deuce.clmanager.gwt.main.client.model.MailResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync {
    
    public void sendMail(String username, Long postId, String to, String subject, String body, ImageModel[] pics, AsyncCallback callback);
}
