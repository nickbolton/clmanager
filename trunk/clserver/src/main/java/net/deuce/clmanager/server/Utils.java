package net.deuce.clmanager.server;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class Utils {
    
    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.hashCode() != o2.hashCode()) return false;
        return o1.equals(o2);
    }
    
    public static HttpResponse getResponse(String url, int numRetries) throws Exception {
        return getResponse(url, numRetries, false, true);
    }
    
    public static HttpResponse getResponse(String url, int numRetries, boolean onlyIf200, boolean followRedirects) throws Exception {
        if (numRetries < 0) {
            numRetries = 0;
        }
        Exception exception = null;
        int numTries = 0;
        while (numTries++ <= numRetries) {
            try {
                return _getResponse(url, onlyIf200, followRedirects);
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        }
        throw new RuntimeException("Unknown exception");
    }
    
    private static HttpResponse _getResponse(String url, boolean onlyIf200, boolean followRedirects) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        method.setFollowRedirects(followRedirects);
        client.executeMethod(method);
        
        int rc = method.getStatusCode();
        
        if (onlyIf200 && rc != 200) {
            return new HttpResponse(rc);
        }
        InputStream is = null;
        try {
            is = method.getResponseBodyAsStream();
            Parser p = new Parser();
            p.setFeature("http://xml.org/sax/features/namespace-prefixes",true);
            SAXContentHandler saxch = new SAXContentHandler();
            p.setContentHandler(saxch);
            p.parse(new InputSource(is));
            return new HttpResponse(rc, saxch.getDocument());
        } finally {
            if (is != null) { is.close(); is = null; }
        }
    }
    
    public static HttpResponse getRssResponse(String url, int numRetries, boolean onlyIf200, boolean followRedirects) throws Exception {
        if (numRetries < 0) {
            numRetries = 0;
        }
        Exception exception = null;
        int numTries = 0;
        while (numTries++ <= numRetries) {
            try {
                return _getRssResponse(url, onlyIf200, followRedirects);
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        }
        throw new RuntimeException("Unknown exception");
    }
    
    private static HttpResponse _getRssResponse(String url, boolean onlyIf200, boolean followRedirects) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        method.setFollowRedirects(followRedirects);
        client.executeMethod(method);
        
        int rc = method.getStatusCode();
        
        if (onlyIf200 && rc != 200) {
            return new HttpResponse(rc);
        }
        InputStream is = null;
        try {
            is = method.getResponseBodyAsStream();
            SAXReader reader = new SAXReader();
            return new HttpResponse(rc, reader.read(is));
        } finally {
            if (is != null) { is.close(); is = null; }
        }
    }

    public static int getResponseCode(String url, int numRetries) throws Exception {
        if (numRetries < 0) {
            numRetries = 0;
        }
        Exception exception = null;
        int numTries = 0;
        while (numTries++ <= numRetries) {
            try {
                return _getResponseCode(url);
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        }
        throw new RuntimeException("Unknown exception");
    }
    
    private static int _getResponseCode(String url) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        client.executeMethod(method);
        return method.getStatusCode();
    }
}
