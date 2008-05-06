/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;

import com.google.gwt.user.client.DOM;

/**
 * A vertical container of <code>ExpandItems</code>.
 * 
 * When using a style of MULTI the height of the expand bar should not be set
 * and the expand item containers should only use a FlowLayout. The expand item
 * container's size is not managed by the expand bar.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE - Single item can be expanded (Accordian)</dd>
 * <dd>MULTI - Multiple items can be expanded </dd>
 * <dd>HEADER - Enables expand / collpase on expand item clicks</dd>
 * 
 * <dt>Events:</dt>
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
 * <div>Fires before an item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : (widget, item)<br>
 * <div>Fires before an item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeExpand</b> : (widget, item)<br>
 * <div>Fires before an item is expanded. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget, item)<br>
 * <div>Fires before an item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after an item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after an item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget, item)<br>
 * <div>Fires after an item has been expanded.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget, item)<br>
 * <div>Fires ater an item is collapsed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being collapsed</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-expand-bar (the bar itself)</dd>
 * </dl>
 */
public class ExpandBar extends Container {

  boolean headerCollapse = false;
  boolean single = false;
  private ExpandItem expanded;
  private int headerHeight = 22;

  /**
   * Creates a new SINGLE expand bar.
   */
  public ExpandBar() {
    this(Style.SINGLE);
  }

  /**
   * Creates a new expand bar.
   * 
   * @param style the style information
   */
  public ExpandBar(int style) {
    this.style = style;
  }

  /**
   * Adds an expand item.
   * 
   * @param item the item to be added
   */
  public void add(ExpandItem item) {
    insert(item, getItemCount());
  }

  /**
   * Returns the header height.
   * 
   * @return the height
   */
  public int getHeaderHeight() {
    return headerHeight;
  }

  /**
   * Returns the item at the given index.
   * 
   * @param index the index of the item to return
   * @return the item at the given index
   */
  public ExpandItem getItem(int index) {
    if ((index < 0) || (index >= items.size())) return null;
    return (ExpandItem) items.get(index);
  }

  /**
   * Returns the item count.
   * 
   * @return the count
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the index of the given item.
   * 
   * @param item the item
   * @return the items index
   */
  public int indexOf(ExpandItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts the item at the specified index.
   * 
   * @param item the item
   * @param index the insert location
   */
  public void insert(ExpandItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      items.add(index, item);
      item.parent = this;
      adopt(item);

      if (rendered) {
        renderItem(item, index);
        adjustExpandedSize();
        updateStyles();
      }
      fireEvent(Events.Add, this, item, index);
    }
  }

  public void recalculate() {
    if (expanded != null) {
      adjustExpandedSize();
    }
    updateStyles();
  }

  /**
   * Removes a item.
   * 
   * @param item the item
   */
  public void remove(ExpandItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      super.remove(item);
      updateStyles();
      fireEvent(Events.Remove, this, item);
    }
  }

  /**
   * Removes all expand items.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      ExpandItem item = getItem(0);
      remove(item);
    }
  }

  /**
   * Sets the expanded state of the item.
   * 
   * @param item the item
   * @param expanded the new expanded state
   */
  public void setExpanded(ExpandItem item, final boolean expanded) {
    item.setExpanded(expanded);
  }

  /**
   * Sets the header height. Default value is 22.
   * 
   * @param headerHeight the header height
   */
  public void setHeaderHeight(int headerHeight) {
    this.headerHeight = headerHeight;
  }

  /**
   * Sets the expand bar's style. Has no effect if called after the expand bar
   * has been rendered.
   * 
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  protected void afterCollapse(ExpandItem item) {
    item.expanded = false;
    if (expanded == item) {
      expanded = null;
    }
    updateStyles();
    item.fireEvent(Events.Collapse);
    fireEvent(Events.Collapse, this, item);
  }

  protected void afterExpand(ExpandItem item) {
    item.expanded = true;
    updateStyles();
    item.fireEvent(Events.Expand);
    fireEvent(Events.Expand, this, item);
  }

  protected void onCollapse(ExpandItem item) {
    item.getContainer().setVisible(false);
    item.collapseBtn.changeStyle("my-tool-plus");
    afterCollapse(item);
  }

  protected void onExpand(ExpandItem item) {
    item.getContainer().setVisible(true);
    adjustExpandedSize();
    afterExpand(item);
    item.collapseBtn.changeStyle("my-tool-minus");
  }
  

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-expand-bar");
    setStyleAttribute("position", "static");

    if ((style & Style.HEADER) != 0) {
      headerCollapse = true;
    }
    if ((style & Style.SINGLE) != 0) {
      single = true;
    }

    renderAll();
  }

  void collapse(final ExpandItem item) {
    onCollapse(item);
  }

  void expand(final ExpandItem item) {
    if (single) {
      if (expanded != null) {
        onCollapse(expanded);
      }
      expanded = item;
    }
    onExpand(item);
  }


  protected void afterRender() {
    // TODO Auto-generated method stub
    super.afterRender();
  }

  protected void onLoad() {
    // TODO Auto-generated method stub
    super.onLoad();
  }

  private void adjustExpandedSize() {
    if (single && expanded != null) {
      expanded.getContainer().setWidth(getWidth(true));
      if (single) {
        expanded.getContainer().setHeight(10);
        int h = getOffsetHeight();
        int hh = 0;
        for (int i = 0; i < getItemCount(); i++) {
          h -= getItem(i).header.getHeight();
        }
        int th = h - hh;
        expanded.getContainer().setHeight(th - 1);
      }
    }
  }

  private void renderAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      ExpandItem item = getItem(i);
      renderItem(item, i);
    }
  }

  private void renderItem(ExpandItem item, int index) {
    String o = single ? "auto" : "visible";
    item.content.setStyleAttribute("overflow", o);
    if (headerCollapse) {
      item.getHeader().setStyleAttribute("cursor", "pointer");
    }
    DOM.insertChild(getElement(), item.getElement(), index);
    item.setHeight(headerHeight);
  }

  private void updateStyles() {
    String s = "my-expand-item-noborder";
    for (int i = 0; i < getItemCount(); i++) {
      ExpandItem item = getItem(i);
      boolean add = !item.isExpanded();
      MyDOM.setStyleName(item.getElement(), s, add);
    }
    if (getItemCount() > 0) {
      ExpandItem last = getItem(getItemCount() - 1);
      if (single && expanded != null) {
        MyDOM.setStyleName(last.getElement(), s, !last.isExpanded());
      } else if (single) {
        MyDOM.setStyleName(last.getElement(), s, false);
      } else {
        MyDOM.setStyleName(last.getElement(), s, false);
      }
    }

  }

}
