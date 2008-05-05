/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.DelayedTask;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.util.StyleTemplate;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.menu.Menu;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * The table is used to display two-dimensional table of cells.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, HORIZONTAL</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item add)<br>
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
 * <dd><b>Add</b> : (widget, item)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget this, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectionChange</b> : (widget this)<br>
 * <div>Fired after the selection has changed</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : (widget)<br>
 * <div>Fires before the tables context menu is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellClick</b> : (widget, rowIndex, index, event)<br>
 * <div>Fired after a cell is clicked</div>
 * <ul>
 * <li>widget : this</li>
 * <li>rowIndex : row index</li>
 * <li>index : cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellDoubleClick</b> : (widget, rowIndex, index, event)<br>
 * <div>Fired after a cell is double clicked</div>
 * <ul>
 * <li>widget : this</li>
 * <li>rowIndex : row index</li>
 * <li>index : cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ColumnClick</b> : (widget, index column index)<br>
 * <div>Fired after a column is clicked</div>
 * <ul>
 * <li>widget : this</li>
 * <li>index : the column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowClick</b> : (widget, rowIndex, index cell index, event)<br>
 * <div>Fired after a row is clicked</div>
 * <ul>
 * <li>widget : this</li>
 * <li>rowIndex : the row index</li>
 * <li>index : the cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowDoubleClick</b> : (widget, rowIndex, index cell index, event)<br>
 * <div>Fired after a row is double clicked</div>
 * <ul>
 * <li>widget : this</li>
 * <li>rowIndex : the row index</li>
 * <li>index : the cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SortChange</b> : (widget, index, size)<br>
 * <div>Fires before the table is sorted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>index : the column index</li>
 * <li>size : the sort direction</li>
 * </ul>
 * </dd>
 * 
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p>
 * 
 * @see TableColumn
 * @see TableColumnModel
 */
public class Table extends Component implements ITable {

  /**
   * The table's column model.
   */
  protected TableColumnModel cm;

  /**
   * The selection model.
   */
  protected RowSelectionModel sm;

  /**
   * The table header.
   */
  protected TableHeader header;

  boolean verticalLines;
  StyleTemplate styleTemplate = null;
  private List items;
  private Map nodes = new HashMap();
  private TableView view;
  private boolean disableColumnContextMenu;
  private boolean highlight;
  private Size lastSize;

  private int lastLeft;

  private DelayedTask scrollTask = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      header.updateSplitBars();
    }
  });

  /**
   * Creates a new single select table. A column model must be set before the
   * table is rendered.
   */
  public Table() {
    super(Style.SINGLE | Style.FOCUSABLE);
    init();
  }

  /**
   * Creates a new table.
   * 
   * @param style the style information
   * @param cm the column model
   */
  public Table(int style, TableColumnModel cm) {
    super(style | Style.FOCUSABLE);
    this.cm = cm;
    cm.table = this;
    init();
  }

  /**
   * Adds a item to the table.
   * 
   * @param item the item to be added
   */
  public void add(TableItem item) {
    insert(item, getItemCount());
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param index the item to deselect
   */
  public void deselect(int index) {
    sm.deselect(index);
  }

  /**
   * Deselects all items.
   */
  public void deselectAll() {
    sm.deselectAll();
  }

  /**
   * Returns the item using the specified target.
   * 
   * @param element the element or child element
   * @return the item
   */
  public TableItem findItem(Element element) {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      TableItem item = getItem(i);
      if (DOM.isOrHasChild(item.getElement(), element)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the column at the specified index.
   * 
   * @param index the column index
   * @return the column
   */
  public TableColumn getColumn(int index) {
    return cm.getColumn(index);
  }

  /**
   * Returns the column with the given id.
   * 
   * @param id the column id
   * @return the column
   */
  public TableColumn getColumn(String id) {
    return cm.getColumn(id);
  }

  /**
   * Returns the column context menu enabed state.
   * 
   * @return <code>true</code> if enabled, <code>false</code> otherwise.
   */
  public boolean getColumnContextMenu() {
    return !disableColumnContextMenu;
  }

  /**
   * Returns the number of columns contained in the table.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return cm.getColumnCount();
  }

  /**
   * Returns the table's column model.
   * 
   * @return the column model
   */
  public TableColumnModel getColumnModel() {
    return cm;
  }

  public Menu getContextMenu() {
    return super.getContextMenu();
  }

  /**
   * Returns the item at the given index.
   * 
   * @param index the index of the item to return
   * @return the item at the given index or <code>null</code>
   */
  public TableItem getItem(int index) {
    if (index < 0 || index >= getItemCount()) return null;
    return (TableItem) items.get(index);
  }

  /**
   * Returns the number of items contained in the table.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the table's items.
   * 
   * @return the table items
   */
  public List getItems() {
    return items;
  }

  /**
   * Returns the selected item. If the list is multi-select, returns the first
   * selected item.
   * 
   * @return the item or <code>null</code> if no selections
   */
  public TableItem getSelectedItem() {
    return sm.getSelection().size() == 0 ? null : (TableItem) sm.getSelection().get(0);
  }

  /**
   * Returns an array of <code>TableItems</code> that are currently selected.
   * 
   * @return a list of selected items
   */
  public TableItem[] getSelection() {
    return (TableItem[]) sm.getSelection().toArray(new TableItem[0]);
  }

  /**
   * Returns the table's selection model.
   * 
   * @return the selection model
   */
  public RowSelectionModel getSelectionModel() {
    return sm;
  }

  /**
   * Returns the table's style information.
   * 
   * @return the style information
   */
  public int getStyle() {
    return style;
  }

  /**
   * Returns the table's header.
   * 
   * @return the table header
   */
  public TableHeader getTableHeader() {
    if (header == null) {
      header = new TableHeader(this);
    }
    return header;
  }

  /**
   * Returns <code>true</code> if vertical lines are enabled.
   * 
   * @return the vertical line state
   */
  public boolean getVerticalLines() {
    return verticalLines;
  }

  /**
   * Returns the table's view.
   * 
   * @return the view
   */
  public TableView getView() {
    if (view == null) {
      view = new TableView();
    }
    return view;
  }

  /**
   * Returns the index of the item or -1 if not found.
   * 
   * @param item the search item
   * @return the index of the item or -1 if not found
   */
  public int indexOf(TableItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts a item into the table.
   * 
   * @param item the item to insert
   * @param index the insert location
   */
  public void insert(TableItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      items.add(index, item);
      register(item);
      if (rendered) {
        view.renderItem(item, index);
      }
      fireEvent(Events.Add, this, item, index);
    }
  }

  public boolean isHighlight() {
    return highlight;
  }

  public void onBaseEvent(BaseEvent be) {
    TableItem item = findItem(be.getTarget());
    if (item != null) {
      item.onBaseEvent(be);
    }
  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    int type = DOM.eventGetType(event);

    if (type == Event.ONSCROLL) {
      int left = MyDOM.getScrollLeft(view.getScrollElement());
      if (left == lastLeft) {
        return;
      }
      lastLeft = left;
      MyDOM.setLeft(header.getElement(), -left);
      scrollTask.delay(400);
    }
  }

  /**
   * Recalculates the ui based on the table's current size.
   */
  public void recalculate() {
    if (isRendered()) {
      int h = getOffsetHeight();
      int w = getOffsetWidth();
      if (lastSize != null) {
        if (lastSize.width == w && lastSize.height == h) {
          return;
        }
      }
      lastSize = new Size(w, h);
      header.resizeColumns(false, true);
    }
  }

  /**
   * Removes the item from the table.
   * 
   * @param item the item to be removed
   */
  public void remove(TableItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      sm.remove(item);
      items.remove(item);
      unregister(item);
      if (rendered) {
        view.removeItem(item);
      }
      fireEvent(Events.Remove, this, item);
    }
  }

  /**
   * Removes all the item's.
   */
  public void removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(TableItem item) {
    MyDOM.scrollIntoView(item.getElement(), view.getScrollElement(), false);
  }

  /**
   * Selects the item at the given index.
   * 
   * @param index the row to select
   */
  public void select(int index) {
    sm.select(index);
  }

  /**
   * Selects the the item.
   * 
   * @param item the item to be selected
   */
  public void select(TableItem item) {
    select(indexOf(item));
  }

  /**
   * Sets whether the column context menu is enabled. Initial value is
   * <code>true</code>.
   * 
   * @param enabled the enabled state
   */
  public void setColumnContextMenu(boolean enabled) {
    this.disableColumnContextMenu = !enabled;
  }

  public void setColumnModel(TableColumnModel cm) {
    this.cm = cm;
    cm.table = this;
  }

  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  public void setHighlight(boolean highlight) {
    this.highlight = highlight;
  }

  /**
   * Sets the table's selection model.
   * 
   * @param sm the selection model
   */
  public void setSelectionModel(RowSelectionModel sm) {
    if (sm != null) {
      this.sm.unbind(this);
      this.sm = null;
    }
    this.sm = sm;
    this.sm.init(this);
  }

  /**
   * Sets the table's style. Has no effect if called after the table has been
   * rendered. See the class documentation for a list of valid style.
   * 
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  /**
   * Sets the table's header. Should only be called when providing a custom
   * table header. Has no effect if called after the table has been rendered.
   * 
   * @param header the table header
   */
  public void setTableHeader(TableHeader header) {
    if (!isRendered()) {
      this.header = header;
    }
  }

  /**
   * Sets whether cells should have have a horizontal border. Default value is
   * <code>false</code>.
   * 
   * @param show <code>true</code> to display horizontal borders
   */
  public void setVerticalLines(boolean show) {
    this.verticalLines = show;
  }

  /**
   * Sets the table's view. Provides a way to provide specialized views. table
   * views.
   * 
   * @param view the view
   */
  public void setView(TableView view) {
    this.view = view;
  }

  /**
   * Sorts the table using the specified column index.
   * 
   * @param index the column index
   * @param direction the direction to sort (NONE, ASC, DESC)
   */
  public void sort(int index, int direction) {
    if (isRendered()) {
      BaseEvent be = new BaseEvent();
      be.index = index;
      be.size = direction;
      if (fireEvent(Events.SortChange, be)) {
        getTableHeader().sort(index, direction);
        getView().sort(index, direction);
      }
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(header);
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = getItem(i);
      if (item.hasWidgets) {
        int ct = item.getValues().length;
        for (int j = 0; j < ct; j++) {
          Object obj = item.getValue(j);
          if (obj != null && obj instanceof Widget) {
            WidgetHelper.doAttach((Widget) item.getValue(j));
          }
        }
      }
    }
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(header);
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = getItem(i);
      if (item.hasWidgets) {
        int ct = item.getValues().length;
        for (int j = 0; j < ct; j++) {
          Object obj = item.getValue(j);
          if (obj != null && obj instanceof Widget) {
            WidgetHelper.doDetach((Widget) item.getValue(j));
          }
        }
      }
    }
  }

  protected String getRenderedValue(int column, Object value) {
    TableColumn col = cm.getColumn(column);
    if (col.getRenderer() != null) {
      return col.getRenderer().render(col.getID(), value);
    } else {
      if (value != null) {
        return value.toString();
      }
      return null;
    }
  }

  protected void init() {
    baseStyle = "my-tbl";
    items = new ArrayList();
    initSelectionModel();
  }

  protected void initSelectionModel() {
    if ((style & Style.MULTI) != 0) {
      sm = new RowSelectionModel(Style.MULTI);
    } else {
      sm = new RowSelectionModel(Style.SINGLE);
    }
    sm.init(this);
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-tbl");

    header = getTableHeader();
    header.init(this);

    DOM.appendChild(getElement(), header.getElement());

    if (styleTemplate == null) {
      Element style = DOM.createElement("style");
      DOM.setElementProperty(style, "id", getId() + "-cols-style");
      DOM.appendChild(MyDOM.getHead(), style);
      styleTemplate = new StyleTemplate(style);
    }

    for (int i = 0, n = cm.getColumnCount(); i < n; i++) {
      TableColumn c = cm.getColumn(i);
      int w = cm.getWidthInPixels(c.index);
      styleTemplate.set("." + getId() + "-col-" + i, "width:" + w + "px !important;");
    }
    styleTemplate.apply();

    view = getView();
    view.init(this);
    view.render();
    
    disableTextSelection(false);

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS
        | Event.ONSCROLL);

    view.renderItems();
  }

  protected void onResize(int width, int height) {
    recalculate();
  }

  protected void onRightClick(BaseEvent be) {
    TableItem item = findItem(be.getTarget());
    if (item != null) {
      item.onClick(be);
    }
    super.onRightClick(be);
  }

  protected void onShowContextMenu(int x, int y) {
    super.onShowContextMenu(x, y);
    getView().clearHoverStyles();
  }

  private void register(TableItem item) {
    nodes.put(item.getId(), item);
  }

  private void unregister(TableItem item) {
    nodes.remove(item.getId());
  }

}
