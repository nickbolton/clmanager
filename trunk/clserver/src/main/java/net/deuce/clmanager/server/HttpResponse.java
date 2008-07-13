package net.deuce.clmanager.server;

import org.dom4j.Document;

public class HttpResponse {

    private int responseCode;
    private Document response;
    
    public HttpResponse(int responseCode) {
        this.responseCode = responseCode;
    }
    
    public HttpResponse(int responseCode, Document doc) {
        this.responseCode = responseCode;
        this.response = doc;
    }
    
    public int getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public Document getResponse() {
        return response;
    }
    public void setResponse(Document response) {
        this.response = response;
    }
    
    
}
