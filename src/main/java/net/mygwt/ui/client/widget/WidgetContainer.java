/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.widget.layout.FlowLayout;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A <code>Container</code> that lays out its children using a <code>Layout</code>.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : (widget, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see Layout
 */
public class WidgetContainer extends ScrollContainer {
  
  /**
   * The container's layout.
   */
  protected Layout layout;

  /**
   * The size at the last time the layout executed.
   */
  protected Size lastSize;
  
  /**
   * disableLayout specifies if the container's layout is disabled. Default
   * value is <code>false</code>.
   */
  protected boolean disableLayout = false;
  
  private boolean layoutOnChange = false;
  private boolean monitorResize = true;
  private Map layoutMap;
  
  /**
   * Adds a widget to the container.
   * 
   * @param widget the widget to add
   */
  public void add(Widget widget) {
    insert(widget, getWidgetCount());
  }
  
  /**
   * Adds a widget the the container.
   * 
   * @param widget the widget to add
   * @param layoutData the layout data
   */
  public void add(Widget widget, Object layoutData) {
    insert(widget, getWidgetCount(), layoutData);
  }
  
 
  /**
   * Sets the container's layout.
   * 
   * @param layout the new layout
   */
  public void setLayout(Layout layout) {
    this.layout = layout;
  }
  
  /**
   * Creates a new HTML instance and adds it to the container.
   * 
   * @param text the html text
   */
  public void addText(String text) {
    HTML html = new HTML(text);
    add(html);
  }
  
  /**
   * Returns the layout which is associated with the container, or
   * <code>null</code> if one has not been set.
   * 
   * @return the container's layout or <code>null</code>
   */
  public Layout getLayout() {
    return layout;
  }

  /**
   * Returns the widget's layout data.
   * 
   * @param widget the widget
   * @return the layout data
   */
  public Object getLayoutData(Widget widget) {
    if (layoutMap == null) {
      return null;
    }
    return layoutMap.get(widget);
  }

  /**
   * Returns <code>true</code> if the layout will be executed when widgets are
   * added or removed.
   * 
   * @return the layout on change state
   */
  public boolean getLayoutOnChange() {
    return layoutOnChange;
  }

  /**
   * Override this method to specify the element the container's children will
   * be inserted.
   * 
   * @return the element to be used as the panel's container
   */
  public Element getLayoutTarget() {
    return getElement();
  }

  /**
   * Returns the monitor resize state.
   * 
   * @return <code>true</code> if resizing is being monitored
   */
  public boolean getMonitorResize() {
    return monitorResize;
  }

  /**
   * Returns the index of the widget.
   * 
   * @param widget the widget
   * @return the index
   */
  public int indexOf(Widget widget) {
    return items.indexOf(widget);
  }

  /**
   * Inserts a widget into the container.
   * 
   * @param widget the widget to add
   * @param index the insert location
   */
  public void insert(Widget widget, int index) {
    insert(widget, index, null);
  }

  /**
   * Inserts a widget into the container.
   * 
   * @param widget the widget to insert
   * @param index the insert location
   * @param layoutData the layout data
   */
  public void insert(Widget widget, int index, Object layoutData) {
    if (fireEvent(Events.BeforeAdd, this, widget, index)) {
      setLayoutData(widget, layoutData);
      items.add(index, widget);

      if (isRendered() && layoutOnChange) {
        layout(true);
      }

      fireEvent(Events.Add, this, widget, index);
    }
  }

  /**
   * Asks the layout to lay out the container's children. If a layout has not
   * been set a <code>FlowLayout</code> will be used. If the size of the
   * container has not changed since the last time layout was called it will not
   * be execute. See {@link #layout(boolean)} to force the layout to execute.
   */
  public void layout() {
    if (disableLayout) {
      onResize(getOffsetWidth(), getOffsetHeight());
      return;
    }
    if (layout == null) {
      layout = new FlowLayout();
    }
    onLayout();
  }

  /**
   * Asks the layout to lay out the container's children. If a layout has not
   * been set a <code>FlowLayout</code> will be used.
   * 
   * @param force <code>true</code> to force the layout to execute
   */
  public void layout(boolean force) {
    if (force) {
      lastSize = null;
    }
    if (!rendered) {
      render();
    }
    layout();
  }

  public boolean remove(Widget widget) {
    if (fireEvent(Events.BeforeRemove, this, widget)) {
      boolean result = super.remove(widget);

      if (isRendered() && layoutOnChange) {
        layout(true);
      }

      fireEvent(Events.Remove, this, widget);
      return result;
    }
    return false;
  }

  /**
   * Removes all widgets.
   */
  public void removeAll() {
    int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      remove(getWidget(0));
    }
  }

  /**
   * Removes the layout data for the specified widget.
   * 
   * @param widget the widget
   */
  public void removeLayoutData(Widget widget) {
    if (layoutMap != null) {
      layoutMap.remove(widget);
    }
  }

  /**
   * Sets the layout data for the widget.
   * 
   * @param widget the widget
   * @param layoutData the widget's layout data
   */
  public void setLayoutData(Widget widget, Object layoutData) {
    if (layoutMap == null) {
      layoutMap = new HashMap();
    }
    layoutMap.put(widget, layoutData);
  }

  /**
   * Specifies if the container's layout should be called when widgets are added
   * or removed. Default value is <code>false</code>.
   * 
   * @param layoutOnChange <code>true</code> to enable
   */
  public void setLayoutOnChange(boolean layoutOnChange) {
    this.layoutOnChange = layoutOnChange;
  }

  /**
   * Sets the monitor resize state. When <code>true</code> the container's
   * layout will be executed when the container is resized. Default value is
   * <code>true</code>.
   * 
   * @param monitorResize <code>true</code> to monitor resizing
   */
  public void setMonitorResize(boolean monitorResize) {
    this.monitorResize = monitorResize;
  }

  protected void onAttach() {    
    layout(true);
    lastSize = null;
    super.onAttach();
  }

  protected void onLayout() {
    if (getWidgetCount() > 0) {
      Size size = MyDOM.getSize(getLayoutTarget());
      int width = size.width;
      int height = size.height;
      if (lastSize != null) {
        if (lastSize.width == width && lastSize.height == height) {
          return;
        }
      }
      lastSize = new Size(width, height);
    }
    layout.layout(this);
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleAttribute("overflow", "hidden");
    setStyleAttribute("position", "relative");
  }

  protected void onResize(int width, int height) {
    if (monitorResize && !disableLayout) {
      layout();
    }
  }

}
