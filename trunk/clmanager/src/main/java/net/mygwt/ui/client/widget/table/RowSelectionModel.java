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
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Row selection model.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * </dl>
 */
public class RowSelectionModel implements Listener {

  protected int style;
  protected Table table;
  protected List selectedItems;
  protected TableItem lastSelected;

  /**
   * Creates a new selection model.
   * 
   * @param style the style information
   */
  public RowSelectionModel(int style) {
    this.style = style;
    selectedItems = new ArrayList();
  }

  /**
   * Deselects the item at the given index. If the item at the index was already
   * deselected, it remains deselected.
   * 
   * @param index the item to deselect
   */
  public void deselect(int index) {
    selectItems(index, index, false, true);
  }

  /**
   * Deselects the items at the given indices. If the item at the given index is
   * selected, it is deselected. If the item at the index was not selected, it
   * remains deselected.
   * 
   * @param begin the start index
   * @param end the end index
   */
  public void deselect(int begin, int end) {
    selectItems(begin, end, false, true);
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    while (selectedItems.size() > 0) {
      TableItem item = (TableItem) selectedItems.remove(0);
      deselect(table.indexOf(item));

    }
    lastSelected = null;
  }

  public void handleEvent(BaseEvent be) {
    switch (be.type) {
      case Events.CellClick:
        onCellClick(be);
        break;
      case Events.CellDoubleClick:
        onCellDoubleClick(be);
        break;
      case Events.KeyDown:
        onKeyPress(be);
        break;
      case Events.Remove:
        TableItem item = (TableItem) be.item;
        if (lastSelected == item) {
          lastSelected = null;
        }
        selectedItems.remove(item);
    }
  }

  /**
   * Returns <code>true</code> if the row is selected.
   * 
   * @param index the row index
   * @return the selected state
   */
  public boolean isSelected(int index) {
    TableItem item = table.getItem(index);
    return selectedItems.contains(item);
  }

  /**
   * Refreshes the selections.
   */
  public void refresh() {
    int rows = table.getItemCount();
    for (int i = 0; i < rows; i++) {
      TableItem item = table.getItem(i);
      item.setSelected(isSelected(i));
    }
  }

  /**
   * Selects the item at the given index. If the item at the index was already
   * selected, it remains selected.
   * 
   * @param index the row to select
   */
  public void select(int index) {
    selectItems(index, index, true, true);
  }

  /**
   * Selects the item in the range specified by the given indices. The current
   * selection is not cleared before the new rows are selected.
   * 
   * @param start the start of the range
   * @param end the end of the range
   */
  public void select(int start, int end) {
    selectItems(start, end, true, true);
  }

  /**
   * Selects all items.
   */
  public void selectAll() {
    selectItems(0, table.getItemCount() - 1, true, true);
  }

  protected void init(Table table) {
    this.table = table;
    table.addListener(Events.CellClick, this);
    table.addListener(Events.CellDoubleClick, this);
    table.addListener(Events.KeyDown, this);
    table.addListener(Events.Remove, this);
  }

  protected void unbind(Table table) {
    this.table = null;
    table.removeListener(Events.CellClick, this);
    table.removeListener(Events.CellDoubleClick, this);
    table.removeListener(Events.KeyDown, this);
    table.removeListener(Events.Remove, this);
  }

  protected void onCellClick(BaseEvent be) {
    int row = be.rowIndex;
    boolean shiftkey = be.isShiftKey();
    boolean controlKey = be.isControlKey();

    boolean single = style == Style.SINGLE;
    if (single) {
      selectItems(row, row, !(isSelected(row) && controlKey), false);
      return;
    }

    if (be.isRightClick()) {
      TableItem item = table.findItem(be.getTarget());
      if (selectedItems.contains(item)) {
        return;
      }
    }

    if (shiftkey) {
      if (lastSelected != null) {
        selectItems(table.indexOf(lastSelected), row, true, false);
      } else {
        selectItems(0, row, true, false);
      }
    } else if (controlKey) {
      selectItems(row, row, !isSelected(row), true);
    } else {
      selectItems(row, row, true, false);
    }

    // safari needs focus put on table of click unless
    // the source is a widget - determined by sunk events
    if (MyGWT.isSafari) {
      Element elem = be.getTarget();
      if (DOM.getEventsSunk(elem) == 0) {
        table.focus();
      }

    }
  }

  protected void onCellDoubleClick(BaseEvent be) {

  }

  protected void onKeyPress(BaseEvent be) {
    int code = be.getKeyCode();
    switch (code) {
      case KeyboardListener.KEY_UP: {
        int index = table.indexOf(lastSelected) - 1;
        selectItems(index, index, true, false);
        be.stopEvent();
        break;
      }
      case KeyboardListener.KEY_DOWN: {
        int index = table.indexOf(lastSelected) + 1;
        selectItems(index, index, true, false);
        be.stopEvent();
        break;
      }

    }
  }

  protected void remove(TableItem item) {
    if (lastSelected == item) {
      lastSelected = null;
    }
    if (selectedItems.remove(item)) {
      BaseEvent be = new BaseEvent();
      be.widget = table;
      table.fireEvent(Events.SelectionChange, be);
    }
  }

  protected void selectItems(final int start, final int end, final boolean state,
      final boolean keepSelected) {
    if (!table.isRendered()) {
      table.addListener(Events.Render, new Listener() {
        public void handleEvent(BaseEvent be) {
          selectItems(start, end, state, keepSelected);
        }
      });
      return;
    }
    if (start < 0 || end >= table.getItemCount()) return;
    if (!keepSelected) {
      for (int i = 0; selectedItems.size() > 0; i++) {
        TableItem item = (TableItem) selectedItems.remove(0);
        item.setSelected(false);
      }
    }
    lastSelected = table.getItem(end);
    int beginIndex = start < end ? start : end;
    int endIndex = start < end ? end : start;
    for (int i = beginIndex; i <= endIndex; i++) {
      boolean sel = isSelected(i);
      TableItem item = table.getItem(i);
      if (state) {
        if (!sel) {
          selectedItems.add(item);
        }
        table.scrollIntoView(item);
      } else {
        selectedItems.remove(item);
      }
      item.setSelected(state);
    }

    BaseEvent be = new BaseEvent();
    be.widget = table;
    table.fireEvent(Events.SelectionChange, be);
  }

  List getSelection() {
    return selectedItems;
  }

}
