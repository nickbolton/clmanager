package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.DebugUtils;
import net.mygwt.ui.client.viewer.IAsyncContentCallback;
import net.mygwt.ui.client.viewer.ICheckStateListener;
import net.mygwt.ui.client.viewer.ICheckable;
import net.mygwt.ui.client.viewer.ModelTreeContentProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class CategoryContentProvider extends ModelTreeContentProvider 
implements ICheckable {
    
    private CategoryView view;
    private UserModel userModel;
    
    public CategoryContentProvider(UserModel userModel, CategoryView view) {
        this.userModel = userModel;
        this.view = view;
    }
    
    public void getChildren(Object parent, final IAsyncContentCallback callback) {
        if (parent == null) return;
        if (parent instanceof Folder) {
            final Folder folder = (Folder)parent;
            String parentName = folder.getName();
            if ("root".equals(parentName)) {
                CategoryServiceAsync serviceProxy = (CategoryServiceAsync)GWT.create(CategoryService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
                final String modalOriginator = "CategoryContentProvider.CategoryService::getCategories";
                view.goModal(modalOriginator, "Loading Categories...");
                serviceProxy.getCategories(userModel.getUsername(), new AsyncCallback() {
                    public void onFailure (Throwable caught) { 
                        view.clearModal(modalOriginator);
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
                    } 
                     
                    public void onSuccess (Object result) { 
                        view.clearModal(modalOriginator);
                        List l = (List)result;
                        List categories = new ArrayList();
                        for (int i=0; i<l.size(); i++) {
                            CategoryModel model = (CategoryModel)l.get(i);
                            categories.add(model);
                        }
                        callback.setElements(categories.toArray());
                        
                    } 
                });
            }
        }
    }
    
    public boolean hasChildren(Object element) {
        return element instanceof Folder;
    }

    public void addCheckStateListener(ICheckStateListener listener) {
    }

    public boolean getChecked(Object element) {
        return element instanceof CategoryModel && 
            ((CategoryModel)element).isSubscribed().booleanValue();
    }

    public void removeCheckStateListener(ICheckStateListener listener) {
    }

    public boolean setChecked(final Object element, final boolean state) {
        return true;
    }

}
