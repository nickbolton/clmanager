package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.MessageBox;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.user.client.Timer;

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
    
}
