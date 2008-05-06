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
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.widget.menu.Menu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A selectable item in a <code>ToolBar</code>.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, TOGGLE, MENU, SPLIT_MENU, SEPERATOR, FILL</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : (widget)<br>
 * <div>Fires after the item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>my-toolitem (the tool item)</dd>
 * <dd>my-toolitem-text (the tool item text)</dd>
 * </dl>
 * 
 * <p>
 * Note: Only one of the styles PUSH, TOGGLE, MENU, SPLIT_MENU, and FILL may be
 * specified.
 * </p>
 */
public class ToolItem extends Item {

  ToolBar toolBar;
  Menu menu;
  private Listener menuListener;
  int style;
  
  /**
   * Creates a new PUSH toolitem.
   */
  public ToolItem() {
    this(Style.PUSH);
  }

  /**
   * Creates a new tool item.
   * 
   * @param style the item's style information
   */
  public ToolItem(int style) {
    super("my-toolitem");
    this.style = style;
    setDisabledStyle("my-toolitem-disabled");
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
   * Returns the item's menu.
   * 
   * @return the menu
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Returns the item's parent tool bar.
   * 
   * @return the toolbar
   */
  public ToolBar getToolBar() {
    return toolBar;
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @return the selected state
   */
  public boolean isSelected() {
    return super.isSelected();
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    unhook(Events.EffectStart, listener);
  }

  public void setIconStyle(String style) {
    super.setIconStyle(style);
    if (rendered) {
      MyDOM.setVisible(midleftElem, true);
    }
  }

  /**
   * Sets the item's menu.
   * 
   * <p>
   * Only applicable when item is of type <code>MENU</code> or
   * <code>SPLIT_MENU</code>.
   * </p>
   * 
   * @param menu the menu for the drop down
   */
  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  /**
   * Sets the item's selected state.
   * 
   * @param selected the select state
   */
  public void setSelected(boolean selected) {
    super.setSelected(selected);
  }

  /**
   * Sets the item's style. Has no effect if called after the item has been rendered.
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  /**
   * Sets the item's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    super.setText(text);
    if (rendered) {
      MyDOM.setVisible(centerElem, true);
    }
  }

  protected void onClick(BaseEvent be) {
    super.onClick(be);
    be.stopEvent();
    switch (style) {
      case Style.TOGGLE:
        setSelected(!isSelected(), false);
        break;
      case Style.MENU:
        onMenuClick(be.event);
        break;
      case Style.SPLIT_MENU:
        onSplitMenuClick(be.event);
        break;
      default:
        fireEvent(Events.Select);
        break;
    }
  }

  protected void onHideMenu() {
    setSelected(false);
    menu.removeListener(Events.Close, menuListener);
    menu.hide();
  }

  protected void onMenuClick(Event event) {
    if (menu == null) {
      return;
    }
    if (isSelected()) {
      setSelected(false);
      onHideMenu();
    } else {
      setSelected(true);
      onShowMenu();
    }
  }

  protected void onMouseOut(BaseEvent be) {
    super.onMouseOut(be);
    if (style == Style.SPLIT_MENU) {
      MyDOM.setStyleName(midRightElem, "my-toolitem-split", false);
    }
  }

  protected void onMouseOver(BaseEvent be) {
    super.onMouseOver(be);
    if (style == Style.SPLIT_MENU) {
      MyDOM.setStyleName(midRightElem, "my-toolitem-split", true);
    }
  }

  protected void onRender() {
    super.onRender();

    MyDOM.setVisible(centerElem, false);
    MyDOM.setVisible(midleftElem, false);
    MyDOM.setVisible(midRightElem, false);

    if (getText() != null) {
      MyDOM.setVisible(centerElem, true);
    }

    if (iconStyle != null) {
      MyDOM.setVisible(midleftElem, true);
    }

    switch (style) {
      case Style.SEPARATOR:
        Element div = DOM.createDiv();
        MyDOM.setStyleName(div, "my-toolitem-seperator");
        setElement(div);
        break;
      case Style.MENU:
      case Style.SPLIT_MENU:
        MyDOM.setVisible(midRightElem, true);
        Element d = DOM.createDiv();
        MyDOM.setStyleName(d, "my-toolitem-split");
        DOM.appendChild(midRightElem, d);
        break;
    }

    menuListener = new Listener() {
      public void handleEvent(BaseEvent be) {
        onHideMenu();
      }
    };
  }

  protected void onShowMenu() {
    addStyleName(baseStyle + "-sel");
    final ToolItem fItem = this;
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        menu.show(fItem);
        menu.addListener(Events.Close, menuListener);
      }
    });

  }

  protected void onSplitMenuClick(Event event) {
    Element target = DOM.eventGetTarget(event);
    Element right = DOM.getNextSibling(midRightElem);
    if (DOM.isOrHasChild(midRightElem, target) || DOM.isOrHasChild(right, target)) {
      onMenuClick(event);
    } else {
      fireEvent(Events.Select);
    }
  }

  private void setSelected(boolean selected, boolean supressEvents) {
    super.setSelected(selected);
    if (!supressEvents) {
      fireEvent(Events.Select);
    }
  }

}
