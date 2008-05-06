/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.util.Markup;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A standard push button.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Select</b> : (widget)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-btn (the button itself)</dd>
 * <dd>.my-btn .my-btn-text (the button text)</dd>
 * </dl>
 */
public class Button extends Item {

  private int buttonId;
  private String name;
  private int tabIndex = Style.DEFAULT;

  /**
   * Creates a new button.
   */
  public Button() {
    baseStyle = "my-btn";
  }

  /**
   * Creates a new button with the given text.
   * 
   * @param text the button's text
   */
  public Button(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new button with the given text and specified event listener.
   * 
   * @param text the button's text
   * @param listener the selection listener
   */
  public Button(String text, SelectionListener listener) {
    this(text);
    addSelectionListener(listener);
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
   * Returns the button's id. The button id differs from the component id as it
   * does not have to be unique and is typically used when grouping sets of buttons.
   * 
   * @return the button id
   */
  public int getButtonId() {
    return buttonId;
  }

  /**
   * Returns button's name.
   * 
   * @return the button name
   */
  public String getName() {
    return name;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    unhook(Events.Select, listener);
  }

  /**
   * Sets the button's id.
   * 
   * @param id the button id
   */
  public void setButtonId(int id) {
    this.buttonId = id;
  }

  public void setIconStyle(String style) {
    addStyleName("my-btn-icon");
    super.setIconStyle(style);
  }

  /**
   * Sets the button's name.
   * 
   * @param name the button name
   */
  public void setName(String name) {
    this.name = name;
    if (rendered) {
      DOM.setElementProperty(getElement(), "name", name);
    }
  }

  /**
   * Sets the button's tab index.
   * 
   * @param index the tab index
   */
  public void setTabIndex(int index) {
    this.tabIndex = index;
    if (isRendered()) {
      DOM.setElementPropertyInt(textElem, "tabIndex", index);
    }
  }

  protected Element getFocusElement() {
    return textElem;
  }

  protected String getTemplate() {
    return Markup.BUTTON;
  }

  protected void onClick(BaseEvent be) {
    super.onClick(be);
    fireEvent(Events.Select);
  }

  protected void onDisable() {
    super.onDisable();
    DOM.setElementProperty(textElem, "disabled", "true");
  }

  protected void onEnable() {
    super.onEnable();
    DOM.setElementProperty(textElem, "disabled", "");
  }
  
  protected void onMouseDown(BaseEvent be) {
    super.onMouseDown(be);
    MyDOM.setFocus(textElem, true);
  }

  protected void onRender() {
    super.onRender();
    setDisabledStyle(baseStyle + "-disabled");
    if (name != null) {
      setName(name);
    }
    if (tabIndex != Style.DEFAULT) {
      setTabIndex(tabIndex);
    }
  }

}
