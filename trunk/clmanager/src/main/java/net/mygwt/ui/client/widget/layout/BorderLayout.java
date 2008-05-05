/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.Layout;
import net.mygwt.ui.client.widget.SplitBar;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A border layout lays out it children, arranging and resizing its widgets to
 * fit in five regions: north, south, east, west, and center. Each region may
 * contain no more than one widget, and is identified by a corresponding
 * constant: NORTH, SOUTH, EAST, WEST, and CENTER.
 * 
 * <p>
 * Note: Only regions containing a component can be resized.
 * </p>
 */
public class BorderLayout extends Layout {

  protected Map splitBars;
  
  private int spacing = 4;
  private int margin = 4;
  private int minimumSize = 100;
  private Widget north, south;
  private Widget west, east, center;
  private Element ct;

  /**
   * Creates a new border layout.
   */
  public BorderLayout() {
    splitBars = new HashMap();
  }

  /**
   * Returns the margin.
   * 
   * @return the margin in pixels
   */
  public int getMargin() {
    return margin;
  }

  /**
   * Returns the minimum size.
   * 
   * @return the minimum size
   */
  public int getMinimumSize() {
    return minimumSize;
  }

  /**
   * Returns the spacing.
   * 
   * @return the spacing in pixels
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * Sets the number of pixels of indentation that will be placed along the
   * sides of the panel. Default value is 4.
   * 
   * @param margin the margin
   */
  public void setMargin(int margin) {
    this.margin = margin;
  }

  /**
   * Sets the minimum size of a region when resizing. Default value is 100.
   * 
   * @param minimumSize the new minimumSize
   */
  public void setMinimumSize(int minimumSize) {
    this.minimumSize = minimumSize;
  }

  /**
   * Sets the amount of spacing between regions. Default value is 4.
   * 
   * @param spacing the spacing in pixels
   */
  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

  protected SplitBar createSplitBar(int region, Component component) {
    return new SplitBar(region, component);
  }

  protected void onLayout(WidgetContainer c, Element target) {
    super.onLayout(c, target);

    ct = c.getLayoutTarget();
    MyDOM.makePositionable(ct);

    north = getRegionWidget(Style.NORTH);
    south = getRegionWidget(Style.SOUTH);
    west = getRegionWidget(Style.WEST);
    east = getRegionWidget(Style.EAST);
    center = getRegionWidget(Style.CENTER);

    if (center == null) {
      throw new RuntimeException("BorderLayout requires a widget in the center region.");
    }

    Rectangle rect = MyDOM.getBounds(ct, true);

    if (MyGWT.isSafari) {
      rect.width -= 1;
      rect.height -= 1;
    }

    int height = rect.height;
    int width = rect.width;

    int t = rect.y + margin;
    int b = t + height - (2 * margin);
    int l = rect.x + margin;
    int r = l + width - (2 * margin);

    // NORTH
    if (north != null) {
      BorderLayoutData northData = (BorderLayoutData) c.getLayoutData(north);

      if (northData.resizeable && north instanceof Component) {
        initSplitBar(Style.SOUTH, (Component) north, northData);
      } else {
        removeSplitBar(Style.SOUTH);
      }

      if (northData.exclude) {
        north.setVisible(false);
        setSplitBarEnabled(Style.SOUTH, false);
      } else {
        float northHeight = northData.size;
        if (northHeight <= 1) {
          northHeight = height * northHeight;
        }
        north.setVisible(true);
        setSplitBarEnabled(Style.EAST, false);
        setBounds(north, l, t, r - l, (int) northHeight);
        t += northHeight + spacing;
      }

    }

    // SOUTH
    if (south != null) {
      BorderLayoutData southData = (BorderLayoutData) c.getLayoutData(south);
      if (southData.resizeable && south instanceof Component) {
        initSplitBar(Style.NORTH, (Component) south, southData);
      } else {
        removeSplitBar(Style.NORTH);
      }

      if (southData.exclude) {
        south.setVisible(false);
        setSplitBarEnabled(Style.NORTH, false);
      } else {
        float southHeight = southData.size;
        if (southHeight <= 1) {
          southHeight = height * southHeight;
        }
        south.setVisible(true);
        setBounds(south, l, (int) (b - southHeight), r - l, (int) southHeight);
        b -= southHeight + spacing;
      }

    }

    // EAST
    if (east != null) {
      BorderLayoutData eastData = (BorderLayoutData) c.getLayoutData(east);
      if (eastData.resizeable && east instanceof Component) {
        initSplitBar(Style.WEST, (Component) east, eastData);
      } else {
        removeSplitBar(Style.WEST);
      }

      if (eastData.exclude) {
        east.setVisible(false);
        setSplitBarEnabled(Style.WEST, false);
      } else {
        float eastWidth = eastData.size;
        if (eastWidth <= 1) {
          eastWidth = width * eastWidth;
        }
        east.setVisible(true);
        setSplitBarEnabled(Style.EAST, true);
        setBounds(east, (int) (r - eastWidth), t, (int) eastWidth, b - t);
        r -= eastWidth + spacing;
      }

    }

    // WEST
    if (west != null) {
      final BorderLayoutData westData = (BorderLayoutData) c.getLayoutData(west);
      if (westData.resizeable && west instanceof Component) {
        initSplitBar(Style.EAST, (Component) west, westData);
      } else {
        removeSplitBar(Style.EAST);
      }

      if (westData.exclude) {
        west.setVisible(false);
        setSplitBarEnabled(Style.EAST, false);
      } else {
        float westWidth = westData.size;
        if (westWidth <= 1) {
          westWidth = width * westWidth;
        }
        west.setVisible(true);
        setBounds(west, l, t, (int) westWidth, b - t);
        l += westWidth + spacing;
      }
    }

    // CENTER
    if (center != null) {
      setBounds(center, l, t, r - l, b - t);
    }

  }

  private Widget getRegionWidget(int region) {
    for (int i = 0; i < container.getWidgetCount(); i++) {
      MyDOM.makePositionable(container.getWidget(i).getElement(), true);
    }
    for (int i = 0; i < container.getWidgetCount(); i++) {
      Widget w = container.getWidget(i);
      if (container.getLayoutData(w) != null
          && container.getLayoutData(w) instanceof BorderLayoutData) {
        BorderLayoutData data = (BorderLayoutData) container.getLayoutData(w);
        if (data.region == region) {
          return w;
        }
      }
    }

    return null;
  }

  private void initSplitBar(final int region, Component comp, final BorderLayoutData data) {
    SplitBar bar = (SplitBar) splitBars.get(new Integer(region));
    if (bar == null || bar.getResizeWidget() != comp) {
      bar = createSplitBar(region, comp);
      final SplitBar fBar = bar;
      Listener splitBarListener = new Listener() {

        public void handleEvent(BaseEvent be) {
          switch (be.type) {
            case Events.DragStart:
              switch (region) {
                case Style.WEST: {
                  int min = Math.max(minimumSize, data.minimumSize);
                  int max = east.getOffsetWidth() + center.getOffsetWidth() - minimumSize;
                  if (data.maximumSize > 0) {
                    max = Math.min(max, data.maximumSize);
                  }
                  fBar.setMinSize(min);
                  fBar.setMaxSize(max);
                  break;
                }
                case Style.EAST: {
                  int min = Math.max(minimumSize, data.minimumSize);
                  int max = west.getOffsetWidth() + center.getOffsetWidth() - minimumSize;
                  max = Math.min(data.maximumSize, max);
                  fBar.setMinSize(min);
                  fBar.setMaxSize(max);
                  break;
                }
                case Style.NORTH:
                  int max = south.getOffsetHeight() + center.getOffsetHeight()
                      - minimumSize;
                  max = Math.min(max, data.maximumSize);
                  fBar.setMaxSize(max);
                  break;
                case Style.SOUTH:
                  // TODO
                  break;
              }
              break;
          }

        }

      };

      bar.addListener(Events.DragStart, splitBarListener);
      bar.addListener(Events.DragEnd, splitBarListener);
      bar.setMinSize(data.minimumSize);
      bar.setMaxSize(data.maximumSize == 0 ? bar.getMaxSize() : data.maximumSize);
      bar.setBarWidth(6);
      bar.setAutoSize(false);
      bar.addListener(Events.Resize, new Listener() {
        public void handleEvent(BaseEvent be) {
          if (be.size < 1) {
            return;
          }
          if (data.size < 1.1) {
            float size = 0;
            if (region == Style.SOUTH || region == Style.NORTH) {
              size = MyDOM.getHeight(ct);
            } else {
              size = MyDOM.getWidth(ct);
            }
            data.size = be.size / size;
          } else {
            data.size = be.size;
          }
          layout(container);
        }
      });
      splitBars.put(new Integer(region), bar);
    }
  }

  private void removeSplitBar(int region) {
    splitBars.put(new Integer(region), null);
  }

  private void setSplitBarEnabled(int region, boolean enabled) {
    SplitBar bar = (SplitBar) splitBars.get(new Integer(region));
    if (bar != null) {

    }
  }

}
