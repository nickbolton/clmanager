/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributers:
 *      Darell Meyer <darrell@mygwt.net> - Derived implementation
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.util.Point;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A shadow panel that can "pop up" over other widgets. It overlays the
 * browser's client area (and any previously-created popups).
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeOpen</b> : (widget)<br>
 * <div>Fires before the popup is displayed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Open</b> : (widget)<br>
 * <div>Fires after a popup is opened.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : (widget)<br>
 * <div>Fires before the popup is closed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget)<br>
 * <div>Fires after a popup is closed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-popup (the popup itself)</dd>
 * </dl>
 */
public class Popup extends Component implements EventPreview {

  private boolean animate = true;
  private boolean eventPreview = true;
  private boolean autoFocus = true;
  private int yOffset = 15;
  private int xOffset = 10;
  private boolean constrainViewport = true;
  private int duration = 200;
  private boolean showing, autoHide;
  private Widget widget;
  private FramePanel framePanel;
  private Shadow shadow;
  private boolean showShadow;
  private List ignoreElements;

  /**
   * Creates a new popup panel.
   */
  public Popup() {
    baseStyle = "my-popup";
  }

  /**
   * Creates an new popup panel.
   * 
   * @param autoHide <code>true</code> to hide if "click" occurs outside of
   *            the popup
   */
  public Popup(boolean autoHide) {
    this();
    this.autoHide = autoHide;
  }

  /**
   * Centers the panel within the viewport.
   */
  public void center() {
    MyDOM.center(getElement());
  }

  /**
   * Returns <code>true</code> if animations are enabled.
   * 
   * @return the animation state
   */
  public boolean getAnimate() {
    return animate;
  }

  /**
   * Returns <code>true</code> if auto focus is enabled.
   * 
   * @return the auto focus state
   */
  public boolean getAutoFocus() {
    return autoFocus;
  }

  /**
   * Returns <code>true</code> if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean getAutoHide() {
    return autoHide;
  }

  /**
   * Returns <code>true</code> if the popup will be constrained to the
   * viewport.
   * 
   * @return the constrain viewport state
   */
  public boolean getConstrainViewport() {
    return constrainViewport;
  }

  /**
   * Returns the animation duration.
   * 
   * @return the duration
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Returns <code>true</code> if event preview is enabled.
   * 
   * @return the event preview state
   */
  public boolean getEventPreview() {
    return eventPreview;
  }

  /**
   * Any elements added to this list will be ignored when auto close is enabled.
   * 
   * @return the list of ignored elements
   */
  public List getIgnoreList() {
    if (ignoreElements == null) {
      ignoreElements = new ArrayList();
    }
    return ignoreElements;
  }

  /**
   * Returns <code>true</coe> if the shadow is enabled.
   * 
   * @return the show shadow state
   */
  public boolean getShadow() {
    return showShadow;
  }

  /**
   * Returns the x offset.
   * 
   * @return the x offset
   */
  public int getXOffset() {
    return xOffset;
  }

  /**
   * Returns the y offset.
   * 
   * @return the offset
   */
  public int getYOffset() {
    return yOffset;
  }

  /**
   * Hides the popup. This has no effect if it is not currently visible.
   */
  public void hide() {
    if (!fireEvent(Events.BeforeClose)) {
      return;
    }
    if (!showing) return;
    showing = false;

    framePanel.onHide(getElement());
    FramePanel.push(framePanel);

    if (showShadow) {
      shadow.remove();
      Shadow.push(shadow);
    }

    if (eventPreview) {
      DOM.removeEventPreview(this);
    }

    if (animate) {
      FXStyle fx = new FXStyle(getElement());
      fx.duration = duration;
      fx.addListener(Events.EffectComplete, new Listener() {
        public void handleEvent(BaseEvent be) {
          afterHide();
        }
      });
      fx.fadeOut();
    } else {
      afterHide();
    }
  }

  public boolean isVisible() {
    return showing;
  }

  public boolean onEventPreview(Event event) {
    int type = DOM.eventGetType(event);
    Element target = DOM.eventGetTarget(event);
    switch (type) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONCLICK:
      case Event.ONDBLCLICK: {
        if (DOM.getCaptureElement() == null) {
          // Disallow mouse events outside of the popup.
          if (!DOM.isOrHasChild(getElement(), target)) {
            // If it's a click event, and auto-hide is enabled: hide the popup
            // and _don't_ eat the event.
            if (autoHide && (type == Event.ONCLICK) || isRightClick(event)) {
              if (ignoreElements != null) {
                for (int i = 0; i < ignoreElements.size(); i++) {
                  Element elem = (Element) ignoreElements.get(i);
                  if (DOM.isOrHasChild(elem, target)) {
                    return true;
                  }
                }
              }
              if (onAutoHide(event)) {
                hide();
                return true;
              }
              return false;
            }
            return false;
          }
        }
        break;
      }
      case Event.ONKEYUP:
        int code = DOM.eventGetKeyCode(event);
        BaseEvent be = new BaseEvent();
        be.event = event;
        be.widget = this;
        be.item = widget;
        onKeyPress(be);
        switch (code) {
          case KeyboardListener.KEY_ESCAPE:
            onAutoHide(event);
        }
        break;
    }
    return true;
  }

  /**
   * Specifies if the hiding and showing of the panel should be animated.
   * Default value is <code>true</code>.
   * 
   * @param animate <code>true</code> to enable animations
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * Specifies if focus should be set on the popup when displayed. Default value
   * is <code>true</code>.
   * 
   * @param autoFocus <code>true</code> to enable auto focus
   */
  public void setAutoFocus(boolean autoFocus) {
    this.autoFocus = autoFocus;
  }

  /**
   * Sets the auto hide state. When auto hide is enabled, the popup is closed
   * when a click occurs outside the popup's bounds.
   * 
   * @param autoHide <code>true</code> to enable auto hide
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * Specifies if the popup location should be forced to the viewport area.
   * Default value is <code>true</code>.
   * 
   * @param constrainViewport <code>true<code> to enable
   */
  public void setConstrainViewport(boolean constrainViewport) {
    this.constrainViewport = constrainViewport;
  }

  /**
   * Sets the length of the fade effect in milliseconds. Default value is 200.
   * 
   * @param duration the duration in millisecond
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Sets whether event preview should be enabled when opening the popup.
   * Default value is <code>true</code>.
   * 
   * @param eventPreview the event preview state
   */
  public void setEventPreview(boolean eventPreview) {
    this.eventPreview = eventPreview;
  }

  /**
   * Sets whether the shadow should be displayed. Defualt value is
   * <code>false</code>.
   * 
   * @param show <code>true</code> to show the shadow
   */
  public void setShadow(boolean show) {
    this.showShadow = show;
  }

  /**
   * Sets the popup's content.
   * 
   * @param widget the content widget
   */
  public void setWidget(Widget widget) {
    this.widget = widget;
  }

  /**
   * Sets the x offset. Only applies when constrainViewerport is
   * <code>true</code>. Default value is 10.
   * 
   * @param xOffset the x offset
   */
  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
  }

  /**
   * Sets the y offset. Only applies when constrainViewerport is
   * <code>true</code>. Default value is 15.
   * 
   * @param yOffset the y offset
   */
  public void setYOffset(int yOffset) {
    this.yOffset = yOffset;
  }

  /**
   * Displays the popup. It must have a child widget before this method is
   * called.
   */
  public void show() {
    if (showing) {
      return;
    }
    if (!fireEvent(Events.BeforeOpen)) {
      return;
    }
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the position
   */
  public void show(Element elem, String pos) {
    if (showing) {
      return;
    }
    if (!fireEvent(Events.BeforeOpen)) {
      return;
    }
    DOM.appendChild(getElement(), widget.getElement());
    Point p = MyDOM.getAlignToXY(getElement(), elem, pos, null);
    MyDOM.setLeftTop(getElement(), p.x, p.y);
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the postion
   * @param offsets the offsets
   */
  public void show(Element elem, String pos, int[] offsets) {
    if (showing) {
      return;
    }
    if (!fireEvent(Events.BeforeOpen)) {
      return;
    }
    DOM.appendChild(getElement(), widget.getElement());
    Point p = MyDOM.getAlignToXY(getElement(), elem, pos, offsets);
    MyDOM.setLeftTop(getElement(), p.x, p.y);
    onShowPopup();
  }

  /**
   * Shows the popup at the specified location.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void show(int x, int y) {
    DOM.appendChild(getElement(), widget.getElement());
    MyDOM.setLeftTop(getElement(), x, y);
    onShowPopup();
  }

  /**
   * Displays the popup aligned to the bottom left of the widget. For exact
   * control of popup position see {@link MyDOM#alignTo}.
   * 
   * @param widget the widget to use for alignment
   */
  public void show(Widget widget) {
    int[] offset = new int[] {0, 2};
    Point p = MyDOM.getAlignToXY(getElement(), widget.getElement(), null, offset);
    MyDOM.setLeftTop(getElement(), p.x, p.y);
    onShowPopup();
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    showing = false;

    WidgetHelper.doDetach(widget);
    fireEvent(Events.Close);
  }

  protected void afterShow() {
    if (showShadow) {
      shadow = Shadow.pop();
      shadow.show(this);
    }

    if (autoFocus) {
      MyDOM.setFocus(getElement(), true);
    }

    fireEvent(Events.Open);
  }

  /**
   * Subclasses may override to cancel the close from an auto hide.
   * 
   * @param event the current event
   * @return <code>true</code> to allow close, <code>false</code> to cancel
   */
  protected boolean onAutoHide(Event event) {
    return true;
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName(baseStyle);
    setStyleAttribute("position", "absolute");
    setStyleAttribute("zIndex", "100");
  }

  protected void onKeyPress(BaseEvent be) {

  }

  protected void onShowPopup() {
    showing = true;

    DOM.appendChild(getElement(), widget.getElement());

    int zIndex = MyDOM.getZIndex();
    DOM.setIntStyleAttribute(getElement(), "zIndex", zIndex);

    MyDOM.setVisibility(getElement(), false);
    setStyleAttribute("position", "absolute");

    RootPanel.get().add(this);

    framePanel = FramePanel.pop();
    framePanel.onShow(getElement(), MyDOM.getZIndex() - 1);

    if (constrainViewport) {
      int clientHeight = Window.getClientHeight() + MyDOM.getBodyScrollTop();
      int clientWidth = Window.getClientWidth() + MyDOM.getBodyScrollLeft();

      Rectangle r = MyDOM.getBounds(getElement());

      int x = r.x;
      int y = r.y;

      if (y + r.height > clientHeight) {
        y = clientHeight - r.height - yOffset;
        MyDOM.setTop(getElement(), y);
      }
      if (x + r.width > clientWidth) {
        x = clientWidth - r.width - xOffset;
        MyDOM.setLeft(getElement(), x);
      }
    }

    if (!widget.isAttached()) {
      WidgetHelper.doAttach(widget);
    }

    MyDOM.setVisibility(getElement(), true);

    if (eventPreview) {
      DOM.addEventPreview(this);
    }

    if (animate) {
      FXStyle fx = new FXStyle(getElement());
      fx.duration = duration;
      fx.addListener(Events.EffectComplete, new Listener() {
        public void handleEvent(BaseEvent be) {
          afterShow();
        }
      });
      fx.fadeIn();
    } else {
      afterShow();
    }
  }

  private boolean isRightClick(Event event) {
    int type = DOM.eventGetType(event);
    if (type == Event.ONMOUSEUP) {
      if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT
          || (MyGWT.isMac && DOM.eventGetCtrlKey(event))) {
        return true;
      }
    }
    return false;
  }

}
