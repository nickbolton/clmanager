package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.mygwt.ui.client.viewer.Viewer;
import net.mygwt.ui.client.viewer.ViewerFilter;

public interface ViewState {
    public Folder getRootData();
    public Viewer getViewer();
    public ViewerFilter getFilter();
}
