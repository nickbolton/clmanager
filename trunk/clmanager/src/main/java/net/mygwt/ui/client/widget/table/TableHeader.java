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
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.util.DelayedTask;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.SplitBar;
import net.mygwt.ui.client.widget.menu.Menu;
import net.mygwt.ui.client.widget.menu.MenuItem;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.WidgetHelper;

public class TableHeader extends Component {

  protected static String html;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=my-tbl-header style='position: relative'>");
    sb.append("<table border=0 cellpadding=0 cellspacing=0 style='position:relative'><tbody><tr class=my-tbl-header-row>");
    sb.append("</tr></table></div>");
    html = sb.toString();
  }

  protected List columns;
  protected ITable table;
  protected TableColumnModel columnModel;
  protected TableColumnUI sortColumn, endColumn, hoverColumn;
  protected Element headerRow;
  private DelayedTask task = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      updateSplitBars();
    }
  });
  
  protected TableHeader() {
    
  }

  protected TableHeader(ITable table) {
    this.table = table;
    this.columnModel = this.table.getColumnModel();
  }
  
  public void sort(int index, int dir) {
    TableColumn column = table.getColumn(index);
    column.sortDir = dir;
    onSortChange(column, dir);
  }
  
  protected void addColumn(TableColumnUI ui) {
    Element td = DOM.createTD();
    MyDOM.setStyleName(td, "my-tbl-col");
    DOM.appendChild(td, ui.getElement());
    DOM.appendChild(headerRow, td);
    columns.add(ui);
  }

  protected SplitBar createSplitBar(int direction, TableColumnUI column) {
    return new SplitBar(direction, column, (Component) table);
  }

  protected TableColumnUI createTableColumnUI(int index) {
    return new TableColumnUI(table, index);
  }

  protected void doAttachChildren() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      WidgetHelper.doAttach(getColumnUI(i));
    }
  }

  protected void doDetachChildren() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      WidgetHelper.doDetach(getColumnUI(i));
    }
  }

  protected TableColumnUI getColumnUI(int index) {
    return (TableColumnUI) columns.get(index);
  }

  protected void init(ITable table) {
    this.table = table;
    this.columnModel = this.table.getColumnModel();
  }
  
  protected void onColumnClick(TableColumnUI columnUI) {
    BaseEvent be = new BaseEvent((Component) table);
    be.index = columnUI.index;

    if (!((Component) table).fireEvent(Events.ColumnClick, be)) {
      return;
    }

    if (columnUI.column.isSortable()) {
  
      int sortDir = columnUI.column.sortDir;
      switch (sortDir) {
        case Style.NONE:
        case Style.DESC:
          sortDir = Style.ASC;
          break;
        default:
          sortDir = Style.DESC;
      }
      table.sort(be.index, sortDir);
    }
  }

  protected void onColumnMouseMove(TableColumnUI column, BaseEvent be) {
    
  }

  protected void onRender() {
    setElement(MyDOM.create(html));
    headerRow = MyDOM.findChild("my-tbl-header-row", getElement());

    columns = new ArrayList();
    int cols = columnModel.getColumnCount();
    for (int i = 0; i < cols; i++) {
      TableColumnUI columnUI = createTableColumnUI(i);
      addColumn(columnUI);
    }
    endColumn = createTableColumnUI(cols);  
    endColumn.end = true;
    addColumn(endColumn);
    
    disableContextMenu(true);
  }

  protected void onRightClick(final TableColumn column, Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);

    if (!table.getColumnContextMenu()) {
      return;
    }

    final int x = DOM.eventGetClientX(event);
    final int y = DOM.eventGetClientY(event);
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onShowContextMenu(column).show(x, y);
      }
    });
  }

  protected Menu onShowContextMenu(final TableColumn column) {
    final Menu menu = new Menu();

    if (column.isSortable()) {
      MenuItem item = new MenuItem(Style.PUSH);
      item.setText(MyGWT.MESSAGES.sortAsc());
      item.setIconStyle("icon-sort-asc");
      item.addSelectionListener(new SelectionListener() {
        public void widgetSelected(BaseEvent be) {
          table.sort(column.index, Style.ASC);
        }

      });
      menu.add(item);

      item = new MenuItem(Style.PUSH);
      item.setText(MyGWT.MESSAGES.sortDesc());
      item.setIconStyle("icon-sort-desc");
      item.addSelectionListener(new SelectionListener() {
        public void widgetSelected(BaseEvent be) {
          table.sort(column.index, Style.DESC);
        }
      });
      menu.add(item);
    }

    MenuItem columns = new MenuItem(Style.MENU);
    columns.setText(MyGWT.MESSAGES.columns());
    columns.setIconStyle("icon-columns");

    final Menu columnMenu = new Menu();

    int cols = columnModel.getColumnCount();
    for (int i = 0; i < cols; i++) {
      final TableColumn def = columnModel.getColumn(i);
      final MenuItem check = new MenuItem(Style.CHECK);
      check.setHideOnClick(false);
      check.setText(def.getText());
      check.setSelected(!def.isHidden());
      check.addSelectionListener(new SelectionListener() {

        public void widgetSelected(BaseEvent be) {
          def.setHidden(!check.isSelected());
          showColumn(def.index, !def.isHidden());

          if (columnModel.getVariableColumnCount() > 0) {
            resizeColumns(false, true);
          }

          if (columnModel.getVisibleColumnCount() == 1) {
            int count = columnMenu.getItemCount();
            for (int j = 0; j < count; j++) {
              MenuItem item = columnMenu.getItem(j);
              if (item.isSelected()) {
                item.disable();
              }
            }
          } else if (columnModel.getVisibleColumnCount() == 2) {
            int count = columnMenu.getItemCount();
            for (int j = 0; j < count; j++) {
              MenuItem item = columnMenu.getItem(j);
              item.enable();
            }
          }
        }

      });
      columnMenu.add(check);

      if (columnModel.getVisibleColumnCount() == 1) {
        int count = columnMenu.getItemCount();
        for (int j = 0; j < count; j++) {
          MenuItem item = columnMenu.getItem(j);
          if (item.isSelected()) {
            item.disable();
          }
        }
      }
    }

    columns.setSubMenu(columnMenu);
    menu.add(columns);

    return menu;
  }
  
  public int getSortColumn() {
    if (sortColumn != null) {
      return sortColumn.index;
    }
    return Style.DEFAULT;
  }

  protected void onSortChange(TableColumn column, int sortDir) {
    column.sortDir = sortDir;
    if (sortColumn != null) {
      getColumnUI(sortColumn.index).onSortChange(Style.NONE);
    }
    sortColumn = getColumnUI(column.index);
    sortColumn.onSortChange(sortDir);
  }

  protected void resizeColumn(int index, boolean resizeBody) {
    TableColumn column = columnModel.getColumn(index);
    TableColumnUI ui = getColumnUI(index);
    if (column.isHidden()) {
      ui.setVisible(false);
      return;
    } else {
      ui.setVisible(true);
    }
    int w = columnModel.getWidthInPixels(column.index);

    if (w != ui.lastWidth) {
      Element td = DOM.getParent(ui.getElement());
      w -= MyDOM.getBorderWidth(td, Style.LEFT | Style.RIGHT);

      MyDOM.setWidth(ui.getElement(), w);

      SplitBar splitBar = ui.splitBar;
      if (splitBar != null) {
        if (column.isResizable()) {
          splitBar.setVisible(true);
        }
        splitBar.setMinSize(column.getMinWidth());
        splitBar.setMaxSize(column.getMaxWidth());
      }
      task.delay(400);
      doTableComponentResizeCells(index);
    }

    ui.lastWidth = w;

    if (resizeBody) {
      doTableComponentResize();
    }
  }

  protected void resizeColumns(boolean fireEvent, boolean resizeBody) {
    int cols = columnModel.getColumnCount();
    int totWidth = 0;
    for (int i = 0; i < cols; i++) {
      resizeColumn(i, false);
    }

    int tableWidth = ((Component) table).getOffsetWidth();
    if (totWidth < tableWidth) {
      MyDOM.setWidth(endColumn.getElement(), tableWidth - totWidth + 25);
    } else {
      MyDOM.setWidth(endColumn.getElement(), 25);
    }
    task.delay(400);

    if (resizeBody) {
      doTableComponentResize();
    }

  }

  protected void showColumn(int index, boolean show) {
    TableColumnUI ui = getColumnUI(index);
    Element td = DOM.getParent(ui.getElement());
    MyDOM.setVisible(td, show);
    doTableComponentShowColumn(index, show);
    updateSplitBars();
    doTableComponentResize();
  }

  protected void updateSplitBars() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      TableColumnUI ui = getColumnUI(i);
      if (ui.splitBar != null) {
        ui.splitBar.updateHandle();
      }
    }
  }

  protected void doTableComponentResize() {
    if (table instanceof Table) {
      ((Table) table).getView().resize();
    }
  }
  
  protected void doTableComponentResizeCells(int columnIndex) {
    if (table instanceof Table) {
      ((Table) table).getView().resizeCells(columnIndex);
    }
  }
  
  protected void doTableComponentShowColumn(int index, boolean show) {
    if (table instanceof Table) {
      ((Table) table).getView().showColumn(index, show);
    }
  }

}
