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
 *     Tom Schindl <tom.schindl@bestsolution.at> - bugfix in issues: 40
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.tree.Tree;
import net.mygwt.ui.client.widget.tree.TreeItem;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * An interface to content providers for tree-structure-oriented viewers.
 * 
 * Content providers for tree viewers must implement either the
 * <code>ITreeContentProvider</code> interface or the
 * <code>IAsyncTreeContentProvider</code> interface.
 * 
 * @see TreeViewer
 */
public class TreeViewer extends Viewer implements ICheckable, ITreeViewer {

  protected boolean checkable;
  private Tree tree;
  private List checkChangeListener;
  private boolean caching = true;
  private boolean force;
  private Map nodeMap = new HashMap();
  
  /**
   * Creates a new tree viewer instance.
   * 
   * @param tree the underlying tree widget
   */
  public TreeViewer(Tree tree) {
    this.tree = tree;
    // force render
    tree.getElement();
    hookWidget(tree);

    Listener l = new Listener() {
      public void handleEvent(BaseEvent be) {
        switch (be.type) {
          case Events.BeforeExpand: {
            TreeItem item = (TreeItem) be.widget;
            Object loaded = item.getData("loaded");
            if (loaded == null) {
              be.doit = false;
              loadChildren(item, true);
            }
            break;
          }
          case Events.Collapse: {
            if (!caching) {
              TreeItem item = (TreeItem) be.widget;
              int count = item.getItemCount();
              for (int i = 0; i < count; i++) {
                item.remove(item.getItem(0));
              }
              item.setData("loaded", null);
            }
            break;
          }

        }
      }
    };

    tree.addListener(Events.BeforeExpand, l);
    tree.addListener(Events.Collapse, l);
  }

  public void add(Object parent, Object child) {
    TreeItem p = (TreeItem) findItem(parent);
    internalAdd(p, child, p.getItemCount());
  }

  public void addCheckStateListener(ICheckStateListener listener) {
    if (checkChangeListener == null) {
      checkChangeListener = new ArrayList();
    }
    if (!checkChangeListener.contains(listener)) {
      checkChangeListener.add(listener);
    }
  }

  /**
   * Applies the viewer's filters to the current elements.
   */
  public void applyFilters() {
    filterItems(tree.getRootItem());
  }

  public Widget findItem(Object elem) {
    TreeItem[] items = tree.getAllItems();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
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

  /**
   * Returns <code>true</code> if the viewer is caching.
   * 
   * @return the caching state
   */
  public boolean getCaching() {
    return caching;
  }

  public boolean getChecked(Object element) {
    TreeItem item = (TreeItem) findItem(element);

    if (item != null) {
      return item.isChecked();
    }

    return false;
  }

  /**
   * Returns the current checked selection.
   * 
   * @return the checked elements
   */
  public ISelection getCheckedSelection() {
    TreeItem[] items = tree.getChecked();
    if (items.length == 0) {
      return DefaultSelection.EMPTY;
    }
    ArrayList checked = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      checked.add(items[i].getData());
    }
    return new DefaultSelection(checked);
  }

  /**
   * Returns the viewer's tree widget.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return tree;
  }

  public Widget getWidget() {
    return tree;
  }

  public void insert(Object parent, Object child, int position) {
    TreeItem p = (TreeItem) findItem(parent);
    internalAdd(p, child, position);
  }

  /**
   * Refreshes this viewer starting with the given element.
   * 
   * @param elem the element
   */
  public void refresh(Object elem) {
    TreeItem item = (TreeItem) findItem(elem);
    if (item != null) {
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        item.remove(item.getItem(0));
      }
      item.setData("loaded", null);
      loadChildren(item, item.isExpanded());
    }
  }

  public void remove(Object element) {
    TreeItem item = (TreeItem) findItem(element);
    if (item != null) {
      TreeItem parent = item.getParentItem();
      parent.remove(item);
      removeElement(item.getData());
      
      nodeMap.remove(element);
      item.setData(null);
    }
  }

  public void removeCheckStateListener(ICheckStateListener listener) {
    if (checkChangeListener != null) {
      checkChangeListener.remove(listener);
    }
  }

  /**
   * Selects the elements.
   * 
   * @param elem the element to be selected
   */
  public void select(Object elem) {
    TreeItem item = (TreeItem) findItem(elem);
    tree.getSelectionModel().select(item);
  }

  /**
   * Sets whether the children should be cached after first being retrieved from
   * the content provider. When <code>false</code>, the tree items will be
   * removed when collapsed.Default value is <code>true</code>.
   * 
   * @param caching
   */
  public void setCaching(boolean caching) {
    this.caching = caching;
  }

  public boolean setChecked(Object element, boolean state) {
    TreeItem item = (TreeItem) findItem(element);

    if (item != null) {
      item.setChecked(state);
      return true;
    }

    return false;
  }

  public void setContentProvider(IContentProvider contentProvider) {
    super.setContentProvider(contentProvider);
    if (contentProvider instanceof ICheckable) {
      checkable = true;
    }
  }

  /**
   * Sets the expanded state for the element.
   * 
   * @param element the element
   * @param expanded the expand state
   */
  public void setExpanded(Object element, boolean expanded) {
    TreeItem item = (TreeItem) findItem(element);
    if (item != null) {
      item.setExpanded(expanded);
    }
  }
  
  public void setInput(final Object input) {
    ITreeContentProvider cp = (ITreeContentProvider) getContentProvider();
    cp.inputChanged(this, this.input, input);
    this.input = input;
    cp.getChildren(input, new IAsyncContentCallback() {
      public void setElements(Object[] elems) {
        elements = elems;
        onInputReceived(input);
      }
    });
  }

  /**
   * Sets the viewer's input.
   * 
   * @param input the input
   * @param force true to force loading of add children
   */
  public void setInput(Object input, boolean force) {
    this.force = force;
    setInput(input);
  }

  public void setSelection(ISelection selection, boolean reveal) {
    List selected = selection.toList();
    tree.getSelectionModel().deselectAll();
    TreeItem[] items = tree.getAllItems();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
      Object elem = item.getData();
      if (selected.contains(elem)) {
        tree.getSelectionModel().select(item);
      }
    }
  }
  
  public void update() {
    TreeItem root = tree.getRootItem();
    TreeItem[] items = tree.getAllItems();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
      if (item != root) {
        updateInternal(item);
      }
    }
  }

  public void update(Object elem) {
    TreeItem item = (TreeItem) findItem(elem);
    if (item != null) {
      item.setData(elem);
      updateInternal(item);
    }
  }

  protected void add(Object elem) {

  }

  protected Object[] filter(Object[] elements) {
    filterItems(tree.getRootItem());
    return null;
  }

  protected List getSelectedFromWidget() {
    ArrayList elems = new ArrayList();
    TreeItem[] items = tree.getSelection();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
      elems.add(item.getData());
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    init(tree);
    widget.addListener(Events.CheckChange, new Listener() {
      public void handleEvent(BaseEvent be) {
        fireCheckStateChanged(be);
      }
    });
  }

  protected void insert(Object elem, int index) {
    // do nothing
  }

  protected void onInputReceived(Object input) {
    renderTree();
  }

  protected TreeItem renderItem(TreeItem parent, Object elem, int position) {
    boolean hasChildren = false;

    hasChildren = ((ITreeContentProvider) getContentProvider()).hasChildren(elem);

    LabelProvider lp = (LabelProvider) getLabelProvider();

    TreeItem item = new TreeItem();
    item.setData(elem);
    item.setText(lp.getText(elem));
    item.setIconStyle(lp.getIconStyle(elem));
    item.setTextStyle(lp.getTextStyle(elem));
    item.setLeaf(!hasChildren);

    nodeMap.put(elem, item);

    if (checkable) {
      item.setChecked(((ICheckable) getContentProvider()).getChecked(elem));
    }

    if (position == -1) {
      parent.add(item);
    } else {
      parent.insert(item, position);
    }

    return item;
  }

  private void filterItems(TreeItem item) {
    if (item.isRoot() || isOrDecendantSelected(item.getData())) {
      item.setVisible(true);
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        filterItems(item.getItem(i));
      }
    } else {
      item.setVisible(false);
    }
  }

  private void fireCheckStateChanged(BaseEvent be) {
    if (checkChangeListener != null) {
      TreeItem item = (TreeItem) be.widget;

      CheckStateChangedEvent evt = new CheckStateChangedEvent(this, item.getData(),
          item.isChecked());
      Iterator it = checkChangeListener.iterator();

      while (it.hasNext()) {
        ((ICheckStateListener) it.next()).checkStateChanged(evt);
      }
    }
  }

  private native void init(Tree tree) /*-{
    tree.@net.mygwt.ui.client.widget.tree.Tree::isViewer = true;
  }-*/;

  private void internalAdd(TreeItem parent, Object elem, int position) {
    parent.setLeaf(false);
    // if the children have not been loaded then do nothing
    if (parent.getData("loaded") != null) {
      renderItem(parent, elem, position);
      if (getSorter() != null) {
        sortChildren(parent);
      }
      filterItems(parent);
    }
  }
  
  private boolean isOrDecendantSelected(Object elem) {
    if (!isFiltered(null, elem)) {
      return true;
    }

    TreeItem item = (TreeItem) nodeMap.get(elem);
    if (item != null) {
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        TreeItem child = item.getItem(i);
        boolean result = isOrDecendantSelected(child.getData());
        if (result) {
          return true;
        }
      }
    }
    return false;
  }

  private void loadChildren(final TreeItem item, final boolean expand) {

    ITreeContentProvider acp = (ITreeContentProvider) getContentProvider();

    // if there is an async call out for my children already, I want to make
    // sure that I don't make another call and load the same items twice
    if (!item.isEnabled()) {
      return;
    }
    item.setEnabled(false);

    if (expand) {
      item.getUI().onLoadingChange(true);
    }

    acp.getChildren(item.getData(), new IAsyncContentCallback() {

      public void setElements(Object[] children) {
        boolean rendered = item.isRendered();
        if (rendered) {
          item.getUI().onLoadingChange(false);
        }
        if (children == null) {
          item.setData("state", null);
          return;
        }
        item.setEnabled(true);
//        children = filterChildren(item, children);
        sortElements(children);

        boolean f = item.getData("force") != null;
        item.setData("force", null);

        for (int i = 0; i < children.length; i++) {
          TreeItem child = renderItem(item, children[i], -1);
          if (f) {
            child.setData("force", "true");
            loadChildren(child, false);
          }
        }
        item.setData("loaded", "true");
        if (item.hasChildren()) {
          item.setExpanded(expand);
        } else {
          item.setLeaf(true);
          if (rendered) {
            item.getUI().updateJoint();
          }
        }

      }
    });

  }

  private void renderTree() {
    TreeItem root = tree.getRootItem();
    root.setData(input);

    int count = root.getItemCount();
    for (int i = 0; i < count; i++) {
      root.remove(root.getItem(0));
    }

    nodeMap.clear();

    Object[] elems = elements;
    elems = sortElements(elems);
    for (int i = 0; i < elems.length; i++) {
      Object elem = elems[i];
      TreeItem item = null;
//      if (getFilters().length > 0) {
//        if (isOrDecendantSelected(elem)) {
//          item = renderItem(root, elem, -1);
//        }
//      } else {
        item = renderItem(root, elem, -1);
//      }
      if (force && item != null) {
        item.setData("force", "true");
        loadChildren(item, false);
      }
    }
    force = false;
  }

  private void sortChildren(TreeItem parent) {
    Object[] elems = new Object[parent.getItemCount()];
    Element p = parent.getContainer();
    for (int i = 0; i < elems.length; i++) {
      TreeItem item = parent.getItem(i);
      DOM.removeChild(p, item.getElement());
      elems[i] = item.getData();
    }
    sortElements(elems);

    for (int i = 0; i < elems.length; i++) {
      TreeItem item = (TreeItem) findItem(elems[i]);
      Element elem = item.getElement();
      DOM.appendChild(p, elem);
    }

  }

  private void updateInternal(TreeItem item) {
    LabelProvider lp = (LabelProvider) getLabelProvider();
    Object elem = item.getData();

    item.setText(lp.getText(elem));
    item.setIconStyle(lp.getIconStyle(elem));
    item.setTextStyle(lp.getTextStyle(elem));
  }
}
