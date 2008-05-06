package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.MessagePhotoService;
import net.deuce.clmanager.gwt.main.client.MessagePhotoServiceAsync;
import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.MessageTemplateServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.ImageModel;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.deuce.clmanager.gwt.main.client.widget.SelectableImage;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import net.mygwt.ui.client.widget.layout.RowData;
import net.mygwt.ui.client.widget.layout.RowLayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

public class ViewMessageTemplateView extends BaseView {

    private WidgetContainer page;
    private WidgetContainer photos;
    private TextField nameField;
    private TextArea messageField;
    private ComboBox categories;
    private Store categoryStore;
    private MessageTemplateModel model;
    
    public ViewMessageTemplateView(Controller controller) {
        super(controller);
    }
    
    private void clearPhotoSelections() {
        for (int i=0; i<photos.getWidgetCount(); i++) {
            ((SelectableImage)photos.getWidget(i)).setSelected(false);
        }
    }
    
    private void selectPhoto(ImageModel im) {
        for (int i=0; i<photos.getWidgetCount(); i++) {
            SelectableImage si = (SelectableImage)photos.getWidget(i);
            if (si.getImageModel().getPath().equals(im.getPath())) {
                si.setSelected(true);
            }
        }
    }
    
    private void reset() {
        clearPhotoSelections();
        nameField.setValue(model.getName());
        messageField.setValue(model.getMessage());
        if (model.getCategoryName() != null) {
            categories.setValue(model.getCategoryName());
        }
        List l = model.getChildren();
        for (int i=0; i<l.size(); i++) {
            ImageModel im = (ImageModel)l.get(i);
            selectPhoto(im);
        }
    }

    protected void handleEvent(AppEvent event) {
        int eventType = event.type;
        int viewPostList = AppEvents.ViewMessageTemplate;
        if (eventType == viewPostList) {
            AppEvent evt = new AppEvent(AppEvents.FocusCenter);
            fireEvent(evt);

            if (event.data instanceof MessageTemplateModel) {
                model = (MessageTemplateModel) event.data;
                
                final ContentPanel focus = (ContentPanel) Registry.get("focus");
                focus.setText("Edit Message Template");
                focus.removeAll();
                focus.add(page);
                photos.removeAll();
                
                MessagePhotoServiceAsync serviceProxy = (MessagePhotoServiceAsync)GWT.create(MessagePhotoService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessagePhotoService");
                serviceProxy.getMessagePhotos(new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        List l = (List)result;
                        for (int i=0; i<l.size(); i++) {
                            photos.add(new SelectableImage((ImageModel)l.get(i)), new RowData(((RowData.FILL_BOTH))));
                        }
                        reset();
                        focus.layout(true);
                    }
                });

                //viewer.setInput(selection);

            }
        }
    }

    protected void initialize() {
        page = new WidgetContainer();
        page.setLayout(new BorderLayout());
        page.addListener(Events.BeforeRemove, new Listener() {
            public void handleEvent(BaseEvent be) {
            }
        });
        FormPanel form = new FormPanel();
        page.add(form, new BorderLayoutData(Style.CENTER));
        
        categories = new ComboBox("Default Category");
        categories.setDisplayField("category");
        categories.setMode(ComboBox.LOCAL);
        buildCategories();
        form.add(categories);
        
        nameField = new TextField("Name", "name", 400);
        nameField.setAllowBlank(false);
        form.add(nameField);
        
        messageField = new TextArea("Message", "message");
        messageField.setGrow(true);
        messageField.setHeight(200);
        messageField.setWidth(400);
        form.add(messageField);

        Button saveButton = new Button("Save");
        saveButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                model.setName(nameField.getValueAsString());
                model.setMessage(messageField.getValueAsString());
                model.setCategoryName(categories.getValue());
                model.removeAll();
                for (int i=0; i<photos.getWidgetCount(); i++) {
                    SelectableImage si = (SelectableImage)photos.getWidget(i);
                    if (si.isSelected()) {
                        model.add(si.getImageModel());
                    }
                }
                MessageTemplateServiceAsync serviceProxy = (MessageTemplateServiceAsync)GWT.create(MessageTemplateService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MessageTemplateService");
                LoadingPanel.get().show("Saving Template...");
                serviceProxy.updateMessageTemplate(model, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        LoadingPanel.get().hide();
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
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
        form.add(buttons);
        buttons.add(saveButton);
        
        Button resetButton = new Button("Reset");
        resetButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                reset();
            }
        });
        buttons.add(resetButton);
        
        photos = new WidgetContainer();
        photos.setBorders(true);
        page.add(photos, new BorderLayoutData(Style.EAST, 300, 150, 250));
        photos.setScrollEnabled(true);
        photos.setLayout(new RowLayout());
        
    }
    
    private void buildCategories() {
        List l = (List)Registry.get("categories");
        if (l == null) {
            fetchAndBuildCategories();
            return;
        }
        Object[][] categoryData = new Object[l.size()][];
        for (int i=0; i<l.size(); i++) {
            CategoryModel category = (CategoryModel)l.get(i);
            categoryData[i] = new Object[] {category.getName() };
        }
        categoryStore = new SimpleStore(new String[]{"category"}, categoryData);
        categoryStore.load();
        categories.setStore(categoryStore);
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
                Window.alert(DebugUtils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                LoadingPanel.get().hide();
                List l = (List)result;
                Object[][] categoryData = new Object[l.size()][];
                for (int i=0; i<l.size(); i++) {
                    CategoryModel category = (CategoryModel)l.get(i);
                    categoryData[i] = new Object[] {category.getName() };
                }
                categoryStore = new SimpleStore(new String[]{"category"}, categoryData);
                categoryStore.load();
                categories.setStore(categoryStore);
                Registry.register("categories", l);
            }   
        });
    }
}
