/*******************************************************************************
 * Copyright (c) 2007 MyGWT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.data.DataLoader;
import net.mygwt.ui.client.data.LoadEvent;
import net.mygwt.ui.client.data.Loader;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.table.TableColumn;
import net.mygwt.ui.client.widget.tree.Tree;
import net.mygwt.ui.client.widget.tree.TreeItem;
import net.mygwt.ui.client.widget.treetable.TreeTable;
import net.mygwt.ui.client.widget.treetable.TreeTableItem;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * An interface to content providers for treetable-structure-oriented viewers.
 * 
 * Content providers for treetable viewers must implement either the
 * <code>ITreeContentProvider</code> interface or the
 * <code>IAsyncTreeContentProvider</code> interface.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 * 
 * @see TreeViewer
 */
public class TreeTableViewer extends ColumnViewer implements ICheckable, ITreeViewer {

  protected boolean checkable;

  private ViewerCell viewerCell = new ViewerCell();
  private TreeTable treeTable;
  private List checkChangeListener;
  private boolean caching = true;
  private Map nodeMap = new HashMap();
  private boolean force;
  private Loader loader;
  private Listener loadListener;

  /**
   * Creates a new treetable viewer instance.
   * 
   * @param treeTable the underlying treetable widget
   */
  public TreeTableViewer(TreeTable treeTable) {
    this.treeTable = treeTable;

    // force render
    treeTable.getElement();
    hookWidget(treeTable);

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

    treeTable.addListener(Events.BeforeExpand, l);
    treeTable.addListener(Events.Collapse, l);
  }

  /**
   * Adds the given child element to this viewer as a child of the given parent
   * element.
   * 
   * @param parent the parent element
   * @param child the child element
   */
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
    filterItems(treeTable.getRootItem());
  }

  public Widget findItem(Object elem) {
    TreeItem[] items = treeTable.getAllItems();
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
   * Returns the viewer's tree widget.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return treeTable;
  }

  /**
   * Returns the underlying treetable widget
   * 
   * @return the treetable
   */
  public TreeTable getTreeTable() {
    return treeTable;
  }

  /**
   * Returns the table viewer column for the specified column.
   * 
   * @param columnId the column id
   * @return the table viewer column
   */
  public TreeTableViewerColumn getViewerColumn(int columnId) {
    TableColumn column = getTreeTable().getColumnModel().getColumn(columnId);
    TreeTableViewerColumn vc = (TreeTableViewerColumn) column.getData(ViewerColumn.COLUMN_VIEWER_KEY);
    if (vc == null) {
      vc = new TreeTableViewerColumn(this, column);
    }
    return vc;
  }

  public Widget getWidget() {
    return treeTable;
  }

  /**
   * Inserts the given element as a new child element of the given parent
   * element at the given position.
   * 
   * @param parent the parent element
   * @param child the child element
   * @param position the insert position
   */
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

  /**
   * Removes the given element from the viewer.
   * 
   * @param element the element to be removed
   */
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
    treeTable.getSelectionModel().select(item);
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
    if (contentProvider instanceof Loader) {
      bind((Loader) contentProvider);
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
  public void setInput(final Object input, boolean force) {
    this.force = force;
    setInput(input);
  }

  public void setSelection(ISelection selection, boolean reveal) {
    List selected = selection.toList();
    treeTable.getSelectionModel().deselectAll();
    TreeItem[] items = treeTable.getAllItems();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
      Object elem = item.getData();
      if (selected.contains(elem)) {
        treeTable.getSelectionModel().select(item);
      }
    }
  }

  public void update() {
    TreeItem[] items = treeTable.getAllItems();
    for (int i = 0; i < items.length; i++) {
      TreeTableItem item = (TreeTableItem) items[i];
      updateInternal(item);
    }
  }

  public void update(Object elem) {
    TreeTableItem item = (TreeTableItem) findItem(elem);
    if (item != null) {
      item.setData(elem);
      updateInternal(item);
    }
  }

  protected void add(Object elem) {

  }

  protected List getSelectedFromWidget() {
    ArrayList elems = new ArrayList();
    TreeItem[] items = treeTable.getSelection();
    for (int i = 0; i < items.length; i++) {
      TreeItem item = items[i];
      elems.add(item.getData());
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
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

  protected TreeTableItem renderItem(TreeItem parent, Object elem, int position) {
    boolean hasChildren = false;

    hasChildren = ((ITreeContentProvider) getContentProvider()).hasChildren(elem);

    int cols = getTreeTable().getColumnCount();
    String[] values = new String[cols];
    String[] toolTips = new String[cols];
    String[] styles = new String[cols];

    String iconStyle = null;

    for (int j = 0; j < cols; j++) {
      CellLabelProvider lp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      if (lp != null) {
        viewerCell.reset(elem, null, j, getTreeTable().getColumn(j).getID());
        lp.update(viewerCell);
        values[j] = viewerCell.getText();
        toolTips[j] = viewerCell.getToolTipText();
        styles[j] = viewerCell.getTextStyle();
        if (j == 0 && viewerCell.getIconStyle() != null) {
          iconStyle = viewerCell.getIconStyle();
        }
      }
    }

    LabelProvider lp = (LabelProvider) getLabelProvider();
    if (lp != null) {
      values[0] = lp.getText(elem);
      if (lp.getIconStyle(elem) != null) {
        iconStyle = lp.getIconStyle(elem);
      }
    }

    TreeTableItem item = new TreeTableItem(values);
    item.setData(elem);
    item.setText(values[0]);
    item.setCellToolTips(toolTips);
    item.setIconStyle(iconStyle);
    item.setLeaf(!hasChildren);

    nodeMap.put(elem, item);

    if (checkable) {
      item.setChecked(((ICheckable) getContentProvider()).getChecked(elem));
    }

    for (int i = 0; i < styles.length; i++) {
      if (styles[i] != null) {
        item.setCellStyle(i, styles[i]);
      }
    }

    if (position == -1) {
      parent.add(item);
    } else {
      parent.insert(item, position);
    }

    if (force && item != null) {
      item.setData("force", "true");
      loadChildren(item, false);
    }

    return item;

  }

  private Object[] filterChildren(Object parent, Object[] elems) {
    List temp = new ArrayList();
    for (int i = 0; i < elems.length; i++) {
      if (isOrDecendantSelected(elems[i])) {
        temp.add(elems[i]);
      }
    }
    return temp.toArray();
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

  private void internalAdd(TreeItem parent, Object elem, int position) {
    // if the children have not been loaded then do nothing
    if (parent.getData("loaded") != null) {
      if (!isFiltered(parent, elem)) {
        renderItem(parent, elem, position);
        if (getSorter() != null) {
          sortChildren(parent);
        }
      }
    }
  }

  /**
   * Returns <code>true</code> if the element or any child elements is not
   * filtered.
   * 
   * @param elem the element
   * @return the select state
   */

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

  private void loadChildren(final TreeItem item, boolean expand) {

    ITreeContentProvider cp = (ITreeContentProvider) getContentProvider();

    // if there is an async call out for my children already, I want to make
    // sure that I don't make another call and load the same items twice
    if (!item.isEnabled()) {
      return;
    }
    item.setEnabled(false);

    item.getUI().onLoadingChange(true);
    cp.getChildren(item.getData(), new IAsyncContentCallback() {

      public void setElements(Object[] children) {
        if (children == null) {
          item.setData("state", null);
          item.getUI().onLoadingChange(false);
          return;
        }
        item.getUI().onLoadingChange(false);
        item.setEnabled(true);
        children = filterChildren(item, children);
        sortElements(children);

        boolean f = item.getData("force") != null;
        item.setData("force", null);

        for (int i = 0; i < children.length; i++) {
          TreeTableItem child = renderItem(item, children[i], -1);
          if (f) {
            child.setData("force", "true");
            loadChildren(child, false);
          }
        }
        item.setData("loaded", "true");
        if (item.hasChildren()) {
          item.setExpanded(true);
        } else {
          item.setLeaf(true);
          item.getUI().updateJoint();
        }

      }
    });

  }

  protected void renderTree() {
    TreeItem root = treeTable.getRootItem();
    root.setData(input);

    int count = root.getItemCount();
    for (int i = 0; i < count; i++) {
      root.remove(root.getItem(0));
    }

    Object[] elems = elements;
    elems = sortElements(elems);
    for (int i = 0; i < elems.length; i++) {
      Object elem = elems[i];
      TreeTableItem item = null;
      if (getFilters().length > 0) {
        if (isOrDecendantSelected(elem)) {
          item = renderItem(root, elem, -1);
        }
      } else {
        item = renderItem(root, elem, -1);
      }
      if (item != null) {
        if (force && item != null) {
          item.setData("force", "true");
          loadChildren(item, false);
        }
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

  private void updateInternal(TreeTableItem item) {
    Object elem = item.getData();

    String iconStyle = null;
    String text = null;

    int cols = treeTable.getColumnCount();
    for (int j = 0; j < cols; j++) {
      viewerCell.reset(elem, item, j, getTreeTable().getColumn(j).getID());
      CellLabelProvider clp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      if (clp != null) {
        clp.update(viewerCell);
        item.setValue(j, viewerCell.getText());
        item.setCellToolTip(j, viewerCell.getToolTipText());
        String style = viewerCell.getTextStyle();
        if (style != null) {
          item.setCellStyle(j, style);
        }
        if (j == 0) {
          text = viewerCell.getText();
          iconStyle = viewerCell.getIconStyle();
        }
      }
    }

    LabelProvider lp = (LabelProvider) getLabelProvider();
    if (lp != null) {
      text = lp.getText(elem);
      if (lp.getIconStyle(elem) != null) {
        iconStyle = lp.getIconStyle(elem);
      }
    }
    item.setText(text);
    item.setIconStyle(iconStyle);
  }

  private void onBeforeLoad(LoadEvent be) {
    LoadingPanel.get().show(treeTable);
  }

  private void onLoad(LoadEvent de) {
    ISelection sel = getSelection();
    // apply sort if sorting locally
    if (!de.loader.getRemoteSort()) {
      if (de.loader.getSortField() != null) {
        String field = de.loader.getSortField();
        TableColumn column = treeTable.getColumn(field);
        if (column != null) {
          // int index = treeTable.getColumnModel().indexOf(column);
          // TODO doLocalSort(index);
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
}
