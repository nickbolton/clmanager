package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
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

public class CategoryView extends View {

    private Tree tree;
    TreeViewerFilterTextBox filterTextBox;
    ViewerFilter filter;
    private TreeViewer folders;

    private Folder root;
    private ContentPanel panel;

    public CategoryView(Controller controller) {
        super(controller);
    }

    TreeViewer getTreeViewer() {
        return folders;
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
                if (e1 instanceof CategoryModel && e2 instanceof CategoryModel) {
                    CategoryModel cm1 = (CategoryModel) e1;
                    CategoryModel cm2 = (CategoryModel) e2;
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
                if (element instanceof CategoryModel) {
                    CategoryServiceAsync serviceProxy = (CategoryServiceAsync) GWT.create(CategoryService.class);
                    ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
                    final CategoryModel cm = (CategoryModel)element;
                    if (state != cm.isSubscribed().booleanValue()) {
                        UserModel userModel = (UserModel)Registry.get("user");
                        serviceProxy.subscribe(userModel.getUsername(), ((CategoryModel) element).getName(),
                            state, new AsyncCallback() {
                                public void onFailure(Throwable caught) {
                                    Debug.println(Utils
                                        .getStacktraceAsString(caught));
                                }

                                public void onSuccess(Object result) {
                                    CategoryModel cat = (CategoryModel) element;
                                    cat.setSubscribed(new Boolean(state));
                                    root.remove(cat);
                                    cat.setSubscribed(new Boolean(state));
                                    root.add(cat);
                                    AppEvent evt = new AppEvent(AppEvents.CategorySubscribed);
                                    evt.data = cat;
                                    fireEvent(evt);
                                }

                            });
                    }
                }
            }
        });
        folders.setLabelProvider(new ModelLabelProvider() {

            public String getText(Object element) {
                if (element instanceof CategoryModel) {
                    return ((CategoryModel)element).getName();
                }
                return super.getText(element);
            }
            
        });
        
        filterTextBox = new TreeViewerFilterTextBox();
        filterTextBox.bind(folders);
        filterTextBox.setText(Utils.getUser().getPreference("categorySearchTerm"));
        filterTextBox.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                Utils.savePreference("categorySearchTerm", filterTextBox.getText(), null);
            }

        });
        
        filterTextBox.bind(folders);
        filter = new ViewerFilter() {
            public boolean select(Viewer viewer, Object parent, Object element) {
                if (filterTextBox.getText().length() == 0) {
                    return true;
                }

                if (element instanceof CategoryModel) {
                    CategoryModel category = (CategoryModel) element;
                    if (category.getName().toLowerCase().indexOf(
                        filterTextBox.getText().toLowerCase()) >= 0) {
                        return true;
                    }
                }
                return false;
            }

        };
        folders.addFilter(filter);

        folders.setContentProvider(new CategoryContentProvider());
        
        loadCategories();
    }
    
    private void loadCategories() {
        CategoryServiceAsync serviceProxy = (CategoryServiceAsync)GWT.create(CategoryService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
        final String modalOriginator = "CategoryContentProvider.CategoryService::getCategories";
        Utils.goModal(modalOriginator, "Loading Categories...");
        serviceProxy.getCategories(Utils.getUser().getUsername(), new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                Utils.clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                Utils.clearModal(modalOriginator);
                List l = (List)result;
                for (int i=0; i<l.size(); i++) {
                    CategoryModel model = (CategoryModel)l.get(i);
                    root.add(model);
                }
                folders.setInput(root);
            } 
        });
    }

    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.ViewCategories) {
            if (panel == null) {
                panel = (ContentPanel)event.data;
                panel.setText("Categories");
                panel.add(tree);
                panel.setScrollEnabled(true);
                panel.getHeader().addWidget(filterTextBox);
                final ToolItem clearSearchButton = new ToolItem(Style.PUSH);
                clearSearchButton.setText("x");
                clearSearchButton.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(BaseEvent be) {
                        filterTextBox.setText("");
                        folders.refresh();
                        Utils.savePreference("categorySearchTerm", "", null);
                    }
                });
                panel.getHeader().addWidget(clearSearchButton);
            } else if (root.getChildCount() == 0) {
                loadCategories();
            }
        }
    }

}
