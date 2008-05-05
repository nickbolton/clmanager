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
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.TypedListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A item contained in a <code>List</code>.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : (widget this)<br>
 * <div>Fired when an item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-listitem (list item)</dd>
 * <dd>.my-listitem .my-listitem-text (list item text)</dd>
 * </dl>
 * 
 * @see List
 */
public class ListItem extends Item {

  List list;
  IconButton checkBtn;
  private boolean checked;

  /**
   * Creates a new list item.
   */
  public ListItem() {
    baseStyle = "my-listitem";
    sinkEvents = false;
  }

  /**
   * Creates a new list item.
   * 
   * @param text the text
   */
  public ListItem(String text) {
    this();
    setText(text);
  }

  protected void onMouseOver(BaseEvent be) {
    super.onMouseOver(be);
    list.hoverItem = this;
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
   * Returns the item's parent list.
   * 
   * @return the list
   */
  public List getList() {
    return list;
  }

  /**
   * Returns <code>true</code> if the item is checked.
   * 
   * @return the checked state
   */
  public boolean isChecked() {
    return checked;
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
   * Sets the item's checked state.
   * 
   * @param checked the check state
   */
  public void setChecked(boolean checked) {
    this.checked = checked;
    if (rendered) {
      String s = checked ? "icon-checked" : "icon-notchecked";
      checkBtn.changeStyle(s);
    }
  }

  protected void onClick(BaseEvent be) {
    fireEvent(Events.Select);
  }

  protected void onRender() {
    super.onRender();

    if (list.check) {
      checkBtn = new IconButton("icon-notchecked");
      checkBtn.setStyleAttribute("marginRight", "4px");
      Element elem = MyDOM.findChild("my-listitem-check", getElement());
      DOM.appendChild(elem, checkBtn.getElement());

      if (checked) {
        setChecked(checked);
      }
    }

  }

}
