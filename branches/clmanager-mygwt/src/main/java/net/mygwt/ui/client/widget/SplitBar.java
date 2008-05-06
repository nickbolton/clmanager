/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.fx.Draggable;
import net.mygwt.ui.client.util.DelayedTask;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * Creates draggable splitter bar on the side of a widget.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NORTH, WEST, SOUTH, EAST</dd>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Resize</b> : (widget, item, size)<br>
 * <div>Fires after the split bar has been moved.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the widget being resized</li>
 * <li>size : the new size</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragStart</b> : (widget,event) <div>Fires after a drag has started.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragEnd</b> : (widget,event) <div>Fires after a drag has ended.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dl>
 */
public class SplitBar extends Component {

  /**
   * Specifies if a frame panel should be displayed while dragging split bars.
   * Frame panel uses a hidden iframe to allow the split bar to travel over
   * embedded objects such as applets and flash. There is a slight performance
   * hit when enabled. Default value is <code>false</code>.
   */
  public static boolean useFramePanel;

  /**
   * Transparent shim that allows drags over iframes.
   */
  private static HTML shim;
  private static FramePanel framePanel;

  private static List attachedBars;
  private static DelayedTask delayedTask;

  static {
    shim = new HTML();
    shim.setStyleName("my-splitbar-shim");
    shim.setSize("2000px", "2000px");
    RootPanel.get().add(shim);
    shim.setVisible(false);
    attachedBars = new ArrayList();

    delayedTask = new DelayedTask(new Listener() {
      public void handleEvent(BaseEvent be) {
        int count = attachedBars.size();
        for (int i = 0; i < count; i++) {
          SplitBar bar = (SplitBar) attachedBars.get(i);
          bar.updateHandle();
        }
      }
    });
  }

  static void updateHandles() {
    delayedTask.delay(400);
  }

  private int minSize = 10;
  private int maxSize = 2000;
  private int barWidth = 4;
  private boolean autoSize = true;
  private int yOffset = 0;
  private int xOffset = 0;
  private Element resizeElem;
  private Component resizeWidget;
  private Component containerWidget;
  private Draggable draggable;
  private Rectangle startBounds;
  private Listener listener;
  private DelayedTask delay;

  /**
   * Creates a new split bar.
   * 
   * @param style the bar location
   * @param resizeWidget the widget being resized
   */
  public SplitBar(final int style, final Component resizeWidget) {
    this.style = style;
    this.resizeWidget = resizeWidget;
    this.resizeElem = resizeWidget.getElement();

    final Widget fSplitBar = this;

    listener = new Listener() {
      public void handleEvent(BaseEvent be) {
        switch (be.type) {
          case Events.Attach:
            MyDOM.insertBefore(getElement(), resizeElem);
            WidgetHelper.doAttach(fSplitBar);
            updateHandle();
            attachedBars.add(fSplitBar);
            break;
          case Events.Detach:
            WidgetHelper.doDetach(fSplitBar);
            MyDOM.removeFromParent(getElement());
            attachedBars.remove(fSplitBar);
            break;
          case Events.Resize:
            delay.delay(400);
            break;
        }
      }
    };

    resizeWidget.addListener(Events.Attach, listener);
    resizeWidget.addListener(Events.Detach, listener);
    resizeWidget.addListener(Events.Resize, listener);

    setElement(DOM.createDiv());

    if (style == Style.SOUTH || style == Style.NORTH) {
      setStyleName("my-hsplitbar");
    } else {
      setStyleName("my-vsplitbar");
    }
    MyDOM.setStyleAttribute(getElement(), "position", "absolute");

    draggable = new Draggable(this);
    draggable.updateZIndex = false;
    draggable.proxyStyle = "my-splitbar-proxy";

    Listener dragListener = new Listener() {
      public void handleEvent(BaseEvent be) {
        if (be.type == Events.DragStart) {
          onStartDrag(be);
        }
        if (be.type == Events.DragEnd) {
          onEndDrag(be);
        }

        if (be.type == Events.DragCancel) {
          onCancelDrag(be);
        }
      }

    };
    draggable.addListener(Events.DragStart, dragListener);
    draggable.addListener(Events.DragEnd, dragListener);
    draggable.addListener(Events.DragCancel, dragListener);

    sinkEvents(Event.MOUSEEVENTS);

    if (resizeWidget.isAttached()) {
      BaseEvent be = new BaseEvent();
      be.type = Events.Attach;
      listener.handleEvent(be);
    }

    delay = new DelayedTask(new Listener() {
      public void handleEvent(BaseEvent be) {
        updateHandle();
      }
    });
  }

  /**
   * Creates a new split bar.
   * 
   * @param style the bar location
   * @param resizeWidget the widget being resized
   * @param container the widget the split bar proxy will be sized to
   */
  public SplitBar(int style, Component resizeWidget, Component container) {
    this(style, resizeWidget);
    this.containerWidget = container;
    draggable.container = container;
  }

  /**
   * Returns <code>true</code> if auto size is enabled.
   * 
   * @return the auto size state
   */
  public boolean getAutoSize() {
    return autoSize;
  }

  /**
   * Returns the bar's width.
   * 
   * @return the bar width
   */
  public int getBarWidth() {
    return barWidth;
  }

  /**
   * Returns the split bar's draggable instance.
   * 
   * @return the draggable instance
   */
  public Draggable getDraggable() {
    return draggable;
  }

  /**
   * Returns the max size.
   * 
   * @return the max size
   */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * Returns the minimum size.
   * 
   * @return the minium size
   */
  public int getMinSize() {
    return minSize;
  }

  /**
   * Returns the resize widget.
   * 
   * @return the resize widget
   */
  public Widget getResizeWidget() {
    return resizeWidget;
  }

  /**
   * Returns the split bar's style information.
   * 
   * @return the style information
   */
  public int getStyle() {
    return style;
  }

  /**
   * Returns the x offset.
   * 
   * @return the x offset
   */
  public int getXOffset() {
    return yOffset;
  }

  /**
   * Returns the y offset.
   * 
   * @return the y offset
   */
  public int getYOffset() {
    return xOffset;
  }

  /**
   * Removes the split bar from the resize widget.
   */
  public void release() {
    resizeWidget.removeListener(Events.Attach, listener);
    resizeWidget.removeListener(Events.Detach, listener);
    resizeWidget.removeListener(Events.Resize, listener);
    RootPanel.get().remove(this);
  }

  /**
   * Specifies if the size of the the resize widget should be updated
   * automatically after a drag operation using a proxy. Default value is
   * <code>true</code>.
   * 
   * @param autoSize <code>true</code> to enable auto sizing
   */
  public void setAutoSize(boolean autoSize) {
    this.autoSize = autoSize;
  }

  /**
   * Sets width of bar in pixels. Default value is 4.
   * 
   * @param barWidth the bar width
   */
  public void setBarWidth(int barWidth) {
    this.barWidth = barWidth;
  }

  /**
   * Sets the maximum size of the resize widget. Default value is 2000.
   * 
   * @param maxSize the max size
   */
  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Sets the minimum size of the resize widget. Default value is 10.
   * 
   * @param minSize the minimum size
   */
  public void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  /**
   * Specifies the amount of pixels the bar should be offset to the left.
   * Default value is 0.
   * 
   * @param xOffset the x offset
   */
  public void setXOffset(int xOffset) {
    this.yOffset = xOffset;
  }

  /**
   * Sets the amount of pixels the bar should be offset to the top. Default
   * value is 0.
   * 
   * @param yOffset the y offset
   */
  public void setYOffset(int yOffset) {
    this.xOffset = yOffset;
  }

  public void updateHandle() {
    if (!isAttached() || !resizeWidget.isAttached()) {
      return;
    }
    Rectangle rect = MyDOM.getBounds(resizeElem, false);
    int x = rect.x;
    int y = rect.y;

    if (!MyDOM.isVisibleBox()) {
      y -= MyDOM.getDecorationWidth(resizeElem, Style.TOP);
      x -= MyDOM.getDecorationWidth(resizeElem, Style.LEFT);
    }

    int w = rect.width;
    int h = rect.height;

    switch (style) {
      case Style.SOUTH:
        MyDOM.setBounds(getElement(), x + yOffset, y + h + xOffset, w, barWidth, false);
        break;
      case Style.WEST:
        MyDOM.setBounds(getElement(), x - barWidth + yOffset, y + xOffset, barWidth, h,
            false);
        break;
      case Style.NORTH:
        MyDOM.setBounds(getElement(), x + yOffset, y - barWidth + xOffset, w, barWidth,
            false);
        break;
      case Style.EAST:
        MyDOM.setBounds(getElement(), x + w + yOffset, y + xOffset, barWidth, h, false);
        break;
    }

  }

  private void onCancelDrag(BaseEvent be) {
    shim.setVisible(false);
    resizeWidget.enableEvents(true);
    updateHandle();
  }

  private void onEndDrag(BaseEvent bee) {
    shim.setVisible(false);
    if (useFramePanel) {
      framePanel.onHide(shim.getElement());
      FramePanel.push(framePanel);
    }

    int x = bee.x;
    int y = bee.y;
    int width = resizeWidget.getOffsetWidth();
    int height = resizeWidget.getOffsetHeight();

    int diffY = y - startBounds.y + 4;
    int diffX = x - startBounds.x + 4;

    resizeWidget.enableEvents(true);

    BaseEvent be = new BaseEvent(this);
    be.item = resizeWidget;

    switch (style) {
      case Style.NORTH: {
        be.size = height - diffY;
        if (autoSize) {
          MyDOM.setY(resizeElem, y);
          MyDOM.setHeight(resizeElem, height - diffY);
        }
        break;
      }
      case Style.SOUTH: {
        be.size = height + diffY;
        if (autoSize) {
          MyDOM.setHeight(resizeElem, diffY);
          resizeWidget.setHeight(diffY);
        }
        break;
      }
      case Style.WEST: {
        be.size = width - diffX;
        if (autoSize) {
          MyDOM.setX(getElement(), x);
          resizeWidget.setWidth(width - diffX);
        }
        break;
      }
      case Style.EAST: {
        be.size = width + diffX;
        if (autoSize) {
          resizeWidget.setWidth(diffX);
        }
        break;
      }
    }
    be.type = Events.DragEnd;
    be.widget = this;
    fireEvent(Events.DragEnd, be);

    fireEvent(Events.Resize, be);
    updateHandle();
  }

  private void onStartDrag(BaseEvent be) {
    be.type = Events.DragStart;
    be.widget = this;
    fireEvent(Events.DragStart, be);

    shim.setVisible(true);
    
    DOM.setIntStyleAttribute(shim.getElement(), "zIndex", MyDOM.getZIndex() - 1);
    
    if (useFramePanel) {
      framePanel = FramePanel.pop();
      DOM.setIntStyleAttribute(framePanel.getElement(), "zIndex", MyDOM.getZIndex() - 3);
      framePanel.onShow(shim.getElement());
    }

    resizeWidget.enableEvents(false);

    if (containerWidget != null) {
      switch (style) {
        case Style.WEST:
        case Style.EAST:
          int h = containerWidget.getHeight(true);
          be.height = h;
          break;
        case Style.NORTH:
        case Style.SOUTH:
          int w = containerWidget.getWidth(true);
          be.width = w;
          break;
      }
    }

    startBounds = new Rectangle();
    startBounds.y = be.getClientY();
    startBounds.x = be.getClientX();

    boolean v = style == Style.WEST || style == Style.EAST;
    int size;
    if (v) {
      size = MyDOM.getWidth(resizeElem, false);
    } else {
      size = MyDOM.getHeight(resizeElem, false);
    }

    int c1 = size - minSize;
    if (size < minSize) {
      c1 = 0;
    }
    int c2 = Math.max(maxSize - size, 0);
    if (v) {
      draggable.constrainVertical = true;
      draggable.setXConstraint(style == Style.WEST ? c2 : c1, style == Style.WEST ? c1
          : c2);
    } else {
      draggable.constrainHorizontal = true;
      draggable.setYConstraint(style == Style.NORTH ? c2 : c1, style == Style.NORTH ? c1
          : c2);
    }
  }

}
