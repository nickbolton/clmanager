/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.widget.Layout;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <code>TableLayout</code> allows you to easily render content into an HTML
 * table. The total number of columns can be specified.
 * <p>
 * Rather than explicitly creating and nesting rows and columns as you would in
 * HTML, you simply specify the total column count and start adding widgets in
 * their natural order from left to right, top to bottom. The layout will
 * automatically figure out, based on the column count how to position each
 * panel within the table.
 * </p>
 */
public class TableLayout extends Layout {

  int columns = 1;
  int cellPadding = 0;
  int cellSpacing = 0;
  int border = 0;
  String width, height;

  private Element table, tbody;
  private int currentColumn;
  private int currentRow;

  /**
   * Creates a new table layout.
   */
  public TableLayout() {

  }

  /**
   * Creates a new table layout.
   * 
   * @param columns the number of columns
   */
  public TableLayout(int columns) {
    this.columns = columns;
  }

  /**
   * Returns the border.
   * 
   * @return the border
   */
  public int getBorder() {
    return border;
  }

  /**
   * Returns the table's cell padding.
   * 
   * @return the cell padding
   */
  public int getCellPadding() {
    return cellPadding;
  }

  /**
   * Returns the table's cell spacing.
   * 
   * @return the cell spacing
   */
  public int getCellSpacing() {
    return cellSpacing;
  }

  /**
   * Returns the number of columns.
   * 
   * @return the number of columns
   */
  public int getColumns() {
    return columns;
  }

  /**
   * Sets the table's height.
   * 
   * @return the height
   */
  public String getHeight() {
    return height;
  }

  /**
   * Returns the table width.
   * 
   * @return the width
   */
  public String getWidth() {
    return width;
  }

  /**
   * Sets the table's border property. Default value is 0.
   * 
   * @param border the border in pixels
   */
  public void setBorder(int border) {
    this.border = border;
  }

  /**
   * Sets the table's cellpadding property. Default value is 0.
   * 
   * @param cellPadding the cell padding
   */
  public void setCellPadding(int cellPadding) {
    this.cellPadding = cellPadding;
  }

  /**
   * @param cellSpacing
   */
  public void setCellSpacing(int cellSpacing) {
    this.cellSpacing = cellSpacing;
  }

  /**
   * Sets the number of columns. Default value is 1.
   * 
   * @param columns the number of columns
   */
  public void setColumns(int columns) {
    this.columns = columns;
  }

  /**
   * Sets the table's height.
   * 
   * @param height the height
   */
  public void setHeight(String height) {
    this.height = height;
  }

  /**
   * Sets the table's width.
   * 
   * @param width the width
   */
  public void setWidth(String width) {
    this.width = width;
  }

  protected Element getNextCell(Widget widget) {
    TableLayoutData data = (TableLayoutData) container.getLayoutData(widget);
    if (data == null) {
      data = new TableLayoutData();
      container.setLayoutData(widget, data);
    }

    Element td = DOM.createTD();
    Element row;

    if (currentColumn != 0 && (currentColumn % columns == 0)) {
      row = getRow(++currentRow);
      currentColumn += data.colspan != -1 ? data.colspan : 1;
    } else {
      row = getRow(currentRow);
      currentColumn += data.colspan != -1 ? data.colspan : 1;
    }
    if (data.colspan != 1) {
      DOM.setElementPropertyInt(td, "colSpan", data.colspan);
    }

    if (data.padding > 0) {
      DOM.setIntStyleAttribute(td, "padding", data.padding);
    }
    if (data.style != null) {
      MyDOM.setStyleName(td, data.style);
    }
    if (data.align != null) {
      DOM.setElementProperty(td, "align", data.align);
    }
    if (data.valign != null) {
      DOM.setStyleAttribute(td, "verticalAlign", data.valign);
    }
    if (data.height != null) {
      DOM.setElementProperty(td, "height", data.height);
    }
    if (data.width != null) {
      DOM.setElementProperty(td, "width", data.width);
    }
    DOM.appendChild(row, td);
    return td;
  }

  protected Element getRow(int index) {
    Element row = DOM.getChild(tbody, index);
    if (row == null) {
      row = DOM.createTR();
      DOM.appendChild(tbody, row);
    }
    return row;
  }

  protected boolean isValidParent(Element elem, Element parent) {
    return false;
  }

  protected void onLayout(WidgetContainer container, Element target) {
    currentColumn = 0;
    currentRow = 0;

    MyDOM.removeChildren(target);
    table = DOM.createTable();
    DOM.setElementPropertyInt(table, "cellPadding", cellPadding);
    DOM.setElementPropertyInt(table, "cellSpacing", cellSpacing);

    if (border > 0) {
      DOM.setElementPropertyInt(table, "border", border);
    }

    if (height != null) {
      DOM.setStyleAttribute(table, "height", height);
    }

    if (width != null) {
      DOM.setStyleAttribute(table, "width", width);
    }

    tbody = DOM.createTBody();
    DOM.appendChild(table, tbody);
    DOM.appendChild(target, table);

    renderAll(container, target);
  }

  protected void renderWidget(Widget widget, int index, Element target) {
    DOM.appendChild(getNextCell(widget), widget.getElement());
  }

}
