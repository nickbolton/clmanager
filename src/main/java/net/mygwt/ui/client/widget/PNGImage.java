/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.widget.impl.PNGImageImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

/**
 * Image widget that overcomes PNG browser incompatabilities.
 * 
 * <p>
 * Implementation adapted from GWT Widget Library
 * (http://gwt-widget.sourceforge.net/).
 * </p>
 */
public class PNGImage extends Image {

  private PNGImageImpl impl;

  /**
   * Creates a new image instance.
   * 
   * @param url the url of the image
   */
  public PNGImage(String url) {
    this(url, 1, 1);
  }

  /**
   * Creates a new image instance.
   * 
   * @param url the url of the image
   * @param width the width
   * @param height the height
   */
  public PNGImage(String url, int width, int height) {
    impl = (PNGImageImpl) GWT.create(PNGImageImpl.class);

    setElement(impl.createElement(url, width, height));
  }

  /**
   * Should not be used. Throws a RuntimeException.
   */
  public void setUrl(String url) {
    throw new RuntimeException("Not allowed to set url for a PNG image");
  }
}
