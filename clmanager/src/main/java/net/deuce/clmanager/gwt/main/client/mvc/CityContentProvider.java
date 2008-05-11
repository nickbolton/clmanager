package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.model.CityModel;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.viewer.IAsyncContentCallback;
import net.mygwt.ui.client.viewer.ICheckStateListener;
import net.mygwt.ui.client.viewer.ICheckable;
import net.mygwt.ui.client.viewer.ModelTreeContentProvider;

public class CityContentProvider extends ModelTreeContentProvider 
implements ICheckable {
    
    
    public CityContentProvider() {
    }
    
    public void getChildren(Object parent, final IAsyncContentCallback callback) {
        if (parent == null) return;
        if (parent instanceof Folder) {
            final Folder folder = (Folder)parent;
            String parentName = folder.getName();
            if (Utils.isEqual("root", parentName)) {
                callback.setElements(folder.getChildren().toArray());
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
