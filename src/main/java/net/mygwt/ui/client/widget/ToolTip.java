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
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.util.Markup;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays a information popup when a user hovers over a widget.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeOpen</b> : (widget)<br>
 * <div>Fires before the tooltip is displayed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Open</b> : (widget)<br>
 * <div>Fires after a tooltip is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : (widget)<br>
 * <div>Fires before the tooltip is hidden. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget)<br>
 * <div>Fires after a tooltip is hidden.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dl>
 * <dt><b>CSS Styles:</b></dt>
 * <dd>.my-tooltip { the tool tip }</dd>
 * <dd>.my-tooltip .my-tooltip-title { title text }</dd>
 * <dd>.my-tooltip .my-tooltip-text { body text }</dd>
 * </dl>
 */
public class ToolTip extends Component implements Listener {

  private static WidgetContainer tooltip;
  private static EventPreview preview;
  private static int lastX, lastY;
  private static boolean hovering, showing;
  private static Timer timer;

  static {
    timer = new Timer() {
      public void run() {
        if (hovering) {
          ToolTip tip = (ToolTip) tooltip.getData("current");
          if (tip.title == null && tip.text == null) {
            return;
          }
          tip.showAt(lastX, lastY);
        }
      }
    };
    tooltip = new WidgetContainer();
    tooltip.setLayoutOnChange(true);
    DOM.setStyleAttribute(tooltip.getElement(), "position", "absolute");
    MyDOM.setLeftTop(tooltip.getElement(), -1000, -1000);
    RootPanel.get().add(tooltip);

    preview = new EventPreview() {

      public boolean onEventPreview(Event event) {
        Element target = DOM.eventGetTarget(event);

        ToolTip tip = (ToolTip) tooltip.getData("current");
        if (tip.trackMouse) {
          tip.setToolTipPosition(DOM.eventGetClientX(event), DOM.eventGetClientY(event));
        }

        Widget source = (Widget) tooltip.getData("source");
        if (target == null || !DOM.isOrHasChild(source.getElement(), target)) {
          hovering = false;
          doHide();
        }
        return true;
      }

    };
  }

  /**
   * Hides the tool tip.
   */
  protected static void doHide() {
    DOM.removeEventPreview(preview);
    timer.cancel();

    showing = false;
    hovering = false;

    Component tip = (Component) tooltip.getData("current");
    if (tip != null) {
      tip.fireEvent(Events.Close);
    }

    tooltip.setData("current", null);
    tooltip.setData("source", null);
    tooltip.setVisible(false);

  }

  private int delay = 700;
  private boolean hideOnClick = true;
  private boolean trackMouse = false;
  private int xOffset = 5;
  private int yOffset = 15;
  private Component source;
  private Element contentElem;
  private String html = "<div class=my-tooltip-title>{0}</div><div class=my-tooltip-text>{1}</div>";
  private String title, text;
  private Element titleElem, textElem;
  private Element mouseOver;

  /**
   * Creates a new tooltip.
   * 
   * @param source the source widget
   */
  public ToolTip(Component source) {
    this.source = source;
    MyDOM.addEventsSunk(source.getElement(), Event.MOUSEEVENTS);
    source.addListener(Events.MouseOver, this);
    source.addListener(Events.MouseOut, this);
    source.addListener(Events.Click, this);
  }

  /**
   * Creates a new tooltip.
   * 
   * @param source the source widget
   * @param mouseOver the mouse over element
   */
  public ToolTip(Component source, Element mouseOver) {
    this(source);
    this.mouseOver = mouseOver;
  }

  /**
   * Returns the delay in milliseconds.
   * 
   * @return the delay
   */
  public int getDelay() {
    return delay;
  }

  /**
   * Returns <code>true</code> if hide on click is enabled.
   * 
   * @return the hide on click state
   */
  public boolean getHideOnClick() {
    return hideOnClick;
  }

  /**
   * Returns the tool tip source.
   * 
   * @return the source
   */
  public Component getSource() {
    return source;
  }

  /**
   * Returns <code>true</code> if track mouse is enabled.
   * 
   * @return the track mouse state
   */
  public boolean getTrackMouse() {
    return trackMouse;
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
   * @return the y offset
   */
  public int getYOffset() {
    return yOffset;
  }

  public void handleEvent(BaseEvent be) {
    if (be.type == Events.MouseOver || be.type == Events.MouseOut) {
      try {
        lastX = be.getClientX();
        lastY = be.getClientY();
      } catch (Exception e) {
        // TODO: why exception when paging
      }

      if (isEnabled()) {
        Element elem = mouseOver != null ? mouseOver : source.getElement();
        Rectangle rect = MyDOM.getBounds(elem);

        if (rect.contains(lastX, lastY)) {
          if (!hovering) {
            onMouseOver(be);
          }
        } else {
          doHide();
        }
      }

    }

    if (hideOnClick && be.type == Events.Click) {
      doHide();
    }
  }

  /**
   * Hides the tool tip.
   */
  public void hide() {
    if (!fireEvent(Events.BeforeClose)) {
      return;
    }
    doHide();
  }

  /**
   * Returns whether or not the tooltip is currently showing.
   * 
   * @return true if the tooltip is showing
   */
  public boolean isShowing() {
    return showing;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * Specifies if the tool tip should be hidden when the source widget is
   * clicked. Default value is <code>true</code>.
   * 
   * @param hideOnClick <code>true</code> to enable
   */
  public void setHideOnClick(boolean hideOnClick) {
    this.hideOnClick = hideOnClick;
  }

  /**
   * Sets the tool tip's title and text.
   * 
   * @param title the title
   * @param text the text
   */
  public void setText(String title, String text) {
    this.title = title;
    this.text = text;
    if (rendered) {
      if (title != null && !title.equals("")) {
        MyDOM.setInnerHTML(titleElem, title);
        MyDOM.setVisible(titleElem, true);
      } else {
        MyDOM.setVisible(titleElem, false);
      }
      if (text != null && !text.equals("")) {
        MyDOM.setInnerHTML(textElem, text);
      }
    }
  }

  public void disable() {
    super.disable();
    setVisible(false);
  }

  public void enable() {
    super.enable();
    setVisible(true);
  }

  public void setToolTipPosition(int x, int y) {
    MyDOM.setLeftTop(tooltip.getElement(), x + xOffset, y + yOffset);

    Rectangle r = MyDOM.getBounds(tooltip.getElement());

    int clientHeight = Window.getClientHeight() + MyDOM.getBodyScrollTop();
    int clientWidth = Window.getClientWidth() + MyDOM.getBodyScrollLeft();

    x = r.x;
    y = r.y;

    if (y + r.height > clientHeight) {
      y = clientHeight - r.height - 30;
      MyDOM.setTop(tooltip.getElement(), y);
    }
    if (x + r.width > clientWidth) {
      x = clientWidth - r.width - 4;
      MyDOM.setLeft(tooltip.getElement(), x);
    }
  }

  /**
   * Specifies if the tool tip should move with the current cursor location.
   * Default value is <code>false</code>.
   * 
   * @param trackMouse <code>true</code> to enable mouse tracking
   */
  public void setTrackMouse(boolean trackMouse) {
    this.trackMouse = trackMouse;
  }

  /**
   * Sets the x offset amount. Default value is 5.
   * 
   * @param offset the offset
   */
  public void setXOffset(int offset) {
    xOffset = offset;
  }

  /**
   * Sets the y offset amount. Default value is 15.
   * 
   * @param offset the y offset
   */
  public void setYOffset(int offset) {
    yOffset = offset;
  }

  /**
   * Displays the tool tip at the specified location. Offsets will be applie to
   * values.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    if (showing || !isEnabled()) {
      return;
    }
    BaseEvent be = new BaseEvent();
    be.x = x;
    be.y = y;
    if (!fireEvent(Events.BeforeOpen, be)) {
      return;
    }
    showing = true;
    onShowToolTip(x, y);
  }

  protected void onMouseOut(BaseEvent be) {
    if (hovering) {
      hovering = false;
      tooltip.setData("current", null);
      timer.cancel();
    }
  }

  protected void onMouseOver(BaseEvent be) {
    if (!hovering) {
      DOM.setIntStyleAttribute(tooltip.getElement(), "zIndex", MyDOM.getZIndex()); 
      hovering = true;
      tooltip.setData("current", this);
      timer.schedule(delay);
    } else {

    }
  }

  protected void onRender() {
    String html = Format.substitute(Markup.BOX, "my-tooltip");
    setElement(MyDOM.create(html));
    contentElem = MyDOM.findChild("my-tooltip-mc", getElement());

    if (title == null) title = "";
    if (text == null) text = "";
    String temp = Format.substitute(this.html, new String[] {this.title, this.text});
    MyDOM.setInnerHTML(contentElem, temp);

    titleElem = MyDOM.findChild("my-tooltip-title", getElement());
    textElem = MyDOM.findChild("my-tooltip-text", getElement());
  }

  protected void onShowToolTip(int x, int y) {
    tooltip.removeAll();
    tooltip.add(this);
    tooltip.setVisible(true);
    tooltip.setData("current", this);
    tooltip.setData("source", source);
    showing = true;
    setToolTipPosition(x, y);

    DOM.addEventPreview(preview);

    fireEvent(Events.Open);
  }

}
