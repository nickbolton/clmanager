package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.mygwt.ui.client.viewer.Viewer;
import net.mygwt.ui.client.viewer.ViewerFilter;

public class DefaultViewState implements ViewState {
    
    private Folder rootData;
    private Viewer viewer;
    private ViewerFilter filter;
    
    public DefaultViewState(Folder rootData, Viewer viewer, ViewerFilter filter) {
        this.rootData = rootData;
        this.viewer = viewer;
        this.filter = filter;
    }

    public ViewerFilter getFilter() {
        return filter;
    }

    public Folder getRootData() {
        return rootData;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setRootData(Folder rootData) {
        this.rootData = rootData;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void setFilter(ViewerFilter filter) {
        this.filter = filter;
    }

}
