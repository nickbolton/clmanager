/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * A <code>CellRenderer</code> implementaiton for numbers.
 * 
 * @see NumberFormat
 */
public class NumberCellRenderer implements CellRenderer {

  private NumberFormat format;

  public NumberCellRenderer(String pattern) {
    this.format = NumberFormat.getFormat(pattern);
  }

  public NumberCellRenderer(NumberFormat format) {
    this.format = format;
  }

  public String render(String property, Object value) {
    if (value instanceof Double) {
      return format.format(((Double) value).doubleValue());
    } else if (value instanceof Float) {
      return format.format(((Float) value).floatValue());
    } else if (value instanceof Long) {
      return format.format(((Long) value).longValue());
    } else if (value instanceof Integer) {
      return format.format(((Integer) value).intValue());
    }
    return null;
  }

}
