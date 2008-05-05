/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.menu.Menu;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A standard hierarchical tree widget. The tree contains a hierarchy of
 * <code>TreeItems</code> that the user can open, close, and select.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK</dd>
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
 * <li>widget : the check item</li>
 * </ul>
 * </dd>
 * </dd>
 * 
 * <dd><b>Select</b> : (widget)<br>
 * <div>Fires after a item is clicked.</div>
 * <ul>
 * <li>widget : the item that was clicked</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : (widget)<br>
 * <div>Fires before the tree's context menu is shown.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree (the tree itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class Tree extends Component {

  protected TreeItem root;

  int checkStyle = Style.PARENT;
  int checkNodes = Style.DEFAULT;
  private String nodeImageStyle = "tree-folder";
  private String openNodeImageStyle = "tree-folder-open";
  private String itemImageStyle;
  private boolean animate = true;
  private int indentWidth = 18;
  protected TreeSelectionModel sm;
  private Map nodeHash;
  private int selectionMode = Style.SINGLE;
  boolean isViewer;

  /**
   * Creates a new single select tree.
   */
  public Tree() {
    this(Style.NONE);
  }

  /**
   * Creates a new tree.
   * 
   * @param style the tree style
   */
  public Tree(int style) {
    super(style | Style.FOCUSABLE);

    if ((style & Style.MULTI) != 0) {
      selectionMode = Style.MULTI;
    }

    createRootItem();
    root.root = true;

    nodeHash = new HashMap();
  }

  /**
   * Collapses all item's.
   */
  public void collapseAll() {
    root.setExpanded(false, true);
  }

  /**
   * Deselects a item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(TreeItem item) {
    sm.deselect(item);
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    sm.deselectAll();
  }

  /**
   * Expands all item's.
   */
  public void expandAll() {
    root.setExpanded(true, true);
  }

  /**
   * Expands a specified path. A path can be retrieved from a tree item with
   * {@link TreeItem#getPath()}.
   * 
   * @param path the path to expand
   * @return <code>true</code> if all paths expanded
   */
  public boolean expandPath(String path) {
    String[] ids = path.split(",");
    if (ids.length == 0) return false;
    if (ids[0].equals(root.getId())) {
      root.setExpanded(true);

      TreeItem current = root;
      for (int i = 1; i < ids.length; i++) {
        String id = ids[i];
        boolean match = false;
        for (int j = 0; j < current.getItemCount(); j++) {
          TreeItem child = current.getItem(j);
          if (!match && child.getId().equals(id)) {
            child.setExpanded(true);
            current = child;
            match = true;
            break;
          }
        }
        if (!match) {
          return false;
        }
      }

    }
    return true;
  }

  /**
   * Returns the tree whose element or child elements match the passed target.
   * 
   * @param element the target element
   * @return the matching tree item or <code>null</code> if no match
   */
  public TreeItem findItem(Element element) {
    Element elem = MyDOM.findParent("my-treeitem", element, 15);
    if (elem != null) {
      String id = DOM.getElementProperty(elem, "id");
      if (id != null && !id.equals("")) {
        TreeItem item = getItemById(id);
        return item;
      }
    }
    return null;
  }

  /**
   * Returns all tree item's contained by the tree.
   * 
   * @return all tree item's
   */
  public TreeItem[] getAllItems() {
    List temp = new ArrayList();
    temp.add(root);
    Iterator it = nodeHash.values().iterator();
    while (it.hasNext()) {
      temp.add(it.next());
    }
    return (TreeItem[]) temp.toArray(new TreeItem[temp.size()]);
  }

  /**
   * Returns <code>true</code> if animation is enabled.
   * 
   * @return the animate state
   */
  public boolean getAnimate() {
    return animate;
  }

  /**
   * Returns a list of id's for all checked items.
   * 
   * @return the list of checked id's
   */
  public TreeItem[] getChecked() {
    List list = new ArrayList();
    Iterator it = nodeHash.values().iterator();
    while (it.hasNext()) {
      TreeItem item = (TreeItem) it.next();
      if (item.isChecked()) {
        list.add(item);
      }
    }
    return (TreeItem[]) list.toArray(new TreeItem[0]);
  }

  public Menu getContextMenu() {
    // made public
    return super.getContextMenu();
  }

  /**
   * Returns the indent width.
   * 
   * @return the indent width
   */
  public int getIndentWidth() {
    return indentWidth;
  }

  /**
   * Returns the item by id.
   * 
   * @param id the id of the element to return
   * @return the item
   */
  public TreeItem getItemById(String id) {
    return (TreeItem) nodeHash.get(id);
  }

  /**
   * Returns the item image style.
   * 
   * @return the item image style
   */
  public String getItemImageStyle() {
    return itemImageStyle;
  }

  /**
   * Returns the node image style.
   * 
   * @return the node image style
   */
  public String getNodeImageStyle() {
    return nodeImageStyle;
  }

  /**
   * Returns the open node image style.
   * 
   * @return the open node image style
   */
  public String getOpenNodeImageStyle() {
    return openNodeImageStyle;
  }

  /**
   * Returns the tree's root item.
   * 
   * @return the root item
   */
  public TreeItem getRootItem() {
    return root;
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item or <code>null</code> if no selection
   */
  public TreeItem getSelectedItem() {
    return sm.getSelected();
  }

  /**
   * Returns an array of selected items.
   * 
   * @return the selected items
   */
  public TreeItem[] getSelection() {
    if (sm == null) {
      return new TreeItem[0];
    }
    if (sm instanceof MultiSelectionModel) {
      return sm.getSelection();
    } else {
      TreeItem item = getSelectedItem();
      if (item != null) {
        return new TreeItem[] {item};
      }
      return new TreeItem[0];
    }
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public int getSelectionMode() {
    return selectionMode;
  }

  /**
   * Returns the tree's selection model.
   * 
   * @return the selection model
   */
  public TreeSelectionModel getSelectionModel() {
    return sm;
  }

  public void onBaseEvent(BaseEvent be) {

  }

  public void onBrowserEvent(Event event) {
    int type = DOM.eventGetType(event);

    // hack to receive keyboard events in safari
    if (MyGWT.isSafari && type == Events.KeyDown) {
      if (sm.getSelected() != null) {
        BaseEvent be = new BaseEvent();
        be.event = event;
        sm.onKeyDown(be);
        return;
      }
    }
    if (type == Events.MouseUp) {
      if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT
          || (MyGWT.isMac && DOM.eventGetCtrlKey(event))) {
        super.onBrowserEvent(event);
        return;
      }
    }

    if (isEnabled()) {
      TreeItem item = findItem(DOM.eventGetTarget(event));
      if (item != null) {
        item.onBrowserEvent(event);
      }
    }
  }

  /**
   * Specifies if expand / collapse should be animated. Default value is
   * <code>true</code>.
   * 
   * @param animate <code>true</code> to enable animations
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * Sets what nodes should have a check box. Default value is DEFAULT.
   * 
   * @see Style#DEFAULT - both nodes and leafs
   * @see Style#PARENT - only nodes with children
   * @see Style#LEAF - only leafs
   * 
   * @param value the value
   */
  public void setCheckNodes(int value) {
    this.checkNodes = value;
  }

  /**
   * Sets the tree check style which determines how checking cascades. Default
   * value is CHILD.
   * 
   * @param style the check style SINGLE, PARENT, CHILDREN.
   */
  public void setCheckStyle(int style) {
    this.checkStyle = style;
  }

  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Sets the number of pixels child items are indented. Default value is 18.
   * 
   * @param indentWidth the indent width
   */
  public void setIndentWidth(int indentWidth) {
    this.indentWidth = indentWidth;
  }

  /**
   * Sets the item image style. If specified all items without a image style
   * will use this style. Default value is <code>null</code>.
   * 
   * @param itemImageStyle the item image style
   */
  public void setItemImageStyle(String itemImageStyle) {
    this.itemImageStyle = itemImageStyle;
  }

  /**
   * Sets the global icon style for tree items with children. Default value is
   * 'tree-folder'.
   * 
   * @param nodeImageStyle the node image style
   */
  public void setNodeImageStyle(String nodeImageStyle) {
    this.nodeImageStyle = nodeImageStyle;
  }

  /**
   * Sets the global icon style for open tree items. Default value is
   * 'tree-folder-open'.
   * 
   * @param openNodeImageStyle the open node image style
   */
  public void setOpenNodeImageStyle(String openNodeImageStyle) {
    this.openNodeImageStyle = openNodeImageStyle;
  }

  /**
   * Sets the selection to the tree items. The current selection is cleared
   * before the new items are selected. If the tree is single-select then all
   * items are ignored.
   * 
   * 
   * @param selected the items to select
   */
  public void setSelection(List selected) {
    if (sm instanceof MultiSelectionModel) {
      ((MultiSelectionModel) sm).selectItems(selected);
    }
  }

  /**
   * Sets the selection to the item. The current selection is cleared before the
   * new items are selected.
   * 
   * @param item the item to select
   */
  public void setSelection(TreeItem item) {
    sm.select(item);
  }

  /**
   * Sets the selection mode for the list. Calling after the tree has been
   * rendered will have no effect.
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(int selectionMode) {
    if (!isRendered()) {
      this.selectionMode = selectionMode;
    }
  }

  protected void createRootItem() {
    root = new RootTreeItem(this);
    root.tree = this;
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-tree");
    
    if (selectionMode == Style.MULTI) {
      sm = new MultiSelectionModel();
    } else {
      sm = new TreeSelectionModel();
    }

    this.sm.init(this);

    DOM.appendChild(getElement(), root.getElement());

    if (!root.childrenRendered) {
      root.renderChildren();
    }

    disableTextSelection(true);
    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.KEYEVENTS | Event.MOUSEEVENTS);
  }

  protected void onRightClick(BaseEvent be) {
    TreeItem item = findItem(be.getTarget());
    if (selectionMode == Style.SINGLE) {
      if (item != null) {
        setSelection(item);
      }
    } else {
      if (item != null && !sm.isSelected(item)) {
        setSelection(item);
      }
    }
    super.onRightClick(be);
  }

  void registerItem(TreeItem item) {
    nodeHash.put(item.getId(), item);
  }

  void unregisterItem(TreeItem item) {
    int count = item.getItemCount();
    for (int i = 0; i < count; i++) {
      unregisterItem(item.getItem(i));
    }
    nodeHash.remove(item.getId());
  }
}
