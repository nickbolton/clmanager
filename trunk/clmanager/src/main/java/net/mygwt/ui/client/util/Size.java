/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

/**
 * Instances of this class represent a rectangle's size.
 */
public class Size {

  public static Size newInstance(int x, int y) {
    return new Size(x, y);
  }
  
  /**
   * The width.
   */
  public int width;

  /**
   * The height.
   */
  public int height;

  /**
   * Creates a new size instance.
   * 
   * @param width the width
   * @param height the height
   */
  public Size(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public String toString() {
    return "height: " + height + ", width: " + width;
  }

}
