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
public class PNGImageImplIE6 extends PNGImageImpl {
  private String url;
  private boolean isPng;

  public Element createElement(String url, int width, int height) {
    this.url = url;

    if (url.endsWith(".png") || url.endsWith(".PNG")) {
      isPng = true;
    } else {
      isPng = false;
    }

    if (isPng) {
      Element div = DOM.createDiv();
      DOM.setInnerHTML(
          div,
          "<div id='d' style=\"position: relative;width:"
              + width
              + "px;height:"
              + height
              + "px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
              + url + "', sizingMethod='scale')\"></div>");

      return DOM.getFirstChild(div);
    } else {
      return super.createElement(url, width, height);
    }
  }

  public String getUrl(PNGImage image) {
    if (isPng) {
      return url;
    } else {
      return super.getUrl(image);
    }
  }

}
