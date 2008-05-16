package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.ExternalPhoto;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.deuce.clmanager.gwt.main.client.widget.SelectableImages;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.WidgetContainer;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewMessageTemplateView extends View {

    private DockPanel page;
    private SelectableImages photos;
    private TextBox nameField;
    private TextArea messageField;
    private ListBox categories;
    private MessageTemplateModel model;
    private WidgetContainer container;
    
    public ViewMessageTemplateView(Controller controller) {
        super(controller);
    }
    
    private void clearPhotoSelections() {
        photos.primePhotos();
        photos.clearSelection();
    }
    
    private void reset() {
        clearPhotoSelections();
        if (model != null) {
            nameField.setText(model.getName());
            messageField.setText(model.getMessage());
            if (model.getCategoryName() != null) {
                int itemIndex=-1;
                for (int i=0; itemIndex < 0 && i<categories.getItemCount(); i++) {
                    if (Utils.isEqual(categories.getItemText(i), model.getCategoryName())) {
                        itemIndex = i;
                    }
                }
                if (itemIndex >= 0) {
                    categories.setSelectedIndex(itemIndex);
                }
            }
            photos.setSelected(model.getChildren());
        } else {
            nameField.setText("");
            messageField.setText("");
        }
    }

    protected void handleEvent(AppEvent event) {
        int eventType = event.type;
        int viewPostList = AppEvents.ViewMessageTemplate;
        switch (event.type) {
        case AppEvents.ViewMessageTemplate:
            if (event.data instanceof MessageTemplateModel) {
                model = (MessageTemplateModel) event.data;
                if (container == null) {
                    container = (ContentPanel) Registry.get("messageTemplateView");
                    container.removeAll();
                    container.add(page);
                    container.setBorders(true);
                    container.layout(true);
                }
            }
            reset();
            break;
        case AppEvents.ResetMessageTemplateView:
            model = null;
            container.removeAll();
            container = null;
            break;
        }
    }

    private HorizontalPanel createLabeledField(String label, FocusWidget field, int labelWidth, int fieldWidth) {
        HorizontalPanel hp = new HorizontalPanel();
        Label l = new Label(label);
        l.setWidth(""+labelWidth);
        hp.add(l);
        field.setWidth(""+fieldWidth);
        hp.add(field);
        return hp;
    }

    protected void initialize() {
        
        page = new DockPanel();

        photos = new SelectableImages(75, 75, .2, .5, "500px", "150px");
        page.add(photos, DockPanel.NORTH);
        FormPanel form = new FormPanel();
        VerticalPanel vp = new VerticalPanel();
        vp.add(form);
        page.add(vp, DockPanel.CENTER);
        
        categories = new ListBox(false);
        categories.setVisibleItemCount(1);
        buildCategories();
        vp.add(createLabeledField("Default Category", categories, 100, 400));
        
        nameField = new TextBox();
        vp.add(createLabeledField("Name", nameField, 100, 400));
        
        messageField = new TextArea();
        messageField.setHeight("200px");
        vp.add(createLabeledField("Message", messageField, 100, 400));

        Button saveButton = new Button("Save");
        saveButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                if (model == null) return;
                
                model.setName(nameField.getText());
                model.setMessage(messageField.getText());
                model.setCategoryName(categories.getItemText(categories.getSelectedIndex()));
                
                List selected = photos.getSelected();
                List photoWrappers = new ArrayList(selected.size());
                for (int i=0; i<selected.size(); i++) {
                    ExternalPhoto p = (ExternalPhoto)selected.get(i);
                    photoWrappers.add(new PhotoWrapper(new Long(-1L), p.getService(), p.getId(), p.getStandard()));
                }
                model.setChildren(photoWrappers);
                
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                LoadingPanel.get().show("Saving Template...");
                serviceProxy.updateMessageTemplate(model, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        LoadingPanel.get().hide();
                        Debug.println(Utils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        LoadingPanel.get().hide();
                        AppEvent evt = new AppEvent(AppEvents.RefreshMessageTemplates);
                        fireEvent(evt);
                    }
                });
            }

        });
        HorizontalPanel buttons = new HorizontalPanel();
        vp.add(buttons);
        buttons.add(saveButton);
        
        Button resetButton = new Button("Reset");
        resetButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                reset();
            }
        });
        buttons.add(resetButton);
        
    }
    
    private void buildCategories() {
        List l = (List)Registry.get("categories");
        if (l == null) {
            fetchAndBuildCategories();
            return;
        }
        for (int i=0; i<l.size(); i++) {
            CategoryModel category = (CategoryModel)l.get(i);
            categories.addItem(category.getName());
        }
    }
    
    private void fetchAndBuildCategories() {
        CategoryServiceAsync serviceProxy = (CategoryServiceAsync)GWT.create(CategoryService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
        LoadingPanel.get().show("Loading Categories...");
        UserModel userModel = (UserModel)Registry.get("user");
        serviceProxy.getSubscribedCategories(userModel.getUsername(), new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                LoadingPanel.get().hide();
                Debug.println(Utils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                LoadingPanel.get().hide();
                List l = (List)result;
                for (int i=0; i<l.size(); i++) {
                    CategoryModel category = (CategoryModel)l.get(i);
                    categories.addItem(category.getName());
                }
                Registry.register("categories", l);
            }   
        });
    }
}
