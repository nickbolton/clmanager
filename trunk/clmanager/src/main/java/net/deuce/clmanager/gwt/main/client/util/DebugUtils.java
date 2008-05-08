package net.deuce.clmanager.gwt.main.client.util;

import asquare.gwt.debug.client.Debug;
import asquare.gwt.debug.client.DebugConsole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

public class DebugUtils {
    
    private static int maxDepth = 7;
    
    private DebugUtils() {
        // hide constructor because we are a utility class with static methods only.
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
        DebugUtils.maxDepth = maxDepth;
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
            final String stacktrace = DebugUtils.getStacktraceAsString(caught);
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
