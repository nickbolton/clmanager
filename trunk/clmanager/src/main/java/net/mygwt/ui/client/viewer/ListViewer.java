/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.Iterator;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.List;
import net.mygwt.ui.client.widget.ListItem;

import com.google.gwt.user.client.ui.Widget;

/**
 * A concrete viewer based for a <code>List</code>.
 * 
 * @see List
 */
public class ListViewer extends Viewer implements ICheckable {

  private List list;
  private ArrayList checkChangeListener;

  /**
   * Creates a new list intance.
   * 
   * @param list the list
   */
  public ListViewer(List list) {
    this.list = list;
    list.getElement();
    hookWidget(list);
  }

  public void add(Object elem) {
    renderItem(elem, list.getWidgetCount());
  }

  public void addCheckStateListener(ICheckStateListener listener) {
    if (checkChangeListener == null) {
      checkChangeListener = new ArrayList();
    }
    if (!checkChangeListener.contains(listener)) {
      checkChangeListener.add(listener);
    }
  }

  public Widget findItem(Object elem) {
    int size = list.getWidgetCount();
    for (int i = 0; i < size; i++) {
      ListItem item = list.getItem(i);
      Object data = item.getData();
      if (getComparer() != null) {
        if (getComparer().equals(elem, data)) {
          return item;
        } else {
          continue;
        }
      }
      if (elem == data || (elem != null && elem.equals(data))) {
        return item;
      }
    }
    return null;
  }

  public Object[] getActiveElements() {
    ArrayList temp = new ArrayList();
    int count = list.getItemCount();
    for (int i = 0; i < count; i++) {
      ListItem item = list.getItem(i);
      if (item.isVisible()) {
        temp.add(item.getData());
      }
    }
    return temp.toArray();
  }

  public boolean getChecked(Object element) {
    ListItem item = (ListItem) findItem(element);
    if (item != null) {
      return item.isChecked();
    }
    return false;
  }

  /**
   * Returns the current checked selection.
   * 
   * @return the checked elements
   */
  public ISelection getCheckedSelection() {
    ListItem[] items = list.getChecked();
    if (items.length == 0) {
      return DefaultSelection.EMPTY;
    }
    ArrayList checked = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      checked.add(items[i].getData());
    }
    return new DefaultSelection(checked);
  }

  /**
   * Returns the viewer's list widget.
   * 
   * @return the list
   */
  public List getList() {
    return list;
  }

  public Widget getWidget() {
    return list;
  }

  public void insert(Object elem, int index) {
    renderItem(elem, index);
  }

  public void remove(Object elem) {
    ListItem item = (ListItem) findItem(elem);
    if (item != null) {
      removeElement(elem);
      item.setData(null);
    }
  }

  public void removeCheckStateListener(ICheckStateListener listener) {
    if (checkChangeListener != null) {
      checkChangeListener.remove(listener);
    }
  }

  public boolean setChecked(Object element, boolean state) {
    ListItem item = (ListItem) findItem(element);
    if (item != null) {
      item.setChecked(state);
      return true;
    }
    return false;
  }

  public void setSelection(ISelection selection, boolean reveal) {
    java.util.List selected = selection.toList();
    int ct = list.getWidgetCount();
    for (int i = 0; i < ct; i++) {
      ListItem item = list.getItem(i);
      Object elem = item.getData();
      if (selected.contains(elem)) {
        list.select(i);
      } else {
        list.deselect(i);
      }
    }
  }

  public void update() {
    int ct = list.getWidgetCount();
    for (int i = 0; i < ct; i++) {
      updateInternal(list.getItem(i));
    }
  }

  public void update(Object elem) {
    ListItem item = (ListItem) findItem(elem);
    if (item != null) {
      item.setData(elem);
      updateInternal(item);
    }

  }

  protected java.util.List getSelectedFromWidget() {
    ArrayList elems = new ArrayList();
    for (int i = 0; i < list.getSelection().length; i++) {
      ListItem item = (ListItem) list.getSelection()[i];
      elems.add(item.getData());
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    widget.addListener(Events.CheckChange, new Listener() {
      public void handleEvent(BaseEvent be) {
        fireCheckStateChanged(be);
      }
    });
  }

  protected void onInputReceived(Object input) {
    renderList();
  }

  protected void renderItem(Object elem, int position) {
    LabelProvider lp = (LabelProvider) getLabelProvider();
    ListItem item = new ListItem();
    item.setData(elem);
    item.setText(lp.getText(elem));
    String style = lp.getIconStyle(elem);
    if (style != null) {
      item.setIconStyle(lp.getIconStyle(elem));
    }
    list.insert(item, position);
  }

  protected void renderList() {
    list.removeAll();
    Object[] elems = filter(elements);
    elems = sortElements(elems);
    for (int i = 0; i < elems.length; i++) {
      Object element = elems[i];
      renderItem(element, i);
    }
  }

  private void fireCheckStateChanged(BaseEvent be) {
    if (checkChangeListener != null) {
      ListItem item = (ListItem) be.item;
      CheckStateChangedEvent evt = new CheckStateChangedEvent(this, item.getData(),
          item.isChecked());
      Iterator it = checkChangeListener.iterator();
      while (it.hasNext()) {
        ((ICheckStateListener) it.next()).checkStateChanged(evt);
      }
    }
  }

  private void updateInternal(ListItem item) {
    LabelProvider lp = (LabelProvider) getLabelProvider();
    Object elem = item.getData();
    item.setText(lp.getText(elem));
    String style = lp.getIconStyle(elem);
    if (style != null) {
      item.setIconStyle(lp.getIconStyle(elem));
    }
  }

}
