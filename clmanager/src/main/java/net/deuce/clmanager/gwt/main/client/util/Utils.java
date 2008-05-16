package net.deuce.clmanager.gwt.main.client.util;

import java.util.HashMap;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.MessageBox;
import asquare.gwt.debug.client.Debug;
import asquare.gwt.debug.client.DebugConsole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class Utils {
    
    private static Map modalRequests = new HashMap();
    
    private static int maxDepth = 7;
    
    private Utils() {
        // hide constructor because we are a utility class with static methods only.
    }
    
    public static UserModel getUser() {
        return (UserModel)Registry.get("user");
    }
    
    public static void goModal(final String originator, String message) {
        Debug.println("Going modal: " + originator + ", " + message);
        LoadingPanel.get().show(message);
        modalRequests.put(originator, Boolean.TRUE);
        
        Timer timer = new Timer() {
            public void run() {
                Boolean b = (Boolean)modalRequests.get(originator);
                Debug.println("Modal timer fired for: " + originator + ", active: " + b);
                if (b != null) {
                    clearModal(originator);
                    warningMessage("Server is not responding at this time.");
                }
            }
        };
        timer.schedule(10000);
    }
    
    public static void clearModal(String originator) {
        Debug.println("Clearing modal for: " + originator);
        LoadingPanel.get().hide();
        modalRequests.remove(originator);
    }
    
    public static void infoMessage(String msg) {
        MessageBox messageSent = new MessageBox(Style.ICON_INFO, Style.MODAL | Style.OK);
        messageSent.setMessage(msg);
        messageSent.open(); 
    }
    
    public static void warningMessage(String msg) {
        MessageBox messageSent = new MessageBox(Style.ICON_WARNING, Style.MODAL | Style.OK);
        messageSent.setMessage(msg);
        messageSent.open(); 
    }
    
    public static void errorMessage(String msg) {
        MessageBox messageSent = new MessageBox(Style.ICON_ERROR, Style.MODAL | Style.OK);
        messageSent.setMessage(msg);
        messageSent.open(); 
    }
    
    public static void savePreferences(Map m, final AsyncCallback callback) {
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
    
    public static void savePreference(String name, String value, final AsyncCallback callback) {
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
    
    
    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.hashCode() != o2.hashCode()) return false;
        return o1.equals(o2);
    }
    
    public static void initDebugAndErrorHandling() {
        Debug.enable();
        DebugConsole.getInstance().disable(); // can be enabled for web mode
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable tracepoint) {
                performDefaultErrorHandling(tracepoint);
            }
        });
    }

    public static void initDebugAndErrorHandling(int maxDepth) {
        Utils.maxDepth = maxDepth;
        Debug.enable();
        DebugConsole.getInstance().disable(); // can be enabled for web mode
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable tracepoint) {
                performDefaultErrorHandling(tracepoint);
            }
        });
    }

    public static void performDefaultErrorHandling(final Throwable caught) {
        if (caught != null) {
            final String stacktrace = Utils.getStacktraceAsString(caught);
            Debug.println(stacktrace);
        } else {
            final String message = "Error ocuured, but we have no further information about the cause";
            Debug.println(message);
        }
    }

    public static String getStacktraceAsString(final Throwable tracepoint) {
        final StackTraceElement[] trace = tracepoint.getStackTrace();
        final StringBuffer sbuf = new StringBuffer(2048);
        sbuf.append(tracepoint.toString());
        sbuf.append(": at\n");
        
        final int length = Math.min(maxDepth, trace.length);
        for (int i = 0; i < length; i++) {
            sbuf.append(trace[i].toString());
            sbuf.append("\n");
        }
        if (trace.length > maxDepth) {
            sbuf.append("...");
        }
        final String stacktrace = sbuf.toString();
        return stacktrace;
    }

}
