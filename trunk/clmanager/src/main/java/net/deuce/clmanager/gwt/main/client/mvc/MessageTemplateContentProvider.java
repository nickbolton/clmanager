package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.data.DataCallback;
import net.mygwt.ui.client.data.LoadConfig;
import net.mygwt.ui.client.viewer.IAsyncContentCallback;
import net.mygwt.ui.client.viewer.IContentProvider;
import net.mygwt.ui.client.viewer.RemoteContentProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MessageTemplateContentProvider extends RemoteContentProvider
    implements IContentProvider {
    
    private BaseView view;
    
    public MessageTemplateContentProvider(BaseView view) {
        this.view = view;
    }

    public void getData(LoadConfig config, DataCallback callback) {
    }

    public void getElements(Object input, final IAsyncContentCallback contentCallback) {
        MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
        final String modalOriginator = "MessageTemplateContentProvider.MessageTemplateService::getMessageTemplates";
        view.goModal(modalOriginator, "Loading Message Templates...");
        serviceProxy.getMessageTemplates(new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                view.clearModal(modalOriginator);
                Window.alert(DebugUtils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                view.clearModal(modalOriginator);
                List l = (List)result;
                MessageTemplateModel[] mtm = new MessageTemplateModel[l.size()];
                for (int i=0; i<l.size(); i++) {
                    mtm[i] = (MessageTemplateModel)l.get(i);
                }
                contentCallback.setElements(mtm);
            } 
        });
    }

}
