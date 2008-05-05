/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Observable;
import net.mygwt.ui.client.widget.Component;

/**
 * This is the default implementation of a ColumnModel used by the Table.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Resize</b> : (this,widget,index)<br>
 * <div>Fires after a column is resized.</div>
 * <ul>
 * <li>source : this</li>
 * <li>widget : table</li>
 * <li>index : column index</li>
 * </ul>
 * </dd>
 */
public class TableColumnModel extends Observable {

  Component table;
  List columns;

  /**
   * Constructs a new instance.
   */
  public TableColumnModel(TableColumn[] columns) {
    this.columns = new ArrayList();
    for (int i = 0; i < columns.length; i++) {
      columns[i].index = i;
      this.columns.add(columns[i]);
    }
  }

  /**
   * Returns the column at the given, zero-relative index or <code>null</code>
   * if the index is out of range.
   * 
   * @param index the index of the column to return
   * @return the column at the given index
   */
  public TableColumn getColumn(int index) {
    if ((index < 0) || (index >= getColumnCount())) return null;
    return (TableColumn) columns.get(index);
  }

  /**
   * Returns the column with the given name.
   * 
   * @param name the column name
   * @return the column or <code>null</code> if no match
   */
  public TableColumn getColumn(String name) {
    for (int i = 0; i < columns.size(); i++) {
      TableColumn column = getColumn(i);
      if (column.getID().equals(name)) {
        return column;
      }
    }
    return null;
  }

  /**
   * Returns the number of columns contained in the table.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return columns.size();
  }

  /**
   * Returns the total column model width.
   * 
   * @return the width in pixels
   */
  public int getTotalWidth() {
    int total = 0;
    int cols = getColumnCount();
    for (int i = 0; i < cols; i++) {
      TableColumn col = getColumn(i);
      if (!col.isHidden()) {
        total += getWidthInPixels(col.index);
      }

    }
    return total;
  }

  /**
   * Returns the number of visible columns.
   * 
   * @return the visible column count
   */
  public int getVisibleColumnCount() {
    int count = 0;
    for (int i = 0; i < getColumnCount(); i++) {
      TableColumn column = getColumn(i);
      if (!column.isHidden()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns the index of the column.
   * 
   * @param column the column
   * @return the column index
   */
  public int indexOf(TableColumn column) {
    return columns.indexOf(column);
  }

  /**
   * Sets the column's width.
   * 
   * @param index the column index
   * @param width the new width
   */
  public void setColumnWidth(int index, float width) {
    TableColumn col = getColumn(index);
    col.setWidth(width);
    BaseEvent be = new BaseEvent();
    be.source = this;
    be.widget = table;
    be.index = index;
    fireEvent(Events.Resize);
  }

  int getFixedWidth() {
    int cols = getColumnCount();
    int total = 0;
    for (int i = 0; i < cols; i++) {
      TableColumn col = getColumn(i);
      if (col.isHidden()) {
        continue;
      }
      if (col.getWidth() > 1) {
        total += col.getWidth();
      }
    }
    return total;
  }

  protected int getVariableColumnCount() {
    int count = 0;
    for (int i = 0; i < getColumnCount(); i++) {
      TableColumn col = getColumn(i);
      if (col.getWidth() <= 1) {
        count++;
      }
    }
    return count;
  }

  protected int getVariableWidth() {
    return (int) (.99 * (table.getOffsetWidth() - MyDOM.getScrollBarWidth()) - getFixedWidth());
  }

  protected int getWidthInPixels(int index) {
    // TODO: need to stop calcs when column size has not changed
    TableColumn col = getColumn(index);
    float width = getColumn(index).getWidth();
    if (width <= 1) {
      if (getVariableColumnCount() == 1) {
        return getVariableWidth() - 25;
      }
      int w = (int) (getVariableWidth() * getColumn(index).getWidth());
      w = Math.max(col.getMinWidth(), w);
      w = Math.min(col.getMaxWidth(), w);
      return w;
    } else {
      return (int) getColumn(index).getWidth();
    }
  }

  protected void setWidthAsPercent(int index, float width) {
    TableColumn col = getColumn(index);
    col.setWidth(getAdjustedWidth(col, width));

    float o = 0;
    for (int i = 0; i < getColumnCount(); i++) {
      TableColumn tc = getColumn(i);
      if (tc != col && tc.getWidth() < 1.1) {
        o += tc.getWidth();
      }
    }

    float dif = 1 - col.getWidth() - o;

    int afterCols = 0;
    for (int i = (index + 1); i < getColumnCount(); i++) {
      TableColumn tc = getColumn(i);
      if (tc.getWidth() < 1.1) {
        afterCols++;
      }
    }

    float adj = dif / (afterCols);
    for (int i = (index + 1); i < getColumnCount(); i++) {
      TableColumn other = getColumn(i);
      if (other.getWidth() <= 1) {
        if (other != col) {
          other.setWidth(other.getWidth() + adj);
        }
      }
    }
  }

  private float getAdjustedWidth(TableColumn col, float width) {
    int totalWidth = getTotalWidth();
    int pixels = (int) (width * totalWidth);
    if (pixels < col.getMinWidth()) {
      width = (float) col.getMinWidth() / totalWidth;
    }
    if (pixels > col.getMaxWidth()) {
      width = (float) col.getMaxWidth() / totalWidth;
    }

    return width;
  }
  
  public Component getTable() {
    return table;
  }
  
  public void setTable(Component table) {
    this.table = table;
  }
}
