/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.data.DataLoader;
import net.mygwt.ui.client.data.LoadEvent;
import net.mygwt.ui.client.data.Loader;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.table.Table;
import net.mygwt.ui.client.widget.table.TableColumn;
import net.mygwt.ui.client.widget.table.TableItem;

import com.google.gwt.user.client.ui.Widget;

/**
 * A concrete viewer based on a <code>Table</code> widget.
 * 
 * @see Table
 */
public class TableViewer extends ColumnViewer {

  private ViewerCell viewerCell = new ViewerCell();
  private Table table;
  private Loader loader;
  private Listener loadListener;

  /**
   * Creates a new table viewer.
   * 
   * @param table the table
   */
  public TableViewer(Table table) {
    this.table = table;
    table.getElement();
    hookWidget(table);
  }

  public void add(Object elem) {
    renderItem(elem, table.getItemCount());
  }

  public Widget findItem(Object elem) {
    int size = table.getItemCount();
    for (int i = 0; i < size; i++) {
      TableItem item = table.getItem(i);
      Object data = item.getData();

      if (getComparer() != null) {
        if (getComparer().equals(elem, data)) {
          return item;
        } else {
          continue;
        }
      }

      if (elem == data || (elem != null && elem.equals(data))) {
        return item;
      }
    }
    return null;
  }

  public Object[] getActiveElements() {
    ArrayList list = new ArrayList();
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      if (item.isVisible()) {
        list.add(item.getData());
      }
    }
    return list.toArray();
  }
  
  /**
   * Returns the viewer's table widget.
   * 
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  /**
   * Returns the table viewer column for the specified column.
   * 
   * @param columnId the column id
   * @return the table viewer column
   */
  public TableViewerColumn getViewerColumn(int columnId) {
    TableColumn column = table.getColumnModel().getColumn(columnId);
    TableViewerColumn vc = (TableViewerColumn) column.getData(ViewerColumn.COLUMN_VIEWER_KEY);
    if (vc == null) {
      vc = new TableViewerColumn(this, column);
    }
    return vc;
  }

  public Widget getWidget() {
    return table;
  }

  public void refresh() {
    if (loader != null) {
      loader.reload();
    } else {
      super.refresh();
    }
  }

  public void remove(Object elem) {
    TableItem item = (TableItem) findItem(elem);
    if (item != null) {
      table.remove(item);
      removeElement(elem);
      item.setData(null);
    }
  }

  public void setContentProvider(IContentProvider contentProvider) {
    super.setContentProvider(contentProvider);
    if (contentProvider instanceof Loader) {
      bind((Loader) contentProvider);
    }
  }

  public void setSelection(ISelection selection, boolean reveal) {
    List selected = selection.toList();
    int ct = table.getItemCount();

    for (int i = 0; i < ct; i++) {
      TableItem item = table.getItem(i);
      Object elem = item.getData();
      if (selected.contains(elem)) {
        table.select(i);
      } else {
        table.deselect(i);
      }
    }
  }

  public void update() {
    int ct = table.getItemCount();
    for (int i = 0; i < ct; i++) {
      updateInternal(table.getItem(i));
    }
  }

  public void update(Object elem) {
    TableItem item = (TableItem) findItem(elem);
    if (item != null) {
      item.setData(elem);
      updateInternal(item);
    }
  }

  protected void doLocalSort(int col) {
    ViewerColumn vc = getViewerColumn(col);

    final ViewerSorter sorter = vc.getViewerSorter();
    final TableViewer fViewer = this;

    List items = table.getItems();

    Collections.sort(items, new Comparator() {
      public int compare(Object arg0, Object arg1) {
        TableItem item1 = (TableItem) arg0;
        TableItem item2 = (TableItem) arg1;
        Object o1 = item1.getData();
        Object o2 = item2.getData();
        return sorter.compare(fViewer, o1, o2);
      }

    });
    if (getTable().getColumn(col).getSortDir() == Style.DESC) {
      Collections.reverse(items);
    }

  }

  protected void doRemoteSort(BaseEvent be) {
    int col = be.index;
    TableColumn column = getTable().getColumn(col);
    if (column.isSortable()) {
      int dir = column.getSortDir();
      switch (dir) {
        case Style.NONE:
        case Style.DESC:
          dir = Style.ASC;
          break;
        case Style.ASC:
          dir = Style.DESC;
          break;
      }
      loader.sort(column.getID(), dir);
      table.getTableHeader().sort(col, dir);
      be.index = dir;
      be.doit = false;
    }
  }

  protected void doSort(BaseEvent be) {
    if (loader != null && loader.getRemoteSort()) {
      doRemoteSort(be);
      return;
    } else if (loader != null) {
      // update loader sort info
      TableColumn column = getTable().getColumn(be.index);
      loader.setSortDir(be.size);
      loader.setSortField(column.getID());
    }

    int col = be.index;
    doLocalSort(col);

    table.getTableHeader().sort(col, be.size);
    table.getView().reorderItems();
    be.doit = false;

  }

  protected List getSelectedFromWidget() {
    ArrayList elems = new ArrayList();
    for (int i = 0; i < table.getSelection().length; i++) {
      TableItem item = (TableItem) table.getSelection()[i];
      elems.add(item.getData());
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    table.addListener(Events.SortChange, new Listener() {
      public void handleEvent(BaseEvent be) {
        doSort(be);
      }
    });
  }

  protected void insert(Object elem, int index) {
    renderItem(elem, index);
    TableItem item = (TableItem) findItem(elem);
    if (item != null) {
      item.setVisible(isFiltered(null, elem) ? false : true);
    }
  }

  protected void onInputReceived(Object input) {
    renderTable();
  }

  protected void preserveSelections(ISelection selection) {
    Iterator it = selection.iterator();
    while (it.hasNext()) {
      Object elem = it.next();
      TableItem item = (TableItem) findItem(elem);
      if (item != null) {
        table.select(item);
      }
    }
  }

  protected void renderItem(Object elem, int index) {
    int cols = table.getColumnCount();
    Object[] values = new Object[cols];
    String[] toolTips = new String[cols];

    for (int j = 0; j < cols; j++) {
      CellLabelProvider lp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      viewerCell.reset(elem, null, j, table.getColumn(j).getID());
      lp.update(viewerCell);
      Widget widget = viewerCell.getWidget();
      if (widget != null) {
        values[j] = widget;
      } else {
        values[j] = viewerCell.getText();
      }
      toolTips[j] = viewerCell.getToolTipText();
    }
    TableItem item = new TableItem(values);

    item.setData(elem);
    item.setCellToolTips(toolTips);

    table.insert(item, index);
    update(elem);
  }

  protected void renderTable() {
    table.removeAll();
    Object[] elems = elements;
    elems = sortElements(elems);
    for (int i = 0; i < elems.length; i++) {
      Object element = elems[i];
      renderItem(element, i);
    }
    applyFilters();
  }

  private void bind(Loader loader) {
    if (loadListener == null) {
      loadListener = new Listener() {
        public void handleEvent(BaseEvent be) {
          LoadEvent le = (LoadEvent) be;
          switch (be.type) {
            case Loader.BeforeLoad:
              onBeforeLoad(le);
              break;
            case DataLoader.Load:
              onLoad(le);
              break;
            case DataLoader.LoadException:
              onLoadException(le);
              break;
          }
        }
      };
    }
    if (loader != null) {
      loader.removeListener(DataLoader.BeforeLoad, loadListener);
      loader.removeListener(DataLoader.Load, loadListener);
      loader.removeListener(DataLoader.LoadException, loadListener);
    }
    this.loader = loader;
    this.loader.addListener(DataLoader.BeforeLoad, loadListener);
    this.loader.addListener(DataLoader.Load, loadListener);
    this.loader.addListener(DataLoader.LoadException, loadListener);
  }

  private void onBeforeLoad(LoadEvent de) {
    LoadingPanel.get().show(table);
  }

  private void onLoad(LoadEvent de) {
    ISelection sel = getSelection();
    // apply sort if sorting locally
    if (!de.loader.getRemoteSort()) {
      if (de.loader.getSortField() != null) {
        String field = de.loader.getSortField();
        TableColumn column = table.getColumn(field);
        if (column != null) {
          int index = table.getColumnModel().indexOf(column);
          doLocalSort(index);
        }
      }
    }
    setInput(de.result.getData());
    preserveSelections(sel);
    LoadingPanel.get().hide();

  }

  private void onLoadException(LoadEvent le) {
    LoadingPanel.get().hide();
  }

  private void updateInternal(TableItem item) {
    Object elem = item.getData();
    int cols = table.getColumnCount();;
    for (int j = 0; j < cols; j++) {
      viewerCell.reset(elem, item, j, table.getColumn(j).getID());
      CellLabelProvider lp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      lp.update(viewerCell);
      Widget widget = viewerCell.getWidget();
      if (widget != null) {
        item.setValue(j, widget);
      } else {
        String text = viewerCell.getText();
        item.setValue(j, text);
      }
      item.setCellToolTip(j, viewerCell.getToolTipText());

      String style = viewerCell.getTextStyle();
      if (style != null) {
        item.setCellStyle(j, style);
      }
    }
  }

}
