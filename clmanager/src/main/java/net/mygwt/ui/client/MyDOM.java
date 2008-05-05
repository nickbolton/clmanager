/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client;

import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.impl.MyDOMImpl;
import net.mygwt.ui.client.util.Point;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.util.Region;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Provides additional static methods that allow you to manipulate the browser's
 * Document Object Model (DOM).
 * 
 * @see DOM
 */
public class MyDOM {

  private static final MyDOMImpl impl = (MyDOMImpl) GWT.create(MyDOMImpl.class);
  private static boolean isVisibleBox;
  private static int scrollBarHeight = Style.DEFAULT;
  private static int AUTO_ID = 0;
  private static int Z_INDEX = 1000;

  static {
    MyGWT.init();
    init();
    isVisibleBox = isVisibleBoxInternal();
  }
  
  /**
   * Adds the event type to the element's sunk events.
   * 
   * @param elem the element
   * @param event the events to add
   */
  public static void addEventsSunk(Element elem, int event) {
    int bits = DOM.getEventsSunk(elem);
    DOM.sinkEvents(elem, bits | event);
  }
  
  /**
   * Adds a style name to the element.
   * 
   * @param elem the element
   * @param style the style to be added
   */
  public static void addStyleName(Element elem, String style) {
    if (style != null) {
      setStyleName(elem, style, true);
    }
  }
  
  /**
   * Aligns the element with another element relative to the specified anchor
   * points. Two values from the table below should be passed separated by a
   * dash, the first value is used as the element's anchor point, and the second
   * value is used as the target's anchor point.
   * <p>
   * In addition to the anchor points, the position parameter also supports the
   * "?" character. If "?" is passed at the end of the position string, the
   * element will attempt to align as specified, but the position will be
   * adjusted to constrain to the viewport if necessary. Note that the element
   * being aligned might be swapped to align to a different position than that
   * specified in order to enforce the viewport constraints.
   * </p>
   * <dl>
   * <dt>Following are all of the supported anchor positions:</dt>
   * </dl>
   * <code><pre>
   *  Value  Description
   *  -----  -----------------------------
   *  tl     The top left corner (default)
   *  t      The center of the top edge
   *  tr     The top right corner
   *  l      The center of the left edge
   *  c      In the center of the element
   *  r      The center of the right edge
   *  bl     The bottom left corner
   *  b      The center of the bottom edge
   *  br     The bottom right corner
   * </code></pre>
   * 
   * @param elem the element to be aligned
   * @param align the element to align to
   * @param pos the position to align to
   * @param offsets the offsets or <code>null</code>
   */
  public static void alignTo(Element elem, Element align, String pos, int[] offsets) {
    Point p = getAlignToXY(elem, align, pos, offsets);
    setXY(elem, p);
  }
  
  /**
   * Sets multiple style on the given element.
   * 
   * @param elem the element
   * @param styles the styles
   */
  public native static void applyStyles(Element elem, String styles) /*-{
      var re = /\s?([a-z\-]*)\:\s?([^;]*);?/gi;
      var matches;
      while ((matches = re.exec(styles)) != null){
          elem.style[matches[1]] = matches[2];
      }
   }-*/;
  
  /**
   * Centers the element.
   * 
   * @param elem the element
   */
  public static void center(Element elem) {
    Size size = getViewportSize();
    int width = size.width;
    int height = size.height;
    int w = getWidth(elem);
    int h = getHeight(elem);
    int left = (width / 2) - (w / 2);
    int top = (height / 2) - (h / 2);
    Element p = DOM.getParent(elem);
    if (p != null) {
      left += getScrollLeft(p);
      top += getScrollTop(p);
    }
    setLeft(elem, left);
    setTop(elem, top);  
  }
  
  /**
   * Clips overflow on the element.
   */
  public static void clip(Element elem) {
    DOM.setStyleAttribute(elem, "overflow", "hidden");
    DOM.setStyleAttribute(elem, "overflowX", "hidden");
    DOM.setStyleAttribute(elem, "overflowY", "hidden");
  }
  
  /**
   * Creates an element form the given markup.
   * 
   * @param html the markup
   * @return the new element
   */
  public static Element create(String html) {
    Element div = DOM.createDiv();
    MyDOM.setInnerHTML(div, html);
    Element firstChild = DOM.getFirstChild(div);
    // support text node creation
    return (firstChild != null) ? firstChild : div;
  }
  
  /**
   * Disable the element.
   */
  public native static void disable(Element elem) /*-{
      elem.disabled = true;
    }-*/;

  /**
   * Enables and disables the browsers default context menu for the specified
   * element.A circular reference will be created when disabling text selection.
   * Disabling should be cleared when the element is detached. See the
   * <code>Component</code> source for an example.
   * 
   * @param elem the element
   * @param disable <code>true</code> to disable
   */
  public static native void disableContextMenu(Element elem, boolean disable) /*-{
     if (disable) {
       elem.oncontextmenu = function() {  return false};
     } else {
       elem.oncontextmenu = null;
     }
   }-*/;

  /**
   * Enables or disables text selection for the element. A circular reference
   * will be created when disabling text selection. Disabling should be cleared
   * when the element is detached. See the <code>Component</code> source for
   * an example.
   * 
   * @param elem the element
   * @param disable <code>true</code> to disable
   */
  public static void disableTextSelection(Element elem, boolean disable) {
    setStyleName(elem, "my-no-selection", disable);
    disableTextSelectInternal(elem, disable);
  }

  /**
   * Enable the element.
   * 
   * @param elem the element
   */
  public native static void enable(Element elem) /*-{
     elem.disabled = false;
   }-*/;

  /**
   * Removes script and javascript code from the given html markup.
   * 
   * @param html the html
   * @return the updated html
   */
  public native static String escapeHTML(String html) /*-{
    return $wnd.escapeHTML(html);
  }-*/;

  /**
   * Returns the first child with a matching style name.
   * 
   * @param style the style
   * @param elem the element
   * @return the matching element or <code>null</code>
   */
  public native static Element findChild(String style, Element elem) /*-{
    var n = elem.getElementsByTagName("*");
    for (var i=0; i < n.length; i++) {
      var e = n[i];
      if ((" " + e.className + " ").indexOf(" " + style + " ")>-1) {
        return e;
      }
    }
    return null;
  }-*/;

  /**
   * Finds the first parent element with the given style name.
   * 
   * @param style the style name
   * @param elem the start element
   * @param maxDepth the max amount of parent to search
   * @return the matching element
   */
  public static Element findParent(String style, Element elem, int maxDepth) {
    Element p = elem;
    int depth = 0;
    while (p != null && depth < maxDepth) {
      String cls = DOM.getElementProperty(p, "className");
      String[] classes = cls.split(" ");
      for (int i = 0; i < classes.length; i++) {
        if (style.equals(classes[i])) {
          return p;
        }
      }
      depth++;
      p = DOM.getParent(p);
    }
    return null;

  }

  /**
   * Finds the first parent element with an id.
   * 
   * @param elem the start element
   * @param maxDepth the max amount of parent to search
   * @return the matching element
   */
  public static Element findParentWithId(Element elem, int maxDepth) {
    Element p = elem;
    int depth = 0;
    while (p != null && depth < maxDepth) {
      String id = DOM.getElementProperty(p, "id");
      if (id != null && !id.equals("")) {
        return p;
      }
      depth++;
      p = DOM.getParent(p);
    }
    return null;

  }

  /**
   * Returns the x,y coordinates to align this element with another element. See
   * {@link #alignTo} for more info on the supported position values.
   * 
   * @param elem the align to be aligned
   * @param align the element to align to
   * @param pos the position, see alignTo
   * @param offsets the optional offsets
   * @return the the point
   */
  public static Point getAlignToXY(Element elem, Element align, String pos, int[] offsets) {
    if (pos == null) {
      pos = "tl-bl";
    }

    String[] m = pos.split("-");

    String p1 = m[0];
    String p2 = m[1];

    Point a1 = getAnchorXY(elem, p1, true);
    Point a2 = getAnchorXY(align, p2, false);

    if (offsets == null) {
      offsets = new int[] {0, 0};
    }

    int x = a2.x - a1.x + offsets[0];
    int y = a2.y - a1.y + offsets[1];

    if (m.length == 3) {
      int w = getComputedWidth(elem);
      int h = getHeight(elem);

      int dw = Window.getClientWidth() - 5;
      int dh = Window.getClientHeight() - 5;

      Region r = getRegion(elem);

      char p1y = p1.charAt(0), p1x = p1.charAt(p1.length() - 1);
      char p2y = p2.charAt(0), p2x = p2.charAt(p2.length() - 1);
      boolean swapY = ((p1y == 't' && p2y == 'b') || (p1y == 'b' && p2y == 't'));
      boolean swapX = ((p1x == 'r' && p2x == 'l') || (p1x == 'l' && p2x == 'r'));

      int scrollX = getBodyScrollLeft();
      int scrollY = getBodyScrollTop();

      if ((x + w) > dw + scrollX) {
        x = swapX ? r.left - w : dw + scrollX - w;
      }
      if (x < scrollX) {
        x = swapX ? r.right : scrollX;
      }
      if ((y + h) > dh + scrollY) {
        y = swapY ? r.top - h : dh + scrollY - h;
      }
      if (y < scrollY) {
        y = swapY ? r.bottom : scrollY;
      }
    }

    return new Point(x, y);
  }

  /**
   * Returns the x,y coordinates specified by the anchor position on the
   * element.
   * 
   * @param elem the element
   * @param anchor the specified anchor position (defaults to "c"). See
   *            {@link #alignTo} for details on supported anchor positions.
   * @param local <code>true</code> to get the local (element
   *            top/left-relative) anchor position instead of page coordinates
   * @return the position
   */
  public static Point getAnchorXY(Element elem, String anchor, boolean local) {
    int w = getWidth(elem);
    int h = getHeight(elem);

    int x = 0;
    int y = 0;

    if (anchor == null) {
      anchor = "c";
    }

    if (anchor.equalsIgnoreCase("c")) {
      x = (int) Math.round(x * .5);
      y = (int) Math.round(h * .5);
    } else if (anchor.equalsIgnoreCase("t")) {
      x = (int) Math.round(x * .5);
      y = 0;
    } else if (anchor.equalsIgnoreCase("l")) {
      x = 0;
      y = (int) Math.round(h * .5);
    } else if (anchor.equalsIgnoreCase("r")) {
      x = w;
      y = (int) Math.round(h * .5);
    } else if (anchor.equalsIgnoreCase("b")) {
      x = (int) Math.round(x * .5);
      y = h;
    } else if (anchor.equalsIgnoreCase("tl")) {
      x = 0;
      y = 0;
    } else if (anchor.equalsIgnoreCase("bl")) {
      x = 0;
      y = h;
    } else if (anchor.equalsIgnoreCase("br")) {
      x = w;
      y = h;
    } else if (anchor.equalsIgnoreCase("tr")) {
      x = w;
      y = 0;
    }

    if (local) {
      return new Point(x, y);
    }

    Point p = getXY(elem);
    p.x += x;
    p.y += y;
    return p;
  }

  /**
   * Gets any named property from an element, as a string.
   * 
   * @param elem the element whose attribute is to be retrieved
   * @param property the name of the property
   * @return the property value
   */
  public static native String getAnyElementProperty(Element elem, String property)/*-{
     return elem.getAttribute(property);
     }-*/;

  /**
   * Returns the body element.
   * 
   * @return the body
   */
  public static native Element getBody() /*-{
     return $doc.body;
   }-*/;

  /**
   * Returns the body elements horizontal scroll.
   * 
   * @return the scroll amount in pixels
   */
  public static native int getBodyScrollLeft() /*-{
     return $doc.body.scrollLeft;
   }-*/;

  /**
   * Return the body elements vertical scroll.
   * 
   * @return the scroll amount in pixels
   */
  public static native int getBodyScrollTop() /*-{
     return $doc.body.scrollTop;
   }-*/;

  /**
   * Returns the total border width of the specified sides.
   * 
   * @param elem the element
   * @param sides can be any combination of LEFT, RIGHT, TOP, BOTTOM
   * @return the width of the sides passed added together
   */
  public static int getBorderWidth(Element elem, int sides) {
    int width = 0;
    if ((sides & Style.LEFT) != 0) {
      width += getIntStyleAttribute(elem, "borderLeftWidth");
    }
    if ((sides & Style.RIGHT) != 0) {
      width += getIntStyleAttribute(elem, "borderRightWidth");
    }
    if ((sides & Style.TOP) != 0) {
      width += getIntStyleAttribute(elem, "borderTopWidth");
    }
    if ((sides & Style.BOTTOM) != 0) {
      width += getIntStyleAttribute(elem, "borderBottomWidth");
    }
    return width;
  }

  /**
   * Returns the bottom Y coordinate of the element (element Y position +
   * element height).
   * 
   * @param elem the element
   * @param local <code>true</code> to get the local css position instead of
   *            page coordinate
   * @return the bottom value
   */
  public static int getBottom(Element elem, boolean local) {
    if (!local) {
      return getY(elem) + getHeight(elem);
    } else {
      return getTop(elem) + getHeight(elem);
    }
  }

  /**
   * Returns the element's bounds.
   * 
   * @param elem the element
   * @return the elements bounds
   */
  public static Rectangle getBounds(Element elem) {
    return getBounds(elem, false);
  }

  /**
   * Returns the element's bounds.
   * 
   * @param elem the element
   * @param content <code>true</code> to adjust for box model issues
   * @return the elements bounds
   */
  public static Rectangle getBounds(Element elem, boolean content) {
    int x = DOM.getAbsoluteLeft(elem);
    int y = DOM.getAbsoluteTop(elem);
    int width = getWidth(elem);
    int height = getHeight(elem);
    if (content) {
      x += getBorderWidth(elem, Style.LEFT);
      y += getBorderWidth(elem, Style.TOP);
      width -= getDecorationWidth(elem, Style.LEFT | Style.RIGHT);
      height -= getDecorationWidth(elem, Style.TOP | Style.BOTTOM);
    }
    width = Math.max(0, width);
    height = Math.max(0, height);
    return new Rectangle(x, y, width, height);
  }

  /**
   * Returns either the offsetHeight or the height of this element based on it's
   * CSS height.
   * 
   * @param elem the element
   * @return the height
   */
  public static int getComputedHeight(Element elem) {
    int h = getHeight(elem);
    if (h == 0) {
      h = DOM.getIntStyleAttribute(elem, "height");
    }
    return h;
  }

  /**
   * Returns either the offsetWidth or the width of this element based on it's
   * CSS width.
   * 
   * @param elem the element
   * @return the width
   */
  public static int getComputedWidth(Element elem) {
    int w = getWidth(elem);
    if (w == 0) {
      w = DOM.getIntStyleAttribute(elem, "width");
    }
    return w;
  }

  /**
   * Returns the total width of borders and padding for the specified sides.
   * 
   * @param elem the element
   * @param sides can be any combination of LEFT, RIGHT, TOP, BOTTOM
   * @return the width in pixels
   */
  public static int getDecorationWidth(Element elem, int sides) {
    int width = 0;
    width += getBorderWidth(elem, sides);
    width += getPaddingWidth(elem, sides);
    return width;
  }

  /**
   * Returns the document element.
   * 
   * @return the docuemnt
   */
  public static native Element getDocument() /*-{
     return $doc;
   }-*/;

  public static native Element getHead() /*-{
     return $doc.getElementsByTagName('head')[0];
   }-*/;

  /**
   * Returns the element's offset height in pixels. This is the total height of
   * the object, including decorations such as border, margin, and padding.
   * 
   * @param elem the element
   * @return the element's offset height
   */
  public static int getHeight(Element elem) {
    return DOM.getElementPropertyInt(elem, "offsetHeight");
  }

  /**
   * Returns the element's height.
   * 
   * @param elem the element
   * @param contentHeight <code>true</code> to get the height minus borders
   *            and padding
   * @return the element's height
   */
  public static int getHeight(Element elem, boolean contentHeight) {
    int h = DOM.getElementPropertyInt(elem, "offsetHeight");
    if (contentHeight & !isVisibleBox) {
      h -= getDecorationWidth(elem, Style.TOP | Style.BOTTOM);
    }
    return h;
  }

  /**
   * Returns the element's id.
   * 
   * @param elem the element
   * 
   * @return the id
   */
  public static String getId(Element elem) {
    return DOM.getElementProperty(elem, "id");
  }

  /**
   * Returns the element's style attribute value.
   * 
   * @param elem the element
   * @param attr the attribute name
   * @return the attribute value
   */
  public static int getIntStyleAttribute(Element elem, String attr) {
    String s = impl.getStyle(elem, attr);
    try {
      if (s.indexOf("px") != -1) {
        s = s.substring(0, s.indexOf("px"));
      }
      int i = Integer.parseInt(s);
      return i;
    } catch (Exception e) {
    }
    return 0;
  }

  /**
   * Returns the left X coordinate.
   * 
   * @param elem the element
   * @return the left value
   */
  public static int getLeft(Element elem) {
    return DOM.getIntStyleAttribute(elem, "left");
  }

  /**
   * Returns the total padding width of the given sides.
   * 
   * @param elem the element
   * @param sides can be any combination of LEFT, RIGHT, TOP, BOTTOM
   * @return the padding
   */
  public static int getPaddingWidth(Element elem, int sides) {
    int width = 0;
    if ((sides & Style.LEFT) != 0) {
      width += DOM.getIntStyleAttribute(elem, "paddingLeft");
    }
    if ((sides & Style.RIGHT) != 0) {
      width += DOM.getIntStyleAttribute(elem, "paddingRight");
    }
    if ((sides & Style.TOP) != 0) {
      width += DOM.getIntStyleAttribute(elem, "paddingTop");
    }
    if ((sides & Style.BOTTOM) != 0) {
      width += DOM.getIntStyleAttribute(elem, "paddingBottom");
    }
    return width;
  }

  /**
   * Returns the region of the given element. The element must be part of the
   * DOM tree to have a region.
   * 
   * @param elem the element
   * @return a region containing top, left, bottom, right
   */
  public static Region getRegion(Element elem) {
    Rectangle bounds = getBounds(elem, false);
    Region r = new Region();
    r.left = bounds.x;
    r.top = bounds.y;
    r.right = r.left + bounds.width;
    r.bottom = r.top + bounds.height;
    return r;
  }

  /**
   * Returns the right X coordinate of the element (element X position + element
   * width).
   * 
   * @param elem the element
   * @param local <code>true</code> to get the local css position instead of
   *            page coordinate
   * @return the right value
   */
  public static int getRight(Element elem, boolean local) {
    if (!local) {
      return getX(elem) + getWidth(elem);
    } else {
      return getLeft(elem) + getWidth(elem);
    }
  }

  /**
   * Returns the width of the scroll bar.
   * 
   * @return the scroll bar width
   */
  public static int getScrollBarWidth() {
    if (scrollBarHeight == Style.DEFAULT) {
      scrollBarHeight = getScrollBarWidthInternal();
    }
    return scrollBarHeight;
  }

  /**
   * Returns the element's scroll height.
   * 
   * @param elem the element
   * @return the scroll height
   */
  public native static int getScrollHeight(Element elem) /*-{
    return elem.scrollHeight;
  }-*/;

  /**
   * Returns the element's horizontal scroll position.
   * 
   * @param elem the element
   * @return the scroll amount in pixels
   */
  public static int getScrollLeft(Element elem) {
    return DOM.getElementPropertyInt(elem, "scrollLeft");
  }

  /**
   * Returns the element's vertical scroll position.
   * 
   * @param elem the element
   * @return the scroll amount in pixel
   */
  public static int getScrollTop(Element elem) {
    return DOM.getElementPropertyInt(elem, "scrollTop");
  }

  /**
   * Returns the element's scroll width.
   * 
   * @param elem the element
   * @return the scroll width
   */
  public native static int getScrollWidth(Element elem) /*-{
    return elem.scrollWidth;
  }-*/;

  /**
   * Returns the elements size.
   * 
   * @param elem the element
   * @return the size
   */
  public static Size getSize(Element elem) {
    return new Size(getWidth(elem), getHeight(elem));
  }

  public static String getStyleName(Element elem) {
    return DOM.getElementProperty(elem, "className");
  }

  /**
   * Returns the element's sub child.
   * 
   * @param elem the element
   * @param depth the child node depth
   * @return the child element
   */
  public static Element getSubChild(Element elem, int depth) {
    Element child = elem;
    while (depth-- > 0) {
      child = DOM.getChild(child, 0);
    }
    return child;
  }

  /**
   * Returns the top Y coordinate.
   * 
   * @param elem the element
   * @return the top value
   */
  public static int getTop(Element elem) {
    return DOM.getIntStyleAttribute(elem, "top");
  }

  /**
   * Returns an unique id.
   * 
   * @return the id
   */
  public static String getUniqueID() {
    return "my-" + AUTO_ID++;
  }

  /**
   * Returns the the "value" attribute.
   * 
   * @param elem the element
   * @return the value
   */
  public static String getValue(Element elem) {
    return DOM.getElementProperty(elem, "value");
  }

  /**
   * Returns the viewports size.
   * 
   * @return the size
   */
  public static native Size getViewportSize() /*-{
     var vw;
     var vh;
     if (typeof $wnd.innerWidth != 'undefined') {
       vw = $wnd.innerWidth;
       vh = $wnd.innerHeight;
     } else if (typeof $doc.documentElement != 'undefined'
               && typeof $doc.documentElement.clientWidth !=
               'undefined' && $doc.documentElement.clientWidth != 0) {
       vw = $doc.documentElement.clientWidth;
       vh = $doc.documentElement.clientHeight; 
     } else {
       vw = $doc.getElementsByTagName('body')[0].clientWidth;
       vh = $doc.getElementsByTagName('body')[0].clientHeight;
     }
     var size = @net.mygwt.ui.client.util.Size::newInstance(II)(vw, vh);
     return size;
  
   }-*/;

  /**
   * Returns the element's offset width in pixels. This is the total width of
   * the element, including decorations such as border, margin, and padding.
   * 
   * @param elem the element
   * @return the element's offset width
   */
  public static int getWidth(Element elem) {
    return DOM.getElementPropertyInt(elem, "offsetWidth");
  }

  /**
   * Returns the element's width.
   * 
   * @param elem the element
   * @param contentWidth <code>true</code> to get the width minus borders and
   *            padding
   * @return the element's width
   */
  public static int getWidth(Element elem, boolean contentWidth) {
    int w = getWidth(elem);
    if (contentWidth) {
      w -= getDecorationWidth(elem, Style.LEFT | Style.RIGHT);
    }
    return w;
  }

  /**
   * Gets the current X position of the element based on page coordinates.
   * Element must be part of the DOM tree to have page coordinates.
   * 
   * @param elem the element
   * @return the x coordinate
   */
  public static int getX(Element elem) {
    return DOM.getAbsoluteLeft(elem);
  }

  /**
   * Returns the current position of the element based on page coordinates.
   * Element must be part of the DOM tree to have page coordinates.
   * 
   * @param elem the element
   * @return the current location
   */
  public static Point getXY(Element elem) {
    return new Point(getX(elem), getY(elem));
  }

  /**
   * Returns the current Y position of the element based on page coordinates.
   * Element must be part of the DOM tree to have page coordinates.
   * 
   * @param elem the element
   * @return the y coordinate
   */
  public static int getY(Element elem) {
    return DOM.getAbsoluteTop(elem);
  }

  /**
   * Returns the current max z-index.
   * 
   * @return the z-index
   */
  public static int getZIndex() {
    return ++Z_INDEX;
  }

  /**
   * Checks if the specified CSS class exists on this element's DOM node.
   * 
   * @param elem the element
   * @param className the CSS class to check for
   * @return <code>true</code> if the class exists, else <code>false</code>
   */
  public static boolean hasClass(Element elem, String className) {
    String[] classes = getStyleName(elem).split(" ");
    for (int i = 0; i < classes.length; i++) {
      if (className.equals(classes[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * Inserts this element after the passed element in the DOM.
   * 
   * @param elem the element
   * @param after the element to insert after
   */
  public native static void insertAfter(Element elem, Element after) /*-{
      after.parentNode.insertBefore(elem, after.nextSibling);
   }-*/;

  /**
   * Inserts the element before the passed element in the DOM.
   * 
   * @param elem the element to be inserted
   * @param before the element to insert before
   */
  public native static void insertBefore(Element elem, Element before) /*-{
     before.parentNode.insertBefore(elem, before);
   }-*/;

  /**
   * Returns <code>true</code> if the horizontal scroll bar is present
   * 
   * @param container the container element
   * @return the vertical scroll bar state
   */
  public static boolean isHScrollBarShowing(Element container) {
    int orig = MyDOM.getScrollTop(container);
    if (orig > 0) {
      return true;
    }
    setScrollLeft(container, 10);
    if (MyDOM.getScrollLeft(container) > 0) {
      setScrollLeft(container, orig);
      return true;
    }
    setScrollLeft(container, orig);
    return false;
  }

  /**
   * Returns <code>true</code> if the element is visible using the css
   * 'visibiliy' attribute.
   * 
   * @param elem the element
   * @return the visible state
   */
  public static boolean isVisibility(Element elem) {
    return !DOM.getStyleAttribute(elem, "visibility").equals("hidden");
  }

  /**
   * Checks whether the element is currently visible using both visibility and
   * display properties.
   * 
   * @return <code>true</code> if the element is currently visible, else
   *         <code>false</code>
   */
  public static boolean isVisible(Element elem) {
    if (DOM.getStyleAttribute(elem, "visibility").equals("hidden")) {
      return false;
    } else if (DOM.getStyleAttribute(elem, "display").equals("none")) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Determines if the visible box or content area is set when sizing elements.
   * 
   * @return <code>true</code> for visible box sizing
   */
  public static boolean isVisibleBox() {
    return isVisibleBox;
  }

  /**
   * Returns <code>true</code> if the vertical scroll bar is present
   * 
   * @param container the container element
   * @return the vertical scroll bar state
   */
  public static boolean isVScrollBarShowing(Element container) {
    int orig = MyDOM.getScrollLeft(container);
    if (orig > 0) {
      return true;
    }
    setScrollLeft(container, 10);
    if (MyDOM.getScrollLeft(container) > 0) {
      setScrollLeft(container, orig);
      return true;
    }
    setScrollLeft(container, orig);
    return false;
  }

  /**
   * Makes an element positionable.
   * 
   * @param elem the element
   */
  public static void makePositionable(Element elem) {
    String position = DOM.getStyleAttribute(elem, "position");
    if (position.equals("") || position.equals("static")) {
      DOM.setStyleAttribute(elem, "position", "relative");
    }
  }

  /**
   * Makes an element positionable.
   * 
   * @param elem the element
   * @param absolute <code>true</code> to position absolute
   */
  public static void makePositionable(Element elem, boolean absolute) {
    if (absolute) {
      DOM.setStyleAttribute(elem, "position", "absolute");
    } else {
      makePositionable(elem);
    }
  }

  /**
   * Returns the offsets between two elements. Both element must be part of the
   * DOM tree and not have display:none to have page coordinates.
   * 
   * @param elem the element
   * @param to the to element
   * @return the xy page offsets
   */
  public static Point offsetsTo(Element elem, Element to) {
    Point o = getXY(elem);
    Point e = getXY(to);
    return new Point(o.x - e.x, o.y - e.y);
  }

  /**
   * Removes all the elements children.
   * 
   * @param elem the element
   */
  public static void removeChildren(Element elem) {
    while (DOM.getChildCount(elem) > 0) {
      DOM.removeChild(elem, DOM.getChild(elem, 0));
    }
  }
  
  /**
   * Removes the element from it's parent.
   * 
   * @param elem the element
   */
  public static void removeFromParent(Element elem) {
    Element p = DOM.getParent(elem);
    if (p != null) {
      DOM.removeChild(p, elem);
    }
  }

  /**
   * Removes a style name.
   * 
   * @param elem the element
   * @param style the style to be removed
   */
  public static void removeStyleName(Element elem, String style) {
    if (style != null) {
      setStyleName(elem, style, false);
    }
  }

  /**
   * Scrolls the element into view.
   * 
   * @param elem the element to scroll
   * @param container the container element
   * @param hscroll <code>false</code> to disable horizontal scrolling.
   */
  public static native void scrollIntoView(Element elem, Element container,
      boolean hscroll) /*-{
      var c = container || $doc.body;
      var o = @net.mygwt.ui.client.MyDOM::offsetsTo(Lcom/google/gwt/user/client/Element;Lcom/google/gwt/user/client/Element;)(elem, container);
      var l = o.@net.mygwt.ui.client.util.Point::x;
      var t = o.@net.mygwt.ui.client.util.Point::y;
      l = l + c.scrollLeft;
      t = t + c.scrollTop;
      var b = t + elem.offsetHeight;
      var r = l + elem.offsetWidth;
      
      var ch = c.clientHeight;
      var ct = parseInt(c.scrollTop, 10);
      var cl = parseInt(c.scrollLeft, 10);
      var cb = ct + ch;
      var cr = cl + c.clientWidth;
      
      if (t < ct){
        c.scrollTop = t;
      }else if(b > cb){
        c.scrollTop = b-ch;
      }
      c.scrollTop = c.scrollTop; 
      
      if(hscroll !== false){
        if(l < cl){
          c.scrollLeft = l;
        } else if(r > cr){
          c.scrollLeft = r-c.clientWidth;
        }
        c.scrollLeft = c.scrollLeft;
      }
    }-*/;

  /**
   * Adds or removes a border. The style name 'my-border' is added to the widget
   * to display a border.
   * 
   * @param elem the element
   * @param show the show state
   */
  public static void setBorders(Element elem, boolean show) {
    if (show) {
      addStyleName(elem, "my-border");
    } else {
      setStyleAttribute(elem, "border", "none");
    }
  }

  public static void setBounds(Element elem, int x, int y, int width, int height) {
    setBounds(elem, x, y, width, height, false);
  }

  /**
   * Sets the widgets size and location to the rectangular area specified by the
   * arguments.
   * 
   * @param elem the element
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   * @param adjust <code>true</code> to adjust for box model issues
   */
  public static void setBounds(Element elem, int x, int y, int width, int height,
      boolean adjust) {
    Rectangle rect = new Rectangle(x, y, width, height);
    setBounds(elem, rect, adjust);
  }

  /**
   * Sets the elements size and location to the specified rectangle.
   * 
   * @param elem the element
   * @param rect the rectangle
   */
  public static void setBounds(Element elem, Rectangle rect) {
    setLocation(elem, rect.x, rect.y);
    setSize(elem, rect.width, rect.height);
  }

  /**
   * Sets the widgets size and location to the specified rectangle.
   * 
   * @param elem the element
   * @param adjust <code>true</code> to adjust for box model issues
   * @param rect the rectangle
   */
  public static void setBounds(Element elem, Rectangle rect, boolean adjust) {
    setLocation(elem, rect.x, rect.y);
    setSize(elem, rect.width, rect.height, adjust);
  }

  /**
   * Sets the elements style attribute.
   * 
   * @param elem the element
   * @param style the style
   * @param value the new double value
   */
  public static void setDoubleStyleAttribute(Element elem, String style, double value) {
    setStyleAttribute(elem, style, "" + value);
  }

  /**
   * Sets the focus state of the element.
   * 
   * @param focused the new focus state
   */
  public native static void setFocus(Element elem, boolean focused) /*-{
     try {
       if (focused)
         elem.focus();
       else
         elem.blur();
     } 
     catch(err) {
     }
   }-*/;

  /**
   * Sets the element's height.
   * 
   * @param elem the element
   * @param height the new height
   */
  public static void setHeight(Element elem, int height) {
    setHeight(elem, height, false);
  }

  /**
   * Sets the element's height.
   * 
   * @param elem the element
   * @param height the new height
   * @param adjust <code>true</code> to adjust for box model issue
   */
  public static void setHeight(Element elem, int height, boolean adjust) {
    if (height == Style.DEFAULT || height < 1) {
      return;
    }
    if (adjust && !isVisibleBox) {
      height -= getDecorationWidth(elem, Style.TOP | Style.BOTTOM);
    }
    DOM.setStyleAttribute(elem, "height", height + "px");
  }

  /**
   * Sets the HTML contained within an element. The specified html will be
   * escaped for XSS exploits if {@link MyGWT#enableEscapeHTML(boolean)} has
   * been enabled.
   * 
   * @param elem the element
   * @param html the html
   */
  public native static void setInnerHTML(Element elem, String html) /*-{
    if (!html) {
      html = '';
    }
    if ($wnd.escapeFlag === true) {
      html = $wnd.escapeHTML(html);
    }
    elem.innerHTML = html;
  }-*/;

  /**
   * Sets the HTML contained within an element. The specified html will be
   * escaped for XSS exploits if {@link MyGWT#enableEscapeHTML(boolean)} has
   * been enabled.
   * 
   * @param elem the element
   * @param html the html
   * @param escape <code>true</code> to escape html for XSS exploits.
   */
  public native static void setInnerHTML(Element elem, String html, boolean escape) /*-{
    if (!html) {
      html = '';
    }
    html = $wnd.escapeHTML(html);
    elem.innerHTML = html;
  }-*/;
  
  /**
   * Sets the element's left position directly using CSS style (instead of
   * {@link #setX}).
   * 
   * @param elem the element
   * @param left the left value
   */
  public static void setLeft(Element elem, int left) {
    DOM.setStyleAttribute(elem, "left", left + "px");
  }

  /**
   * Sets the element's left position directly using CSS style.
   * 
   * @param elem the element
   * @param left the left value
   * @param top the top value
   */
  public static void setLeftTop(Element elem, int left, int top) {
    setLeft(elem, left);
    setTop(elem, top);
  }

  /**
   * Sets the element position using page coordinates.
   * 
   * @param elem the element
   * @param x the x coordinate value
   * @param y the y coordinate value
   */
  public static void setLocation(Element elem, int x, int y) {
    setX(elem, x);
    setY(elem, y);
  }

  /**
   * Sets the element's scroll left value.
   * 
   * @param elem the element
   * @param left the value
   */
  public static void setScrollLeft(Element elem, int left) {
    DOM.setElementPropertyInt(elem, "scrollLeft", left);
  }

  /**
   * Sets the element's scroll top value.
   * 
   * @param elem the element
   * @param top the value
   */
  public static void setScrollTop(Element elem, int top) {
    DOM.setElementPropertyInt(elem, "scrollTop", top);
  }

  /**
   * Set the size of the element. Values equal to My.DEFAULT are ignored.
   * 
   * @param elem the element
   * @param width the new width
   * @param height the new height
   */
  public static void setSize(Element elem, int width, int height) {
    setSize(elem, width, height, false);
  }

  /**
   * Set the size of the element. Values equal to My.DEFAULT are ignored.
   * 
   * @param elem the element
   * @param width the new width
   * @param height the new height
   * @param adjust <code>true</code> to adjust for box model issues
   */
  public static void setSize(Element elem, int width, int height, boolean adjust) {
    if (width != Style.DEFAULT) {
      setWidth(elem, width, adjust);
    }
    if (height != Style.DEFAULT) {
      setHeight(elem, height, adjust);
    }
  }

  /**
   * Sets the element's style attribute.
   * 
   * @param elem the element
   * @param attr the attribute
   * @param value the value
   */
  public static void setStyleAttribute(Element elem, String attr, String value) {
    impl.setStyle(elem, attr, value);
  }

  /**
   * Sets the element's style name.
   * 
   * @param elem the element
   * @param style the style name
   */
  public static void setStyleName(Element elem, String style) {
    DOM.setElementProperty(elem, "className", style);
  }

  /**
   * This convenience method adds or removes a style name for a given element.
   * This method is typically used to add and remove secondary style names, but
   * it can be used to remove primary stylenames as well, but that is not
   * recommended.
   * 
   * @param elem the element whose style is to be modified
   * @param style the secondary style name to be added or removed
   * @param add <code>true</code> to add the given style, <code>false</code>
   *            to remove it
   */
  public static void setStyleName(Element elem, String style, boolean add) {
    if (style == null) return;
    style = style.trim();
    if (style.length() == 0) {
      throw new IllegalArgumentException("EMPTY STRING");
    }

    // Get the current style string.
    String oldStyle = DOM.getElementProperty(elem, "className");
    int idx = oldStyle.indexOf(style);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || oldStyle.charAt(idx - 1) == ' ') {
        int last = idx + style.length();
        int lastPos = oldStyle.length();
        if ((last == lastPos) || ((last < lastPos) && (oldStyle.charAt(last) == ' '))) {
          break;
        }
      }
      idx = oldStyle.indexOf(style, idx + 1);
    }

    if (add) {
      // Only add the style if it's not already present.
      if (idx == -1) {
        if (oldStyle.length() > 0) {
          oldStyle += " ";
        }
        DOM.setElementProperty(elem, "className", oldStyle + style);
      }
    } else {
      // Don't try to remove the style if it's not there.
      if (idx != -1) {
        // Get the leading and trailing parts, without the removed name.
        String begin = oldStyle.substring(0, idx).trim();
        String end = oldStyle.substring(idx + style.length()).trim();

        // Some contortions to make sure we don't leave extra spaces.
        String newClassName;
        if (begin.length() == 0) {
          newClassName = end;
        } else if (end.length() == 0) {
          newClassName = begin;
        } else {
          newClassName = begin + " " + end;
        }

        DOM.setElementProperty(elem, "className", newClassName);
      }
    }
  }

  /**
   * Sets the element's top position directly using CSS style (instead of
   * {@link #setY}).
   * 
   * @param elem the element
   * @param top the top value
   */
  public static void setTop(Element elem, int top) {
    DOM.setStyleAttribute(elem, "top", top + "px");
  }

  /**
   * Sets the element's value property.
   * 
   * @param elem the element
   * @param value the value
   */
  public static void setValue(Element elem, String value) {
    DOM.setElementAttribute(elem, "value", value);
  }

  /**
   * Sets the elements css 'visibility' property. Behavior is different than
   * using the 'display' property.
   * 
   * @param elem the element
   * @param visible <code>true</code> to show, <code>false</code> to hide
   */
  public static void setVisibility(Element elem, boolean visible) {
    String value = visible ? "" : "hidden";
    DOM.setStyleAttribute(elem, "visibility", value);
  }

  /**
   * Sets the element's css 'display' property.
   * 
   * @param elem the element
   * @param visible <code>true</code> to show, <code>false</code> to hide
   */
  public static void setVisible(Element elem, boolean visible) {
    String value = visible ? "" : "none";
    DOM.setStyleAttribute(elem, "display", value);
  }

  /**
   * Sets the element's width.
   * 
   * @param elem the element
   * @param width the new width value
   */
  public static void setWidth(Element elem, int width) {
    setWidth(elem, width, false);
  }

  /**
   * Sets the element's width.
   * 
   * @param elem the element
   * @param adjust <code>true</code> to adjust for box model issues
   * @param width the new width value
   */
  public static void setWidth(Element elem, int width, boolean adjust) {
    if (width == Style.DEFAULT || width < 1) {
      return;
    }
    if (adjust && !isVisibleBox) {
      width -= getDecorationWidth(elem, Style.LEFT | Style.RIGHT);
    }
    DOM.setStyleAttribute(elem, "width", width + "px");
  }

  /**
   * Set the x position of the element.
   * 
   * @param elem the element
   * @param x the x coordinate
   */
  public static void setX(Element elem, int x) {
    makePositionable(elem);
    int l = DOM.getIntStyleAttribute(elem, "left");
    x = x - DOM.getAbsoluteLeft(elem) + l;
    DOM.setStyleAttribute(elem, "left", x + "px");
  }

  /**
   * Sets the position of the element.
   * 
   * @param elem the element
   * @param point the position
   */
  public static void setXY(Element elem, Point point) {
    setX(elem, point.x);
    setY(elem, point.y);
  }

  /**
   * Set the y position of an html element in page coordinates, regardless of
   * how the element is positioned. The element must be part of the DOM tree to
   * have page coordinates.
   * 
   * @param elem the element
   * @param y the y coordinate
   */
  public static void setY(Element elem, int y) {
    makePositionable(elem);
    int t = DOM.getIntStyleAttribute(elem, "top");
    y = y - DOM.getAbsoluteTop(elem) + t;
    DOM.setStyleAttribute(elem, "top", y + "px");
  }

  /**
   * Sets the element's z-index.
   * 
   * @param elem the element
   * @param index the z-index
   */
  public static void setZIndex(Element elem, int index) {
    DOM.setIntStyleAttribute(elem, "zIndex", index);
  }

  /**
   * Unwraps the child element.
   * 
   * @param wrapper the wrapper element
   * @param child the child element
   * @param bounds the size
   */
  public static void unwrap(Element wrapper, Element child, Rectangle bounds) {
    MyDOM.setTop(child, bounds.y);
    MyDOM.setLeft(child, bounds.x);

    Element p = DOM.getParent(wrapper);
    DOM.removeChild(p, wrapper);
    DOM.appendChild(p, child);
  }


  /**
   * Unwraps the child element.
   * 
   * @param wrapper the wrapper element
   * @param child the child element
   * @param bounds the size
   * @param index the insert location
   */
  public static void unwrap(Element wrapper, Element child, Rectangle bounds, int index) {
    MyDOM.setTop(child, bounds.y);
    MyDOM.setLeft(child, bounds.x);

    Element p = DOM.getParent(wrapper);
    DOM.removeChild(p, wrapper);
    DOM.insertChild(p, child, index);
  }
  
  /**
   * Wraps the element with the specified wrapper. The wrapper will have the
   * same size and position of the element. The original bounds can be used to
   * 'unwrap' the element.
   * 
   * @param wrapper the wrapper element
   * @return the original bounds
   */
  public static Rectangle wrap(Element elem, Element wrapper) {
    MyDOM.setVisible(wrapper, false);

    String pos = DOM.getStyleAttribute(elem, "position");
    MyDOM.setStyleAttribute(wrapper, "position", pos);

    int l = MyDOM.getLeft(elem);
    int t = MyDOM.getTop(elem);

    MyDOM.setLeft(elem, 5000);
    MyDOM.setVisible(elem, true);

    int h = getComputedHeight(elem);
    int w = getComputedWidth(elem);

    MyDOM.setLeft(elem, 1);
    MyDOM.setStyleAttribute(elem, "overflow", "hidden");
    MyDOM.setVisible(elem, false);

    MyDOM.insertBefore(wrapper, elem);
    DOM.appendChild(wrapper, elem);

    MyDOM.setStyleAttribute(wrapper, "overflow", "hidden");

    MyDOM.setLeft(wrapper, l);
    MyDOM.setTop(wrapper, t);

    MyDOM.setTop(elem, 0);
    MyDOM.setLeft(elem, 0);

    return new Rectangle(l, t, w, h);

  }

  /**
   * This function can be replaced to plugin an alternate function to handle XSS exploits.
   */
  native static void init() /*-{
    $wnd.escapeHTML = function(html) {
      html = html.replace(/[\"\'][\s]*javascript:(.*)[\"\']/g, "\"\"");
      html = html.replace(/<script(.*)/g, "");
      html = html.replace(/eval\((.*)\)/g, "");
      return html;
    }
  }-*/;

  private native static void disableTextSelectInternal(Element e, boolean disable)/*-{
      if (disable) {
        e.ondrag = function () { return false; };
        e.onselectstart = function () { return false; };
      } else {
        e.ondrag = null;
        e.onselectstart = null;
      }
    }-*/;

  private static int getScrollBarWidthInternal() {
    WidgetContainer wc = new WidgetContainer();
    MyDOM.setVisibility(wc.getElement(), false);
    wc.setScrollEnabled(true);
    wc.setSize(300, 300);

    HTML html = new HTML("sdff");
    html.setHeight("284");
    html.setWidth("500");

    wc.add(html);

    RootPanel.get().add(wc);

    int height = 17;

    for (int i = 280; i < 300; i++) {
      html.setHeight("" + i);
      wc.setVScrollPosition(20);
      if (wc.getVScrollPosition() == 1) {
        height = 300 - i + 1;
      }
    }
    RootPanel.get().remove(wc);
    return height;
  }

  private static native boolean isVisibleBoxInternal() /*-{
       if (!$wnd.isVisibleBox) {
         var d = $wnd.document;
         var test = d.createElement('div');
         d.body.appendChild(test);
         test.style.position = "absolute";
         test.style.border = "2px solid";
         test.style.height = "50";
         $wnd.isVisibleValue = test.offsetHeight == 50 ? true : false;
         $wnd.isVisibleBox = true;
         d.body.removeChild(test);
       }
      return $wnd.isVisibleValue;
    }-*/;

  private MyDOM() {

  }

}