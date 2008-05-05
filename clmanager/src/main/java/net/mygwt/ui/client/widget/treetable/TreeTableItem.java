/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.widget.ToolTip;
import net.mygwt.ui.client.widget.tree.Tree;
import net.mygwt.ui.client.widget.tree.TreeItem;
import net.mygwt.ui.client.widget.tree.TreeItemUI;

import com.google.gwt.user.client.Element;

/**
 * A item in a <code>TreeTable</code>. All events are bubbled to the item's
 * parent treetable.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : (widget, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeExpand</b> : (widget, item)<br>
 * <div>Fires before a item is expanded. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget, item)<br>
 * <div>Fires before a item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget, item)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget, item)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being collapsed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : (widget)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : this</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellDoubleClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : this</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item that represents the row</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowDoubleClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item that represents the row</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree-item (the item itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class TreeTableItem extends TreeItem {

  protected TreeTable treeTable;

  String[] toolTips;
  ToolTip cellToolTip;
  boolean hasWidgets;
  String[] cellStyles;
  boolean cellsRendered;
  private Object[] values;

  /**
   * Creates a new tree table item.
   * 
   * @param values the cell values
   */
  public TreeTableItem(Object[] values) {
    this.values = values;
  }

  /**
   * Creates a new tree table item.
   * 
   * @param text the text
   * @param values the values
   */
  public TreeTableItem(String text, Object[] values) {
    super(text);
    this.values = values;
  }

  /**
   * Returns the item's cell tooltip.
   * 
   * @return the tooltip
   */
  public ToolTip getCellToolTip() {
    return cellToolTip;
  }

  /**
   * Returns the item's tree table.
   * 
   * @return the tree table
   */
  public TreeTable getTreeTable() {
    return treeTable;
  }

  /**
   * Returns a cell value.
   * 
   * @param index the cell index
   * @return the value
   */
  public Object getValue(int index) {
    return values[index];
  }

  /**
   * Returns the item's values.
   * 
   * @return the values
   */
  public Object[] getValues() {
    return values;
  }

  public void onBaseEvent(BaseEvent be) {
    if (be.widget != this) {
      return;
    }

    if (ui != null) {
      getUI().getListener().handleEvent(be);
    }

    if (cellToolTip != null) {
      cellToolTip.handleEvent(be);
    }

    switch (be.type) {
      case Events.Click:
        onClick(be);
        break;
      case Events.DoubleClick:
        onDoubleClick(be);
        break;
      case Events.MouseOver:
        onMouseOver(be);
        break;
    }

  }

  /**
   * Sets the style for a table cell.
   * 
   * @param index the column index
   * @param style the new style
   */
  public void setCellStyle(int index, String style) {
    if (cellStyles == null) cellStyles = new String[values.length];
    cellStyles[index] = style;
    if (isRendered()) {
      treeTable.getView().setCellStyle(this, index, style);
    }
  }

  /**
   * Returns the style for the given cell.
   * 
   * @param index the column index
   * @return the style
   */
  public String getCellStyle(int index) {
    if (cellStyles != null) {
      return cellStyles[index];
    }
    return null;
  }

  /**
   * Sets a cell tooltip.
   * 
   * @param index the column index
   * @param text the text of the tool tip
   */
  public void setCellToolTip(int index, String text) {
    if (toolTips == null) toolTips = new String[values.length];
    toolTips[index] = text;
    initCellToolTips();
  }

  /**
   * Sets all of the cell tooltips
   * 
   * @param toolTips the tool tips to use
   */
  public void setCellToolTips(String[] toolTips) {
    this.toolTips = toolTips;
    initCellToolTips();
  }

  /**
   * Sets a cell value.
   * 
   * @param index the column index
   * @param text the text
   */
  public void setText(int index, String text) {
    setValue(index, text);
  }

  /**
   * Sets a cell value.
   * 
   * @param index the column index
   * @param value the value
   */
  public void setValue(int index, Object value) {
    values[index] = value;
    if (rendered) {
      treeTable.getView().renderItemValue(this, index, value);
    }
  }

  /**
   * Sets the item's values.
   * 
   * @param values the values
   */
  public void setValues(Object[] values) {
    this.values = values;
  }

  protected String[] getRenderedValues() {
    String[] svalues = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      svalues[i] = treeTable.getRenderedValue(i, values[i]);
    }
    return svalues;
  }

  /**
   * Subclasses may override.
   * 
   * @return the ui
   */
  protected TreeItemUI getTreeItemUI() {
    return new TreeTableItemUI(this);
  }

  protected void init(TreeTable treeTable) {
    this.tree = treeTable;
    this.treeTable = treeTable;
  }

  protected void initCellToolTips() {
    if (cellToolTip == null && isRendered()) {
      cellToolTip = new ToolTip(this);
      cellToolTip.setTrackMouse(true);
    }
  }

  public void setElement(Element elem) {
    super.setElement(elem);
  }

  protected void onMouseOver(BaseEvent be) {
    onCellMouseOver(be);
  }

  protected void onCellMouseOver(BaseEvent be) {
    Element target = be.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    if (cellToolTip != null) {
      if (toolTips != null && toolTips[index] != null && toolTips[index].length() > 0) {
        cellToolTip.setText(null, toolTips[index]);
        cellToolTip.setVisible(true);
      } else {
        cellToolTip.setVisible(false);
      }
    }
  }

  protected void onClick(BaseEvent be) {
    Element target = be.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    be.widget = getTreeTable();
    be.item = this;
    be.index = index;

    treeTable.fireEvent(Events.CellClick, be);
    treeTable.fireEvent(Events.RowClick, be);
  }

  protected void onDoubleClick(BaseEvent be) {
    Element target = be.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }
    BaseEvent evt = new BaseEvent();
    evt.widget = getTreeTable();
    evt.item = this;
    evt.index = index;

    treeTable.fireEvent(Events.CellDoubleClick, evt);
    treeTable.fireEvent(Events.RowDoubleClick, evt);
  }

  protected void setTree(Tree tree) {
    super.setTree(tree);
    treeTable = (TreeTable) tree;
  }

}
