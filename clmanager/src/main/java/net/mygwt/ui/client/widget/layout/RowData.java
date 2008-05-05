/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.Style;

/**
 * Layout data for <code>RowLayouts</code>. A layout will not adjust the
 * height or width if its value is DEFAULT.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FILL_BOTH, FILL_HORIZONTAL, FILL_VERTICAL</dd>
 * </dl>
 */
public class RowData {

  /**
   * Fills both horizontally and vertically.
   */
  public static final int FILL_BOTH = 0;

  /**
   * Fills horizontally.
   */
  public static final int FILL_HORIZONTAL = 1;

  /**
   * Fills vertically.
   */
  public static final int FILL_VERTICAL = 2;

  
  float height = Style.DEFAULT;
  float width = Style.DEFAULT;
  boolean fillWidth;
  boolean fillHeight;
  float calcWidth, calcHeight;

  /**
   * Creates a new row data.
   */
  public RowData() {

  }

  /**
   * Creates a new row data instance.
   * 
   * @param height the height in pixels
   * @param width the width in pixels
   */
  public RowData(float height, float width) {
    this.height = height;
    this.width = width;
  }

  /**
   * Creates a new row data.
   * 
   * @param style the style information
   */
  public RowData(int style) {
    switch (style) {
      case FILL_BOTH:
        fillWidth = true;
        fillHeight = true;
        break;
      case FILL_HORIZONTAL:
        fillWidth = true;
        break;
      case FILL_VERTICAL:
        fillHeight = true;
        break;
    }
  }

  /**
   * Returns <code>true</code> if the height should fill.
   * 
   * @return the fill height state
   */
  public boolean getFillHeight() {
    return fillHeight;
  }

  /**
   * Returns <code>true</code> if the width should fill.
   * 
   * @return the fill width state
   */
  public boolean getFillWidth() {
    return fillWidth;
  }

  /**
   * Returns the height.
   * 
   * @return the height
   */
  public float getHeight() {
    return height;
  }

  /**
   * Returns the width.
   * 
   * @return the width
   */
  public float getWidth() {
    return width;
  }

  /**
   * Sets if the widget's height should fill its region. When <code>true</code>,
   * the height field is ignored. Default value is <code>false</code>.
   * 
   * @param fillHeight the fill height state
   */
  public void setFillHeight(boolean fillHeight) {
    this.fillHeight = fillHeight;
  }

  /**
   * Sets if the widget's width should fill its region. When <code>true</code>,
   * the width field is ignored. Default value is <code>false</code>.
   * 
   * @param fillWidth the fill width state
   */
  public void setFillWidth(boolean fillWidth) {
    this.fillWidth = fillWidth;
  }

  /**
   * Sets the preferred height in pixels. Default value is DEFAULT.
   * 
   * @param height the height
   */
  public void setHeight(float height) {
    this.height = height;
  }

  /**
   * Sets the desired width in pixels. Default value is DEFAULT.
   * 
   * @param width the width
   */
  public void setWidth(float width) {
    this.width = width;
  }
}
