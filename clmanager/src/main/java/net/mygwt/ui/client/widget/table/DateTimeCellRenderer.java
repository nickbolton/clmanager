/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A <code>CellRenderer</code> implementation for date and times.
 * 
 * @see DateTimeFormat
 */
public class DateTimeCellRenderer implements CellRenderer {

  private DateTimeFormat format;

  public DateTimeCellRenderer(String pattern) {
    this.format = DateTimeFormat.getFormat(pattern);
  }

  public DateTimeCellRenderer(DateTimeFormat format) {
    this.format = format;
  }

  public String render(String property, Object value) {
    if (value == null) {
      return "";
    }
    return format.format((Date) value);
  }
}
