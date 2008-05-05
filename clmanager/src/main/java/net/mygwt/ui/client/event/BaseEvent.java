/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.event;

import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * Instances of this class provide a description of a particular MyGWT event.
 * 
 * <p>
 * Note: For a given event, only the fields which are appropriate will be filled
 * in. The appropriate fields for each event are documented by the event source.
 * </p>
 */
public class BaseEvent {

  /**
   * The event type.
   */
  public int type;

  /**
   * The source object.
   */
  public Object source;

  /**
   * Depending on the event, a flag indicating whether the operation should be
   * allowed.
   */
  public boolean doit = true;

  /**
   * The widget that issued the event.
   */
  public Widget widget;

  /**
   * The item that the event occurred in.
   */
  public Widget item;

  /**
   * List of widgets.
   */
  public Component[] items;

  /**
   * The dom event.
   */
  public Event event;

  /**
   * The index.
   */
  public int index;

  /**
   * The row index.
   */
  public int rowIndex;

  /**
   * The width.
   */
  public int width;

  /**
   * The height.
   */
  public int height;

  /**
   * X coordinate
   */
  public int x;

  /**
   * Y coordinate
   */
  public int y;

  /**
   * The size.
   */
  public int size;

  /**
   * The name.
   */
  public String name;

  /**
   * The value.
   */
  public Object value;

  /**
   * The old value.
   */
  public Object oldValue;

  /**
   * Creates a new base event.
   */
  public BaseEvent() {

  }

  /**
   * Creates a new base event.
   * 
   * @param widget the source widget
   */
  public BaseEvent(Widget widget) {
    this.widget = widget;
  }

  /**
   * Creates a new base event.
   * 
   * @param widget the source widget
   * @param item the item widget
   */
  public BaseEvent(Widget widget, Widget item) {
    this.widget = widget;
    this.item = item;
  }

  /**
   * Cancels bubbling for the given event. This will stop the event from being
   * propagated to parent elements.
   */
  public void cancelBubble() {
    if (event != null) {
      DOM.eventCancelBubble(event, true);
    }
  }

  /**
   * Returns the event's x coordinate.
   * 
   * @return the x coordinate or -1 if no dom event.
   */
  public int getClientX() {
    if (event != null) {
      return DOM.eventGetClientX(event);
    }
    return -1;
  }

  /**
   * Returns the event's y coordinate.
   * 
   * @return the y coordinate or -1 if no dom event.
   */
  public int getClientY() {
    if (event != null) {
      return DOM.eventGetClientY(event);
    }
    return -1;
  }

  /**
   * Returns the dom event type.
   * 
   * @return the event type
   */
  public int getEventType() {
    return DOM.eventGetType(event);
  }

  /**
   * Returns the key code associated with this event.
   * 
   * @return the key code
   */
  public int getKeyCode() {
    return DOM.eventGetKeyCode(event);
  }
  
  /**
   * Returns the event's target element.
   * 
   * @return the target element or <code>nulol</code> if no dom event.
   */
  public Element getTarget() {
    if (event != null) {
      return DOM.eventGetTarget(event);
    }
    return null;
  }

  /**
   * Returns <code>true</code> if the control, alt, shift, or meta key is
   * pressed.
   * 
   * @return the modifier state
   */
  public boolean hasModifier() {
    if (event != null) {
      if (DOM.eventGetAltKey(event) || DOM.eventGetCtrlKey(event)
          || DOM.eventGetShiftKey(event) || DOM.eventGetMetaKey(event)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns <code>true</code> if the control key (or meta key) is pressed.
   * 
   * @return the control key state
   */
  public boolean isControlKey() {
    return event == null ? false
        : (DOM.eventGetCtrlKey(event) || DOM.eventGetMetaKey(event));
  }

  /**
   * Returns <code>true</code> if the event is a right click.
   * 
   * @return the right click state
   */
  public boolean isRightClick() {
    if (event != null) {
      if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT
          || (MyGWT.isMac && DOM.eventGetCtrlKey(event))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns <code>true</code> if the shift key is pressed.
   * 
   * @return the shift key state
   */
  public boolean isShiftKey() {
    return event == null ? false : DOM.eventGetShiftKey(event);
  }

  /**
   * Prevents the browser from taking its default action for the given event.
   */
  public void preventDefault() {
    DOM.eventPreventDefault(event);
  }

  /**
   * Stops the event (preventDefault and cancelBubble).
   */
  public void stopEvent() {
    cancelBubble();
    preventDefault();
  }

  /**
   * Returns <code>true</code> if the target of this event equals or is a
   * child of the given element.
   * 
   * @param element the element
   * @return the within state
   */
  public boolean within(Element element) {
    if (event != null) {
      return DOM.isOrHasChild(element, getTarget());
    }
    return false;
  }

}
