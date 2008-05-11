package net.deuce.clmanager.gwt.main.client.util;

import asquare.gwt.debug.client.Debug;
import asquare.gwt.debug.client.DebugConsole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

public class Utils {
    
    private static int maxDepth = 7;
    
    private Utils() {
        // hide constructor because we are a utility class with static methods only.
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
