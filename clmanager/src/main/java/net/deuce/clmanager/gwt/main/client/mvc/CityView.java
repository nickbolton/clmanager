package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.CityService;
import net.deuce.clmanager.gwt.main.client.CityServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.CityModel;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.viewer.CheckStateChangedEvent;
import net.mygwt.ui.client.viewer.ICheckStateListener;
import net.mygwt.ui.client.viewer.ISelectionChangedListener;
import net.mygwt.ui.client.viewer.ModelLabelProvider;
import net.mygwt.ui.client.viewer.SelectionChangedEvent;
import net.mygwt.ui.client.viewer.TreeViewer;
import net.mygwt.ui.client.viewer.TreeViewerFilterTextBox;
import net.mygwt.ui.client.viewer.Viewer;
import net.mygwt.ui.client.viewer.ViewerFilter;
import net.mygwt.ui.client.viewer.ViewerSorter;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.ToolItem;
import net.mygwt.ui.client.widget.tree.Tree;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

public class CityView extends View {

    private Tree tree;

    private TreeViewer folders;
    private TreeViewerFilterTextBox filterTextBox;
    private ViewerFilter filter;
    private Folder root;
    private ContentPanel panel;

    public CityView(Controller controller) {
        super(controller);
    }

    protected void initialize() {
        root = new Folder("root");
        tree = new Tree(Style.CHECK);
        tree.setItemImageStyle("tree-folder");
        tree.setSelectionMode(Style.MULTI);
        // tree.setCheckNodes(Style.DEFAULT);
        tree.setCheckStyle(Style.CHILDREN);

        folders = new TreeViewer(tree);
        folders.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object e1, Object e2) {
                if (e1 instanceof CityModel && e2 instanceof CityModel) {
                    CityModel cm1 = (CityModel) e1;
                    CityModel cm2 = (CityModel) e2;
                    if (cm1.isSubscribed().booleanValue() != cm2.isSubscribed()
                        .booleanValue()) {
                        if (cm1.isSubscribed().booleanValue()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    return cm1.getName().toLowerCase().compareTo(
                        cm2.getName().toLowerCase());
                } else if (e1 instanceof CategoryModel && e2 instanceof CategoryModel) {
                    return ((CategoryModel)e1).getName().compareTo(((CategoryModel)e2).getName());
                }
                return super.compare(viewer, e1, e2);
            }
        });
        folders.addSelectionListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                Object element = event.getSelection().getFirstElement();
                if (element != null) {
                    folders.setChecked(element, !folders.getChecked(element));
                }
            }
        });
        folders.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                final Object element = event.getElement();
                final boolean state = event.getChecked();
                if (element instanceof CityModel) {
                    CityServiceAsync serviceProxy = (CityServiceAsync) GWT.create(CityService.class);
                    ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CityService");
                    final CityModel cm = (CityModel)element;
                    if (state != cm.isSubscribed().booleanValue()) {
                        UserModel userModel = (UserModel)Registry.get("user");
                        serviceProxy.subscribe(userModel.getUsername(), ((CityModel) element).getName(),
                            state, new AsyncCallback() {
                                public void onFailure(Throwable caught) {
                                    Debug.println(Utils
                                        .getStacktraceAsString(caught));
                                }

                                public void onSuccess(Object result) {
                                    CityModel city = (CityModel) element;
                                    root.remove(city);
                                    city.setSubscribed(new Boolean(state));
                                    root.add(city);
                                    AppEvent evt = new AppEvent(AppEvents.CitySubscribed);
                                    evt.data = city;
                                    fireEvent(evt);
                                }

                            });
                    }
                }
            }
        });
        folders.setLabelProvider(new ModelLabelProvider() {

            public String getText(Object element) {
                if (element instanceof CityModel) {
                    return ((CityModel)element).getName();
                }
                return super.getText(element);
            }
            
        });
        
        filterTextBox = new TreeViewerFilterTextBox();
        filterTextBox.bind(folders);
        filterTextBox.setText(Utils.getUser().getPreference("citySearchTerm"));
        filterTextBox.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                Utils.savePreference("citySearchTerm", filterTextBox.getText(), null);
            }

        });
        filter = new ViewerFilter() {
            public boolean select(Viewer viewer, Object parent, Object element) {
                if (filterTextBox.getText().length() == 0) {
                    return true;
                }

                if (element instanceof CityModel) {
                    CityModel city = (CityModel) element;
                    if (city.getName().toLowerCase().indexOf(
                        filterTextBox.getText().toLowerCase()) >= 0) {
                        return true;
                    }
                }
                return false;
            }

        };
        
        folders.setContentProvider(new CityContentProvider());
        
        folders.addFilter(filter);
        
        loadCities();
        
    }
    
    private void loadCities() {
        CityServiceAsync serviceProxy = (CityServiceAsync)GWT.create(CityService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CityService");
        final String modalOriginator = "CityContentProvider.CityService::getCities";
        Utils.goModal(modalOriginator, "Loading Cities...");
        serviceProxy.getCities(Utils.getUser().getUsername(), new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                Utils.clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
            } 
                 
            public void onSuccess (Object result) { 
                Utils.clearModal(modalOriginator);
                List l = (List)result;
                for (int i=0; i<l.size(); i++) {
                    CityModel model = (CityModel)l.get(i);
                    root.add(model);
                }
                folders.setInput(root);
            } 
        });
    }

    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.ViewCities) {
            if (panel == null) {
                panel = (ContentPanel)event.data;
                panel.setText("Cities");
                panel.add(tree);
                panel.setScrollEnabled(true);
                panel.getHeader().addWidget(filterTextBox);
                final ToolItem clearSearchButton = new ToolItem(Style.PUSH);
                clearSearchButton.setText("x");
                clearSearchButton.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(BaseEvent be) {
                        filterTextBox.setText("");
                        folders.refresh();
                        Utils.savePreference("citySearchTerm", "", null);
                    }
                });
                panel.getHeader().addWidget(clearSearchButton);
            } else if (root.getChildCount() == 0) {
                loadCities();
            }
        }
    }
    
    public void delegateEvent(AppEvent evt) {
        fireEvent(evt);
    }

}
