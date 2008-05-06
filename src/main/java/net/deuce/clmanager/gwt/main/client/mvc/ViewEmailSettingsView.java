package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.HashMap;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class ViewEmailSettingsView extends BaseView {

    private WidgetContainer page;
    private TextField smtpServer;
    private Checkbox smtpAuth;
    private NumberField smtpPort;
    private TextField fromEmailAddress;
    private TextField bccEmailAddress;
    private TextField smtpUser;
    private TextField smtpPassword;
    
    public ViewEmailSettingsView(Controller controller) {
        super(controller);
    }
    
    private void reset() {
        UserModel user = (UserModel)Registry.get("user");

        String value = user.getPreference("smtpServer");
        if (value != null) {
            smtpServer.setValue(value);
        }
        value = user.getPreference("smtpAuth");
        if (value != null) {
            smtpAuth.setChecked(Boolean.valueOf(value).booleanValue());
        }
        value = user.getPreference("smtpPort");
        if (value != null) {
            smtpPort.setValue(value);
        }
        value = user.getPreference("fromEmailAddress");
        if (value != null) {
            fromEmailAddress.setValue(value);
        }
        value = user.getPreference("bccEmailAddress");
        if (value != null) {
            bccEmailAddress.setValue(value);
        }
        value = user.getPreference("smtpUser");
        if (value != null) {
            smtpUser.setValue(value);
        }
        value = user.getPreference("smtpPassword");
        if (value != null) {
            smtpPassword.setValue(value);
        }
    }
    
    protected void handleEvent(AppEvent event) {
        int eventType = event.type;
        int viewPostList = AppEvents.ViewEmailSettings;
        if (eventType == viewPostList) {
            AppEvent evt = new AppEvent(AppEvents.FocusCenter);
            fireEvent(evt);
            
            final ContentPanel focus = (ContentPanel) Registry.get("focus");
            focus.setText("Email Settings");
            focus.removeAll();
            focus.add(page);
            reset();
            focus.layout();
        }
    }

    protected void initialize() {
        page = new WidgetContainer();
        page.setLayout(new BorderLayout());
        page.addListener(Events.BeforeRemove, new Listener() {
            public void handleEvent(BaseEvent be) {
            }
        });
        FormPanel form = new FormPanel();
        page.add(form, new BorderLayoutData(Style.CENTER));
        
        smtpAuth = new Checkbox("SMTP AUTH", "smtpAuth");
        form.add(smtpAuth);
        smtpServer = new TextField("SMTP Server", "smtpServer", 400);
        form.add(smtpServer);
        smtpPort = new NumberField("SMTP Port", "smtpPort", 400);
        form.add(smtpPort);
        smtpUser = new TextField("SMTP Email Address", "smtpUser", 400);
        form.add(smtpUser);
        smtpPassword = new TextField("Password", "smtpPassword", 400);
        smtpPassword.setPassword(true);
        form.add(smtpPassword);
        fromEmailAddress = new TextField("From Email Address", "fromEmailAddress", 400);
        form.add(fromEmailAddress);
        bccEmailAddress = new TextField("BCC Email Address", "bccEmailAddress", 400);
        form.add(bccEmailAddress);
        

        Button saveButton = new Button("Save");
        saveButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                
                final Map preferences = new HashMap();
                preferences.put("smtpAuth", smtpAuth.getValueAsString());
                preferences.put("smtpServer", smtpServer.getValueAsString());
                preferences.put("smtpPort", smtpPort.getValueAsString());
                preferences.put("smtpUser", smtpUser.getValueAsString());
                preferences.put("smtpPassword", smtpPassword.getValueAsString());
                preferences.put("fromEmailAddress", fromEmailAddress.getValueAsString());
                preferences.put("bccEmailAddress", bccEmailAddress.getValueAsString());
                
                UserModel user = (UserModel)Registry.get("user");
                UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
                final String modalOriginator = "ViewEmailSettingsView.UserService::setPreferences";
                goModal(modalOriginator, "Saving Template...");
                serviceProxy.setPreferences(user.getUsername(), preferences, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        clearModal(modalOriginator);
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        clearModal(modalOriginator);
                        UserModel user = (UserModel)Registry.get("user");
                        user.addAll(preferences);
                    }
                });
            }

        });
        HorizontalPanel buttons = new HorizontalPanel();
        form.add(buttons);
        buttons.add(saveButton);
        
        Button resetButton = new Button("Reset");
        resetButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                reset();
            }
        });
        buttons.add(resetButton);
        
    }
    
}
