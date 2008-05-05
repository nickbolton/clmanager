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
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Observable;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * A single-select tree selection model.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>SelectionChange</b> : (source, widget, item, items)<br>
 * <div>Fires after the selection has changed.</div>
 * <ul>
 * <li>source : this</li>
 * <li>widget : the table</li>
 * <li>item : single selection or null</li>
 * <li>items : multi selection or null</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see MultiSelectionModel
 */
public class TreeSelectionModel extends Observable {

  protected Tree tree;
  protected TreeItem selItem;
  protected TreeItem lastSelItem;
  protected Listener treeListener;

  public TreeSelectionModel() {
    treeListener = new Listener() {
      public void handleEvent(BaseEvent be) {
        int type = be.type;
        TreeItem item = (TreeItem) be.widget;
        switch (type) {
          case Events.MouseDown:
            if (!be.isRightClick()) {
              onItemClick(item, be);
            }
            break;
          case Events.KeyDown:
            onKeyDown(be);
            break;
        }
        be.cancelBubble();
      }
    };
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    if (selItem != null) {
      selItem.getUI().onSelectedChange(false);
      selItem = null;
      BaseEvent be = new BaseEvent();
      be.widget = tree;
      fireEvent(Events.SelectionChange, be);
      tree.fireEvent(Events.SelectionChange, be);
    }
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item or <code>null</code> if no selection
   */
  public TreeItem getSelected() {
    return selItem;
  }

  /**
   * Returns a array of selected items.
   * 
   * @return the selected items
   */
  public TreeItem[] getSelection() {
    List list = new ArrayList();
    if (selItem != null) {
      list.add(selItem);
    }
    return (TreeItem[])list.toArray(new TreeItem[list.size()]);
  }

  /**
   * Returns the model's tree.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return tree;
  }

  /**
   * Binds the model to the specified tree.
   * 
   * @param tree the tree
   */
  public void init(Tree tree) {
    this.tree = tree;
    tree.addListener(Events.MouseDown, treeListener);
    tree.addListener(Events.KeyDown, treeListener);
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the selected state
   */
  public boolean isSelected(TreeItem item) {
    return selItem == item;
  }

  /**
   * Selects a item.
   * 
   * @param item the item to be selected
   */
  public void select(final TreeItem item) {
    if (isSelected(item)) {
      return;
    }
    if (!tree.isRendered()) {
      tree.addListener(Events.Render, new Listener() {
        public void handleEvent(BaseEvent be) {
          removeListener(Events.Render, this);
          select(item);
        }
      });
      return;
    }
    TreeItem last = selItem != null ? selItem : lastSelItem;
    if (last != null) {
      last.getUI().onSelectedChange(false);
    }
    selItem = item;
    lastSelItem = item;
    selItem.getUI().onSelectedChange(true);
    BaseEvent be = new BaseEvent(tree);
    be.item = item;
    be.items = getSelection();
    be.widget = tree;
    fireEvent(Events.SelectionChange, be);
    tree.fireEvent(Events.SelectionChange, be);
    if (MyGWT.isSafari) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          tree.focus();
        }
      });
    }
  }

  /**
   * Selects the item below the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectNext() {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    if (sel.firstChild() != null && sel.isExpanded()) {
      select(sel.firstChild());
    } else if (sel.nextSibling() != null) {
      select(sel.nextSibling());
    } else if (sel.getParentItem() != null) {
      TreeItem p = sel.getParentItem();
      while (p != null) {
        if (p.nextSibling() != null) {
          select(p.nextSibling());
          return;
        }
        p = p.getParentItem();
      }
    }
  }

  /**
   * Selects the item above the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectPrevious() {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    TreeItem prev = sel.previousSibling();
    if (prev != null) {
      if (!prev.isExpanded() || prev.getItemCount() < 1) {
        select(prev);
      } else {
        TreeItem lastChild = prev.lastChild();
        while (lastChild != null && lastChild.getItemCount() > 0) {
          lastChild = lastChild.lastChild();
        }
        select(lastChild);
      }
    } else if (sel.getParentItem() != null && !sel.getParentItem().isRoot()) {
      select(sel.getParentItem());
    }
  }

  /**
   * Deselect a item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(TreeItem item) {
    if (selItem == item) {
      deselectAll();
    }
  }

  protected void onItemClick(TreeItem item, BaseEvent be) {
    if (!be.within(item.getUI().getJointEl()) && !be.within(item.getUI().getCheckEl())) {
      if (isSelected(item) && be.isControlKey()) { 
        deselect(item);
      } else {
        select(item);
      }
    }
  }

  protected void onKeyDown(BaseEvent be) {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    int key = be.getKeyCode();
    switch (key) {
      case KeyboardListener.KEY_DOWN:
        be.stopEvent();
        selectNext();
        break;
      case KeyboardListener.KEY_UP:
        be.stopEvent();
        selectPrevious();
        break;
      case KeyboardListener.KEY_LEFT:
        be.preventDefault();
        if (!sel.isLeaf() && sel.isExpanded()) {
          sel.setExpanded(false);
        } else if (sel.getParentItem() != null && !sel.getParentItem().isRoot()) {
          select(sel.getParentItem());
        }
        break;
      case KeyboardListener.KEY_RIGHT:
        be.preventDefault();
        if (!sel.isLeaf()) {
          if (!sel.isExpanded()) {
            sel.setExpanded(true);
            return;
          } 
          
        }
        selectNext();
        break;
    }
  }

}
