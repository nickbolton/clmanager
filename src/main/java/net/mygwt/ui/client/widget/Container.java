/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A <code>Component</code> that contains child widgets.
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-container (the container itself)</dd>
 * </dl>
 */
public abstract class Container extends Component {

  /**
   * attachChildren specifies if the container's children should be attached and
   * detached. Default value is <code>true</code>.
   */
  protected boolean attachChildren = true;

  /**
   * The container's children.
   */
  protected List items;

  private boolean autoDispose;

  /**
   * Creates a new container.
   */
  public Container() {
    items = new ArrayList();
  }

  public void dispose() {
    int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      remove(getWidget(0));
    }
    super.dispose();
  }

  /**
   * Returns the widget whose element or child element matches the given
   * element.
   * 
   * @param elem the element
   * @return the matching widget or <code>null</code> if no match
   */
  public Widget findWidget(Element elem) {
    int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      Widget w = getWidget(i);
      if (DOM.isOrHasChild(w.getElement(), elem)) {
        return w;
      }
    }
    return null;
  }

  /**
   * Returns the auto dispose state. When <code>true</code>, child widgets
   * will be disposed when removed from the container. Default value is
   * <code>false</code>.
   * 
   * @return the auto dispose state
   */
  public boolean getAutoDispose() {
    return autoDispose;
  }

  /**
   * Returns the widget at the specified index.
   * 
   * @param index the index
   * @return the widget
   */
  public Widget getWidget(int index) {
    return (Widget) items.get(index);
  }

  /**
   * Returns the number of child widgets.
   * 
   * @return the widget count
   */
  public int getWidgetCount() {
    return items.size();
  }

  /**
   * Returns an iterator over the container's children.
   * 
   * @return an iterator
   */
  public Iterator iterator() {
    return items.iterator();
  }

  /**
   * Removes a widget from the container.
   * 
   * @param widget the widget to remove
   * @return <code>true</code> if the widget was removed
   */
  public boolean remove(Widget widget) {
    // orphon
    if (attachChildren) {
      if (widget.getParent() != this) {
        return false;
      }
      orphan(widget);
    }
    
    // physical remove
    if (rendered) {
      Element elem = widget.getElement();
      Element parent = DOM.getParent(elem);
      if (parent != null) {
        DOM.removeChild(parent, elem);
      }
    }
    
    // logical remove
    items.remove(widget);

    if (autoDispose && widget instanceof Component) {
      ((Component) widget).dispose();
    }
    return true;
  }

  /**
   * Sets the auto dispose state. When <code>true</code>, the container will
   * automatically dispose any child widget that is removed from it. Default
   * value is <code>false</code>
   * 
   * @param autoDispose the auto dispose state
   */
  public void setAutoDispose(boolean autoDispose) {
    this.autoDispose = autoDispose;
  }

  protected final void adopt(Widget child) {
    assert (child.getParent() == null);
    WidgetHelper.setParent(child, this);
  }

  protected void doAttachChildren() {
    if (attachChildren) {
      for (Iterator it = items.iterator(); it.hasNext();) {
        Widget child = (Widget) it.next();
        WidgetHelper.doAttach(child);
      }
    }
  }

  protected void doDetachChildren() {
    if (attachChildren) {
      for (Iterator it = items.iterator(); it.hasNext();) {
        Widget child = (Widget) it.next();
        WidgetHelper.doDetach(child);
      }
    }
  }

  protected final void orphan(Widget child) {
    assert (child.getParent() == this);
    WidgetHelper.setParent(child, null);
  }
}
