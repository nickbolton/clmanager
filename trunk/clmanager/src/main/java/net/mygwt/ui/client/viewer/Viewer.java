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
 *     Tom Schindl <tom.schindl@bestsolution.at> - fix in issue: 38
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Observable;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.ui.Widget;

/**
 * A Viewer is a model-based adapter for a <code>Widget</code>. Supports
 * custom sorting, filtering.
 */
public abstract class Viewer extends Observable implements ISelectionProvider {

  /**
   * This viewer's input, or <code>null</code> if none. The viewer's input
   * provides the "model" for the viewer's content.
   */
  protected Object input;

  protected Object[] elements;

  private IElementComparer comparer;
  private IBaseLabelProvider labelProvider;
  private IContentProvider contentProvider;
  private List filters;
  private ViewerSorter sorter;
  private List selectionListeners;

  /**
   * Adds the given filter to this viewer, and triggers refiltering and
   * resorting of the elements.
   * 
   * @param filter the filter to be added
   */
  public void addFilter(ViewerFilter filter) {
    if (filters == null) {
      filters = new ArrayList();
    }
    filters.add(filter);
    applyFilters();
  }

  /**
   * Applies the viewer's filters.
   */
  public void applyFilters() {
    if (elements == null) return;
    int count = elements.length;
    for (int i = 0; i < count; i++) {
      Widget w = findItem(elements[i]);
      if (w != null) {
        w.setVisible(!isFiltered(null, elements[i]));
      }
    }
  }

  public void addSelectionListener(ISelectionChangedListener listener) {
    if (selectionListeners == null) {
      selectionListeners = new ArrayList();
    }
    if (!selectionListeners.contains(listener)) {
      selectionListeners.add(listener);
    }
  }

  /**
   * Finds the widget which represents the given element.
   * 
   * @param elem the element
   * @return the corresponding widget, or <code>null</code> if none
   */
  public abstract Widget findItem(Object elem);

  /**
   * Returns the comparer to use for comparing elements, or <code>null</code>
   * if none has been set. If specified, the viewer uses this to compare and
   * hash elements rather than the elements' own equals and hashCode methods.
   * 
   * @return the comparer to use for comparing elements or <code>null</code>
   */
  public IElementComparer getComparer() {
    return comparer;
  }

  /**
   * Returns the viewer's content provider.
   * 
   * @return the content provider
   */
  public IContentProvider getContentProvider() {
    return contentProvider;
  }

  public Object[] getElements() {
    return elements;
  }

  /**
   * Returns the active elements in the order being displayed.
   * 
   * @return the active elements
   */
  public Object[] getActiveElements() {
    ArrayList list = new ArrayList();
    int count = elements.length;
    for (int i = 0; i < count; i++) {
      if (!isFiltered(null, elements[i])) {
        list.add(elements[i]);
      }
    }
    return list.toArray();
  }

  /**
   * Returns the viewer's filters.
   * 
   * @return the filters
   */
  public ViewerFilter[] getFilters() {
    if (filters == null) {
      return new ViewerFilter[0];
    }
    ViewerFilter[] result = new ViewerFilter[filters.size()];
    filters.toArray(result);
    return result;
  }

  /**
   * Returns the viewer's input.
   * 
   * @return the input
   */
  public Object getInput() {
    return input;
  }

  /**
   * Returns the viewer's label provider.
   * 
   * @return the label provider
   */
  public IBaseLabelProvider getLabelProvider() {
    return labelProvider;
  }

  /**
   * Returns the current selection for this provider.
   * 
   * @return the current selection
   */
  public ISelection getSelection() {
    Widget widget = getWidget();
    if (widget == null) {
      return DefaultSelection.EMPTY;
    }
    List elements = getSelectedFromWidget();
    return new DefaultSelection(elements);
  }

  /**
   * Returns this viewer's sorter, or <code>null</code> if it does not have
   * one.
   * 
   * @return a viewer sorter, or <code>null</code> if none
   */
  public ViewerSorter getSorter() {
    return sorter;
  }

  /**
   * Returns the primary widget associated with this viewer.
   * 
   * @return the widget which displays this viewer's content
   */
  public abstract Widget getWidget();

  /**
   * Returns <code>true</code> if the element is filtered. Filtered elements
   * will not be displayed by the viewer.
   * 
   * @param parent the parent element
   * @param elem the element to test
   * @return <code>true</code> if the element is filtered
   */
  public boolean isFiltered(Object parent, Object elem) {
    if (filters != null) {
      for (int i = 0; i < filters.size(); i++) {
        ViewerFilter filter = (ViewerFilter) filters.get(i);
        boolean result = filter.select(this, parent, elem);
        if (!result) {
          return true;
        }
      }
    }
    return false;
  }

  protected void preserveSelections(ISelection selection) {

  }

  /**
   * Refreshes this viewer completely with information freshly obtained from
   * this viewer's model.
   */
  public void refresh() {
    DefaultSelection sel = (DefaultSelection) getSelection();
    if (input != null) {
      setInput(input);
    }
    preserveSelections(sel);
    setSelection(sel);
  }

  /**
   * Removes the element.
   * 
   * @param elem the element to be removed
   */
  public abstract void remove(Object elem);

  /**
   * Removes the given filter from this viewer, and triggers refiltering and
   * resorting of the elements if required.
   * 
   * @param filter the filter to be removed
   */
  public void removeFilter(ViewerFilter filter) {
    if (filters != null && filters.contains(filter)) {
      filters.remove(filter);
      refresh();
    }
  }

  public void removeSelectionListener(ISelectionChangedListener listener) {
    if (selectionListeners != null) {
      selectionListeners.remove(listener);
    }
  }

  /**
   * Sets the comparer to use for comparing elements, or <code>null</code> to
   * use the default equals and hashCode methods on the elements themselves.
   * 
   * @param comparer the comparer to use for comparing elements or null
   */
  public void setComparer(IElementComparer comparer) {
    this.comparer = comparer;
  }

  /**
   * Sets the viewer's content provider.
   * 
   * @param contentProvider the new content provider
   */
  public void setContentProvider(IContentProvider contentProvider) {
    this.contentProvider = contentProvider;
  }

  /**
   * Sets the viewer's input.
   * 
   * @param input the new input
   */
  public void setInput(final Object input) {
    contentProvider.inputChanged(this, this.input, input);
    IStructuredContentProvider cp = (IStructuredContentProvider) contentProvider;
    cp.getElements(input, new IAsyncContentCallback() {
      public void setElements(Object[] elems) {
        elements = elems;
        onInputReceived(input);
      }
    });
    this.input = input;
  }

  /**
   * Sets the viewer's label provider.
   * 
   * @param labelProvider the new label provider
   */
  public void setLabelProvider(IBaseLabelProvider labelProvider) {
    this.labelProvider = labelProvider;
  }

  /**
   * The viewer implementation of this <code>ISelectionProvider</code> method
   * make the new selection for this viewer without making it visible.
   * <p>
   * This method is equivalent to <code>setSelection(selection,false)</code>.
   * </p>
   */
  public void setSelection(ISelection selection) {
    setSelection(selection, false);
  }

  /**
   * Sets a new selection for this viewer and optionally makes it visible.
   * <p>
   * Subclasses must implement this method.
   * </p>
   * 
   * @param selection the new selection
   * @param reveal <code>true</code> if the selection is to be made visible,
   *            and <code>false</code> otherwise
   */
  public abstract void setSelection(ISelection selection, boolean reveal);

  /**
   * Sets this viewer's sorter and triggers refiltering and resorting of this
   * viewer's element. Passing <code>null</code> turns sorting off.
   * 
   * @param sorter a viewer sorter, or <code>null</code> if none
   */
  public void setSorter(ViewerSorter sorter) {
    this.sorter = sorter;
    refresh();
  }

  /**
   * Refreshes labels with information from the viewer's label provider.
   */
  public abstract void update();

  /**
   * Refreshes labels with information from the viewer's label provider.
   * 
   * @param elem the element to be updated
   */
  public abstract void update(Object elem);

  /**
   * Adds the element.
   * 
   * @param elem the element to be added
   */
  protected abstract void add(Object elem);

  /**
   * Returns the result of running the given elements through the filters.
   * 
   * @param elements the elements to filter
   * @return only the elements which all filters accept
   */
  protected Object[] filter(Object[] elements) {
    if (filters != null) {
      ArrayList filtered = new ArrayList(elements.length);
      Object root = getRoot();
      for (int i = 0; i < elements.length; i++) {
        boolean add = true;
        for (int j = 0; j < filters.size(); j++) {
          add = ((ViewerFilter) filters.get(j)).select(this, root, elements[i]);
          if (!add) {
            break;
          }
        }
        if (add) {
          filtered.add(elements[i]);
        }
      }
      return filtered.toArray();
    }
    return elements;
  }

  /**
   * Notifies any selection listeners that the viewer's selection has changed.
   * 
   * @param se the selection event
   */
  protected void fireSelectionChanged(SelectionChangedEvent se) {
    if (selectionListeners == null) {
      selectionListeners = new ArrayList();
    }
    for (int i = 0; i < selectionListeners.size(); i++) {
      ISelectionChangedListener listener = (ISelectionChangedListener) selectionListeners.get(i);
      listener.selectionChanged(se);
    }
  }

  /**
   * Returns the root element.
   * <p>
   * The default implementation of this framework method forwards to
   * <code>getInput</code>. Override if the root element is different from
   * the viewer's input element.
   * </p>
   * 
   * @return the root element, or <code>null</code> if none
   */
  protected Object getRoot() {
    return getInput();
  }

  /**
   * Retrieves the selection of the viewer.
   * 
   * @return the list of selected elements
   */
  protected abstract List getSelectedFromWidget();

  protected void hookWidget(Component widget) {
    final ISelectionProvider provider = this;
    widget.addListener(Events.SelectionChange, new Listener() {
      public void handleEvent(BaseEvent be) {
        SelectionChangedEvent se = new SelectionChangedEvent(provider, getSelection());
        fireSelectionChanged(se);
      }
    });
  }

  /**
   * Inserts the element.
   * 
   * @param elem the element
   * @param index the insert location
   */
  protected abstract void insert(Object elem, int index);

  /**
   * Internal hook method called when the input to this viewer is initially set
   * or subsequently changed.
   * 
   * @param input the new input
   */
  protected abstract void onInputReceived(Object input);

  protected void removeElement(Object elem) {
    List list = new ArrayList();
    for (int i = 0; i < elements.length; i++) {
      if (elements[i] != elem) {
        list.add(elements[i]);
      }
    }
    elements = list.toArray();
  }

  protected Object[] sortElements(Object[] elements) {
    if (sorter != null) {
      sorter.sort(this, elements);
    }
    return elements;
  }

}
