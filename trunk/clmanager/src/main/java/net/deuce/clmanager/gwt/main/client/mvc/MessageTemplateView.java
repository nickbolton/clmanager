package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.viewer.ISelection;
import net.mygwt.ui.client.viewer.ISelectionChangedListener;
import net.mygwt.ui.client.viewer.ListViewer;
import net.mygwt.ui.client.viewer.ModelLabelProvider;
import net.mygwt.ui.client.viewer.SelectionChangedEvent;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.List;
import net.mygwt.ui.client.widget.ToolBar;
import net.mygwt.ui.client.widget.ToolItem;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MessageTemplateView extends View {
    
    private Dialog dialog;
    private List list;
    private ListViewer viewer;
    private Folder root;
    private ContentPanel messageTemplateView;
    private WidgetContainer listView;
    
    public MessageTemplateView(Controller controller) {
      super(controller);
    }
    
    protected void initialize() {
        dialog = new Dialog(Style.OK | Style.CLOSE | Style.RESIZE);
        dialog.setMinimumSize(625, 500);
        //dialog.addStyleName("my-shell-plain");
        dialog.setText("Message Templates");
        
        dialog.addListener(Events.Close, new Listener() {  
            public void handleEvent(BaseEvent be) {  
                Button btn = dialog.getButtonPressed();  
                if (btn != null) {  
                    
                }  
            }  
        });
        dialog.getButtonById(0).addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                dialog.close();
            }
        });

        BorderLayout layout = new BorderLayout();
        layout.setMargin(0);
        
        WidgetContainer c = dialog.getContent();
        c.setBorders(false);
        c.setLayout(layout);

        list = new List();
        list.setBorders(false);
        list.setSelectionMode(Style.SINGLE);
        root = new Folder("root");
        viewer = new ListViewer(list);
        viewer.setContentProvider(new MessageTemplateContentProvider());
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
        
        ToolBar toolbar = new ToolBar();
        ToolItem addItem = new ToolItem(Style.PUSH);
        addItem.setText("+");
        addItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                final MessageTemplateModel mtm = new MessageTemplateModel();
                mtm.setName("New");
                serviceProxy.createMessageTemplate(mtm, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Debug.println(Utils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        MessageTemplateModel mm = (MessageTemplateModel)result;
                        viewer.setInput(root);
                    }
                    
                });
            }
        });
        toolbar.add(addItem);
        
        ToolItem deleteItem = new ToolItem(Style.PUSH);
        deleteItem.setText("-");
        deleteItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                ISelection selection = viewer.getSelection();
                if (selection.size() == 1) {
                    MessageTemplateModel mtm = (MessageTemplateModel)selection.getFirstElement();
                    
                    serviceProxy.deleteMessageTemplate(mtm.getId(), new AsyncCallback() {
                        public void onFailure(Throwable caught) {
                            Debug.println(Utils.getStacktraceAsString(caught));
                        }
    
                        public void onSuccess(Object result) {
                            viewer.setInput(root);
                            Dispatcher.forwardEvent(AppEvents.ResetMessageTemplateView);
                        }
                        
                    });
                }
            }
        });
        toolbar.add(deleteItem);
        
        listView = new WidgetContainer();
        listView.setScrollEnabled(true);
        
        listView.add(toolbar);
        listView.add(list);
        
        c.add(listView, new BorderLayoutData(Style.WEST, 100));
        messageTemplateView = new ContentPanel();
        Registry.register("messageTemplateView", messageTemplateView);
        c.add(messageTemplateView, new BorderLayoutData(Style.CENTER));
        viewer.setInput(root);
    }
    
    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.NavMessageTemplates:
            dialog.open();
            break;
        case AppEvents.RefreshMessageTemplates:
            viewer.update();
            break;
        }
    }

}
