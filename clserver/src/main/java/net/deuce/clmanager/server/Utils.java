package net.deuce.clmanager.server;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Document;
import org.dom4j.io.SAXContentHandler;
import org.xml.sax.InputSource;

public class Utils {
    
    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.hashCode() != o2.hashCode()) return false;
        return o1.equals(o2);
    }
    
    public static Document getResponse(String url) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        client.executeMethod(method);
        StringWriter sw = new StringWriter();
        InputStream is = null;
        try {
            is = method.getResponseBodyAsStream();
            Parser p = new Parser();
            p.setFeature("http://xml.org/sax/features/namespace-prefixes",true);
            SAXContentHandler saxch = new SAXContentHandler();
            p.setContentHandler(saxch);
            p.parse(new InputSource(is));
            return saxch.getDocument();
        } finally {
            if (is != null) { is.close(); is = null; }
        }
    }

}
