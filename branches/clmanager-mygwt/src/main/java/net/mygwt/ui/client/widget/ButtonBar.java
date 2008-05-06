/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * A horizontal row of buttons.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, CENTER, RIGHT</dd>
 * 
 * <dt><b>Events:</b></dt>
 * <dd><b>Click</b> : (widget, item)<br>
 * <div>Fires when a button is clicked.</div>
 * <ul>
 * <li>widget : the button bar</li>
 * <li>item : the button that was clicked</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
 * <div>Fires before a widget is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the widget being added</li>
 * <li>index : the index at which the widget will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : (widget, item)<br>
 * <div>Fires before a widget is removed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the widget being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after a widget has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the widget that was added</li>
 * <li>index : the index at which the item was added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after a widget has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the widget that was removed</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-btn-bar (the button bar itself)</dd>
 * </dl>
 */
public class ButtonBar extends Container {

  private int buttonWidth = 75;
  private Button buttonPressed;
  private HorizontalPanel wrap, panel;
  private int alignment = Style.LEFT;
  private Listener listener = new Listener() {
    public void handleEvent(BaseEvent be) {
      onButtonPressed(be);
    }
  };

  /**
   * Creates a left aligned button bar.
   */
  public ButtonBar() {
    this(Style.LEFT);
  }

  /**
   * Creates a new button bar.
   * 
   * @param style the style
   */
  public ButtonBar(int style) {
    this.style = style;
    this.alignment = style;
    baseStyle = "my-btn-bar";
  }

  /**
   * Adds a button to the bar.
   * 
   * @param button the button to be added
   */
  public void add(Button button) {
    insert(button, getButtonCount());
  }

  /**
   * Returns the button alignment.
   * 
   * @return the alignment
   */
  public int getAlignment() {
    return alignment;
  }

  /**
   * Returns the button at the specified index.
   * 
   * @param index the index
   * @return the button or <code>null</code>
   */
  public Button getButton(int index) {
    return (Button) items.get(index);
  }

  /**
   * Returns the button with the specified button id.
   * 
   * @param buttonId the button id
   * @return the button or <code>null</code> if no match
   */
  public Button getButtonById(int buttonId) {
    int count = getButtonCount();
    for (int i = 0; i < count; i++) {
      Button btn = getButton(i);
      if (btn.getButtonId() == buttonId) {
        return btn;
      }
    }
    return null;
  }

  /**
   * Returns the button count.
   * 
   * @return the button count
   */
  public int getButtonCount() {
    return items.size();
  }

  /**
   * Returns the last button that was selected.
   * 
   * @return the last button or <codee>null</code>
   */
  public Button getButtonPressed() {
    return buttonPressed;
  }

  /**
   * Returns the default button width.
   * 
   * @return the button width
   */
  public int getButtonWidth() {
    return buttonWidth;
  }

  /**
   * Inserts a button at the specified location.
   * 
   * @param button the button to be inserted
   * @param index the insert location
   */
  public void insert(Button button, int index) {
    if (fireEvent(Events.BeforeAdd, this, button, index)) {
      items.add(index, button);
      button.addListener(Events.Click, listener);
      if (rendered) {
        renderButton(button, index);
      }
      fireEvent(Events.Add, this, button, index);
    }
  }

  /**
   * Removes a button from the bar.
   * 
   * @param button the button to be removed
   */
  public void remove(Button button) {
    if (fireEvent(Events.BeforeRemove, this, button)) {
      button.removeListener(Events.Click, listener);
      items.remove(button);
      if (rendered) {
        panel.remove(button);
      }
      fireEvent(Events.Remove, this, button);
    }
  }

  /**
   * Removes all the buttons.
   */
  public void removeAll() {
    while (getButtonCount() > 0) {
      remove(getButton(0));
    }
  }

  /**
   * Sets the button alignment. Valid values are LEFT, CENTER, and RIGHT.
   * 
   * @param align the alignment
   */
  public void setAlignment(int align) {
    this.alignment = align;
    if (isRendered()) {
      HorizontalAlignmentConstant dir = HorizontalPanel.ALIGN_LEFT;
      switch (align) {
        case Style.CENTER:
          dir = HorizontalPanel.ALIGN_CENTER;
          break;
        case Style.RIGHT:
          dir = HorizontalPanel.ALIGN_RIGHT;
      }
      wrap.setCellHorizontalAlignment(panel, dir);
      wrap.setCellVerticalAlignment(panel, HorizontalPanel.ALIGN_MIDDLE);
    }
  }

  /**
   * Sets the default button width. Default value is 75.
   * 
   * @param width the button width
   */
  public void setButtonWidth(int width) {
    buttonWidth = width;
  }

  protected void onButtonPressed(BaseEvent be) {
    Button btn = (Button) be.widget;
    buttonPressed = btn;
    fireEvent(Events.Click, this, btn);
  }

  protected void onDisable() {
    super.onDisable();
    for (int i = 0; i < getButtonCount(); i++) {
      getButton(i).disable();
    }
  }

  protected void onEnable() {
    super.onEnable();
    for (int i = 0; i < getButtonCount(); i++) {
      getButton(i).enable();
    }
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName(baseStyle);

    int height = MyGWT.isIE ? 32 : 28;
    setHeight(height);

    wrap = new HorizontalPanel();
    wrap.setWidth("100%");
    wrap.setHeight("100%");
    DOM.appendChild(getElement(), wrap.getElement());

    panel = new HorizontalPanel();
    panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

    wrap.add(panel);
    wrap.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

    int count = getButtonCount();
    for (int i = 0; i < count; i++) {
      Button button = getButton(i);
      renderButton(button, i);
    }

    setAlignment(alignment);
  }

  protected void renderButton(Button button, int index) {
    panel.insert(button, index);
    button.setWidth(buttonWidth);
    Element td = DOM.getParent(button.getElement());
    String p = "0 3 0 3px";
    DOM.setStyleAttribute(td, "padding", p);
  }

}
