/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import java.util.Collections;
import java.util.Comparator;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * This class encapsulates the user interface of a {@link Table}.
 */
public class TableView {

  private static String bodyHTML;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div style='overflow: hidden;'>");
    sb.append("<div style='overflow: scroll;'>");
    sb.append("<div class='my-tbl-data'></div>");
    sb.append("</div></div>");
    bodyHTML = sb.toString();
  }

  // styles
  protected String baseStyle = "my-tbl-item";
  protected String overStyle = baseStyle + "-over";
  protected String selStyle = baseStyle + "-sel";
  protected String cellStyle = baseStyle + "-" + "cell";
  protected String cellOverflowStyle = cellStyle + "-" + "overflow";
  protected String textStyle = cellStyle + "-text";
  protected String widgetStyle = cellStyle + "-widget";

  protected TableColumnModel cm;
  protected Element scrollElem, dataElem;
  protected Table table;
  protected int scrollBarWidth;

  /**
   * Sorts the table items based on the current order.
   */
  public void reorderItems() {
    MyDOM.removeChildren(dataElem);
    int numRows = table.getItemCount();
    for (int i = 0; i < numRows; i++) {
      TableItem item = table.getItem(i);
      DOM.appendChild(dataElem, item.getElement());
    }
    table.getSelectionModel().refresh();
  }

  protected void applyCellStyles(TableItem item) {
    if (item.cellStyles != null) {
      for (int i = 0; i < item.cellStyles.length; i++) {
        setCellStyle(item, i, item.cellStyles[i]);
      }
    }
  }

  protected void clearHoverStyles() {
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      item.onMouseOut(null);
    }
  }

  protected void onSelectItem(TableItem item, boolean select) {
    if (select) {
      item.addStyleName(selStyle);
    } else {
      item.removeStyleName(selStyle);
    }
  }

  protected void doSort(int index, int direction) {
    TableColumn column = table.getColumn(index);
    final Comparator comparator = column.getComparator();
    final int col = index;
    Collections.sort(table.getItems(), new Comparator() {
      public int compare(Object arg0, Object arg1) {
        TableItem item1 = (TableItem) arg0;
        TableItem item2 = (TableItem) arg1;
        Object o1 = item1.getValue(col);
        Object o2 = item2.getValue(col);
        return comparator.compare(o1, o2);
      }
    });

    if (direction == Style.DESC) {
      Collections.reverse(table.getItems());
    }
    reorderItems();
  }

  protected int getCellIndex(Element target) {
    String index = MyDOM.getAnyElementProperty(target, "index");
    if (index == null) {
      target = DOM.getParent(target);
      while (target != null) {
        index = MyDOM.getAnyElementProperty(target, "index");
        if (index == null) {
          target = DOM.getParent(target);
        } else {
          break;
        }
      }
    }
    return index == null ? Style.DEFAULT : Integer.parseInt(index);
  }

  protected Element getScrollElement() {
    return scrollElem;
  }

  protected Element getTextCellElement(TableItem item, int cell) {
    return getTextCellInternal(item.getElement(), cell);
  }

  protected native Element getTextCellInternal(Element elem, int column) /*-{
     return elem.firstChild.firstChild.firstChild.childNodes[column].firstChild.firstChild;
   }-*/;

  protected void init(Table table) {
    this.table = table;
    this.cm = table.getColumnModel();
  }

  protected void onHighlightRow(TableItem item, boolean highlight) {
    if (highlight) {
      item.addStyleName(overStyle);
    } else {
      item.removeStyleName(overStyle);
    }
  }

  protected void removeItem(TableItem item) {
    DOM.removeChild(dataElem, item.getElement());
  }

  protected void render() {
    scrollBarWidth = MyDOM.getScrollBarWidth();

    Element div = DOM.createDiv();
    MyDOM.setInnerHTML(div, bodyHTML.toString());
    scrollElem = MyDOM.getSubChild(div, 2);
    dataElem = DOM.getFirstChild(scrollElem);
    DOM.appendChild(table.getElement(), DOM.getFirstChild(div));

    if (table.verticalLines) {
      table.addStyleName("my-tbl-vlines");
    }

    if (!MyGWT.isIE) {
      DOM.setElementPropertyInt(table.getElement(), "tabIndex", 0);
    }

    DOM.sinkEvents(scrollElem, Event.ONSCROLL);

    table.disableTextSelection(true);
  }

  protected void renderItem(TableItem item, int index) {
    item.setStyleName(baseStyle);
    item.init(table);

    int cols = cm.getColumnCount();
    Object[] values = item.getValues();
    Object[] svalues = new Object[cols];
    for (int i = 0; i < cols; i++) {
      if (!item.hasWidgets && values[i] instanceof Widget) {
        item.hasWidgets = true;
      }
      svalues[i] = table.getRenderedValue(i, values[i]);
    }

    StringBuffer sb = new StringBuffer();
    sb.append("<table cellpadding=0 cellspacing=0 tabIndex=1><tr>");
    for (int i = 0; i < cols; i++) {
      TableColumn c = cm.getColumn(i);
      String display = c.isHidden() ? "none" : "static";
      int w = 0;
      try {
          w = table.getColumnModel().getWidthInPixels(c.index);
      } catch (Exception e) {
          System.out.println("ZZZ item element: " + item.getElement());
          e.printStackTrace();
          w = 0;
      }
      int align = c.getAlignment();
      String salign = "left";
      if (align == Style.CENTER) {
        salign = "center";
      } else if (align == Style.RIGHT) {
        salign = "right";
      }
      sb.append("<td class=" + cellStyle + " style='display: " + display + ";width: " + w
          + "px' index=" + i + "><div class=" + cellOverflowStyle + " style='width:" + w
          + "'><div class=" + textStyle + " style='text-align:" + salign + "'>"
          + svalues[i] + "</div></div></td>");
    }
    sb.append("</tr></table>");

    MyDOM.setInnerHTML(item.getElement(), sb.toString());
    DOM.insertChild(dataElem, item.getElement(), index);

    if (item.hasWidgets) {
      for (int i = 0; i < cols; i++) {
        if (values[i] instanceof Widget) {
          Widget w = (Widget) values[i];
          Element text = getTextCellElement(item, i);
          MyDOM.setInnerHTML(text, "");
          MyDOM.setStyleName(text, widgetStyle);
          DOM.appendChild(text, w.getElement());
          if (table.isAttached()) {
            WidgetHelper.doAttach(w);
          }
        }
      }
    }
    applyCellStyles(item);

    item.cellsRendered = true;
  }

  protected void renderItems() {
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      renderItem(item, i);
    }
  }

  protected void renderItemValue(TableItem item, int index, Object value) {
    Element textElem = getTextCellElement(item, index);
    if (textElem != null) {
      Element child = DOM.getChild(textElem, 0);
      if (child != null) {
        DOM.removeChild(textElem, DOM.getChild(textElem, 0)); 
      }
      DOM.setInnerHTML(textElem, "");
      if (value instanceof Widget) {
        Widget widget = (Widget) value;
        MyDOM.setStyleName(textElem, widgetStyle);
        DOM.appendChild(textElem, widget.getElement());
        if (table.isAttached()) {
          WidgetHelper.doAttach(widget);
        }
      } else {
        String s = table.getRenderedValue(index, value);
        MyDOM.setInnerHTML(textElem, s);
      }
    }
    applyCellStyles(item);
  }

  protected void resize() {
    int width = table.getOffsetWidth();
    int headerHeight = table.getTableHeader().getOffsetHeight();
    int bodyHeight = table.getOffsetHeight() - headerHeight;
    int bodyWidth = width;

    MyDOM.setWidth(dataElem, cm.getTotalWidth());

    boolean vscroll = MyDOM.getHeight(dataElem) > bodyHeight;
    int adj = vscroll ? scrollBarWidth : 0;

    if ((table.getStyle() & Style.HORIZONTAL) != 0) {
      if (MyDOM.getWidth(dataElem) < (width - adj)) {
        bodyHeight += scrollBarWidth;
        MyDOM.setLeft(table.getTableHeader().getElement(), 0);
      }
    } else {
      bodyHeight += scrollBarWidth;
    }
    if (MyDOM.getHeight(dataElem) > bodyHeight) {
      width -= scrollBarWidth;
    }

    bodyHeight -= MyDOM.getBorderWidth(table.getElement(), Style.TOP | Style.BOTTOM);
    bodyWidth -= MyDOM.getBorderWidth(table.getElement(), Style.LEFT | Style.RIGHT);

    MyDOM.setSize(scrollElem, bodyWidth, bodyHeight);

    int w = cm.getTotalWidth();

    if (w < width) {
      adj = width - w;
    }
    MyDOM.setWidth(dataElem, cm.getTotalWidth() + adj);
  }

  protected void resizeCells(int columnIndex) {
    TableColumn c = cm.getColumn(columnIndex);
    int w = cm.getWidthInPixels(c.index);
    if (table.verticalLines) {
      --w;
    }
    int rows = table.getItemCount();
    for (int j = 0; j < rows; j++) {
      TableItem item = table.getItem(j);
      sizeCell(item.getElement(), columnIndex, w);
      if (j == 0) {
        showColumn(item.getElement(), !c.isHidden(), columnIndex);
      }
    }
  }

  protected void setCellStyle(TableItem item, int index, String style) {
    if (item.cellsRendered) {
      Element cell = getTextCellElement(item, index);
      MyDOM.setStyleName(cell, textStyle + " " + style);
    }
  }

  protected native void showColumn(Element elem, boolean show, int index) /*-{
    var tbl = elem.firstChild;
    var cell = tbl.firstChild.firstChild.childNodes[index]
    cell.style.display = show ? '' : 'none';
  }-*/;

  protected void showColumn(int index, boolean show) {
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      showColumn(table.getItem(i).getElement(), show, index);
    }
  }

  protected native void sizeCell(Element elem, int index, int width) /*-{
    var tbl = elem.firstChild;
    var cell = tbl.firstChild.firstChild.childNodes[index];
    cell.style.width = width;
    cell.firstChild.style.width = width - 1;
  }-*/;

  protected void sort(int index, int direction) {
    doSort(index, direction);
  }

}
