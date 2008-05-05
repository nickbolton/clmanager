package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.viewer.ViewerFilterTextBox;

public class SearchableExpandItem extends ExpandItem {

    public SearchableExpandItem() {
        super();

        header = new Item() {
            protected void onClick(BaseEvent be) {
                super.onClick(be);
            }
        };
    }
    
    public void setFilterTextBox(ViewerFilterTextBox filterTextBox) {
        header.insertWidget(filterTextBox, 0);
    }

}
