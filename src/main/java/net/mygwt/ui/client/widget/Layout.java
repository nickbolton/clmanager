/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyDOM;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A layout controls the position and size of the children of a
 * <code>Container</code>.
 * 
 * @see WidgetContainer#setLayout(Layout)
 */
public abstract class Layout {

  protected WidgetContainer container;

  /**
   * Lays out the children of the specified container according to this layout.
   * <p>
   * This method positions and sizes the children of a container using the
   * layout algorithm encoded by this layout. The position of the container is
   * not altered by this method.
   * </p>
   * 
   * @param container a container panel using this layout
   */
  public void layout(WidgetContainer container) {
    this.container = container;
    Element target = container.getLayoutTarget();
    
    // physical attach
    onLayout(container, target);
    
    // set parents & attach
    int count = container.getWidgetCount();
    for (int i = 0; i < count; i++) {
      Widget w = container.getWidget(i);
      if (w.getParent() != container) {
        w.removeFromParent();
        WidgetHelper.setParent(w, container);
      }
      if (container.isAttached() && !w.isAttached()) {
        WidgetHelper.doAttach(w);
      }
    }
  }

  protected boolean isValidParent(Element elem, Element parent) {
    if (DOM.compare(DOM.getParent(elem), parent)) {
      return true;
    }
    return false;
  }

  protected void onLayout(WidgetContainer container, Element target) {
    renderAll(container, target);
  }

  protected void renderAll(Container container, Element target) {
    int count = container.getWidgetCount();
    for (int i = 0; i < count; i++) {
      Widget w = (Widget) container.getWidget(i);

      if (!isValidParent(w.getElement(), target)) {
        renderWidget(w, i, target);
      }

    }
  }

  protected void renderWidget(Widget widget, int index, Element target) {
    DOM.insertChild(target, widget.getElement(), index);
  }

  protected void setBounds(Widget w, int x, int y, int width, int height) {
    if (w instanceof Component) {
      ((Component) w).setBounds(x, y, width, height);
    } else {
      MyDOM.setBounds(w.getElement(), x, y, width, height, true);
    }
  }

  protected void setSize(Widget w, int width, int height) {
    if (w instanceof Component) {
      ((Component) w).setSize(width, height);
    } else {
      MyDOM.setSize(w.getElement(), width, height, true);
    }
  }

}
