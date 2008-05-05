/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import net.mygwt.ui.client.MyDOM;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides precise pixel measurements for blocks of text so that you can
 * determine exactly how high and wide, in pixels, a given block of text will
 * be.
 */
public class TextMetrics {

  private static TextMetrics instance;

  static {
    instance = new TextMetrics();
  }

  /**
   * Returns the singleton instance.
   * 
   * @return the text metrics instance
   */
  public static TextMetrics get() {
    return instance;
  }

  private Element elem;

  private TextMetrics() {
    elem = DOM.createDiv();
    DOM.appendChild(MyDOM.getBody(), elem);
    DOM.setStyleAttribute(elem, "position", "absolute");
    MyDOM.setLeftTop(elem, -10000, -1000);
    MyDOM.setVisibility(elem, false);
  }

  /**
   * Binds this TextMetrics instance to an element from which to copy existing
   * CSS styles that can affect the size of the rendered text.
   * 
   * @param el the element
   */
  public void bind(Element el) {
    DOM.setStyleAttribute(elem, "fontSize", DOM.getStyleAttribute(el, "fontSize"));
    DOM.setStyleAttribute(elem, "fontStyle", DOM.getStyleAttribute(el, "fontStyle"));
    DOM.setStyleAttribute(elem, "fontWeight", DOM.getStyleAttribute(el, "fontWeight"));
  }

  /**
   * Returns the measured height of the specified text. For multiline text, be
   * sure to call {@link #setFixedWidth} if necessary.
   * 
   * @param text the text to be measured
   * @return the height in pixels
   */
  public int getHeight(String text) {
    return getSize(text).height;
  }

  /**
   * Returns the size of the specified text based on the internal element's
   * style and width properties.
   * 
   * @param text the text to measure
   * @return the size
   */
  public Size getSize(String text) {
    MyDOM.setInnerHTML(elem, text);
    Size size = MyDOM.getSize(elem);
    MyDOM.setInnerHTML(elem, "");
    return size;
  }

  /**
   * Returns the measured width of the specified text.
   * 
   * @param text the text to measure
   * @return the width in pixels
   */
  public int getWidth(String text) {
    DOM.setStyleAttribute(elem, "width", "auto");
    return getSize(text).width;
  }

  /**
   * Sets a fixed width on the internal measurement element. If the text will be
   * multiline, you have to set a fixed width in order to accurately measure the
   * text height.
   * 
   * @param width the width to set on the element
   */
  public void setFixedWidth(int width) {
    DOM.setIntStyleAttribute(elem, "width", width);
  }

}
