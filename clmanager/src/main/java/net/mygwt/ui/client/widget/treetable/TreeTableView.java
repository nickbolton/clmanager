/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.table.TableColumnModel;
import net.mygwt.ui.client.widget.table.TableItem;
import net.mygwt.ui.client.widget.tree.TreeItem;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * This class encapsulates the user interface of a {@link TreeTable}.
 */
public class TreeTableView {

  private static String bodyHTML;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div style='overflow: hidden;'>");
    sb.append("<div style='overflow: scroll;'>");
    sb.append("<div class='my-treetbl-data'>");
    sb.append("<div class='my-treetbl-tree'></div>");
    sb.append("</div></div></div>");
    bodyHTML = sb.toString();
  }

  // styles
  protected String baseStyle = "my-tbl-item";
  protected String overStyle = baseStyle + "-over";
  protected String selStyle = baseStyle + "-sel";
  protected String cellStyle = baseStyle + "-" + "cell";
  protected String cellOverflowStyle = baseStyle + "-" + "overflow";
  protected String textStyle = cellStyle + "-text";
  protected String widgetStyle = cellStyle + "-widget";

  protected TableColumnModel cm;
  protected Element scrollElem, dataElem, treeDiv;
  protected TreeTable treeTable;
  protected int scrollBarWidth;

  protected void applyCellStyles(TreeTableItem item) {
    if (item.cellStyles != null) {
      for (int i = 0; i < item.cellStyles.length; i++) {
        setCellStyle(item, i, item.cellStyles[i]);
      }
    }
  }

  protected void clearHoverStyles() {
    TreeItem[] allItems = treeTable.getAllItems();
    for (int i = 0; i < allItems.length; i++) {
      ((TreeTableItem) allItems[i]).getUI().onMouseOut(null);
    }
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

  public Element getScrollElement() {
    return scrollElem;
  }

  public Element getTextCellElement(TreeTableItem item, int cell) {
    return ((TreeTableItemUI) item.getUI()).getTextCellElement(cell);
  }

  public void init(TreeTable treeTable) {
    this.treeTable = treeTable;
    this.cm = treeTable.getColumnModel();
  }

  protected void removeItem(TableItem item) {
    DOM.removeChild(dataElem, item.getElement());
  }

  protected void render() {
    scrollBarWidth = MyDOM.getScrollBarWidth();

    Element div = DOM.createDiv();
    DOM.setInnerHTML(div, bodyHTML.toString());
    scrollElem = MyDOM.getSubChild(div, 2);
    dataElem = DOM.getFirstChild(scrollElem);
    treeDiv = DOM.getFirstChild(dataElem);
    DOM.appendChild(treeDiv, treeTable.getRootItem().getElement());
    DOM.appendChild(treeTable.getElement(), DOM.getFirstChild(div));

    if (!MyGWT.isIE) {
      DOM.setElementPropertyInt(treeTable.getElement(), "tabIndex", 0);
    }

    treeTable.disableTextSelection(true);

    DOM.sinkEvents(scrollElem, Event.ONSCROLL);
  }

  protected void renderItemValue(TreeTableItem item, int index, Object value) {
    Element textElem = getTextCellElement(item, index);
    if (textElem != null) {
      DOM.setInnerHTML(textElem, "");
      if (value instanceof Widget) {
        Widget widget = (Widget) value;
        MyDOM.setStyleName(textElem, widgetStyle);
        DOM.appendChild(textElem, widget.getElement());
        if (treeTable.isAttached()) {
          WidgetHelper.doAttach(widget);
        }
      } else {
        String s = treeTable.getRenderedValue(index, value);
        MyDOM.setInnerHTML(textElem, s);
      }
    }
    applyCellStyles(item);
  }

  public void resize() {
    int width = treeTable.getOffsetWidth();
    int headerHeight = treeTable.getTableHeader().getOffsetHeight();
    int bodyHeight = treeTable.getOffsetHeight() - headerHeight;
    int bodyWidth = width;

    MyDOM.setWidth(dataElem, cm.getTotalWidth());

    boolean vscroll = MyDOM.getHeight(dataElem) > bodyHeight;
    int adj = vscroll ? scrollBarWidth : 0;

    if ((treeTable.getStyle() & Style.HORIZONTAL) != 0) {
      if (MyDOM.getWidth(dataElem) < (width - adj)) {
        bodyHeight += scrollBarWidth;
        MyDOM.setLeft(treeTable.getTableHeader().getElement(), 0);
      }
    } else {
      bodyHeight += scrollBarWidth;
    }
    if (MyDOM.getHeight(dataElem) > bodyHeight) {
      width -= scrollBarWidth;
    }

    bodyHeight -= MyDOM.getBorderWidth(treeTable.getElement(), Style.TOP | Style.BOTTOM);
    bodyWidth -= MyDOM.getBorderWidth(treeTable.getElement(), Style.LEFT | Style.RIGHT);

    MyDOM.setSize(scrollElem, bodyWidth, bodyHeight);

    int w = cm.getTotalWidth();

    if (w < width) {
      adj = width - w;
    }
    MyDOM.setWidth(dataElem, cm.getTotalWidth() + adj);
  }

  public void resizeCells(int columnIndex) {
    TreeTableColumn c = (TreeTableColumn) cm.getColumn(columnIndex);
    int w = ((TreeTableColumnModel) cm).getWidthInPixels(c.getIndex());
    String sel = "." + treeTable.getId() + "-col-" + columnIndex;
    String rule = "width:" + w + "px;" + (!c.isHidden() ? "" : "display: none;");
    treeTable.styleTemplate.set(sel, rule);
    treeTable.styleTemplate.apply();
  }

  protected void setCellStyle(TreeTableItem item, int index, String style) {
    if (item.cellsRendered) {
      Element cell = getTextCellElement(item, index);
      MyDOM.setStyleName(cell, textStyle + " " + style);
    }
  }

  protected void showColumn(int columnIndex, boolean show) {
    TreeTableColumn c = (TreeTableColumn) cm.getColumn(columnIndex);
    int w = ((TreeTableColumnModel) cm).getWidthInPixels(c.getIndex());
    String sel = "." + treeTable.getId() + "-col-" + columnIndex;
    String rule = "width:" + w + "px;" + (!c.isHidden() ? "" : "display: none;");
    treeTable.styleTemplate.set(sel, rule);
    treeTable.styleTemplate.apply();
  }
}
