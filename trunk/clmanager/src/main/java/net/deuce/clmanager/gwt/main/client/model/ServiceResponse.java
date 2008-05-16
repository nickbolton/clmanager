package net.deuce.clmanager.gwt.main.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ServiceResponse implements IsSerializable {
    
    private boolean ok = true;
    private String errorMessage = "";
    
    public ServiceResponse() {
    }
    
    public ServiceResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
    
    public String toString() {
        return "ServiceResponse - ok=" + ok +
            ", errorMessage=" + errorMessage;
    }
    
    public boolean isOk() {
        return ok;
    }
    
    public void setOk(boolean ok) {
        this.ok = ok;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
