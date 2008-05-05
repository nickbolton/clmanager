/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.ToolTip;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A row in a <code>Table</code>.
 */
public class TableItem extends Component {

  String[] toolTips;
  ToolTip cellToolTip;
  boolean hasWidgets;
  String[] cellStyles;
  boolean cellsRendered;
  private Table table;
  private Object[] values;

  /**
   * Creates a new table item.
   * 
   * @param values the cell values
   */
  public TableItem(Object[] values) {
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
   * Returns the item's parent table.
   * 
   * @return the table
   */
  public Table getTable() {
    return table;
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
    // Pass along the event to the related cell tooltip if it exists
    // so that we don't consume the event before the tooltip processes it
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
      case Events.MouseOut:
        onMouseOut(be);
        break;
    }
  }

  /**
   * Sets the style for a cell.
   * 
   * @param index the column index
   * @param style the new style
   */
  public void setCellStyle(int index, String style) {
    if (cellStyles == null) cellStyles = new String[values.length];
    cellStyles[index] = style;
    if (isRendered()) {
      table.getView().setCellStyle(this, index, style);
    }
  }

  /**
   * Sets a cell tooltip.
   * 
   * @param index the column index
   * @param text the text of the tool tip
   */
  public void setCellToolTip(int index, String text) {
    if (toolTips == null) {
      toolTips = new String[values.length];
    }
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
      table.getView().renderItemValue(this, index, value);
    }
  }

  /**
   * Sets a widget as a cell value.
   * 
   * @param index the column index
   * @param widget the widget
   */
  public void setWidget(int index, Widget widget) {
    hasWidgets = true;
    setValue(index, widget);
  }

  protected void init(Table table) {
    this.table = table;
  }

  protected void initCellToolTips() {
    if (cellToolTip == null && isRendered()) {
      cellToolTip = new ToolTip(this);
      cellToolTip.setTrackMouse(true);
    }
  }

  protected void onCellMouseOver(BaseEvent be) {
    Element target = be.getTarget();

    int index = table.getView().getCellIndex(target);
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

    int index = table.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    be.widget = table;
    be.item = this;
    be.rowIndex = table.indexOf(this);
    be.index = index;

    table.fireEvent(Events.CellClick, be);
    table.fireEvent(Events.RowClick, be);
  }

  protected void onDoubleClick(BaseEvent be) {
    Element target = be.getTarget();

    int index = table.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }
    BaseEvent evt = new BaseEvent();
    evt.widget = table;
    evt.item = this;
    evt.rowIndex = table.indexOf(this);
    evt.index = index;

    table.fireEvent(Events.CellDoubleClick, evt);
    table.fireEvent(Events.RowDoubleClick, evt);
  }

  protected void onMouseOut(BaseEvent be) {
    table.getView().onHighlightRow(this, false);
  }

  protected void onMouseOver(BaseEvent be) {
    table.getView().onHighlightRow(this, true);
    onCellMouseOver(be);
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    if (toolTips != null) {
      initCellToolTips();
    }
  }

  protected void setSelected(boolean select) {
    table.getView().onSelectItem(this, select);
  }

}
