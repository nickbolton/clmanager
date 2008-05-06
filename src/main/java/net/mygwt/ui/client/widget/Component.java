/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.EventListener;
import java.util.HashMap;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.event.WidgetListener;
import net.mygwt.ui.client.util.Observable;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.menu.Menu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for MyGWT widgets.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Resize</b> : (widget, width, height)<br>
 * <div>Fires when the components location or size change.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>width : the widget width</li>
 * <li>height : the widget height</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Enable</b> : (widget)<br>
 * <div>Fires after the component is enabled.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Disable</b> : (widget)<br>
 * <div>Fires after the component is disabled.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeDispose</b> : (widget)<br>
 * <div>Fires before the component is disposed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Dispose</b> : (widget)<br>
 * <div>Fires after the component is disposed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeHide</b> : (widget)<br>
 * <div>Fires before the component is hidden.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeShow</b> : (widget)<br>
 * <div>Fires before the component is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : (widget)<br>
 * <div>Fires after the component is hidden.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : (widget)<br>
 * <div>Fires after the component is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : (widget)<br>
 * <div>Fires before the components context menu is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Attach</b> : (widget)<br>
 * <div>Fires after the component is attached.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Detach</b> : (widget) <br>
 * <div>Fires after the component is detached.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Render</b> : (widget)<br>
 * <div>Fires after the component is rendered.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public abstract class Component extends Widget {

  // hack to ensure MyGWT is initialized
  static {
    MyGWT.init();
  }

  /**
   * The base style is typically set as the component's style when rendered. All
   * child styles should be calculated based on the the base style when the
   * component is rendered. This allows the component's style to be swapped by
   * only modifying the basse style. Default value is <code>null</code>.
   */
  protected String baseStyle;

  /**
   * The style used when a component is disabled. Default value is
   * "my-component-disabled".
   */
  protected String disabledStyle = "my-component-disabled";

  protected boolean rendered, disposed;
  protected Menu contextMenu;
  protected int style;
  protected Element focusElem;
  protected Rectangle attachBounds;

  private boolean hidden, disabled;
  private String id, cls, title;
  private Object data;
  private HashMap dataMap;
  private Observable observable;
  private String styles = "";
  private ToolTip toolTip;
  private int disableTextSelection = Style.DEFAULT;
  private int disableContextMenu = Style.DEFAULT;
  // cache size and location calls when widget is not attached
  private String attachHeight, attachWidth;
  private int borders = Style.DEFAULT;
  private String toolTipTitle, toolTipText;

  /**
   * Creates a new component instance.
   */
  public Component() {
    observable = new Observable();
    attachBounds = new Rectangle(-1, -1, -1, -1);
  }

  /**
   * Creates a new component instance with the given style.
   * 
   * @param style the component's style information
   */
  public Component(int style) {
    this();
    this.style = style;
  }

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener) {
    observable.addListener(eventType, listener);
  }

  /**
   * Adds a CSS style name to the component's underlying element.
   * 
   * @param style the CSS style name to add
   */
  public void addStyleName(String style) {
    if (rendered) {
      MyDOM.addStyleName(getStyleElement(), style);
    } else {
      cls = cls == null ? style : cls + " " + style;
    }
  }

  /**
   * Adds a listener to receive widget events.
   * 
   * @param listener the listener to be added
   */
  public void addWidgetListener(WidgetListener listener) {
    TypedListener l = new TypedListener(listener);
    addListener(Events.Attach, l);
    addListener(Events.Detach, l);
    addListener(Events.Resize, l);
  }

  /**
   * Disable this component.
   */
  public void disable() {
    if (rendered) {
      onDisable();
    }
    disabled = true;
    fireEvent(Events.Disable);
  }

  /**
   * Enables and disables text selection for the component.
   * 
   * @param disable <code>true</code> to disable text selection
   */
  public void disableTextSelection(boolean disable) {
    setStyleName(getElement(), "my-no-selection", disable);
    disableTextSelection = disable ? 1 : 0;
    if (isAttached()) {
      MyDOM.disableTextSelection(getElement(), disable);
    }
  }

  /**
   * Disposes this component by purging any event listeners, removing the
   * component's element from the DOM, removing the component from its parent
   * (if applicable). Subclasses should override as needed.
   */
  public void dispose() {
    if (fireEvent(Events.BeforeDispose)) {
      Widget p = getParent();
      if (p != null) {
        if (p instanceof HasWidgets) {
          ((HasWidgets) p).remove(this);
        } else if (p instanceof Container) {
          ((Container) p).remove(this);
        }
      }

      Element el = DOM.getParent(getElement());
      if (el != null) {
        DOM.removeChild(el, getElement());
      }

      if (getElement() != null) {
        clearElemenet();
      }

      disposed = true;
      disabled = true;

      onDestroy();
      fireEvent(Events.Dispose);
      removeAllListeners();
      observable = null;
    }

  }

  /**
   * Enable this component.
   */
  public void enable() {
    if (rendered) {
      onEnable();
    }
    disabled = false;
    fireEvent(Events.Enable);
  }

  /**
   * Enables or disables event processing.
   * 
   * @param enable the enable state
   */
  public void enableEvents(boolean enable) {
    disabled = !enable;
  }

  /**
   * Fires an event.
   * 
   * @param type the event type
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  public boolean fireEvent(int type) {
    BaseEvent be = new BaseEvent();
    be.widget = this;
    return fireEvent(type, be);
  }

  /**
   * Fires an event.
   * 
   * @param type the event type
   * @param be the base event
   * @return <code>false</code> if any listeners return <code>false</code>
   */
  public boolean fireEvent(int type, BaseEvent be) {
    return observable.fireEvent(type, be);
  }

  /**
   * Fires an event.
   * 
   * @param eventType the event type
   * @param widget the source widget
   * @param item the action widget
   * @return <code>true</code> if any listeners cancel the event
   */
  public boolean fireEvent(int eventType, Widget widget, Widget item) {
    BaseEvent be = new BaseEvent();
    be.widget = widget;
    be.item = item;
    return fireEvent(eventType, be);
  }

  /**
   * Fires an event.
   * 
   * @param eventType the event type
   * @param widget the source widget
   * @param item the action widget
   * @param index the index
   * @return <code>true</code> if any listeners cancel the event
   */
  public boolean fireEvent(int eventType, Widget widget, Widget item, int index) {
    BaseEvent be = new BaseEvent();
    be.widget = widget;
    be.item = item;
    be.index = index;
    return fireEvent(eventType, be);
  }

  /**
   * Try to focus this component.
   * 
   * @return this
   */
  public Component focus() {
    if (rendered) {
      if (!MyGWT.isSafari || focusElem == null) {
        MyDOM.setFocus(getElement(), true);
      } else {
        MyDOM.setFocus(focusElem, true);
      }

    }
    return this;
  }

  /**
   * Returns the component's bounds.
   * 
   * @return the component bounds
   */
  public Rectangle getBounds() {
    return MyDOM.getBounds(getElement());
  }

  /**
   * Returns the component's bounds.
   * 
   * @param content <code>true</code> to adjust for box model issues
   * @return the elements bounds
   */
  public Rectangle getBounds(boolean content) {
    return MyDOM.getBounds(getElement(), content);
  }

  /**
   * Returns the application defined data associated with the component, or
   * <code>null</code> if it has not been set.
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the application defined property for the given name, or
   * <code>null</code> if it has not been set.
   * 
   * @param key the name of the property
   * @return the value or <code>null</code> if it has not been set
   */
  public Object getData(String key) {
    if (dataMap == null) return null;
    return dataMap.get(key);
  }

  /**
   * Returns the disabled style.
   * 
   * @return the disabled style
   */
  public String getDisabledStyle() {
    return disabledStyle;
  }

  public Element getElement() {
    if (!rendered) {
      render();
    }
    return super.getElement();
  }

  /**
   * Returns the component's height.
   * 
   * @return the height
   */
  public int getHeight() {
    return MyDOM.getHeight(getElement(), false);
  }

  /**
   * Returns the component's height.
   * 
   * @param content <code>true</code> to get the height minus borders and
   *            padding
   * @return the height
   */
  public int getHeight(boolean content) {
    return MyDOM.getHeight(getElement(), content);
  }

  /**
   * Returns the component's id. If an id has not been set one will be created.
   * 
   * @return the component's id
   */
  public String getId() {
    if (id == null) {
      id = MyDOM.getUniqueID();
      setId(id);
      return id;
    }
    return id;
  }

  /**
   * Returns the component's style information.
   * 
   * @return the style information
   */
  public int getStyle() {
    return style;
  }

  /**
   * Returns the component's tool tip.
   * 
   * @return the tool tip
   */
  public ToolTip getToolTip() {
    if (toolTip == null) {
      toolTip = new ToolTip(this);
    }
    return toolTip;
  }

  /**
   * Returns the component's width.
   * 
   * @return the width
   */
  public int getWidth() {
    return MyDOM.getWidth(getElement(), true);
  }

  /**
   * Returns the component's width.
   * 
   * @param content <code>true</code> to get the width minus borders and
   *            padding
   * @return the component's width
   */
  public int getWidth(boolean content) {
    return MyDOM.getWidth(getElement(), content);
  }

  /**
   * Hide this component.
   */
  public void hide() {
    if (fireEvent(Events.BeforeHide)) {
      hidden = true;
      if (rendered) {
        onHide();
      }
      fireEvent(Events.Hide);
    }
  }

  /**
   * Returns <code>true</code> if the component is disposed.
   * 
   * @return the dispose state
   */
  public boolean isDisposed() {
    return disposed;
  }

  /**
   * Returns <code>true</code> if the component is enabled.
   * 
   * @return the enabled state
   */
  public boolean isEnabled() {
    return !disabled;
  }

  /**
   * Returns <code>true</code> if the component is rendered.
   * 
   * @return the rendered state
   */
  public boolean isRendered() {
    return rendered;
  }

  /**
   * Returns <code>true</code> if the component is visible. A component is
   * visible if it is rendered and visible as defined by
   * {@link MyDOM#isVisible(Element)}.
   */
  public boolean isVisible() {
    return rendered && MyDOM.isVisible(getElement());
  }

  /**
   * Any events a component receives will be forwarded to this method.
   * Subclasses should override as needed. The {@link #onBrowserEvent} method
   * should not be overridden or modified.
   * 
   * @param be the base event
   */
  public void onBaseEvent(BaseEvent be) {

  }

  /**
   * Components delegate event handling to {@link #onBaseEvent}. Sublcasses
   * should not override.
   * 
   * @param event the dom event
   */
  public void onBrowserEvent(Event event) {
    if (disabled) {
      return;
    }

    BaseEvent be = new BaseEvent();
    be.type = DOM.eventGetType(event);
    be.event = event;
    be.widget = this;

    if (be.type == Events.MouseUp && be.isRightClick()) {
      onRightClick(be);
    }

    if (!fireEvent(be.type, be)) {
      return;
    }

    onBaseEvent(be);
  }

  /**
   * Notifies the component that it should recalculate its layout based on its
   * current size if neccessary. Default implementation does nothing.
   */
  public void recalculate() {

  }

  /**
   * Removes all listeners.
   */
  public void removeAllListeners() {
    observable.removeAllListeners();
  }

  public void removeFromParent() {
    if (getParent() instanceof Container) {
      ((Container) getParent()).remove(this);
      return;
    }
    super.removeFromParent();
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener) {
    observable.removeListener(eventType, listener);

  }

  /**
   * Removes a CSS style name from the component's underlying element.
   * 
   * @param style the CSS style name to remove
   */
  public void removeStyleName(String style) {
    if (rendered) {
      MyDOM.setStyleName(getStyleElement(), style, false);
    } else if (style != null && cls != null) {
      String[] s = cls.split(" ");
      cls = "";
      for (int i = 0; i < s.length; i++) {
        if (!s[i].equals(style)) {
          cls += " " + s[i];
        }
      }
    }
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeWidgetListener(WidgetListener listener) {
    if (observable.eventTable == null) return;
    observable.eventTable.unhook(Events.Attach, listener);
    observable.eventTable.unhook(Events.Detach, listener);
    observable.eventTable.unhook(Events.Resize, listener);
  }

  /**
   * Adds or removes a border. The style name 'my-border' is added to the widget
   * to display a border.
   * 
   * @param show the show state
   */
  public void setBorders(boolean show) {
    borders = show ? 1 : 0;
    if (isRendered()) {
      MyDOM.setBorders(getStyleElement(), show);
    }
  }

  /**
   * Sets the component's bounds.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the new width
   * @param height the new height
   */
  public void setBounds(int x, int y, int width, int height) {
    setSize(width, height);
    setPagePosition(x, y);
  }

  /**
   * Sets the component's bounds.
   * 
   * @param rect the bounds
   */
  public void setBounds(Rectangle rect) {
    setBounds(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Sets the application defined component data.
   * 
   * @param data the widget data
   */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * Sets the application defined property with the given name.
   * 
   * @param key the name of the property
   * @param data the new value for the property
   */
  public void setData(String key, Object data) {
    if (dataMap == null) dataMap = new HashMap();
    dataMap.put(key, data);
  }

  /**
   * Sets the disabled style. Default value is 'my-item-disabled'.
   * 
   * @param style the disabled style
   */
  public void setDisabledStyle(String style) {
    this.disabledStyle = style;
  }

  // make public
  public void setElement(Element elem) {
    super.setElement(elem);
  }

  /**
   * Convenience function for setting disabled/enabled by boolean.
   * 
   * @param enabled the enabled state
   */
  public void setEnabled(boolean enabled) {
    if (!enabled) {
      disable();
    } else {
      enable();
    }
  }

  /**
   * Sets the component's height.
   * 
   * @param height the new height
   */
  public void setHeight(int height) {
    setSize(Style.DEFAULT, height);
  }

  /**
   * Sets the object's height. This height does not include decorations such as
   * border, margin, and padding.
   * 
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   */
  public void setHeight(String height) {
    if (rendered) {
      super.setHeight(height);
      onResize(Style.DEFAULT, Style.DEFAULT);
    } else {
      attachHeight = height;
    }
  }

  /**
   * Sets the component's id.
   * 
   * @param id the new id
   */
  public void setId(String id) {
    this.id = id;
    if (rendered) {
      DOM.setElementProperty(getElement(), "id", id);
    }
  }

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute
   * @param value the attribute value
   */
  public void setIntStyleAttribute(String attr, int value) {
    setStyleAttribute(attr, "" + value);
  }

  public void setPixelSize(int width, int height) {
    setSize(width, height);
  }

  /**
   * Sets the width and height of the component. This method fires the resize
   * event.
   * 
   * @param width the new width
   * @param height the new height
   */
  public void setSize(int width, int height) {
    if (width != Style.DEFAULT) {
      attachBounds.width = width;
    }
    if (height != Style.DEFAULT) {
      attachBounds.height = height;
    }
    if (!rendered) {
      return;
    }

    MyDOM.setSize(getElement(), width, height, true);

    if (!isAttached()) {
      return;
    }
    
    onResize(width, height);

    BaseEvent be = new BaseEvent(this);
    be.width = width;
    be.height = height;
    fireEvent(Events.Resize, be);
  }

  /**
   * Sets the component's size. This size does not include decorations such as
   * border, margin, and padding.
   * 
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   */
  public void setSize(String width, String height) {
    setWidth(width);
    setHeight(height);
  }

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute
   * @param value the attribute value
   */
  public void setStyleAttribute(String attr, String value) {
    if (rendered) {
      DOM.setStyleAttribute(getStyleElement(), attr, value);
    } else {
      styles += attr + ":" + value + ";";
    }
  }

  public void setStyleName(String style) {
    if (rendered) {
      super.setStyleName(style);
    } else {
      cls = style;
    }
  }

  public void setTitle(String title) {
    this.title = title;
    if (rendered) {
      super.setTitle(title);
    }
  }

  /**
   * Sets the component's tool tip.
   * 
   * @param text the text
   */
  public void setToolTip(String text) {
    setToolTip(null, text);
  }

  /**
   * Sets the component's tool tip.
   * 
   * @param title the title
   * @param text the text
   */
  public void setToolTip(String title, String text) {
    if (text == null && toolTip == null) {
      return;
    }
    toolTipTitle = title;
    toolTipText = text;
    if (rendered) {
      if (toolTip == null) {
        toolTip = new ToolTip(this);
      }
      toolTip.setText(title, text);
    }

  }

  /**
   * Convenience function to hide or show this component by boolean.
   * 
   * @param visible the visible state
   */
  public void setVisible(boolean visible) {
    if (visible) {
      show();
    } else {
      hide();
    }
  }

  /**
   * Sets the component's width.
   * 
   * @param width the width
   */
  public void setWidth(int width) {
    setSize(width, Style.DEFAULT);
  }

  /**
   * Sets the component's width. This width does not include decorations such as
   * border, margin, and padding.
   * 
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   */
  public void setWidth(String width) {
    if (rendered) {
      super.setWidth(width);
      onResize(Style.DEFAULT, Style.DEFAULT);
    } else {
      attachWidth = width;
    }
  }

  /**
   * Show this component.
   */
  public void show() {
    if (fireEvent(Events.BeforeShow)) {
      hidden = false;
      if (rendered) {
        onShow();
      }
      fireEvent(Events.Show);
    }
  }

  public void unhook(int type, EventListener listener) {
    if (observable.eventTable != null) {
      observable.eventTable.unhook(type, listener);
    }
  }

  /**
   * Called after the component has been rendered.
   */
  protected void afterRender() {
    if (attachBounds != null) {
      setSize(attachBounds.width, attachBounds.height);
    }
  }

  protected Component blur() {
    if (rendered) {
      MyDOM.setFocus(getElement(), false);
    }
    return this;
  }

  /**
   * Enables and disables the component's context menu.
   * 
   * @param disable <code>true</code> to disable the context menu
   */
  protected void disableContextMenu(boolean disable) {
    disableContextMenu = disable ? 1 : 0;
    if (isAttached()) {
      MyDOM.disableContextMenu(getElement(), disable);
    }
  }

  /**
   * Returns the component's context menu. This method is marked protected,
   * subclasses can change access to public to expose the contenxt menu.
   * 
   * @return the context menu
   */
  protected Menu getContextMenu() {
    return contextMenu;
  }

  protected Element getFocusElement() {
    return focusElem == null ? getElement() : focusElem;
  }

  protected void onAttach() {
    if (!isRendered()) {
      render();
    }
    if (disableTextSelection > 0) {
      MyDOM.disableTextSelection(getElement(), disableTextSelection == 1);
    }
    if (disableContextMenu > 0) {
      MyDOM.disableContextMenu(getElement(), disableContextMenu == 1);
    }
    super.onAttach();
  }

  protected void onDestroy() {

  }

  protected void onDetach() {
    super.onDetach();
    if (disableTextSelection > 0) {
      MyDOM.disableTextSelection(getElement(), false);
    }
    if (disableContextMenu > 0) {
      MyDOM.disableContextMenu(getElement(), false);
    }
    fireEvent(Events.Detach);
  }

  protected void onDisable() {
    addStyleName(disabledStyle);
  }

  protected void onEnable() {
    removeStyleName(disabledStyle);
  }

  protected void onHide() {
    super.setVisible(false);
  }

  protected void onHideContextMenu() {
    disabled = false;
  }

  /**
   * Sets the page XY position of the component.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void setPagePosition(int x, int y) {
    if (x != Style.DEFAULT) {
      attachBounds.x = x;
    }
    if (y != Style.DEFAULT) {
      attachBounds.y = y;
    }
    if (!isAttached()) {
      return;
    }

    if (x != Style.DEFAULT) {
      MyDOM.setX(getElement(), x);
    }
    if (y != Style.DEFAULT) {
      MyDOM.setY(getElement(), y);
    }
  }

  protected void onLoad() {
    super.onLoad();

    if (attachHeight != null) {
      setHeight(attachHeight);
      attachHeight = null;
    }

    if (attachWidth != null) {
      setWidth(attachWidth);
      attachWidth = null;
    }

    if (attachBounds != null) {
      setSize(attachBounds.width, attachBounds.height);
      setPagePosition(attachBounds.x, attachBounds.y);
    }
    fireEvent(Events.Attach);
  }

  /**
   * Subclasses should override and ensure setElement is called.
   */
  protected void onRender() {

  }

  /**
   * Called whenever the component is resized. Default implementation calls
   * recalcuate.
   * 
   * @param width the new width
   * @param height the new height
   */
  protected void onResize(int width, int height) {
    recalculate();
  }

  protected void onRightClick(final BaseEvent be) {
    if (contextMenu != null) {
      be.stopEvent();
      disabled = true;
      final int x = be.getClientX();
      final int y = be.getClientY();
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          onShowContextMenu(x, y);
        }
      });
    }
  }

  protected void onShow() {
    super.setVisible(true);
  }

  protected void onShowContextMenu(int x, int y) {
    if (fireEvent(Events.ContextMenu)) {
      contextMenu.addListener(Events.Close, new Listener() {
        public void handleEvent(BaseEvent be) {
          contextMenu.removeListener(Events.Close, this);
          onHideContextMenu();
        }
      });
      contextMenu.show(x + 5, y + 5);
      disabled = true;
    }
  }

  /**
   * Renders the element. This method is not intended to be called directly.
   */
  protected void render() {
    rendered = true;

    onRender();

    if (cls != null) {
      addStyleName(cls);
      cls = null;
    }

    if (title != null) {
      setTitle(title);
    }

    if (id == null) {
      id = MyDOM.getUniqueID();
    }
    setId(id);

    if (styles != null) {
      MyDOM.applyStyles(getElement(), styles);
      styles = null;
    }

    if (toolTipText != null) {
      setToolTip(toolTipTitle, toolTipText);
    }

    if (hidden) {
      hide();
    }

    if (disabled) {
      disable();
    }

    if (borders != Style.DEFAULT) {
      setBorders(borders == 1);
    }

    if (((style & Style.FOCUSABLE) != 0) && MyGWT.isSafari) {
      focusElem = createHiddenInput();
      DOM.appendChild(getElement(), focusElem);
    }

    afterRender();
    fireEvent(Events.Render);
  }

  /**
   * Sets the component's context menu.
   * 
   * @param menu the context menu
   */
  protected void setContextMenu(Menu menu) {
    contextMenu = menu;
    disableContextMenu(true);
    if (isAttached()) {
      MyDOM.disableContextMenu(getElement(), true);
    }
  }

  private native void clearElemenet() /*-{
    this.@com.google.gwt.user.client.ui.UIObject::element = null;
  }-*/;

  private native Element createHiddenInput() /*-{
    var input = $doc.createElement('input');
    input.type = 'text';
    input.style.opacity = 0;
    input.style.zIndex = -1;
    input.style.height = '1px !important';
    input.style.width = '1px !important';
    input.style.overflow = 'hidden !important';
    input.style.position = 'absolute !important';
    input.style.left = '0px !important';
    input.style.top = '0px !important';
    return input;
  }-*/;

}
