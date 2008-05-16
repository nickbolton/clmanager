package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.MailService;
import net.deuce.clmanager.gwt.main.client.MailServiceAsync;
import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.PostService;
import net.deuce.clmanager.gwt.main.client.PostServiceAsync;
import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.MailResponse;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;
import net.deuce.clmanager.gwt.main.client.model.PostModel;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.MessageBox;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.FillLayout;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public abstract class ReplyView extends View {
    
    private static ListBox messageTemplates;
    private static Map messageTemplateMap = new HashMap();
    
    private static List messageTemplateModels = new ArrayList();;
    
    public ReplyView(Controller controller) {
        super(controller);
    }
    
    protected static void setPreference(final String name, final String value) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                UserModel user = (UserModel)Registry.get("user");
                user.addPreference(name, value);
            }
        };
        UserModel user = (UserModel)Registry.get("user");
        serviceProxy.setPreference(user.getUsername(), name, value, callback);
    }
    
    protected static synchronized ListBox getMessageTemplates() {
        if (messageTemplates == null) {
            messageTemplates = new ListBox();
            messageTemplates.setVisibleItemCount(1);
            messageTemplates.addChangeListener(new ChangeListener() {
                public void onChange(Widget sender) {
                    String value = messageTemplates.getItemText(messageTemplates.getSelectedIndex());
                    setPreference("messageTemplate", value);
                }
            });
            refreshMessageTemplates();
        }
        return messageTemplates;
    }
    
    protected static void refreshMessageTemplates() {
        MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                List l = (List)result;
                
                int selectedIndex = 0;
                UserModel user = (UserModel)Registry.get("user");
                String s = user.getPreference("messageTemplate");
                
                messageTemplates.clear();
                messageTemplates.addItem("Category Default");
                for (int i=0; i<l.size(); i++) {
                    MessageTemplateModel mtm = (MessageTemplateModel)l.get(i);
                    messageTemplates.addItem(mtm.getName());
                    messageTemplateMap.put(mtm.getName(), mtm);
                    if (s != null && Utils.isEqual(s, mtm.getName())) {
                        selectedIndex = i+1;
                    }
                }
                messageTemplateModels.clear();
                messageTemplateModels.addAll(l);
                messageTemplates.setItemSelected(selectedIndex, true);
            }
            
        };
        serviceProxy.getMessageTemplates(callback);
    }
    
    private boolean checkReply(PostModel post) {
        if (post.isResponded().booleanValue()) {
            Utils.warningMessage("Already replied to post.");
            return false;
        } else if (post.get("reply-pending") != null && ((Boolean)post.get("reply-pending")).booleanValue()) {
            Utils.infoMessage("Reply pending...");
            return false;
        }
        return true;
    }
    
    private MessageTemplateModel getTemplate(PostModel post) {
        MessageTemplateModel mtm = null;
        String selectedTemplateName = messageTemplates.getItemText(messageTemplates.getSelectedIndex());
        if (Utils.isEqual("Category Default", selectedTemplateName)) {
            for (int i=0; mtm == null && i<messageTemplateModels.size(); i++) {
                MessageTemplateModel m = (MessageTemplateModel)messageTemplateModels.get(i);
                if (Utils.isEqual(post.getCategory(), m.getCategoryName())) {
                    mtm = m;
                }
            }
        } else {
            mtm = (MessageTemplateModel)messageTemplateMap.get(selectedTemplateName);
        }

        return mtm;
    }

    protected void setupReply(PostModel post) {
        if (post != null) {
            if (checkReply(post)) {
                MessageTemplateModel mtm = getTemplate(post);
                if (mtm == null) {
                    setupNewReply(post);
                } else {
                    reply(post, mtm);
                }
            }
        }
    }
    
    protected void setupEditReply(PostModel post) {
        if (post != null) {
            if (checkReply(post)) {
                MessageTemplateModel mtm = getTemplate(post);
                if (mtm == null) {
                    setupNewReply(post);
                } else {
                    editReply(post, mtm);
                }
            }
        }
    }
    
    protected void setupNewReply(PostModel post) {
        if (post != null) {
            if (checkReply(post)) {
                newReply(post);
            }
        }
    }
    
    protected void flag(PostModel post) {
        if (post != null && !post.isFlagged().booleanValue()) {
            PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
            ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
            AsyncCallback callback = new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    Debug.println(Utils.getStacktraceAsString(caught));
                }

                public void onSuccess(Object result) {
                }
                
            };
            serviceProxy.flagPost(post.getId(), callback);
        }
    }

    protected void markReplied(final Long id) {
        PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                AppEvent evt = new AppEvent(AppEvents.PostReplyFailed, id);
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                AppEvent evt = new AppEvent(AppEvents.PostReplied, id);
                fireEvent(evt);
            }
            
        };
        UserModel userModel = (UserModel)Registry.get("user");
        serviceProxy.setReplied(userModel.getUsername(), id, callback);
    }
    
    protected void reply(final PostModel post, MessageTemplateModel mtm) {
        MailServiceAsync serviceProxy = (MailServiceAsync)GWT.create(MailService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MailService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Utils.errorMessage(Utils.getStacktraceAsString(caught));
                AppEvent evt = new AppEvent(AppEvents.ClearReplyPending, post.getClId());
                fireEvent(evt);
            }

            public void onSuccess(Object result) {
                MailResponse response = (MailResponse)result;
                if (response != null) {
                    if (response.getStatus().booleanValue()) {
                        markReplied(response.getClId());
                    } else {
                        Utils.errorMessage("Sending reply failed, reason:\n" + response.getMessage());
                        AppEvent evt = new AppEvent(AppEvents.ClearReplyPending, response.getClId());
                        fireEvent(evt);
                    }
                }
            }
            
        };
        List l = mtm.getChildren();
        PhotoWrapper[] pics = new PhotoWrapper[l.size()];
        for (int i=0; i<pics.length; i++) {
            pics[i] = (PhotoWrapper)l.get(i);
        }
        StringBuffer subject = new StringBuffer("Re: ");
        subject.append(post.getTitle());
        subject.append(" (CL:").append(post.getClId()).append(')');
        post.set("reply-pending", Boolean.TRUE);
        AppEvent evt = new AppEvent(AppEvents.UpdatePost, post);
        fireEvent(evt);
        UserModel user = (UserModel)Registry.get("user");
        serviceProxy.sendMail(user.getUsername(), post.getId(), post.getReplyAddress(), subject.toString(), mtm.getMessage(), pics, callback);

    }
    
    protected void newReply(PostModel post) {
        manualReply("New Message - " + post.getClId() + " - " + post.getTitle(), post, null);
    }
    
    protected void editReply(PostModel post, final MessageTemplateModel mtm) {
        manualReply("Edit Template - " + post.getClId() + " - " + post.getTitle(), post, mtm);
    }
    
    protected void manualReply(String title, final PostModel post, final MessageTemplateModel mtm) {
        final Dialog complex = new Dialog(Style.OK | Style.CLOSE | Style.RESIZE);
        complex.setMinimumSize(300, 200);
        complex.addStyleName("my-shell-plain");
        complex.setText(title);
        complex.setCloseOnButtonClick(true);
        complex.getButtonBar().getButton(0).setText("Send");
        WidgetContainer content = complex.getContent();
        content.setScrollEnabled(true);
        content.setLayout(new FillLayout());
        final TextArea message = new TextArea();
        if (mtm != null) {
            message.setText(mtm.getMessage());
        }
        content.add(message);
        
        complex.addListener(Events.Close, new Listener() {

            public void handleEvent(BaseEvent be) {
                Button btn = complex.getButtonPressed();
                if (btn != null && message.getText().length() > 0) {
                    MessageTemplateModel editedModel = new MessageTemplateModel();
                    if (mtm != null) {
                        editedModel.setId(mtm.getId());
                        editedModel.setChildren(mtm.getChildren());
                        editedModel.setName(mtm.getName());
                        editedModel.setCategoryName(mtm.getCategoryName());
                    }
                    editedModel.setMessage(message.getText());
                    reply(post, editedModel);
                }
            }

        });  
        complex.setSize(375, 250);
        complex.open();
    }

}
