/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.widget.table.TableColumnModel;

/**
 * ColumnModel used by the <code>TreeTable</code>.
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
public class TreeTableColumnModel extends TableColumnModel {

  /**
   * Constructs a new instance.
   */
  public TreeTableColumnModel(TreeTableColumn[] columns) {
    super(columns);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.widget.table.TableColumnModel#getVariableColumnCount()
   */
  protected int getVariableColumnCount() {
    return super.getVariableColumnCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.widget.table.TableColumnModel#getVariableWidth()
   */
  protected int getVariableWidth() {
    return super.getVariableWidth();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.widget.table.TableColumnModel#getWidthInPixels(int)
   */
  protected int getWidthInPixels(int index) {
    return super.getWidthInPixels(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.widget.table.TableColumnModel#setWidthAsPercent(int,
   *      float)
   */
  protected void setWidthAsPercent(int index, float width) {
    super.setWidthAsPercent(index, width);
  }
}
