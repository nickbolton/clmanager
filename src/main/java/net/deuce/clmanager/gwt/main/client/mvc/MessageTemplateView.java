package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.viewer.ISelection;
import net.mygwt.ui.client.viewer.ISelectionChangedListener;
import net.mygwt.ui.client.viewer.ListViewer;
import net.mygwt.ui.client.viewer.ModelLabelProvider;
import net.mygwt.ui.client.viewer.SelectionChangedEvent;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.ButtonBar;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.ExpandItem;
import net.mygwt.ui.client.widget.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MessageTemplateView extends BaseView {
    
    private List list;
    private ListViewer viewer;
    private Folder root;
    
    public MessageTemplateView(Controller controller) {
      super(controller);
    }
    
    protected void initialize() {
        list = new List();
        list.setBorders(false);
        list.setSelectionMode(Style.SINGLE);
        root = new Folder("root");
        viewer = new ListViewer(list);
        viewer.setContentProvider(new MessageTemplateContentProvider(this));
        viewer.setLabelProvider(new ModelLabelProvider());
        
        viewer.addSelectionListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                if (selection.size() == 1) {
                    MessageTemplateModel mtm = (MessageTemplateModel)selection.getFirstElement();
                    AppEvent evt = new AppEvent(AppEvents.ViewMessageTemplate, mtm);
                    fireEvent(evt);
                }
            }
        });
        
        ButtonBar buttonBar = new ButtonBar(Style.LEFT);
        Button addButton = new Button("+");
        addButton.setSize(20, 20);
        addButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                final MessageTemplateModel mtm = new MessageTemplateModel();
                mtm.setName("New Message Template");
                serviceProxy.createMessageTemplate(mtm, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        MessageTemplateModel mm = (MessageTemplateModel)result;
                        viewer.setInput(root);
                    }
                    
                });
            }
        });
        buttonBar.add(addButton);
        Button deleteButton = new Button("-");
        deleteButton.setSize(20, 20);
        deleteButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                ISelection selection = viewer.getSelection();
                if (selection.size() == 1) {
                    MessageTemplateModel mtm = (MessageTemplateModel)selection.getFirstElement();
                    
                    serviceProxy.deleteMessageTemplate(mtm.getId(), new AsyncCallback() {
                        public void onFailure(Throwable caught) {
                            Window.alert(DebugUtils.getStacktraceAsString(caught));
                        }
    
                        public void onSuccess(Object result) {
                            final ContentPanel focus = (ContentPanel) Registry.get("focus");
                            focus.removeAll();
                            viewer.setInput(root);
                        }
                        
                    });
                }
            }
        });
        buttonBar.add(deleteButton);
        
        ExpandItem citiesItem = (ExpandItem) Registry.get("messageTemplateItem");
        citiesItem.getContainer().add(buttonBar);
        citiesItem.getContainer().add(list);
        citiesItem.getContainer().layout(true);
        
        viewer.setInput(root);
    }
    
    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.NavMessageTemplates:
            AppEvent evt = new AppEvent(AppEvents.FocusCenter);
            fireEvent(evt);
            break;
        case AppEvents.RefreshMessageTemplates:
            viewer.update();
            break;
        }
    }

}
