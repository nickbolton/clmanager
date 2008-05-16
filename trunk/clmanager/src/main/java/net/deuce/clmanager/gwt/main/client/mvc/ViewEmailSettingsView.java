package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.HashMap;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.deuce.clmanager.gwt.main.client.widget.NumberTextBox;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewEmailSettingsView extends View {

    private Dialog dialog;
    private WidgetContainer page;
    private TextBox smtpServer;
    private CheckBox smtpAuth;
    private NumberTextBox smtpPort;
    private TextBox fromEmailAddress;
    private TextBox bccEmailAddress;
    private TextBox smtpUser;
    private PasswordTextBox smtpPassword;
    
    public ViewEmailSettingsView(Controller controller) {
        super(controller);
    }
    
    private void reset() {
        UserModel user = (UserModel)Registry.get("user");

        String value = user.getPreference("smtpServer");
        if (value != null) {
            smtpServer.setText(value);
        }
        value = user.getPreference("smtpAuth");
        if (value != null) {
            smtpAuth.setChecked(Boolean.valueOf(value).booleanValue());
        }
        value = user.getPreference("smtpPort");
        if (value != null) {
            smtpPort.setText(value);
        }
        value = user.getPreference("fromEmailAddress");
        if (value != null) {
            fromEmailAddress.setText(value);
        }
        value = user.getPreference("bccEmailAddress");
        if (value != null) {
            bccEmailAddress.setText(value);
        }
        value = user.getPreference("smtpUser");
        if (value != null) {
            smtpUser.setText(value);
        }
        value = user.getPreference("smtpPassword");
        if (value != null) {
            smtpPassword.setText(value);
        }
    }
    
    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.NavEmailSettings) {
            dialog.open();
        }
    }
    
    private HorizontalPanel createLabeledField(String label, FocusWidget field, int labelWidth, int fieldWidth) {
        HorizontalPanel hp = new HorizontalPanel();
        Label l = new Label(label);
        l.setWidth(""+labelWidth);
        hp.add(l);
        field.setWidth(""+fieldWidth);
        hp.add(field);
        return hp;
    }

    protected void initialize() {
        dialog = new Dialog(Style.OK_CANCEL | Style.CLOSE | Style.RESIZE);
        dialog.setMinimumSize(400, 200);
        dialog.center();
        dialog.addListener(Events.Close, new Listener() {  
            public void handleEvent(BaseEvent be) {  
                Button btn = dialog.getButtonPressed();  
                if (btn != null) {  
                    
                }  
            }  
        });
        dialog.getButtonById(0).addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                final Map preferences = new HashMap();
                preferences.put("smtpAuth", smtpAuth.getText());
                preferences.put("smtpServer", smtpServer.getText());
                preferences.put("smtpPort", smtpPort.getText());
                preferences.put("smtpUser", smtpUser.getText());
                preferences.put("smtpPassword", smtpPassword.getText());
                preferences.put("fromEmailAddress", fromEmailAddress.getText());
                preferences.put("bccEmailAddress", bccEmailAddress.getText());
                
                UserModel user = (UserModel)Registry.get("user");
                UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
                final String modalOriginator = "ViewEmailSettingsView.UserService::setPreferences";
                Utils.goModal(modalOriginator, "Saving Template...");
                serviceProxy.setPreferences(user.getUsername(), preferences, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Utils.clearModal(modalOriginator);
                        Debug.println(Utils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        Utils.clearModal(modalOriginator);
                        UserModel user = (UserModel)Registry.get("user");
                        user.addAll(preferences);
                        dialog.close();
                    }
                });
            }
        });
        dialog.getButtonById(1).addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                dialog.close();
            }
        });
        //dialog.addStyleName("my-shell-plain");
        dialog.setText("Email Settings");

        BorderLayout layout = new BorderLayout();
        layout.setMargin(0);

        page = dialog.getContent();
        page.setBorders(false);
        page.setLayout(layout);

        FormPanel form = new FormPanel();
        VerticalPanel vp = new VerticalPanel();
        
        smtpAuth = new CheckBox("SMTP AUTH");
        vp.add(smtpAuth);
        smtpServer = new TextBox();
        vp.add(createLabeledField("SMTP Server", smtpServer, 100, 200));
        smtpPort = new NumberTextBox(0, 99999, 25, 5);
        vp.add(createLabeledField("SMTP PORT", smtpPort, 100, 200));
        smtpUser = new TextBox();
        vp.add(createLabeledField("SMTP Email Address", smtpUser, 100, 200));
        smtpPassword = new PasswordTextBox();
        vp.add(createLabeledField("SMTP Password", smtpPassword, 100, 200));
        fromEmailAddress = new TextBox();
        vp.add(createLabeledField("From Email Address", fromEmailAddress, 100, 200));
        bccEmailAddress = new TextBox();
        vp.add(createLabeledField("BCC Email Address", bccEmailAddress, 100, 200));
        

        form.setWidget(vp);
        page.add(form, new BorderLayoutData(Style.CENTER));
    }
    
}
