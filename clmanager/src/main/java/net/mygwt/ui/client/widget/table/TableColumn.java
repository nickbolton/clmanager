/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import java.util.Comparator;
import java.util.HashMap;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.util.DefaultComparator;

/**
 * A column in a <code>Table</code>. Column sizes can be specified as either
 * pixels or percentanges. Width values less than or equal to 1 are treated as
 * percentages.
 * 
 * <p>
 * Note: Public members should not be modified after the column is added to a
 * column model.
 * </p>
 */
public class TableColumn {

  public static final Comparator DEFAULT_COMPARATOR = new DefaultComparator();

  int sortDir = Style.NONE;
  int index;
  
  private boolean hidden;
  private int align = Style.LEFT;
  private int maxWidth = 500;
  private int minWidth = 20;
  private float width = minWidth;
  private CellRenderer renderer;
  private boolean resizable = true;
  private boolean sortable = true;
  private Comparator comparator;
  private String id, text;
  private Object data;
  private HashMap dataMap;

  /**
   * Creates a new column instance.
   * 
   * @param id the column id
   * @param width the column width, widths that are 1 or less are treated as
   *            percentages.
   */
  public TableColumn(String id, float width) {
    this.id = id;
    this.text = id;
    this.width = width;
  }

  /**
   * Creates a new column instance.
   * 
   * @param id the column id
   * @param text the column text
   */
  public TableColumn(String id, String text) {
    this.id = id;
    this.text = text;
  }

  /**
   * Creates a new column instance.
   * 
   * @param id the column id
   * @param text the column text
   * @param width the width
   */
  public TableColumn(String id, String text, float width) {
    this(id, text);
    this.width = width;
  }

  /**
   * Returns the column's alignment.
   * 
   * @return the alignment
   */
  public int getAlignment() {
    return align;
  }

  /**
   * returns the table's comparator.
   * 
   * @return the comparator
   */
  public Comparator getComparator() {
    if (comparator == null) return DEFAULT_COMPARATOR;
    return comparator;
  }

  /**
   * Returns the application defined data associated with the column, or
   * <code>null</code> if it has not been set.
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the application defined property for the given name, or
   * <code>null</code> if it has not been set.
   * 
   * @param key the name of the property
   * @return the value or <code>null</code> if it has not been set
   */
  public Object getData(String key) {
    if (dataMap == null) return null;
    return dataMap.get(key);
  }

  /**
   * Returns the column's id.
   * 
   * @return the id
   */
  public String getID() {
    return id;
  }

  /**
   * Returns the column's maximum width.
   * 
   * @return the max width
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Returns the column's minimum width.
   * 
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns the column's cell rendered.
   * 
   * @return the cell renderer
   */
  public CellRenderer getRenderer() {
    return renderer;
  }

  /**
   * Returns the column's current sort direction.
   * 
   * @return the sort direction (NONE, ASC, DESC)
   */
  public int getSortDir() {
    return sortDir;
  }

  /**
   * Returns the column's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the column's width.
   * 
   * @return the column width
   */
  public float getWidth() {
    return width;
  }

  /**
   * Returns <code>true</code> if hidden, <code>false</code> otherwise.
   * 
   * @return the hidden state
   */
  public boolean isHidden() {
    return hidden;
  }

  /**
   * Returns <code>true</code> if the column is resizable.
   * 
   * @return the resizable state
   */
  public boolean isResizable() {
    return resizable;
  }

  /**
   * Returns <code>true</code> if the column is sortable.
   * 
   * @return the sortable state
   */
  public boolean isSortable() {
    return sortable;
  }

  /**
   * Sets the column's alignment. Valid values are LEFT, CENTER, and RIGHT.
   * Default value is LEFT.
   * 
   * @param align the alignment
   */
  public void setAlignment(int align) {
    this.align = align;
  }

  /**
   * Sets the column's comparator.
   * 
   * @param comparator the comparator
   */
  public void setComparator(Comparator comparator) {
    this.comparator = comparator;
  }

  /**
   * Sets the application defined column data.
   * 
   * @param data the widget data
   */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * Sets the application defined property with the given name.
   * 
   * @param key the name of the property
   * @param data the new value for the property
   */
  public void setData(String key, Object data) {
    if (dataMap == null) dataMap = new HashMap();
    dataMap.put(key, data);
  }

  /**
   * Sets the column's hidden state.
   * 
   * @param hidden <code>true</code> to hide, <code>false</code> otherwise
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  /**
   * Sets the column's maximum width. Default value = 500.
   * 
   * @param maxWidth the max width
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  /**
   * Sets the column's minimum width. Default value is 20.
   * 
   * @param minWidth the min width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Sets the column's cell renderer.
   * 
   * @param renderer the cell renderer
   */
  public void setRenderer(CellRenderer renderer) {
    this.renderer = renderer;
  }

  /**
   * Specifies if the column may be resized. Default value is <code>true</code>.
   * 
   * @param resizable the resizeable state
   */
  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  /**
   * Sets the sortable state.
   * 
   * @param sortable <code>true</code> to enable sorting
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  /**
   * Sets the column's width. Widths that are 1 or less are treated as
   * percentages.
   * 
   * @param width the width
   */
  public void setWidth(float width) {
    this.width = width;
  }

  protected int getIndex() {
    return index;
  }

  protected void setIndex(int index) {
    this.index = index;
  }
}
