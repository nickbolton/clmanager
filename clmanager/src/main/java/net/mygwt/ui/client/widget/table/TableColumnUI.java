/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.SplitBar;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class TableColumnUI extends Component {

  protected static String html;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=my-tbl-col-overflow style='text-align: {0}'>");
    sb.append("<div class=my-tbl-col-text>{1}</div>");
    sb.append("</div>");
    html = sb.toString();
  }

  protected int lastWidth;
  protected boolean end;
  protected int index;
  protected ITable table;
  protected TableColumn column;
  protected TableHeader header;
  protected SplitBar splitBar;
  protected Listener splitBarListener = new Listener() {
    public void handleEvent(BaseEvent be) {
      switch (be.type) {
        case Events.DragStart:
          header.setEnabled(false);
          break;
        case Events.DragEnd:
          header.setEnabled(true);
          break;
      }
    }
  };

  protected TableColumnUI(ITable table, int index) {
    this.table = table;
    this.index = index;
    column = table.getColumn(index);
    header = table.getTableHeader();
  }

  public void onBaseEvent(BaseEvent be) {
    switch (be.type) {
      case Events.MouseOver:
        onMouseOver(be);
        break;
      case Events.MouseOut:
        onMouseOut(be);
        break;
      case Events.Click:
        header.onColumnClick(this);
        break;
    }
  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    if (DOM.eventGetType(event) == Event.ONMOUSEUP) {
      if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT
          || (MyGWT.isMac && DOM.eventGetCtrlKey(event))) {
        header.onRightClick(column, event);
      }
    }
  }

  protected void onColumnResize(BaseEvent be) {
    if (be.size < 1) {
      return;
    }
    if (column.getWidth() < 1.1) {
      int pixWidth = be.size - 6;
      float percent = (float) pixWidth / table.getColumnModel().getVariableWidth();
      table.getColumnModel().setWidthAsPercent(column.index, percent);
      header.resizeColumns(true, true);
    } else {
      column.setWidth(be.size);
      header.resizeColumn(index, true);
    }
  }
  
  protected void onMouseMove(BaseEvent be) {
    header.onColumnMouseMove(this, be);
  }

  protected void onMouseOut(BaseEvent be) {
    removeStyleName("my-tbl-col-over");
  }

  protected void onMouseOver(BaseEvent be) {
    addStyleName("my-tbl-col-over");
  }

  protected void onRender() {
    String[] params = null;
    if (end) {
      params = new String[] {"", ""};
    } else {
      params = new String[] {getAlignment(), column.getText()};
    }
    String s = Format.substitute(html, params);
    setElement(MyDOM.create(s));
    MyDOM.setHeight(getElement(), 24);

    if (!end && column.isResizable()) {
      splitBar = header.createSplitBar(Style.EAST, this);
      splitBar.setBarWidth(6);
      splitBar.setXOffset(-2);
      splitBar.setAutoSize(false);
      splitBar.addListener(Events.Resize, new Listener() {
        public void handleEvent(BaseEvent be) {
          onColumnResize(be);
        }
      });
      splitBar.addListener(Events.DragStart, splitBarListener);
      splitBar.addListener(Events.DragEnd, splitBarListener);
    }

    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
  }

  protected void onSortChange(int sortDir) {
    Element td = DOM.getParent(getElement());
    switch (sortDir) {
      case Style.ASC:
        MyDOM.addStyleName(td, "my-tbl-col-sort");
        addStyleName("my-tbl-col-asc");
        break;
      case Style.DESC:
        MyDOM.addStyleName(td, "my-tbl-col-sort");
        addStyleName("my-tbl-col-desc");
        break;
      default:
        MyDOM.removeStyleName(td, "my-tbl-col-sort");
        removeStyleName("my-tbl-col-asc");
        removeStyleName("my-tbl-col-desc");
        break;
    }
  }

  protected void onTextChange(String text) {
    Element textEl = MyDOM.findChild("my-tbl-col-text", getElement());
    MyDOM.setInnerHTML(textEl, text);
  }

  private String getAlignment() {
    String align = "left";
    if (column != null) {
      if (column.getAlignment() == Style.CENTER) {
        align = "center";
      } else if (column.getAlignment() == Style.RIGHT) {
        align = "right";
      }
    }
    return align;
  }

}
