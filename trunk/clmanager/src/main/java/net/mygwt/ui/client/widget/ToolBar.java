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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A standard tool bar.
 * 
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
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>my-toolbar (the tool bar)</dd>
 * </dl>
 * 
 * @see ToolItem
 */
public class ToolBar extends Container {

  private HorizontalPanel panel;

  /**
   * Creates a new tool bar.
   */
  public ToolBar() {
    attachChildren = false;
    baseStyle = "my-toolbar";
  }
  
  /**
   * Adds a item to the tool bar.
   * 
   * @param item the item to add
   */
  public void add(ToolItem item) {
    insert(item, getItemCount());
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index the item index
   * @return the item at the index
   */
  public ToolItem getItem(int index) {
    if ((index < 0) || (index >= items.size())) return null;
    return (ToolItem) items.get(index);
  }

  /**
   * Returns the number of tool item's.
   * 
   * @return the item count
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the index of the item.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(ToolItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts a item into the tool bar.
   * 
   * @param item the item to add
   * @param index the insert location
   */
  public void insert(ToolItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      items.add(index, item);
      if (rendered) {
        renderItem(item, index);
      }
      fireEvent(Events.Add, this, item, index);
    }
  }

  /**
   * Removes a tool item.
   * 
   * @param item the item to be removed
   */
  public void remove(ToolItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      items.remove(item);
      if (rendered) {
        panel.remove(item);
      }
      fireEvent(Events.Remove, this, item);
    }
  }
  
  /**
   * Removes all tool items.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      ToolItem item = getItem(0);
      remove(item);
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(panel);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(panel);
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName(baseStyle);

    panel = new HorizontalPanel();
    panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    panel.setSpacing(2);

    DOM.appendChild(getElement(), panel.getElement());

    renderAll();
  }

  private void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      ToolItem item = getItem(i);
      renderItem(item, i);
    }
  }

  private void renderItem(ToolItem item, int index) {
    panel.insert(item, index);
    if (item.style == Style.FILL) {
      MyDOM.setVisibility(item.getElement(), false);
      panel.setCellWidth(item, "100%");
    }
  }

}
