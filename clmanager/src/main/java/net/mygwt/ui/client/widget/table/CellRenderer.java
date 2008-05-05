/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

/**
 * Allows cell values to be modified before being displayed.
 */
public interface CellRenderer {

  /**
   * Returns the formatted cell value.
   * 
   * @param property the property being modified
   * @param value the property value
   * @return the new value as HTML
   */
  public String render(String property, Object value);

}
