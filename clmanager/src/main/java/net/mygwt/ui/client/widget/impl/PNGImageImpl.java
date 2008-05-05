/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.impl;

import net.mygwt.ui.client.widget.PNGImage;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;


/**
 * PNG support.
 * <p>
 * Implementation adapted from GWT Widget Library
 * (http://gwt-widget.sourceforge.net/).
 * </p>
 * 
 */
public class PNGImageImpl {

  public Element createElement(String url, int width, int height) {
    Element result = DOM.createImg();
    DOM.setElementProperty(result, "src", url);
    DOM.setElementPropertyInt(result, "width", width);
    DOM.setElementPropertyInt(result, "height", height);
    return result;
  }

  public String getUrl(PNGImage image) {
    return DOM.getElementProperty(image.getElement(), "src");
  }

}
