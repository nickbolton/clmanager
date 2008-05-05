package net.deuce.clmanager.gwt.main.client.widget;

import net.deuce.clmanager.gwt.main.client.model.ImageModel;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.RowData;
import net.mygwt.ui.client.widget.layout.RowLayout;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

public class SelectableImage extends Composite {
    
    private ImageModel model;
    private CheckBox checkBox;
    
    public SelectableImage() {
    }
    
    public SelectableImage(ImageModel model) {
        super();
        this.model = model;
        final WidgetContainer images = new WidgetContainer();
        images.setLayout(new RowLayout(Style.HORIZONTAL));

        checkBox = new CheckBox();
        images.add(checkBox, new RowData(Style.CENTER));
        
        final Image image = new Image("/clserver"+model.getPath());
        images.add(image);

        initWidget(images);
    }
    
    public ImageModel getImageModel() {
        return model;
    }
    
    public boolean isSelected() {
        return checkBox.isChecked();
    }

    public void setSelected(boolean b) {
        checkBox.setChecked(b);
    }
}
