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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Markup;
import net.mygwt.ui.client.widget.menu.Menu;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Displays a list of list items.
 * 
 * <p>
 * Important: This component MUST be sized in pixels to render properly. This
 * can be done directly or by a layout. Percentage based sizes will not work.
 * </p>
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NONE, SINGLE, MULTI, CHECK, FLAT</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
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
 * <li>item : the item that was removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectionChange</b> : (widget, item)<br>
 * <div>Fires after the selection changes.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that is selected (single select)</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : (widget)<br>
 * <div>Fires before the list's context menu is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : (widget, item)<br>
 * <div>Fires after an item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that is selected (single select)</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-list (the list itself)</dd>
 * <dd>.my-listitem (list item)</dd>
 * <dd>.my-listitem .my-listitem-text (list item text)</dd>
 * </dl>
 */
public class List extends ScrollContainer {

  /**
   * maxDepth specifies the max number of parent nodes to search in
   * {@link #findItem(Element)}. Default value is 15.
   */
  protected int maxDepth = 15;
  protected ListItem lastSelected;
  protected ListItem hoverItem;
  boolean check;
  private Element inner;
  private ArrayList selected;
  private boolean singleSelect;
  private boolean multiSelect;
  private int selectionMode;
  private Map nodes = new HashMap();

  /**
   * Creates a new single select list.
   */
  public List() {
    this(Style.SINGLE);
  }

  /**
   * Creates a new list with the given style.
   * 
   * @param style the style information.
   */
  public List(int style) {
    setStyle(style);
    baseStyle = "my-list";
    attachChildren = false;
  }

  /**
   * Adds an item to the list.
   * 
   * @param item the item to add
   */
  public void add(ListItem item) {
    insert(item, getWidgetCount());
  }

  /**
   * Adds an item to the list.
   * 
   * @param text the item's text
   */
  public void add(String text) {
    ListItem item = new ListItem(text);
    add(item);
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param index the index of the item to deselect
   */
  public void deselect(int index) {
    selectItems(index, index, false, true);
  }

  /**
   * Deselects the items at the given indices.
   * 
   * @param start the start index of the items to deselect
   * @param end the start index of the items to deselect
   */
  public void deselect(int start, int end) {
    selectItems(start, end, false, true);
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param item the item to deselect
   */
  public void deselect(ListItem item) {
    deselect(indexOf(item));
  }

  /**
   * Deselects all selected items.
   */
  public void deselectAll() {
    if (getWidgetCount() > 0) {
      selectItems(0, getWidgetCount() - 1, false, false);
    }
  }

  /**
   * Returns the item using the specified target.
   * 
   * @param element the element or child element
   * @return the item
   */
  public ListItem findItem(Element element) {
    if (getItemCount() > 0) {
      String style = getItem(0).baseStyle;
      Element elem = MyDOM.findParent(style, element, maxDepth);
      if (elem != null) {
        return (ListItem) nodes.get(MyDOM.getId(elem));
      }
    }
    return null;
  }

  /**
   * Returns an array of checked items.
   * 
   * @return the checked items
   */
  public ListItem[] getChecked() {
    java.util.List temp = new ArrayList();
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      if (getItem(i).isChecked()) {
        temp.add(getItem(i));
      }
    }
    return (ListItem[]) temp.toArray(new ListItem[0]);
  }

  public Menu getContextMenu() {
    return super.getContextMenu();
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index the index
   * @return the item
   */
  public ListItem getItem(int index) {
    if ((index < 0) || (index >= items.size())) return null;
    return (ListItem) items.get(index);
  }

  /**
   * Returns the number of child items.
   * 
   * @return the item count
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the child items.
   * 
   * @return the child items
   */
  public java.util.List getItems() {
    return items;
  }

  public Element getLayoutTarget() {
    return inner;
  }

  /**
   * Returns the selected item. If the list is multi-select, returns the first
   * selected item.
   * 
   * @return the item or <code>null</code> if no selections
   */
  public ListItem getSelectedItem() {
    if (selected.size() > 0) {
      return getSelection()[0];
    }
    return null;
  }

  /**
   * Returns the selected items.
   * 
   * @return the selected items
   */
  public ListItem[] getSelection() {
    return (ListItem[]) selected.toArray(new ListItem[0]);
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public int getSelectionMode() {
    return selectionMode;
  }

  /**
   * Returns the index of the item or -1 if not found.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(ListItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts an item into the list at the given index.
   * 
   * @param item the item
   * @param index the insert location
   */
  public void insert(ListItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      item.list = this;
      if (check) {
        item.markup = Markup.ITEM_CHECK;
      }
      items.add(index, item);
      if (rendered) {
        renderItem(item, index);
      }
      register(item);
      fireEvent(Events.Add, this, item);
    }
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the select state
   */
  public boolean isSelected(ListItem item) {
    return selected.contains(item);
  }

  /**
   * Moves the current selections down one level.
   */
  public void moveSelectedDown() {
    int count = selected.size();
    if (count == 0) {
      return;
    }
    Collections.sort(selected, new Comparator() {

      public int compare(Object o1, Object o2) {
        ListItem li1 = (ListItem) o1;
        ListItem li2 = (ListItem) o2;
        return indexOf(li1) < indexOf(li2) ? 1 : 0;
      }

    });
    ListItem[] items = new ListItem[count];
    for (int i = 0; i < count; i++) {
      items[i] = (ListItem) selected.get(i);
    }
    for (int j = 0; j < count; j++) {
      int index = indexOf(items[j]);
      if (index != (getItemCount() - 1)) {
        remove(items[j]);
        insert(items[j], ++index);
        selectItems(index, index, true, true, true);
      }
    }
  }

  /**
   * Moves the current selections up one level.
   */
  public void moveSelectedUp() {
    int count = selected.size();
    if (count == 0) {
      return;
    }
    Collections.sort(selected, new Comparator() {

      public int compare(Object o1, Object o2) {
        ListItem li1 = (ListItem) o1;
        ListItem li2 = (ListItem) o2;
        return indexOf(li1) > indexOf(li2) ? 1 : 0;
      }

    });
    ListItem[] items = new ListItem[count];
    for (int i = 0; i < count; i++) {
      items[i] = (ListItem) selected.get(i);
    }
    for (int j = 0; j < count; j++) {
      int index = indexOf(items[j]);
      if (index > 0) {
        remove(items[j]);
        insert(items[j], --index);
        selectItems(index, index, true, true, true);
      }
    }
  }

  public void onBaseEvent(BaseEvent be) {
    ListItem item = findItem(be.getTarget());
    if (item != null) {
      item.onBrowserEvent(be.event);
    }

    if (item != null && be.type == Events.MouseDown && !be.isRightClick()) {
      onItemClick(be, item);
    }

    if (lastSelected != null && be.type == Events.KeyDown) {
      onItemKeyPress(be, lastSelected);
    } else if (item == null && be.type == Events.KeyDown){
      if (getSelectedItem() == null && getItemCount() > 0) {
        select(0);
      }
    }
  }

  /**
   * Removes the item from the list.
   * 
   * @param item the item to be removed
   */
  public void remove(ListItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      if (lastSelected == item) {
        lastSelected = null;
      }
      selected.remove(item);
      item.list = null;

      unregister(item);

      super.remove(item);
      fireEvent(Events.Remove, this, item);
    }
  }

  /**
   * Removes all the items from the list.
   */
  public void removeAll() {
    int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(ListItem item) {
    MyDOM.scrollIntoView(item.getElement(), inner, false);
  }

  /**
   * Selects the item at the index. If the item at the index was already
   * selected, it remains selected.
   * 
   * @param index the index of the item to select
   */
  public void select(int index) {
    selectItems(index, index, true, multiSelect);
  }

  /**
   * Selects the items in the range specified by the given indices. The current
   * selection is not cleared before the new items are selected.
   * 
   * @param start the start of the range
   * @param end the end of the range
   */
  public void select(int start, int end) {
    selectItems(start, end, true, true);
  }

  /**
   * Selects the specified item.
   * 
   * @param item the item to be selected
   */
  public void select(ListItem item) {
    select(indexOf(item));
  }

  /**
   * Selects all of the items in the list. If the list is single-select, do
   * nothing.
   */
  public void selectAll() {
    if (multiSelect) {
      selectItems(0, getWidgetCount() - 1, true, true);
    }
  }

  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Selects the item. The current selection is cleared.
   * 
   * @param item the item to select
   */
  public void setSelection(ListItem item) {
    int index = indexOf(item);
    selectItems(index, index, true, false);
  }

  /**
   * Selects the items. The current selection is cleared.
   * 
   * @param items the items to select
   */
  public void setSelection(ListItem[] items) {
    deselectAll();
    for (int i = 0; i < items.length; i++) {
      int index = indexOf(items[i]);
      selectItems(index, index, true, false);
    }
  }

  /**
   * Sets the selection mode. Valids values are SINGLE and MULTI. Has no effect
   * if called after the list is rendered.
   * 
   * @param mode the selection mode
   */
  public void setSelectionMode(int mode) {
    if (!isRendered()) {
      this.selectionMode = mode;
      singleSelect = mode == Style.SINGLE;
      multiSelect = mode == Style.MULTI;
    }
  }

  /**
   * Sets the list's style. Has no effect if called after the list is rendered.
   * See the class documentation for valid styles.
   * 
   * @param style the list style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style | Style.FOCUSABLE;
      attachChildren = false;
      selected = new ArrayList();

      selectionMode = (style & Style.MULTI) != 0 ? Style.MULTI : Style.SINGLE;
      singleSelect = selectionMode == Style.SINGLE;
      multiSelect = selectionMode == Style.MULTI;

      if ((style & Style.CHECK) != 0) {
        check = true;
      }
    }
  }

  protected void onHideContextMenu() {
    super.onHideContextMenu();
    clearHoverStyles();
  }

  protected void onItemClick(BaseEvent be, ListItem item) {
    be.stopEvent();
    if (check) {
      Element checkElem;
      if (item.checkBtn == null) {
        checkElem = item.getElement();
      } else {
        checkElem = item.checkBtn.getElement();
      }
      if (DOM.isOrHasChild(checkElem, be.getTarget())) {
        item.setChecked(!item.isChecked());
        fireEvent(Events.CheckChange, this, item);
        return;
      }
    }

    int index = indexOf(item);

    if (DOM.eventGetButton(be.event) == Event.BUTTON_RIGHT) {
      if (singleSelect || getSelection().length == 0) {
        selectItems(index, index, true, false);
      } else {
        selectItems(index, index, true, true);
      }
      return;
    }

    if (singleSelect) {
      boolean sel = true;
      if (isSelected(item) && be.isControlKey()) {
        sel = false;
      }
      if (isSelected(item)) {
        return;
      }
      selectItems(index, index, sel, false);
      return;
    }

    if (multiSelect) {
      if (be.isShiftKey()) {
        if (lastSelected != null) {
          selectItems(indexOf(lastSelected), index, true, true);
        } else {
          selectItems(0, index, true, false);
        }
      } else if (be.isControlKey()) {
        selectItems(index, index, !isSelected(item), true);
      } else {
        selectItems(index, index, true, false);
      }
    }
    
    focus();
  }

  protected void onRender() {
    inner = DOM.createDiv();
    MyDOM.setStyleName(inner, baseStyle + "-inner");

    setElement(DOM.createDiv());
    DOM.appendChild(getElement(), inner);

    if ((style & Style.FLAT) != 0) {
      setStyleName(baseStyle + "-flat");
    } else {
      setStyleName(baseStyle);
    }

    DOM.setElementPropertyInt(getElement(), "tabIndex", 0);
    DOM.setElementProperty(getElement(), "hideFocus", "hideFocus");
    
    setScrollEnabled(true);

    disableTextSelection(true);

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
    renderAll();
  }

  protected void onResize(int width, int height) {
    if (height != Style.DEFAULT) {
      height -= MyDOM.getBorderWidth(getElement(), Style.TOP | Style.BOTTOM);
      height -= 2;// inner padding
      MyDOM.setHeight(inner, height, true);
    }
    if (width != Style.DEFAULT) {
      width -= MyDOM.getBorderWidth(getElement(), Style.LEFT | Style.RIGHT);
      width -= 2;// inner padding
      MyDOM.setWidth(inner, width, true);
    }
  }

  protected void onRightClick(BaseEvent be) {
    be.cancelBubble();
    clearHoverStyles();
    ListItem item = findItem(be.getTarget());
    if (item != null) {
      select(item);
    }
    super.onRightClick(be);
  }

  private void clearHoverStyles() {
    if (hoverItem != null) {
      hoverItem.onMouseOut(null);
    }
  }

  private void onItemKeyPress(BaseEvent be, ListItem item) {
    switch (be.getKeyCode()) {
      case KeyboardListener.KEY_UP: {
        int index = indexOf(lastSelected) - 1;
        if (index < 0) return;
        item = getItem(index);
        if (item != null) {
          selectItems(index, index, true, false);
          MyDOM.scrollIntoView(item.getElement(), getElement(), false);
          be.preventDefault();
        }
        break;
      }
      case KeyboardListener.KEY_DOWN: {
        int index = indexOf(lastSelected) + 1;
        if (index > getItemCount()) return;
        item = getItem(index);
        if (item != null) {
          selectItems(index, index, true, false);
          MyDOM.scrollIntoView(item.getElement(), getElement(), false);
          be.preventDefault();
        }
        break;
      }
    }
  }

  private void register(ListItem item) {
    nodes.put(item.getId(), item);
  }

  private void renderAll() {
    int ct = getWidgetCount();
    for (int i = 0; i < ct; i++) {
      // appendChild is faster than insertChild
      DOM.appendChild(inner, getItem(i).getElement());
    }
  }

  private void renderItem(ListItem item, int index) {
    DOM.insertChild(inner, item.getElement(), index);
  }

  private void selectItems(int startIndex, int endIndex, boolean state,
      boolean keepSelected) {
    selectItems(startIndex, endIndex, state, keepSelected, false);
  }

  private void selectItems(int startIndex, int endIndex, boolean state,
      boolean keepSelected, boolean supressEvents) {
    if (startIndex < 0 || endIndex > getItemCount()) {
      return;
    }

    setSelectionStyles(false);

    if (!keepSelected) {
      selected.clear();
    }

    lastSelected = getItem(endIndex);

    int begin = startIndex < endIndex ? startIndex : endIndex;
    int end = startIndex < endIndex ? endIndex : startIndex;

    for (int i = begin; i <= end; i++) {
      ListItem item = getItem(i);
      if (state) {
        lastSelected = item;
        if (!selected.contains(item)) {
          selected.add(item);
        }
        if (i == begin) {
          scrollIntoView(item);
        }
        if (!supressEvents) {
          fireEvent(Events.SelectionChange, this, item);
        }

      } else {
        selected.remove(item);
        if (!supressEvents) {
          fireEvent(Events.SelectionChange);
        }

      }
    }

    if (MyGWT.isSafari) {
      focus();
    }
    setSelectionStyles(true);
  }

  private void setSelectionStyles(boolean select) {
    int count = selected.size();
    for (int i = 0; i < count; i++) {
      ListItem item = (ListItem) selected.get(i);
      item.setSelected(select);
    }
  }

  private void unregister(ListItem item) {
    nodes.remove(item.getId());
  }

}
