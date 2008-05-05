/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

/**
 * Layout data for <code>TableLayout</code>.
 */
public class TableLayoutData {

  /**
   * Left horizontal alignment.
   */
  public static final String ALIGN_LEFT = "left";

  /**
   * Center horizontal alignment.
   */
  public static final String ALIGN_CENTER = "center";

  /**
   * Right horizontal alignment.
   */
  public static final String ALIGN_RIGHT = "right";

  /**
   * Left vertical alignment.
   */
  public static final String ALIGN_TOP = "top";

  /**
   * Center vertical alignment.
   */
  public static final String ALIGN_MIDDLE = "middle";

  /**
   * Right vertical alignment.
   */
  public static final String ALIGN_BOTTOM = "bottom";

  int colspan = 1;
  int padding;
  int margin;
  String style;
  String align;
  String valign;
  String width;
  String height;

  /**
   * Creates a new table layout instance.
   */
  public TableLayoutData() {

  }

  /**
   * Creates a new table layout instance with the given colspan.
   * 
   * @param colspan the colspan
   */
  public TableLayoutData(int colspan) {
    this.colspan = colspan;
  }

  /**
   * Returns the cell's alignment.
   * 
   * @return the alignment
   */
  public String getAlign() {
    return align;
  }

  /**
   * Returns the number of columns
   * 
   * @return the col span
   */
  public int getColspan() {
    return colspan;
  }

  /**
   * Returns the cell's height.
   * 
   * @return the cell height
   */
  public String getHeight() {
    return height;
  }

  /**
   * Sets the cell's margin.
   * 
   * @return the margin
   */
  public int getMargin() {
    return margin;
  }

  /**
   * Returns the cell's padding.
   * 
   * @return the padding
   */
  public int getPadding() {
    return padding;
  }

  /**
   * Returns the cell's style.
   * 
   * @return the cell style
   */
  public String getStyle() {
    return style;
  }

  /**
   * Returns the cell's vertical alignment.
   * 
   * @return the vertical aligment
   */
  public String getValign() {
    return valign;
  }

  /**
   * Returns the cell's width.
   * 
   * @return the cell's width
   */
  public String getWidth() {
    return width;
  }

  /**
   * Sets the cell's horiztonal alignment.
   * 
   * @param align the alignment
   */
  public void setAlign(String align) {
    this.align = align;
  }

  /**
   * Sets the number of columns to span. Default value is 1.
   * 
   * @param colspan the col span
   */
  public void setColspan(int colspan) {
    this.colspan = colspan;
  }

  /**
   * Sets the cell's height.
   * 
   * @param height the cell height
   */
  public void setHeight(String height) {
    this.height = height;
  }

  /**
   * Returns the cell's margin. Default value is 0.
   * 
   * @param margin the margin
   */
  public void setMargin(int margin) {
    this.margin = margin;
  }

  /**
   * Sets the cell's padding. Default value is 0.
   * 
   * @param padding the cell padding
   */
  public void setPadding(int padding) {
    this.padding = padding;
  }

  /**
   * Sets the cell's style.
   * 
   * @param style the style
   */
  public void setStyle(String style) {
    this.style = style;
  }

  /**
   * Sets the cell's vertical alignment.
   * 
   * @param valign the vertical alignment
   */
  public void setVAlign(String valign) {
    this.valign = valign;
  }

  /**
   * Sets the cell's width.
   * 
   * @param width the width
   */
  public void setWidth(String width) {
    this.width = width;
  }

}
