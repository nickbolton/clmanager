package net.deuce.clmanager.gwt.main.client;

import java.util.Date;

import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.mvc.AppController;
import net.deuce.clmanager.gwt.main.client.mvc.AppEvents;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.mvc.DispatcherListener;
import net.mygwt.ui.client.widget.Viewport;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint, HistoryListener {
    
    private Dispatcher dispatcher;

    public void onModuleLoad() {
        DebugUtils.initDebugAndErrorHandling(20);
      //Registry.register("cl", createModel());
        
      
      dispatcher = Dispatcher.get();
      AppController appController = new AppController();
      //PostController postController = new PostController();
      //appController.addChild(postController);
      dispatcher.addController(appController);
      //dispatcher.addController(postController);
      //dispatcher.addController(new TaskController());
      //dispatcher.addController(new ContactController());
      
      // you can listen for dispatch events
      dispatcher.addDispatcherListener(new DispatcherListener() {
        public void beforeDispatch(AppEvent ae) {
        }
        public void afterDispatch(AppEvent ae) {
        }
      });
      

      String username = Cookies.getCookie("clmanager-user");
      if (username == null) {
          promptForUsername();
      } else {
          startApplication(username);
      }

    }
    
    private void startApplication(String username) {
      UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
      ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
      target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
      serviceProxy.getUser(username, new AsyncCallback() {
          public void onFailure (Throwable caught) { 
              Window.alert(DebugUtils.getStacktraceAsString(caught));
          } 
           
          public void onSuccess (Object result) { 
              UserModel userModel = (UserModel)result;
              Registry.register("user", userModel);
              
              
              dispatcher.dispatch(AppEvents.Init);
          } 
      });
    }
    
    private void promptForUsername() {
        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window("Debug Console", 300, 130, false, true);
        window.setClosable(false);
        
        FormPanel panel = new FormPanel();  
        window.add(panel);
        panel.setFrame(true);  
        //panel.setFieldWidth(210);  
        panel.setLabelWidth(100);  
        panel.setButtonAlign(Position.CENTER);
        panel.setWidth(400);  
        
        final TextField text = new TextField();  
        text.setFieldLabel("Username");  
        text.setEmptyText("Enter your username");  
        text.setAllowBlank(false);  
        text.setMinLength(4);  
        text.addKeyListener(13, new KeyListener() {
            public void onKey(int key, EventObject e) {
                String username = text.getValueAsString().trim();
                if (username.length() > 0) {
                    Date aYearLater = new Date();
                    aYearLater.setTime(aYearLater.getTime()+(365*24*3600000));
                    
                    Cookies.setCookie("clmanager-user", username, aYearLater);
                    startApplication(username);
                    window.close();
                }
            }
        });
        panel.add(text);  
        
        Button ok = new Button("Ok");
        ok.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                String username = text.getValueAsString().trim();
                if (username.length() > 0) {
                    Cookies.setCookie("clmanager-user", username);
                    startApplication(username);
                    window.close();
                } else {
                    e.stopEvent();
                }
            }
        });
        panel.addButton(ok);  
        
        Viewport vp = new Viewport();
        vp.hideLoadingPanel("loading");
        vp.dispose();
        window.show();
    }

    public void onHistoryChanged(String historyToken) {
        // TODO Auto-generated method stub
        
    }

}
