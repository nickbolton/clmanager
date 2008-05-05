/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Markup;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.ToolTip;
import net.mygwt.ui.client.widget.table.TableColumnModel;
import net.mygwt.ui.client.widget.tree.TreeItemUI;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class TreeTableItemUI extends TreeItemUI {

  protected static Listener listener = new Listener() {
    public void handleEvent(BaseEvent be) {
      TreeTableItem item = (TreeTableItem) be.widget;
      TreeItemUI ui = item.getUI();
      Element target = be.getTarget();
      int type = be.type;
      switch (type) {
        case Events.MouseOver:
        case Events.MouseOut:
          treeTableMouseListener.handleEvent(be);
          break;
        case Events.Click:
        case Events.DoubleClick:
          be.stopEvent();
          if (DOM.isOrHasChild(ui.getCheckEl(), target)) {
            item.setChecked(!item.isChecked());
          } else {
            clickListener.handleEvent(be);
          }
          break;
      }
    }

  };

  protected static Listener treeTableMouseListener = new Listener() {
    public void handleEvent(BaseEvent be) {
      TreeTableItem item = (TreeTableItem) be.widget;
      TreeTableItemUI treeUI = (TreeTableItemUI) item.getUI();
      Rectangle rect = MyDOM.getBounds(DOM.getFirstChild(item.getElement()));
      if (rect.contains(be.getClientX(), be.getClientY())) {
        if (!treeUI.hovering) {
          treeUI.hovering = true;
          treeUI.onMouseOver(be);
        }
      } else {
        treeUI.hovering = false;
        treeUI.onMouseOut(be);
      }

    }
  };

  boolean hovering;
  private Element tableItemEl;
  private Element rowTableEl;
  private Element rowTrEl;
  private Element[] cells;

  public TreeTableItemUI(TreeTableItem item) {
    super(item);
    styleTreeOver = "my-treetbl-over";
    styleTreeJointOver = "my-treetbl-joint-over";
    styleTreeChecked = "my-treetbl-checked";
    styleTreeNotChecked = "my-treetbl-notchecked";
    styleTreeLoading = "my-treetbl-loading";
    styleTreeSelected = "my-treetbl-sel";
    classTreeOpen = "my-treetbl-open";
    classTreeClose = "my-treetbl-close";
    render();
  }

  public Element getContainer() {
    return containerEl;
  }

  public Listener getListener() {
    return listener;
  }

  public TreeTableItem getTreeTableItem() {
    return (TreeTableItem) item;
  }

  public void onMouseOut(BaseEvent be) {
    ToolTip tooltip = getTreeTableItem().getCellToolTip();
    if (tooltip != null && tooltip.isShowing()) {
      tooltip.hide();
    }
    if (!item.isRoot()) {
      MyDOM.setStyleName(tableItemEl, "my-treetbl-item-over", false);
    }
  }

  public void onSelectedChange(boolean selected) {
    if (item.isRendered()) {
      MyDOM.setStyleName(tableItemEl, "my-treetbl-item-sel", selected);
    }
  }

  public void setContainer(Element container) {
    containerEl = container;
  }

  protected Element getTextCellElement(int column) {
    if (column == 0) {
      return textSpanEl;
    } else {
      return MyDOM.getSubChild(cells[column], 2);
    }
  }

  protected void onMouseOver(BaseEvent be) {
    if (!item.isRoot()) {
      MyDOM.setStyleName(tableItemEl, "my-treetbl-item-over", true);
    }
  }

  protected void onValuesChanged(TreeTable table, String[] values) {
    onTextChange(item.getText());
    for (int i = 1; i < cells.length; i++) {
      updateText(i, values[i]);
    }
  }

  protected void render() {
    if (item.isRoot() == true) {
      return;
    }

    item.setElement(DOM.createDiv());
    item.setStyleName("my-treeitem");

    TableColumnModel cm = getTreeTableItem().getTreeTable().getColumnModel();

    DOM.appendChild(item.getParentItem().getContainer(), item.getElement());
    DOM.setInnerHTML(item.getElement(), Markup.TREETABLE_ITEM);
    tableItemEl = DOM.getFirstChild(item.getElement());

    rowTableEl = DOM.getFirstChild(tableItemEl);
    rowTrEl = MyDOM.getSubChild(rowTableEl, 2);
    updateCellValues(0, DOM.getFirstChild(rowTrEl), cm.getColumn(0).getAlignment());

    itemEl = MyDOM.getSubChild(rowTrEl, 4);
    Element td = MyDOM.getSubChild(itemEl, 3);
    indentEl = DOM.getFirstChild(td);
    jointEl = DOM.getNextSibling(td);
    jointDivEl = DOM.getFirstChild(jointEl);
    checkEl = DOM.getNextSibling(DOM.getNextSibling(jointEl));
    checkDivEl = DOM.getFirstChild(checkEl);
    iconEl = DOM.getNextSibling(checkEl);
    iconDivEl = DOM.getFirstChild(iconEl);
    textEl = DOM.getNextSibling(iconEl);
    textSpanEl = DOM.getFirstChild(textEl);
    Element tbl = DOM.getFirstChild(item.getElement());
    containerEl = DOM.getNextSibling(tbl);

    int numColumns = cm.getColumnCount();
    cells = new Element[numColumns];

    for (int i = 1; i < numColumns; i++) {
      cells[i] = DOM.createTD();
      DOM.appendChild(rowTrEl, cells[i]);

      DOM.setElementProperty(cells[i], "className", "my-treetbl-cell");
      DOM.setElementAttribute(cells[i], "index", String.valueOf(i));

      Element overflowDiv = DOM.createDiv();
      DOM.setElementProperty(overflowDiv, "className", "my-treetbl-cell-overflow");
      DOM.appendChild(cells[i], overflowDiv);
      Element textDiv = DOM.createDiv();
      
      String textStyle = "my-treetbl-cell-text";
      if (((TreeTableItem)item).getCellStyle(i) != null) {
        textStyle += " " + ((TreeTableItem)item).getCellStyle(i);
      }
      DOM.setElementProperty(textDiv, "className", textStyle);
      DOM.appendChild(overflowDiv, textDiv);
      updateCellValues(i, cells[i], cm.getColumn(i).getAlignment());
    }

    int style = getTreeTableItem().getTreeTable().getStyle();
    if ((style & Style.CHECK) != 0) {
      MyDOM.setVisible(checkEl, true);
    } else {
      MyDOM.setVisible(checkEl, false);
    }

    onValuesChanged(getTreeTableItem().getTreeTable(),
        getTreeTableItem().getRenderedValues());

    onIconStyleChange(item.getIconStyle());

    if (item.isChecked()) {
      onCheckChange(true);
    }

    MyDOM.setWidth(indentEl, getIndent());

    if (!MyGWT.isIE) {
      DOM.setElementPropertyInt(item.getElement(), "tabIndex", 0);
    }
    
    getTreeTableItem().initCellToolTips();

    updateJoint();
    item.disableTextSelection(true);

  }

  protected void updateCellValues(int col, Element cell, int align) {
    String salign = "left";
    if (align == Style.CENTER) {
      salign = "center";
    } else if (align == Style.RIGHT) {
      salign = "right";
    }

    String widthClassName = ((TreeTableItem) item).treeTable.getId() + "-col-" + col;

    String className = DOM.getElementProperty(cell, "className");
    className = (className == null) ? widthClassName : className + " " + widthClassName;
    DOM.setElementProperty(cell, "className", className);

    className = DOM.getElementProperty(DOM.getFirstChild(cell), "className");
    className = (className == null) ? widthClassName : className + " " + widthClassName;
    DOM.setElementProperty(DOM.getFirstChild(cell), "className", className);

    DOM.setStyleAttribute(MyDOM.getSubChild(cell, 2), "textAlign", salign);
  }

  protected void updateText(int column, String value) {
    Element textElem = getTextCellElement(column);
    if (textElem != null) {
      MyDOM.setInnerHTML(textElem, value);
    }
  }
}
