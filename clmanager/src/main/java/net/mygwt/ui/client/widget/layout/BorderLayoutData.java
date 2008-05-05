/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.Style;

/**
 * Layout data describing a "region" in a border panel.
 * 
 * @see Style#NORTH
 * @see Style#WEST
 * @see Style#SOUTH
 * @see Style#EAST
 * @see Style#CENTER
 */
public class BorderLayoutData {

  boolean background = true;
  boolean borders = true;
  boolean exclude = false;
  int maximumSize = 500;
  int minimumSize;
  int region = 0;
  boolean resizeable = false;
  float size;

  /**
   * Creates a new border layout data.
   * <p>
   * <dl>
   * <dt><b>Regions:</b></dt>
   * <dd>NORTH,WEST,SOUTH,EAST,CENTER</dd>
   * </dl>
   * </p>
   * 
   * @param region the region
   */
  public BorderLayoutData(int region) {
    this.region = region;
  }

  /**
   * Creates a new border layout data with a given size.
   * <p>
   * <dl>
   * <dt><b>Regions:</b></dt>
   * <dd>NORTH,WEST,SOUTH,EAST,CENTER</dd>
   * </dl>
   * </p>
   * 
   * @param region the region (NORTH, WEST, SOUTH, EAST, CENTER).
   * @param size the preferred size
   */
  public BorderLayoutData(int region, float size) {
    this.region = region;
    this.size = size;
  }

  /**
   * Creates a new border layout data. Region will be resizable using the min
   * and max values.
   * <p>
   * <dl>
   * <dt><b>Regions:</b></dt>
   * <dd>NORTH,WEST,SOUTH,EAST,CENTER</dd>
   * </dl>
   * </p>
   * 
   * @param region the region (NORTH, WEST, SOUTH, EAST, CENTER).
   * @param size the preferred size
   * @param minSize the minimum size when resizing this region
   * @param maxSize the maximum size when resizing this region
   */
  public BorderLayoutData(int region, float size, int minSize, int maxSize) {
    this.region = region;
    this.size = size;
    this.minimumSize = minSize;
    this.maximumSize = maxSize;
    resizeable = true;
  }

  /**
   * Returns <code>true</code> if the background should be displayed.
   * 
   * @return the backgrond state
   */
  public boolean getBackground() {
    return background;
  }

  /**
   * Displays the region's background if <code>true</code>, clears otherwise.
   * Default value is <code>true</code>.
   * 
   * @param background the background
   */
  public void setBackground(boolean background) {
    this.background = background;
  }

  /**
   * Returns the border state
   * 
   * @return <code>true</code> to display the border
   */
  public boolean getBorders() {
    return borders;
  }

  /**
   * Sets whether the border should be displayed.
   * 
   * @param borders <code>true</code> to show
   */
  public void setBorders(boolean borders) {
    this.borders = borders;
  }

  /**
   * Returns <code>true</code> if the region should be excluded.
   * 
   * @return the exclude state
   */
  public boolean getExclude() {
    return exclude;
  }

  /**
   * Hides the region if <code>true</code>, shows otherwise. Default value is
   * <code>false</code>.
   * 
   * @param exclude the exclude state
   */
  public void setExclude(boolean exclude) {
    this.exclude = exclude;
  }

  /**
   * Returns the region's maximum size.
   * 
   * @return the max size
   */
  public int getMaximumSize() {
    return maximumSize;
  }

  /**
   * Sets the regions's max size. Default value is 500.
   * 
   * @param maximumSize the max size
   */
  public void setMaximumSize(int maximumSize) {
    this.maximumSize = maximumSize;
  }

  /**
   * Returns the region's minimum size.
   * 
   * @return the minimum size
   */
  public int getMinimumSize() {
    return minimumSize;
  }

  /**
   * Sets the regions's min size.
   * 
   * @param minimumSize the minimum size
   */
  public void setMinimumSize(int minimumSize) {
    this.minimumSize = minimumSize;
  }

  /**
   * Returns the region.
   * 
   * @return the region
   */
  public int getRegion() {
    return region;
  }

  /**
   * Specifies the region in the border panel.
   * 
   * @param region the region
   */
  public void setRegion(int region) {
    this.region = region;
  }

  /**
   * Returns <code>true</code> if the region is resizable.
   * 
   * @return the resizable state
   */
  public boolean getResizeable() {
    return resizeable;
  }

  /**
   * Enables resizing if <code>true</code>, disables otherwise. Default value
   * is <code>false</code>.
   * 
   * @param resizeable the resizable state
   */
  public void setResizeable(boolean resizeable) {
    this.resizeable = resizeable;
  }

  /**
   * Returns the region's size.
   * 
   * @return the size
   */
  public float getSize() {
    return size;
  }

  /**
   * The regions preferred size. Values of 1 or less are treated as percentages.
   * @param size the size
   */
  public void setSize(float size) {
    this.size = size;
  }

}
