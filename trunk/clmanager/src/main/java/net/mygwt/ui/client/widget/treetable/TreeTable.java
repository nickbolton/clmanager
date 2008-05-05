/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.DelayedTask;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.util.StyleTemplate;
import net.mygwt.ui.client.widget.table.ITable;
import net.mygwt.ui.client.widget.table.TableColumn;
import net.mygwt.ui.client.widget.table.TableColumnModel;
import net.mygwt.ui.client.widget.table.TableHeader;
import net.mygwt.ui.client.widget.tree.MultiSelectionModel;
import net.mygwt.ui.client.widget.tree.Tree;
import net.mygwt.ui.client.widget.tree.TreeSelectionModel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A hierarchical tree widget with support for additional columns. The tree
 * contains a hierarchy of <code>TreeTableItems</code> that the user can open,
 * close, and select.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, HORIZONTAL</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : (widget, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeExpand</b> : (widget, item)<br>
 * <div>Fires before a item is expanded. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget, item)<br>
 * <div>Fires before a item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget, item)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget, item)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * <li>item : the item being collapsed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : (widget)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>widget : the parent item</li>
 * </ul>
 * </dd>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : (widget)<br>
 * <div>Fires before the tree's context menu is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item represented by the cell</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellDoubleClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item represented by the cell</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item that represents the row</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowDoubleClick</b> : (widget, item, index)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>widget : tree table</li>
 * <li>item : item that represents the row</li>
 * <li>index : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-treetbl (the containing table)</dd>
 * <dd>.my-treetbl-data (the table data)</dd>
 * <dd>.my-treetbl-item (a row in the table)</dd>
 * <dd>.my-treetbl-tree (the tree itself)</dd>
 * <dd>.my-treetbl-item (a node within the tree)</dd>
 * <dd>.my-treetbl-item-text span (the tree item text)</dd>
 * </dl>
 */
public class TreeTable extends Tree implements ITable {
  /**
   * disableColumnContextMenu specifies if the column context menu should be
   * disabled. Default value is <code>false</code>.
   */
  boolean disableColumnContextMenu;

  private TreeTableHeader header;
  private TreeTableColumnModel cm;
  private TreeTableView view;
  private int lastLeft;
  private Size lastSize;

  StyleTemplate styleTemplate = null;

  private DelayedTask scrollTask = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      header.updateSplitBars();
    }
  });

  /**
   * Creates a new single select tree table. A column model must be set before
   * the table is rendered.
   */
  public TreeTable() {
    this.style = Style.SINGLE;
  }

  /**
   * Creates a new tree table with the given column model.
   * 
   * @param cm the tree table column model
   */
  public TreeTable(TreeTableColumnModel cm) {
    this(Style.NONE, cm);
  }

  /**
   * Creates a new tree table with the given style and column model.
   * 
   * @param style the style
   * @param cm the tree table column model
   */
  public TreeTable(int style, TreeTableColumnModel cm) {
    super(style);
    this.cm = cm;
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

  /**
   * Returns the tree table's header.
   * 
   * @return the table header
   */
  public TableHeader getTableHeader() {
    if (header == null) {
      header = new TreeTableHeader(this);
    }
    return header;
  }

  /**
   * Returns <code>true</code> if vertical lines are enabled.
   * 
   * @return the vertical line state
   */
  // public boolean getVeritcalLines() {
  // return verticalLines;
  // }
  /**
   * Returns the tree table's view.
   * 
   * @return the view
   */
  protected TreeTableView getView() {
    if (view == null) {
      view = new TreeTableView();
    }
    return view;
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
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(TreeTableItem item) {
    MyDOM.scrollIntoView(item.getElement(), view.getScrollElement(), false);
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

  /**
   * Sets the tree table's header. Should only be called when providing a custom
   * tree table header. Has no effect if called after the table has been
   * rendered.
   * 
   * @param header the table header
   */
  public void setTableHeader(TreeTableHeader header) {
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
  // public void setVerticalLines(boolean show) {
  // this.verticalLines = show;
  // }
  /**
   * Sets the tree table's view. Provides a way to provide specialized views.
   * table views.
   * 
   * @param view the view
   */
  public void setView(TreeTableView view) {
    this.view = view;
  }

  /**
   * Sorts the tree table using the specified column index.
   * 
   * @param index the column index
   * @param direction the direction to sort (NONE, ASC, DESC)
   */
  public void sort(int index, int direction) {
    // TODO Auto-generated method stub

  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(header);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(header);
  }

  protected String getRenderedValue(int column, Object value) {
    TreeTableColumn col = (TreeTableColumn) cm.getColumn(column);
    if (col.getRenderer() != null) {
      return col.getRenderer().render(col.getID(), value);
    } else {
      if (value != null) {
        return value.toString();
      }
      return null;
    }
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-treetbl");

    if (getSelectionMode() == Style.MULTI) {
      sm = new MultiSelectionModel();
    } else {
      sm = new TreeSelectionModel();
    }

    this.sm.init(this);

    DOM.appendChild(getElement(), root.getElement());

    ((RootTreeTableItem) root).renderChildren();

    cm.setTable(this);
    ((TreeTableItem) root).setValues(new String[getColumnCount()]);

    MyDOM.removeChildren(getElement());

    header = (TreeTableHeader) getTableHeader();
    header.init(this);

    DOM.appendChild(getElement(), header.getElement());

    if (styleTemplate == null) {
      Element style = DOM.createElement("style");
      DOM.setElementProperty(style, "id", getId() + "-cols-style");
      DOM.appendChild(MyDOM.getHead(), style);
      styleTemplate = new StyleTemplate(style);
    }

    for (int i = 0, n = cm.getColumnCount(); i < n; i++) {
      TreeTableColumn c = (TreeTableColumn) cm.getColumn(i);
      int w = cm.getWidthInPixels(c.getIndex());
      styleTemplate.set("." + getId() + "-col-" + i, "width:" + w + "px;");
    }

    view = getView();
    view.init(this);
    view.render();

    disableTextSelection(true);

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS
        | Event.ONSCROLL);
  }

  protected void onResize(int width, int height) {
    if (width < 0 && height < 0) {
      return;
    }
    recalculate();
  }
  
  protected void onShowContextMenu(int x, int y) {
    super.onShowContextMenu(x, y);
    getView().clearHoverStyles();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.widget.tree.Tree#createRootItem()
   */
  protected void createRootItem() {
    root = new RootTreeTableItem(this);
  }

}
