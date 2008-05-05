/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.Element;

/**
 * A item in a <code>Tree</code>. All events are bubbled to the item's parent
 * tree.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : (widget, item, index)<br>
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
 * <dd><b>BeforeExpand</b> : (widget)<br>
 * <div>Fires before a item is expanded. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget)<br>
 * <div>Fires before a item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : (widget, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : (widget, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : (widget)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : (widget)<br>
 * <div>Fires after a item is clicked.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree-item (the item itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class TreeItem extends Component {

  protected Tree tree;

  protected boolean root, expanded, checked;
  protected TreeItemUI ui;
  protected boolean childrenRendered;
  
  TreeItem parentItem;
  boolean leaf = true;
  String text, iconStyle;
  private List children;
  private String textStyle;

  /**
   * Creates a new tree item.
   */
  public TreeItem() {
    children = new ArrayList();
  }

  /**
   * Creates a new tree item.
   * 
   * @param text the item's text
   */
  public TreeItem(String text) {
    this();
    setText(text);
  }

  /**
   * Adds a child item.
   * 
   * @param item the item to be added
   */
  public void add(TreeItem item) {
    insert(item, getItemCount());
  }

  public boolean fireEvent(int type, BaseEvent be) {
    boolean result = super.fireEvent(type, be);
    if (tree != null && result) {
      return tree.fireEvent(type, be);
    }
    return result;
  }

  /**
   * Returns the item's first child.
   * 
   * @return the first child or <code>null</code>
   */
  public TreeItem firstChild() {
    return getItem(0);
  }

  public Element getContainer() {
    return ui.containerEl;
  }

  /**
   * Returns the item's node depth.
   * 
   * @return the depth
   */
  public int getDepth() {
    int depth = 0;
    TreeItem p = getParentItem();
    while (p != null) {
      depth++;
      p = p.getParentItem();
    }
    return depth;
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index the index
   * @return the item at the index
   */
  public TreeItem getItem(int index) {
    if ((index < 0) || (index >= getItemCount())) return null;
    return (TreeItem) children.get(index);
  }
  
  /**
   * Returns the number of child items.
   * 
   * @return the child count
   */
  public int getItemCount() {
    return children.size();
  }

  /**
   * Returns the item's children.
   * 
   * @return the children items
   */
  public TreeItem[] getItems() {
    return (TreeItem[]) children.toArray(new TreeItem[0]);
  }

  /**
   * Returns the item's parent.
   * 
   * @return the parent item
   */
  public TreeItem getParentItem() {
    return parentItem;
  }

  /**
   * Returns the path for this node. The path can be used to expand or select
   * this node programmatically.
   * 
   * @return a comma seperated list of tree item id's
   */
  public String getPath() {
    StringBuffer sb = new StringBuffer();
    TreeItem p = this;
    while (p != null) {
      String id = p.getId();
      sb.insert(0, "," + id);
      p = p.getParentItem();
    }
    return sb.toString().substring(1);
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  public String getTextStyle() {
    return textStyle;
  }

  /**
   * Returns the item's ui instance.
   * 
   * @return the ui instance
   */
  public TreeItemUI getUI() {
    return ui;
  }
  
  /**
   * Returns <code>true</code> if the item's has children.
   * 
   * @return the children state
   */
  public boolean hasChildren() {
    return getItemCount() > 0;
  }

  /**
   * Returns the index of the item or -1 if not found.
   * 
   * @param item the child item
   * @return the item's index
   */
  public int indexOf(TreeItem item) {
    return children.indexOf(item);
  }

  /**
   * Inserts a child item at the specified position.
   * 
   * @param item the item to be added
   * @param index index at which the specified element is to be inserted
   */
  public void insert(TreeItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      item.parentItem = this;
      item.setTree(tree);

      tree.registerItem(item);
      children.add(index, item);
      leaf = false;

      if (childrenRendered) {
        item.render();
      }

      if (rendered && !root) {
        ui.updateJoint();
        ui.onIconStyleChange(getIconStyle());
      }
      fireEvent(Events.Add, this, item, index);
    }

  }

  /**
   * Returns <code>true</code> if the item is checked.
   * 
   * @return the checked state
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Returns <code>true</code> if the item is expanded, and <code>false</code>
   * otherwise.
   * 
   * @return the expanded state
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Returns <code>true</code> if the item is a leaf, and <code>false</code>
   * otherwise. The leaf state allows a tree item to specify if it has children
   * before the children have been realized.
   * 
   * @return the leaf state
   */
  public boolean isLeaf() {
    return leaf;
  }

  /**
   * Returns <code>true</code> if the item is a root item.
   * 
   * @return the rooot state
   */
  public boolean isRoot() {
    return root;
  }

  public void onBaseEvent(BaseEvent be) {
    if (ui != null) {
      ui.getListener().handleEvent(be);
    }
  }

  /**
   * Removes a child from the item.
   * 
   * @param item the item to be removed
   */
  public void remove(TreeItem item) {
    if (!children.contains(item)) {
      return;
    }
    if (fireEvent(Events.BeforeRemove, this, item)) {
      if (tree.getSelectionModel() != null) {
        tree.getSelectionModel().deselect(item);
      }
      children.remove(item);
      tree.unregisterItem(item);
      item.tree = null;
      item.parentItem = null;
      if (rendered && item.rendered) {
        ui.removeItem(item);
        if (getItemCount() == 0 && isExpanded()) {
          setExpanded(false);
        }
      }
      
      fireEvent(Events.Remove, this, item);
    }

  }

  /**
   * Removes all child items.
   */
  public void removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
    leaf = true;
  }

  /**
   * Sets the item's checked value.
   * 
   * @param checked <code>true</code> to check
   */
  public void setChecked(boolean checked) {
    this.checked = checked;
    if (rendered) {
      ui.onCheckChange(checked);
      if (checked) {
        switch (tree.checkStyle) {
          case Style.PARENT:
            TreeItem p = getParentItem();
            while (p != null && !p.root) {
              p.setChecked(true);
              p = p.getParentItem();
            }
            break;
          case Style.CHILDREN:
            for (int i = 0; i < getItemCount(); i++) {
              getItem(i).setChecked(true);
            }
            break;
        }

      } else {
        switch (tree.checkStyle) {
          case Style.PARENT:
            clearCheckChildren(this);
            break;
          case Style.CHILDREN:
            for (int i = 0; i < getItemCount(); i++) {
              getItem(i).setChecked(false);
            }
            break;
        }
      }
    }
  }

  /**
   * Sets the item's expanded state.
   * 
   * @param expanded <code>true</code> to expand
   */
  public void setExpanded(boolean expanded) {
    setExpanded(expanded, false);
  }

  /**
   * Sets the item's expand state.
   * 
   * @param expanded <code>true</code> to expand
   * @param deep <code>true</code> to expand all children
   */
  public void setExpanded(boolean expanded, boolean deep) {
    if (expanded && root) {
      this.expanded = false;
    } else if (!expanded && root) {
      this.expanded = true;
    }

    if (!isRendered()) {
      return;
    }

    if (expanded) {
      if (!this.expanded && !isLeaf()) {
        if (fireEvent(Events.BeforeExpand)) {
          this.expanded = true;
          if (!childrenRendered) {
            renderChildren();
          }
          ui.expand();
          TreeItem p = parentItem;
          while (p != null && !p.root) {
            if (p.expanded == false) {
              p.setExpanded(true);
            }
            p = p.parentItem;
          }
        }
        if (deep) {
          expandChildren(deep);
        }
      } else {
        if (deep) {
          expandChildren(deep);
        }
      }
    }

    else if (this.expanded && !expanded) {
      if (fireEvent(Events.BeforeCollapse)) {
        this.expanded = false;
        ui.collapse();
      }
      if (deep) {
        for (int i = 0; i < getItemCount(); i++) {
          TreeItem item = getItem(i);
          item.setExpanded(false, true);
        }
      }
    }
  }

  /**
   * Sets the item's icon style.
   * 
   * @param style the icon style
   */
  public void setIconStyle(String style) {
    this.iconStyle = style;
    if (rendered) {
      ui.onIconStyleChange(style);
    }
  }

  /**
   * Sets the item's leaf state. The leaf state allows a tree item to specify if
   * it has children before the children have been realized.
   * 
   * @param leaf the state
   */
  public void setLeaf(boolean leaf) {
    this.leaf = leaf;
  }

  /**
   * Sets the item's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      ui.onTextChange(text);
    }
  }

  public void setTextStyle(String style) {
    this.textStyle = style;
    if (isRendered()) {
      getUI().onTextStyleChange(textStyle);
    }
  }

  /**
   * Toggles the item's expand state.
   */
  public void toggle() {
    setExpanded(!isExpanded());
  }

  /**
   * Subclasses may override.
   * 
   * @return the ui
   */
  protected TreeItemUI getTreeItemUI() {
    return new TreeItemUI(this);
  }

  protected void onRender() {
    ui = getTreeItemUI();
  }

  protected void renderChildren() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      getItem(i).render();
    }
    childrenRendered = true;
  }

  protected void setChildrenRendered(boolean rendered) {
    childrenRendered = rendered;
  }

  protected void setRoot(boolean isRoot) {
    root = isRoot;
  }
  
  protected void setTree(Tree tree) {
    this.tree = tree;
  }

  boolean isFirst() {
    if (isRoot()) return true;
    return this == parentItem.getItem(0);
  }

  boolean isLast() {
    if (isRoot()) return true;
    return this == parentItem.getItem(parentItem.getItemCount() - 1);
  }

  TreeItem lastChild() {
    return getItem(getItemCount() - 1);
  }

  TreeItem nextSibling() {
    if (parentItem == null) return null;
    int index = parentItem.indexOf(this);
    return parentItem.getItem(index + 1);
  }

  TreeItem previousSibling() {
    if (parentItem == null) return null;
    int index = parentItem.indexOf(this);
    return parentItem.getItem(index - 1);
  }

  private void clearCheckChildren(TreeItem parent) {
    for (int i = 0; i < parent.getItemCount(); i++) {
      TreeItem sub = parent.getItem(i);
      sub.setChecked(false);
      clearCheckChildren(sub);
    }
  }

  private void expandChildren(boolean deep) {
    for (int i = 0; i < getItemCount(); i++) {
      TreeItem item = getItem(i);
      item.setExpanded(true, deep);
    }
  }
  
}
