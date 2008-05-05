/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.fx;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Observable;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * Applies drag handles to a widget to make it resizable.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>ResizeStart</b> : (source, widget, event) <br>
 * Fires after a resize operation has started.
 * <ul>
 * <li>source : this</li>
 * <li>widget : resize widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ResizeEnd</b> : (source, widget, event)<br>
 * Fires after a resize.
 * <ul>
 * <li>source : this</li>
 * <li>widget : resize widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Resizable extends Observable {

  private class ResizeHandle extends Widget {

    public int dir;

    public ResizeHandle() {
      setElement(DOM.createDiv());
      sinkEvents(Event.MOUSEEVENTS);
    }

    public void onBrowserEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          DOM.eventCancelBubble(event, true);
          DOM.eventPreventDefault(event);
          handleMouseDown(event, this);
          break;
      }
    }
  }

  /**
   * The minumum width for the widget. Default value is 50.
   */
  public int minWidth = 50;

  /**
   * The maximum width for the widget. Default value is 2000;
   */
  public int maxWidth = 2000;

  /**
   * The minimum height for the widget. Default value is 50;
   */
  public int minHeight = 50;

  /**
   * The maximum height for the widget. Default value is 2000;
   */
  public int maxHeight = 2000;

  /**
   * proxyStyle is the style name used for proxy drags. Default value is
   * 'my-resize-proxy'.
   */
  public String proxyStyle = "my-resize-proxy";

  /**
   * <code>true</code> to resize the element while dragging instead of using a
   * proxy.
   */
  public boolean dynamic = false;

  private Component resize;
  private List handles;
  private boolean enabled = true;
  private boolean dragging;
  private Element proxyElem, dragElem;
  private int startX, startY;
  private int dir;
  private EventPreview preview;
  private Rectangle start;
  private Listener listener;

  /**
   * Creates a new resizable instance.
   * 
   * @param resize the resize widget
   */
  public Resizable(Component resize) {
    this(resize, new ResizeConfig());
  }

  /**
   * Creates a new resizable instance.
   * 
   * @param resize the resize widget
   * @param cfg the config
   */
  public Resizable(Component resize, ResizeConfig cfg) {
    this.resize = resize;

    MyDOM.makePositionable(resize.getElement());

    handles = new ArrayList();

    if (cfg.south) create(Style.SOUTH, "s");
    if (cfg.southEast) create(Style.SE, "se");
    if (cfg.east) create(Style.EAST, "e");

    listener = new Listener() {
      public void handleEvent(BaseEvent be) {
        switch (be.type) {
          case Events.Attach:
            onAttach();
            break;
          case Events.Detach:
            onDetach();
            break;
        }
      }
    };

    resize.addListener(Events.Attach, listener);
    resize.addListener(Events.Detach, listener);

    if (resize.isAttached()) {
      onAttach();
    }

    preview = new EventPreview() {
      public boolean onEventPreview(Event event) {
        switch (DOM.eventGetType(event)) {
          case Event.ONMOUSEMOVE:
            int x = DOM.eventGetClientX(event);
            int y = DOM.eventGetClientY(event);
            handleMouseMove(x, y);
            break;
          case Event.ONMOUSEUP:
            handleMouseUp(event);
            break;
        }
        return false;
      }
    };

  }

  /**
   * Returns <code>true</code> if if resizing.
   * 
   * @return the resize state
   */
  public boolean isResizing() {
    return dragging;
  }

  /**
   * Removes the drag handles.
   */
  public void release() {
    onDetach();

    resize.removeListener(Events.Attach, listener);
    resize.removeListener(Events.Detach, listener);
    for (int i = 0; i < handles.size(); i++) {
      ResizeHandle handle = (ResizeHandle) handles.remove(0);
      DOM.removeChild(resize.getElement(), handle.getElement());
    }
  }

  /**
   * Enables or disables the drag handles.
   * 
   * @param enable <code>true</code> to enable
   */
  public void setEnabled(boolean enable) {
    for (int i = 0; i < handles.size(); i++) {
      ResizeHandle handle = (ResizeHandle) handles.get(i);
      MyDOM.setVisibility(handle.getElement(), enable);
    }
  }

  protected void onAttach() {
    for (int i = 0; i < handles.size(); i++) {
      Widget w = (Widget) handles.get(i);
      WidgetHelper.doAttach(w);
    }
  }

  protected void onDetach() {
    for (int i = 0; i < handles.size(); i++) {
      Widget w = (Widget) handles.get(i);
      WidgetHelper.doDetach(w);
    }
  }

  private ResizeHandle create(int dir, String cls) {
    ResizeHandle rh = new ResizeHandle();
    rh.setStyleName("my-resize-handle");
    rh.addStyleName("my-resize-handle-" + cls);
    rh.dir = dir;
    DOM.appendChild(resize.getElement(), rh.getElement());
    handles.add(rh);
    return rh;
  }

  private void handleMouseDown(Event event, ResizeHandle handle) {
    if (!enabled) {
      return;
    }

    dir = handle.dir;
    start = MyDOM.getBounds(resize.getElement(), false);
    startX = DOM.eventGetClientX(event);
    startY = DOM.eventGetClientY(event);
    dragging = true;

    if (!dynamic) {
      if (proxyElem == null) {
        proxyElem = DOM.createDiv();
        MyDOM.setStyleName(proxyElem, proxyStyle, true);
        MyDOM.disableTextSelection(proxyElem, true);

        Element body = RootPanel.getBodyElement();
        DOM.appendChild(body, proxyElem);
      }

      MyDOM.setLeft(proxyElem, start.x);
      MyDOM.setTop(proxyElem, start.y);
      MyDOM.setSize(proxyElem, start.width, start.height);
      MyDOM.setVisible(proxyElem, true);
      dragElem = proxyElem;

    } else {
      dragElem = resize.getElement();
    }
    DOM.addEventPreview(preview);

    BaseEvent be = new BaseEvent();
    be.source = this;
    be.widget = resize;
    be.event = event;
    fireEvent(Events.ResizeStart, be);
  }

  private void handleMouseMove(int xin, int yin) {
    if (dragging) {
      int w = 0;
      int h = 0;

      int difX = xin - startX;
      int difY = yin - startY;

      w = start.width + difX;
      h = start.height + difY;

      // enforce min max
      w = Math.min(Math.max(minWidth, w), maxWidth);
      h = Math.min(Math.max(minHeight, h), maxHeight);

      if (dir == Style.EAST || dir == Style.NE) {
        MyDOM.setWidth(dragElem, w);
      }
      if (dir == Style.SOUTH || dir == Style.SW) {
        MyDOM.setHeight(dragElem, h);
      }
      if (dir == Style.SE) {
        MyDOM.setSize(dragElem, w, h);
      }
    }

  }

  private void handleMouseUp(Event event) {
    dragging = false;
    DOM.removeEventPreview(preview);
    Rectangle rect = MyDOM.getBounds(dragElem, false);

    rect.width = Math.min(rect.width, maxWidth);
    rect.height = Math.min(rect.height, maxHeight);

    if (proxyElem != null) {
      MyDOM.disableTextSelection(proxyElem, false);
    }

    resize.setBounds(rect);

    MyDOM.setVisible(dragElem, false);

    BaseEvent be = new BaseEvent();
    be.source = this;
    be.widget = resize;
    be.event = event;
    fireEvent(Events.ResizeEnd, be);
  }

}
