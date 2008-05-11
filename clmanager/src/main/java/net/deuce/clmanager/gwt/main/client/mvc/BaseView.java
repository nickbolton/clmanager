package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.HashMap;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.MessageBox;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public abstract class BaseView extends View {

    private Map modalRequests = new HashMap();

    public BaseView(Controller controller) {
        super(controller);
    }
    
    protected void goModal(final String originator, String message) {
        Debug.println("Going modal: " + originator + ", " + message);
        LoadingPanel.get().show(message);
        modalRequests.put(originator, Boolean.TRUE);
        
        Timer timer = new Timer() {
            public void run() {
                Boolean b = (Boolean)modalRequests.get(originator);
                Debug.println("Modal timer fired for: " + originator + ", active: " + b);
                if (b != null) {
                    clearModal(originator);
                    MessageBox messageSent = new MessageBox(Style.ICON_WARNING, Style.MODAL | Style.OK);
                    messageSent.setMessage("Server is not responding at this time.");
                    messageSent.open(); 
                }
            }
        };
        timer.schedule(10000);
    }
    
    protected void clearModal(String originator) {
        Debug.println("Clearing modal for: " + originator);
        LoadingPanel.get().hide();
        modalRequests.remove(originator);
    }
    
    protected UserModel getUser() {
        return (UserModel)Registry.get("user");
    }
    
    protected void savePreferences(Map m, final AsyncCallback callback) {
        UserModel user = (UserModel)Registry.get("user");
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        final String modalOriginator = "BaseListView.UserService::setPreferences";
        goModal(modalOriginator, "Saving Preferences...");
        serviceProxy.setPreferences(user.getUsername(), m, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void onSuccess(Object result) {
                clearModal(modalOriginator);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }
    
    protected void savePreference(String name, String value, final AsyncCallback callback) {
        UserModel user = (UserModel)Registry.get("user");
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        serviceProxy.setPreference(user.getUsername(), name, value, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void onSuccess(Object result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }
    
}
