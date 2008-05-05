/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.menu;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.widget.Item;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;

/**
 * A selectable item in a <code>MenuBar</code>.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, MENU, SEPARATOR</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : (widget this)<br>
 * <div>Fired when a item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : (widget)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeOpen</b> : (widget)<br>
 * <div>Fires before the sub menu is displayed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Open</b> : (widget)<br>
 * <div>Fires after the sub menu is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-menuitem (the item itself)</dd>
 * <dd>.my-menuitem-text span (the menu item text)</dd>
 * </dl>
 */
public class MenuItem extends Item {

  boolean canActivate = true;
  boolean enabled;
  Menu parentMenu;
  Menu subMenu;

  private boolean hideOnClick = true;
  private String group = "";
  private boolean selected;

  /**
   * Creates a new PUSH menu item.
   */
  public MenuItem() {
    this(Style.PUSH);
  }

  /**
   * Creates a new menu item.
   */
  public MenuItem(int style) {
    super(style, "my-menuitem");
  }

  /**
   * Adds a listener interface to receive selection events.
   * 
   * @param listener the listener to add
   */
  public void addSelectionListener(SelectionListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.Select, tl);
  }

  /**
   * Returns the item's group value.
   * 
   * @return the group
   */
  public String getGroup() {
    return group;
  }

  /**
   * Returns <code>true</code> if the menu will be closed on a click.
   * 
   * @return the hide on click state
   */
  public boolean getHideOnClick() {
    return hideOnClick;
  }

  /**
   * Returns the item's sub menu.
   * 
   * @return the sub menu
   */
  public Menu getSubMenu() {
    return subMenu;
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * <p>
   * When the item is of type CHECK or RADIO, it is selected when it is checked.
   * </p>
   * 
   * @return the selected state
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    unhook(Events.EffectStart, listener);
  }

  /**
   * Allows radio items to be grouped together.
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Specifies if the menu should be hidden when the item is selected. Default
   * value is <code>true</code>.
   * 
   * @param hideOnClick <code>true</code> to enable
   */
  public void setHideOnClick(boolean hideOnClick) {
    this.hideOnClick = hideOnClick;
  }

  /**
   * Sets the item's selected state.
   * <p>
   * When the item is of type CHECK or RADIO, it is selected when it is checked.
   * </p>
   * 
   * @param selected the select state
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
    if (isRendered()) {
      setSelected(selected, true);
    }
  }

  /**
   * Sets the item's style. Has no effect if called after the item has been
   * rendered.
   * 
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  /**
   * Sets the item's sub menu.
   * 
   * @param menu the sub menu or <code>null</code> to clear the sub menu
   */
  public void setSubMenu(Menu menu) {
    if (menu == null) {
      this.subMenu = null;
      removeStyleName("my-menuitem-submenu");
    } else {
      this.subMenu = menu;
      menu.parentItem = this;
      addStyleName("my-menuitem-submenu");
    }
  }

  protected void onClick(BaseEvent be) {
    be.stopEvent();
    switch (style) {
      case Style.MENU:
        if (subMenu != null && subMenu.isVisible()) {
          return;
        }
        break;
      case Style.CHECK:
      case Style.RADIO:
      case Style.PUSH:
        setSelected(!isSelected());
        break;
    }
    if (hideOnClick) {
      onMouseOut(be);
      parentMenu.closeAllMenus();
    }
  }

  protected void onRender() {
    super.onRender();

    DOM.appendChild(midRightElem, DOM.createDiv());

    switch (style) {
      case Style.SEPARATOR:
        setElement(DOM.createDiv());
        setStyleName("my-menu-separator");
        DOM.appendChild(getElement(), DOM.createDiv());
        hideOnClick = false;
        overStyleEnabled = false;
        break;
      case Style.CHECK:
        String s = isSelected() ? "icon-checked" : "icon-notchecked";
        setIconStyle(s);
        break;
    }

    if (style != Style.SEPARATOR && iconStyle == null) {
      setIconStyle("blank");
    }

    if (selected) {
      setSelected(true, false);
    }
  }

  void activate(boolean autoOpen) {
    if (autoOpen) {
      expandMenu();
    }
  }

  void deactivate() {
    hideMenu();
  }

  void expandMenu() {
    if (subMenu != null) {
      if (!subMenu.showing && fireEvent(Events.BeforeOpen)) {
        subMenu.show(getElement(), "tl-tr-?");
        fireEvent(Events.Open);
      }
    }
  }

  void hideMenu() {
    if (subMenu != null && subMenu.isVisible()) {
      subMenu.hide();
    }
  }

  boolean shouldDeactivate(Event event) {
    if (subMenu != null && subMenu.isVisible()) {
      return DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event));
    }
    return false;
  }

  private void setSelected(boolean selected, boolean fireEvent) {
    super.setSelected(selected);
    switch (style) {
      case Style.CHECK: {
        String s = selected ? "icon-checked" : "icon-notchecked";
        setIconStyle(s);
        if (fireEvent) {
          fireEvent(Events.CheckChange);
        }
        break;
      }
      case Style.RADIO: {
        if (parentMenu == null) {
          setIconStyle("icon-group-sel");
          break;
        }
        int size = parentMenu.getItemCount();
        for (int i = 0; i < size; i++) {
          MenuItem item = parentMenu.getItem(i);
          if (item.style == Style.RADIO) {
            if (!item.group.equals(group)) {
              continue;
            }
            if (item == this) {
              item.setIconStyle("icon-group-sel");
            } else {
              item.setIconStyle("my-none");
              item.selected = false;
            }

          }
        }
        if (fireEvent) {
          fireEvent(Events.CheckChange);
        }
        break;
      }
    }
    if (fireEvent) {
      // let the event processing finish before firing event
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          fireEvent(Events.Select);
        }
      });
    }

  }
}
