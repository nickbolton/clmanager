/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.menu;

import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.widget.Container;
import net.mygwt.ui.client.widget.Popup;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A menu.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeOpen</b> : (widget this)<br>
 * <div>Fired before the menu is opened. Listener can set the doit field to
 * <code>false</code> to cancel the menu being displayed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Open</b> : (widget this)<br>
 * <div>Fired after a the menu is opened</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : (widget this)<br>
 * <div>Fired before the menu is closed. Listener can set the doit field to
 * <code>false</code> to cancel the menu being hidden.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget this)<br>
 * <div>Fired after the menu is closed</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Menu extends Container {

  protected boolean hideOnClick = true;

  boolean showing, closeOnSelect;
  MenuItem parentItem;

  private int minWidth = 140;
  private String subMenuAlign = "tl-tr?";
  private MenuItem activeItem;
  private VerticalPanel panel;
  private Popup popup;
  private boolean shadow = true;

  /**
   * Creates a new menu.
   */
  public Menu() {
    attachChildren = false;
  }

  /**
   * Adds a item to the menu.
   * 
   * @param item the new item
   */
  public void add(MenuItem item) {
    insert(item, getItemCount());
  }

  /**
   * Finds a item.
   * 
   * @param target the target element
   * @return the item
   */
  public MenuItem findItem(Element target) {
    for (int i = 0; i < items.size(); i++) {
      MenuItem item = getItem(i);
      if (DOM.isOrHasChild(item.getElement(), target)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the item at the given index or <code>null</code> if the index is
   * out of range.
   * 
   * @param index the index of the item to return
   * @return the item at the given index
   */
  public MenuItem getItem(int index) {
    return (MenuItem) items.get(index);
  }

  /**
   * Returns the number of items contained in the menu.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the menu's child items.
   * 
   * @return the child items
   */
  public List getItems() {
    return items;
  }

  /**
   * Returns the minimum width.
   * 
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns the menu's parent item, or <code>null</code> if the menu is a
   * root.
   * 
   * @return the parent item
   */
  public MenuItem getParentItem() {
    return parentItem;
  }

  /**
   * Returns true if the shadow is enabled.
   * @return the shadow state
   */
  public boolean getShadow() {
    return shadow;
  }

  /**
   * Returns the sub menu alignment.
   * 
   * @return the alignment
   */
  public String getSubMenuAlign() {
    return subMenuAlign;
  }

  public void hide() {
    if (fireEvent(Events.BeforeClose)) {
      popup.hide();
      showing = false;
      fireEvent(Events.Close);
    }
  }

  /**
   * Returns the index of the item, or -1 if not found.
   * 
   * @param item the item to search for
   * @return the index or -1 if not found
   */
  public int indexOf(MenuItem item) {
    return items.indexOf(item);
  }

  public void insert(MenuItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      item.parentMenu = this;
      items.add(index, item);
      if (rendered) {
        renderItem(item, index);
      }
      fireEvent(Events.Add, this, item, index);
    }
  }

  public void onBrowserEvent(Event event) {
    int type = DOM.eventGetType(event);
    switch (type) {
      case Event.ONMOUSEOVER:
        onMouseOver(event);
        break;
    }

  }

  public void remove(MenuItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      items.remove(item);
      if (rendered) {
        panel.remove(item);
      }
      fireEvent(Events.Remove, this, item);
    }
  }

  /**
   * Removes all menu items from this menu.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Sets minimum width of the menu in pixels. Default value is 120.
   * 
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * True to show a shadow when the menu is displayed.
   * 
   * @param shadow the show shadow state
   */
  public void setShadow(boolean shadow) {
    this.shadow = shadow;
  }

  /**
   * Sets the {@link MyDOM#alignTo} anchor position value to use for submenus of
   * this menu (defaults to "tl-tr?").
   * 
   * @param subMenuAlign the alignment
   */
  public void setSubMenuAlign(String subMenuAlign) {
    this.subMenuAlign = subMenuAlign;
  }

  public void show() {
    if (fireEvent(Events.BeforeOpen)) {
      createPopup();
      popup.show();
      showing = true;
      fireEvent(Events.Open);
    }
  }

  /**
   * Shows the menu.
   * 
   * @param elem the align element
   * @param pos the position
   */
  public void show(Element elem, String pos) {
    if (fireEvent(Events.BeforeOpen)) {
      createPopup();
      popup.show(elem, pos, new int[] {-2, 0});
      showing = true;
      fireEvent(Events.Open);
    }
  }

  /**
   * Shows the menu at the specified location.
   * 
   * @param x the x coordinate value
   * @param y the y corrdinate value
   */
  public void show(int x, int y) {
    if (fireEvent(Events.BeforeOpen)) {
      createPopup();
      popup.show(x, y);
      showing = true;
      fireEvent(Events.Open);
    }
  }

  /**
   * Shows the menu.
   * 
   * @param widget the widget to align to
   */
  public void show(Widget widget) {
    if (fireEvent(Events.BeforeOpen)) {
      createPopup();
      popup.show(widget);
      showing = true;
      fireEvent(Events.Open);
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(panel);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(panel);
  }

  protected void onMouseOut(Event event) {
    MenuItem item = findItem(DOM.eventGetTarget(event));
    if (item != null) {
      if (item == activeItem && item.shouldDeactivate(event)) {
        activeItem.deactivate();
      }
    }
  }

  protected void onMouseOver(Event event) {
    MenuItem item = findItem(DOM.eventGetTarget(event));
    if (item == null) return;
    if (item.canActivate && !item.enabled) {
      setActiveItem(item, true);
    }
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-menu");
    panel = new VerticalPanel();
    DOM.appendChild(getElement(), panel.getElement());

    setBorders(true);

    panel.setWidth(minWidth + "px");

    renderAll();

    disableContextMenu(true);
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  void closeAllMenus() {
    Menu parent = this;
    while (parent != null) {
      parent.hide();
      if (parent.parentItem == null) {
        parent = null;
      } else {
        parent = parent.parentItem.parentMenu;
      }
    }
  }

  private void createPopup() {

    if (popup == null) {
      final Menu fMenu = this;
      popup = new Popup(true) {
        protected void afterHide() {
          super.afterHide();
          fMenu.fireEvent(Events.Close);
        }

        protected boolean onAutoHide(Event event) {
          if (parentItem != null) {
            Element target = DOM.eventGetTarget(event);
            if (DOM.isOrHasChild(parentItem.getElement(), target)) {
              return false;
            }
          }
          closeAllMenus();
          return true;
        }

      };
      popup.setShadow(shadow);
      popup.setAnimate(false);
      popup.setWidget(this);
    }
  }

  private void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      MenuItem item = getItem(i);
      renderItem(item, i);
    }
  }

  private void renderItem(MenuItem item, int index) {
    panel.insert(item, index);
  }

  private void setActiveItem(MenuItem item, boolean autoExpand) {
    if (item != activeItem) {
      if (activeItem != null) {
        activeItem.deactivate();
      }
      activeItem = item;
      item.activate(autoExpand);
    } else if (autoExpand) {
      item.expandMenu();
    }

  }

}
