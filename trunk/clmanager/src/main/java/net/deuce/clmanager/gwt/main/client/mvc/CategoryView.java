package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.viewer.CheckStateChangedEvent;
import net.mygwt.ui.client.viewer.ICheckStateListener;
import net.mygwt.ui.client.viewer.ModelLabelProvider;
import net.mygwt.ui.client.viewer.TreeViewer;
import net.mygwt.ui.client.viewer.TreeViewerFilterTextBox;
import net.mygwt.ui.client.viewer.Viewer;
import net.mygwt.ui.client.viewer.ViewerFilter;
import net.mygwt.ui.client.viewer.ViewerSorter;
import net.mygwt.ui.client.widget.SearchableExpandItem;
import net.mygwt.ui.client.widget.tree.Tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class CategoryView extends BaseView {

    private Tree tree;

    private TreeViewer folders;

    private Folder root;

    public CategoryView(Controller controller) {
        super(controller);
    }

    TreeViewer getTreeViewer() {
        return folders;
    }

    protected void initialize() {
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
                                    Window.alert(DebugUtils
                                        .getStacktraceAsString(caught));
                                }

                                public void onSuccess(Object result) {
                                    CategoryModel cat = (CategoryModel) element;
                                    cat.setSubscribed(new Boolean(state));
                                    folders.setInput(root);
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
        /*
        folders.addSelectionListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent se) {
                AppEvent evt = new AppEvent(AppEvents.ViewPostList, se
                    .getSelection());
                fireEvent(evt);
            }

        });
        */
        
        final TreeViewerFilterTextBox filterTextBox = new TreeViewerFilterTextBox();
        filterTextBox.bind(folders);
        ViewerFilter filter = new ViewerFilter() {
            public boolean select(Viewer viewer, Object parent, Object element) {
                if (filterTextBox.getText().length() == 0) {
                    return true;
                }

                if (element instanceof CategoryModel) {
                    CategoryModel cat = (CategoryModel) element;
                    if (cat.getName().toLowerCase().indexOf(
                        filterTextBox.getText().toLowerCase()) >= 0) {
                        return true;
                    }
                } else if (element instanceof CategoryModel) {
                    CategoryModel category = (CategoryModel)element;
                    return select(viewer, null, category.getParent());
                }
                return false;
            }

        };
        folders.addFilter(filter);

        SearchableExpandItem categoriesItem = (SearchableExpandItem) Registry.get("categoriesItem");
        categoriesItem.setFilterTextBox(filterTextBox);
        categoriesItem.getContainer().add(tree);
        categoriesItem.getContainer().layout(true);

        root = new Folder("root");
        folders.setContentProvider(new CategoryContentProvider((UserModel)Registry.get("user"), this));
        folders.setInput(root);
        
    }

    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.NavCategories) {
            AppEvent evt = new AppEvent(AppEvents.UnfocusCenter);
            fireEvent(evt);
        }
    }
    
    public void delegateEvent(AppEvent evt) {
        fireEvent(evt);
    }

}
