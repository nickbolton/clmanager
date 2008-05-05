/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * A multi-select tree selection model.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>SelectionChange</b> : (source this, widget, item, items)<br>
 * <div>Fires after the selection has changed.</div>
 * <ul>
 * <li>source : this</li>
 * <li>widget : the table</li>
 * <li>item : single selection or null</li>
 * <li>items : multi selection or null</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class MultiSelectionModel extends TreeSelectionModel {

  private List selItems;

  /**
   * Creates a new multi select selection model.
   */
  public MultiSelectionModel() {
    selItems = new ArrayList();
  }

  public void deselectAll() {
    if (selItems.size() > 0) {
      while (selItems.size() > 0) {
        TreeItem item = (TreeItem) selItems.remove(0);
        item.getUI().onSelectedChange(false);
      }
      BaseEvent be = new BaseEvent();
      be.widget = tree;
      be.items = Util.createArray(selItems);
      fireEvent(Events.SelectionChange, be);
      tree.fireEvent(Events.SelectionChange, be);
    }
  }

  /**
   * Returns a list of selected items.
   * 
   * @return the selected items
   */
  public TreeItem[] getSelection() {
    return (TreeItem[]) selItems.toArray(new TreeItem[0]);
  }

  public boolean isSelected(TreeItem item) {
    return selItems.contains(item);
  }

  public void select(TreeItem item) {
    select(item, false);
  }

  /**
   * Selects a item.
   * 
   * @param item the item to be selected
   * @param keepSelected <code>true</code> to preserve selections
   */
  public void select(final TreeItem item, boolean keepSelected) {
     if (!keepSelected) {
      deselectAll();
    }
    if (isSelected(item)) {
      lastSelItem = item;
      return;
    }
    selItems.add(item);
    lastSelItem = item;
    selItem = item;
    item.getUI().onSelectedChange(true);
    BaseEvent be = new BaseEvent();
    be.widget = tree;
    be.items = Util.createArray(selItems);
    fireEvent(Events.SelectionChange, be);
    tree.fireEvent(Events.SelectionChange, be);
    if (MyGWT.isSafari) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          tree.focus();
        }
      });
    }
  }

  /**
   * Selects a list of item's.
   * 
   * @param items the items to be selected
   */
  public void selectItems(List items) {
    for (int i = 0; i < items.size(); i++) {
      TreeItem item = (TreeItem) items.get(i);
      select(item);
    }
  }

  /**
   * Deslects a list of items.
   * 
   * @param items the items to be deselected
   */
  public void deselect(List items) {
    for (int i = 0; i < items.size(); i++) {
      TreeItem item = (TreeItem) items.get(i);
      deselect(item);
    }
  }

  public void deselect(TreeItem item) {
    if (selItems.contains(item)) {
      item.getUI().onSelectedChange(false);
      selItems.remove(item);
      BaseEvent be = new BaseEvent();
      be.widget = tree;
      be.items = Util.createArray(selItems);
      fireEvent(Events.SelectionChange, be);
      tree.fireEvent(Events.SelectionChange, be);
    }
  }

  protected void onItemClick(TreeItem item, BaseEvent be) {
    if (be.isRightClick()) {
      return;
    }
    if (!be.within(item.getUI().getJointEl())) {
      if (isSelected(item) && be.isControlKey()) {
        deselect(item);
      } else {
        select(item, be.isControlKey());
      }
    }
  }

}
