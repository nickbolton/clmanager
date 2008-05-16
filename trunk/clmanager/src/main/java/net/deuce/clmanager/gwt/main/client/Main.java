package net.deuce.clmanager.gwt.main.client;

import java.util.Date;

import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.mvc.AppController;
import net.deuce.clmanager.gwt.main.client.mvc.AppEvents;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.deuce.clmanager.gwt.main.client.widget.LoginWindow;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.mvc.DispatcherListener;
import net.mygwt.ui.client.widget.Viewport;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint, HistoryListener {
    
    private Dispatcher dispatcher;

    public void onModuleLoad() {
        //DebugUtils.initDebugAndErrorHandling(20);
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
          LoginWindow loginWindow = new LoginWindow(new AsyncCallback() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Object result) {
                String username = (String)result;
                Date aYearLater = new Date();
                aYearLater.setTime(aYearLater.getTime()+(365*24*3600000));
                Cookies.setCookie("clmanager-user", username, aYearLater);
                startApplication(username);
            }
              
          });
          Viewport vp = new Viewport();
          vp.hideLoadingPanel("loading");
          vp.dispose();
          loginWindow.show();
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
              Debug.println(Utils.getStacktraceAsString(caught));
          } 
           
          public void onSuccess (Object result) { 
              UserModel userModel = (UserModel)result;
              Registry.register("user", userModel);
              dispatcher.dispatch(AppEvents.Init);
          } 
      });
    }
    
    public void onHistoryChanged(String historyToken) {
        // TODO Auto-generated method stub
        
    }

}
