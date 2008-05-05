/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.fx;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.DragListener;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.util.Observable;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Adds drag behavior to any widget. Drag operations can be initiated from the
 * widget itself, or another widget, such as the header in a dialog.
 * 
 * <p>
 * It is possible to specify event targets that will be ignored. If the target
 * element has a 'my-nodrag' style it will not trigger a drag operation.
 * </p>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>DragStart</b> : (source, widget, event) <br>
 * Fires after a drag has started.
 * <ul>
 * <li>source : this</li>
 * <li>widget : drag widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragMove</b> : (source, widget, event)<br>
 * Fires after the mouse moves.
 * <ul>
 * <li>source : this</li>
 * <li>widget : drag widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragCancel</b> : (source, widget, event)<br>
 * Fires after a drag has been cancelled.
 * <ul>
 * <li>source : this</li>
 * <li>widget : drag widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragEnd</b> : (source, widget, event) <br>
 * Fires after a drag has ended.
 * <ul>
 * <li>source : this</li>
 * <li>widget : drag widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Draggable extends Observable {

  /**
   * <code>true</code> to use a proxy widget during drag operation. Default
   * value is <code>true</code>
   */
  public boolean useProxy = true;

  /**
   * proxyStyle is the style name used for proxy drags. Default value is
   * 'my-drag-proxy'.
   */
  public String proxyStyle = "my-drag-proxy";

  /**
   * <code>true</code> to stop vertical movement. Default value is
   * <code>false</code>
   */
  public boolean constrainVertical = false;

  /**
   * <code>true</code> to stop horizontal movement. Default value is
   * <code>false</code>
   */
  public boolean constrainHorizontal = false;

  /**
   * <code>true</code> to set client area constraints. Default value is
   * <code>true</code>
   */
  public boolean constrainClient = true;

  /**
   * <code>true</code> to move source widget aftet a proxy drag. Default
   * values is <code>true</code>
   */
  public boolean moveAfterProxyDrag = true;

  /**
   * If specified, drag will be constrained by container bounds.
   */
  public Widget container;

  /**
   * <code>true</code> to set proxy dimensions the same as the drag widget.
   * Default value is <code>true</code>
   */
  public boolean sizeProxyToSource = true;

  /**
   * updateZIndex specifies if the CSS z-index should be updated on the widget
   * being dragged. Setting this value to <code>true</code> will ensure that
   * the dragged element is always displayed over all other widgets. Default
   * value is <code>true</code>.
   */
  public boolean updateZIndex = true;

  private Component dragWidget;
  private Component handle;
  private boolean dragging;
  private boolean enabled = true;
  private int dragStartX, dragStartY;
  private int lastX, lastY;
  private int clientWidth, clientHeight;
  private int conX, conY, conWidth, conHeight;
  private Rectangle startBounds;
  private Element proxyElem;
  private EventPreview preview;
  private BaseEvent dragEvent;
  private int xLeft = Style.DEFAULT, xRight = Style.DEFAULT;
  private int xTop = Style.DEFAULT, xBottom = Style.DEFAULT;

  /**
   * Creates a new draggable instance.
   * 
   * @param dragWidget the widget to be dragged
   */
  public Draggable(Component dragWidget) {
    this(dragWidget, dragWidget);
  }

  /**
   * Create a new draggable instance.
   * 
   * @param dragWidget the widget to be dragged
   * @param handle the widget drags will be initiated from
   */
  public Draggable(final Component dragWidget, Component handle) {
    this.dragWidget = dragWidget;
    this.handle = handle;

    MyDOM.makePositionable(dragWidget.getElement());

    handle.sinkEvents(Event.MOUSEEVENTS);
    handle.addListener(Events.MouseDown, new Listener() {
      public void handleEvent(BaseEvent be) {
        onMouseDown(be);
      }
    });

    preview = new EventPreview() {
      public boolean onEventPreview(Event event) {
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
        switch (DOM.eventGetType(event)) {
          case Event.ONKEYDOWN:
            int key = DOM.eventGetKeyCode(event);
            if (key == KeyboardListener.KEY_ESCAPE && dragging) {
              cancelDrag(event);
            }
            break;
          case Event.ONMOUSEMOVE:
            onMouseMove(event);
            break;
          case Event.ONMOUSEUP:
            stopDrag(event);
            break;
        }
        return true;
      }
    };
  }

  /**
   * Adds a listener to receive drag events.
   * 
   * @param listener the drag listener to be added
   */
  public void addDragListener(DragListener listener) {
    TypedListener l = new TypedListener(listener);
    addListener(Events.DragStart, l);
    addListener(Events.DragMove, l);
    addListener(Events.DragCancel, l);
    addListener(Events.DragEnd, l);
  }

  /**
   * Returns the drag handle.
   * 
   * @return the drag handle
   */
  public Widget getDragHandle() {
    return handle;
  }

  /**
   * Returns the widget being dragged.
   * 
   * @return the drag widget
   */
  public Widget getDragWidget() {
    return dragWidget;
  }

  /**
   * Returns <code>true</code> if a drag is in progress.
   * 
   * @return the drag state
   */
  public boolean isDragging() {
    return dragging;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeDragListener(DragListener listener) {
    if (eventTable == null) return;
    eventTable.unhook(Events.DragStart, listener);
    eventTable.unhook(Events.DragMove, listener);
    eventTable.unhook(Events.DragCancel, listener);
    eventTable.unhook(Events.DragEnd, listener);
  }

  /**
   * Enables dragging if the argument is <code>true</code>, and disables it
   * otherwise.
   * 
   * @param enabled the new enabled state
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Constrains the horizontal travel.
   * 
   * @param left the number of pixels the element can move to the left
   * @param right the number of pixels the element can move to the right
   */
  public void setXConstraint(int left, int right) {
    xLeft = left;
    xRight = right;
  }

  /**
   * Constrains the vertical travel.
   * 
   * @param top the number of pixels the element can move to the up
   * @param bottom the number of pixels the element can move to the down
   */
  public void setYConstraint(int top, int bottom) {
    xTop = top;
    xBottom = bottom;
  }

  private void afterDrag() {
    MyDOM.removeStyleName(MyDOM.getBody(), "my-no-selection");
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        dragWidget.enableEvents(true);
      }
    });
  }

  private void cancelDrag(Event event) {
    if (dragging) {
      DOM.removeEventPreview(preview);
      dragging = false;
      if (useProxy) {
        MyDOM.disableTextSelection(proxyElem, false);
        Element body = MyDOM.getBody();
        DOM.removeChild(body, proxyElem);
        proxyElem = null;
      }
      if (!useProxy) {
        MyDOM.setLocation(dragWidget.getElement(), startBounds.x, startBounds.y);
      }

      fireEvent(Events.DragCancel);
      afterDrag();
    }
  }

  private void onMouseDown(BaseEvent be) {
    if (!enabled || dragging) {
      return;
    }
    
    Element target = be.getTarget();
    String s = DOM.getElementProperty(target, "className");
    if (s != null && s.indexOf("my-nodrag") != -1) {
      return;
    }
    be.cancelBubble();
    startBounds = MyDOM.getBounds(dragWidget.getElement(), true);

    dragWidget.enableEvents(false);
    startDrag(be.event);

    DOM.addEventPreview(preview);

    clientWidth = Window.getClientWidth() + MyDOM.getBodyScrollLeft();
    clientHeight = Window.getClientHeight() + MyDOM.getBodyScrollTop();

    dragStartX = be.getClientX();
    dragStartY = be.getClientY();

    if (container != null) {
      conX = container.getAbsoluteLeft();
      conY = container.getAbsoluteTop();
      conWidth = container.getOffsetWidth();
      conHeight = container.getOffsetHeight();
    }
  }

  private void onMouseMove(Event event) {
    if (proxyElem != null) {
      MyDOM.setVisibility(proxyElem, true);
    }
    int x = DOM.eventGetClientX(event);
    int y = DOM.eventGetClientY(event);

    if (dragging) {
      int left = startBounds.x + (x - dragStartX);
      int top = startBounds.y + (y - dragStartY);

      int width = dragWidget.getOffsetWidth();
      int height = dragWidget.getOffsetHeight();

      if (constrainClient) {
        left = Math.max(left, 0);
        top = Math.max(top, 0);
        left = Math.min(clientWidth - width, left);
        
        if (Math.min(clientHeight - height, top) > 0) {
          top = Math.max(2, Math.min(clientHeight - height, top));
        }
      }

      if (container != null) {
        left = Math.max(left, conX);
        left = Math.min(conX + conWidth - dragWidget.getOffsetWidth(), left);
        top = Math.min(conY + conHeight - dragWidget.getOffsetHeight(), top);
        top = Math.max(top, conY);
      }

      if (xLeft != Style.DEFAULT) {
        left = Math.max(startBounds.x - xLeft, left);
      }
      if (xRight != Style.DEFAULT) {
        left = Math.min(startBounds.x + xRight, left);
      }

      if (xTop != Style.DEFAULT) {
        top = Math.max(startBounds.y - xTop, top);

      }
      if (xBottom != Style.DEFAULT) {
        top = Math.min(startBounds.y + xBottom, top);
      }

      if (constrainHorizontal) {
        left = startBounds.x;
      }
      if (constrainVertical) {
        top = startBounds.y;
      }

      lastX = left;
      lastY = top;

      if (useProxy) {
        MyDOM.setLeftTop(proxyElem, left, top);
      } else {
        MyDOM.setLocation(dragWidget.getElement(), left, top);
      }

      dragEvent.source = this;
      dragEvent.widget = dragWidget;
      dragEvent.event = event;
      fireEvent(Events.DragMove, dragEvent);
    }
  }

  private void startDrag(Event event) {
    MyDOM.addStyleName(MyDOM.getBody(), "my-no-selection");

    if (updateZIndex) {
      DOM.setIntStyleAttribute(dragWidget.getElement(), "zIndex", MyDOM.getZIndex());
    }

    BaseEvent be = new BaseEvent(dragWidget);
    be.event = event;
    fireEvent(Events.DragStart, be);

    if (dragEvent == null) {
      dragEvent = new BaseEvent();
    }

    dragging = true;
    if (useProxy) {
      if (proxyElem == null) {
        proxyElem = DOM.createDiv();
        MyDOM.setVisibility(proxyElem, false);
        MyDOM.setStyleName(proxyElem, proxyStyle);
        MyDOM.disableTextSelection(proxyElem, true);
        Element body = MyDOM.getBody();
        DOM.appendChild(body, proxyElem);
        DOM.setIntStyleAttribute(proxyElem, "zIndex", MyDOM.getZIndex());
        DOM.setStyleAttribute(proxyElem, "position", "absolute");
      }
      MyDOM.setVisibility(proxyElem, false);

      if (sizeProxyToSource) {
        MyDOM.setBounds(proxyElem, startBounds);
      }

      // did listeners change size?
      if (be.height > 0) {
        MyDOM.setHeight(proxyElem, be.height, true);
      }
      if (be.width > 0) {
        MyDOM.setWidth(proxyElem, be.width, true);
      }
    }
  }

  private void stopDrag(Event event) {
    if (dragging) {
      DOM.removeEventPreview(preview);
      dragging = false;
      if (useProxy) {
        if (moveAfterProxyDrag) {
          Rectangle rect = MyDOM.getBounds(proxyElem, false);
          MyDOM.setLocation(dragWidget.getElement(), rect.x, rect.y);
        }
        MyDOM.disableTextSelection(proxyElem, false);
        Element body = MyDOM.getBody();
        DOM.removeChild(body, proxyElem);
        proxyElem = null;
      }
      BaseEvent be = new BaseEvent(dragWidget);
      be.event = event;
      be.x = lastX;
      be.y = lastY;
      fireEvent(Events.DragEnd, be);
      afterDrag();
    }
  }

}
