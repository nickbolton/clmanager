package net.deuce.clmanager.gwt.main.client;

import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.mvc.AppController;
import net.deuce.clmanager.gwt.main.client.mvc.AppEvents;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.mvc.DispatcherListener;
import asquare.gwt.debug.client.Debug;
import asquare.gwt.debug.client.DebugConsole;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint, HistoryListener {
    
    private Dispatcher dispatcher;

    public void onModuleLoad() {
        DebugUtils.initDebugAndErrorHandling(20);
      //Registry.register("cl", createModel());
        
        com.gwtext.client.widgets.Window debugWindow = new com.gwtext.client.widgets.Window("Debug Console", 550, 400, false, true);
        debugWindow.add(DebugConsole.getInstance());
        DebugConsole.getInstance().enable();
        debugWindow.setMaximizable(true);
        debugWindow.setMinimizable(true);
        debugWindow.show();
        Debug.println("Debugging...");
      
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
      
      UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
      ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
      target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
      serviceProxy.getUser("deuce", new AsyncCallback() {
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

    public void onHistoryChanged(String historyToken) {
        // TODO Auto-generated method stub
        
    }

}
