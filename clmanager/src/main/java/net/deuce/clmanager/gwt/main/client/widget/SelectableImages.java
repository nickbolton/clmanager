package net.deuce.clmanager.gwt.main.client.widget;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.ExternalPhoto;

import com.totsp.gwittir.client.beans.Binding;
import com.totsp.gwittir.client.fx.ui.SoftScrollArea;
import com.totsp.gwittir.client.ui.Renderer;

public class SelectableImages extends SoftScrollArea {
    
    private ImageSelection imageGroup;
    private FlickrSearchBean search;
    private Binding images;

    public SelectableImages(int baseWidth, int baseHeight, double reflectHeight, double opacity, String width, String height) {
        super();
        init(baseWidth, baseHeight, reflectHeight, opacity);
        setWidth(width);
        setHeight(height);
    }

    public SelectableImages(boolean limit, int baseWidth, int baseHeight, double reflectHeight, double opacity, String width, String height) {
        super(limit);
        init(baseWidth, baseHeight, reflectHeight, opacity);
        setWidth(width);
        setHeight(height);
    }
    
    public void setSelected(List photos) {
        imageGroup.setInitiallySelected(photos);
    }
    
    public List getSelected() {
        return imageGroup.getSelected();
    }
    
    public void clearSelection() {
        imageGroup.clearSelection();
    }
    
    public void primePhotos() {
        search.getPhotos();
    }
    
    private void init(int baseWidth, int baseHeight, double reflectHeight, double opacity) {
        imageGroup = new ImageSelection(baseWidth, baseHeight, reflectHeight, opacity);
        imageGroup.setRenderer(new Renderer() {
            public Object render(Object o) {
                return ((ExternalPhoto)o).getThumbnail();
            }
        });
        setWidget(imageGroup);
        addMouseListener(MOUSE_MOVE_SCROLL_LISTENER);
        
        createFlickrBindings();
    }
    
    private void createFlickrBindings() {
        search = new FlickrSearchBean();
        images = new Binding(imageGroup, "value", search, "photos");
        images.setLeft();
        images.bind();
    }
    
}
