package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.CityService;
import net.deuce.clmanager.gwt.main.client.CityServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CityModel;
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

public class CityContentProvider extends ModelTreeContentProvider 
implements ICheckable {
    
    private CityView view;
    private UserModel userModel;
    
    public CityContentProvider(UserModel userModel, CityView view) {
        this.userModel = userModel;
        this.view = view;
    }
    
    public void getChildren(Object parent, final IAsyncContentCallback callback) {
        if (parent == null) return;
        if (parent instanceof Folder) {
            final Folder folder = (Folder)parent;
            String parentName = folder.getName();
            if ("root".equals(parentName)) {
                CityServiceAsync serviceProxy = (CityServiceAsync)GWT.create(CityService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CityService");
                final String modalOriginator = "CityContentProvider.CityService::getCities";
                view.goModal(modalOriginator, "Loading Cities...");
                serviceProxy.getCities(userModel.getUsername(), new AsyncCallback() {
                    public void onFailure (Throwable caught) { 
                        view.clearModal(modalOriginator);
                        Window.alert(DebugUtils.getStacktraceAsString(caught));
                    } 
                     
                    public void onSuccess (Object result) { 
                        view.clearModal(modalOriginator);
                        List l = (List)result;
                        List cities = new ArrayList();
                        for (int i=0; i<l.size(); i++) {
                            CityModel model = (CityModel)l.get(i);
                            cities.add(model);
                        }
                        callback.setElements(cities.toArray());
                        
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
        return element instanceof CityModel && 
            ((CityModel)element).isSubscribed().booleanValue();
    }

    public void removeCheckStateListener(ICheckStateListener listener) {
    }

    public boolean setChecked(final Object element, final boolean state) {
        return true;
    }

}
