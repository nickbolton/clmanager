/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

/**
 * Defines common methods for Table based components.
 */
public interface ITable {

  /**
   * Returns the table's column model.
   * 
   * @return the column model
   */
  TableColumnModel getColumnModel();

  /**
   * Returns the column at the specified index.
   * 
   * @param index the column index
   * @return the column
   */
  TableColumn getColumn(int index);

  /**
   * Returns the table's header.
   * 
   * @return the table header
   */
  TableHeader getTableHeader();

  /**
   * Sorts the table using the specified column index.
   * 
   * @param index the column index
   * @param direction the direction to sort (NONE, ASC, DESC)
   */
  void sort(int index, int direction);

  /**
   * Returns the column context menu enabled state.
   * 
   * @return <code>true</code> if enabled, <code>false</code> otherwise.
   */
  boolean getColumnContextMenu();

}
