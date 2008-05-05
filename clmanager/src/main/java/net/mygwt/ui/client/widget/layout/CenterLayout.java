/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.widget.Container;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <code>CenterLayout</code> centers a single widget within its container.
 */
public class CenterLayout extends TableLayout {

  private TableLayoutData data;

  /**
   * Creates a new center layout.
   */
  public CenterLayout() {
    width = "100%";
    height = "100%";

    data = new TableLayoutData();
    data.setAlign(TableLayoutData.ALIGN_CENTER);
    data.setVAlign(TableLayoutData.ALIGN_MIDDLE);
  }

  protected void renderAll(Container container, Element target) {
    Widget w = (Widget) container.getWidget(0);
    if (w != null && !isValidParent(w.getElement(), target)) {
      renderWidget(w, 0, target);
    }
  }

  protected Element getNextCell(Widget widget) {
    container.setLayoutData(widget, data);
    return super.getNextCell(widget);
  }

}
