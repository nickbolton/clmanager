/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.widget.layout.StackLayout;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * Represents a tabbed set of pages, each of which contains another widget. Its
 * child widgets are shown as the user selects the various tabs associated with
 * them.
 * 
 * <p>
 * Important: This component MUST be sized in pixels to render properly. This
 * can be done directly or by a layout. Percentage based sizes will not work.
 * </p>
 * 
 * <dl>
 * <dt><b>Style:</b></dt>
 * <dd>TOP</dd>
 * 
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
 * <dd><b>SelectionChange</b> : (widget, item)<br>
 * <div>Fires after a item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that is selected</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget, item)<br>
 * <div>Fires after a item is closed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was closed</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>my-tabfolder (the tab folder)</dd>
 * </dl>
 * 
 * @see TabItem
 */
public class TabFolder extends Container {

  boolean bottom;
  private Element tabPanelWrapper;
  private HorizontalPanel tabPanel;
  private WidgetContainer stack;
  private StackLayout stackLayout;
  private TabItem selectedItem;
  private TabItem attachItem;
  private int defaultWidth = 100;
  private Size lastSize;

  /**
   * Creates a new tab folder.
   */
  public TabFolder() {
    this(Style.NONE);
  }

  /**
   * Creates a new tab folder.
   * 
   * @param style the tab folder style.
   */
  public TabFolder(int style) {
    this.style = style;
    attachChildren = false;
  }

  /**
   * Adds a tab item.
   * 
   * @param item the item to be added
   */
  public void add(TabItem item) {
    insert(item, getWidgetCount());
  }

  /**
   * Returns the tab item's default width.
   * 
   * @return the default width
   */
  public int getDefaultWidth() {
    return defaultWidth;
  }

  /**
   * Returns the item at the specified index.
   * 
   * @param index the index
   * @return the item
   */
  public TabItem getItem(int index) {
    return (TabItem) items.get(index);
  }

  /**
   * Returns the number of tab items's.
   * 
   * @return the child count
   */
  public int getItemCount() {
    return items.size();
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item.
   */
  public TabItem getSelection() {
    return selectedItem;
  }

  /**
   * Returns the tab folder's style information.
   * 
   * @return the style bits
   */
  public int getStyle() {
    return style;
  }

  /**
   * Returns the index of the item.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(TabItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts a tab item.
   * 
   * @param item the item to be inserted
   * @param index the insert postion
   */
  public void insert(TabItem item, int index) {
    if (fireEvent(Events.BeforeAdd, this, item, index)) {
      item.tabFolder = this;
      items.add(index, item);
      if (rendered) {
        renderItem(item, index);
      }
      fireEvent(Events.Add, this, item, index);
    }
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the selected state
   */
  public boolean isSelected(TabItem item) {
    return selectedItem == item;
  }

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
      h -= MyDOM.getBorderWidth(getElement(), Style.LEFT | Style.RIGHT);
      w -= MyDOM.getBorderWidth(getElement(), Style.TOP | Style.BOTTOM);
      MyDOM.setWidth(tabPanelWrapper, w, true);
      h -= tabPanel.getOffsetHeight();
      stack.setSize(w, h);
      if (selectedItem != null) {
        selectedItem.getContainer().layout(true);
      }
    }
  }

  /**
   * Removes the tab item.
   * 
   * @param item the item to be removed
   */
  public void remove(TabItem item) {
    if (fireEvent(Events.BeforeRemove, this, item)) {
      if (rendered) {
        tabPanel.remove(item);
        stack.remove(item.getContainer());
      }

      items.remove(item);
      if (getAutoDispose()) {
        item.dispose();
        item.getContainer().dispose();
      }
      if (item == selectedItem) {
        selectedItem = null;
        if (getItemCount() > 0) {
          setSelection(getItem(0));
        }
      }
      fireEvent(Events.Remove, this, item);
    }
  }

  /**
   * Removes all items.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      TabItem item = getItem(0);
      remove(item);
    }
  }

  /**
   * Specifies the default width for tab items. Default value is 100.
   * 
   * @param defaultWidth the default width
   */
  public void setDefaultWidth(int defaultWidth) {
    this.defaultWidth = defaultWidth;
  }

  /**
   * Sets the selected tab item.
   * 
   * @param item the item to be selected
   */
  public void setSelection(TabItem item) {
    if (!isAttached()) {
      attachItem = item;
      return;
    }

    if (selectedItem != item) {
      if (selectedItem != null) {
        selectedItem.setSelected(false);
        // selectedItem.setEnabled(false);
      }
      selectedItem = item;
      if (item != null) {
        item.setSelected(true);
        // item.setEnabled(true);
        stackLayout.setTopWidget(item.getContainer());

        DeferredCommand.addCommand(new Command() {
          public void execute() {
            stackLayout.layout(stack);
          }
        });
      }
      fireEvent(Events.SelectionChange, this, selectedItem);
    }
  }

  /**
   * Sets the tab folder's style. Has no effect if called after the tab folder
   * has been rendered.
   * 
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  /**
   * Sets the width for all tab item's.
   * 
   * @param width the width
   */
  public void setTabWidth(int width) {
    defaultWidth = width;
    for (int i = 0; i < getWidgetCount(); i++) {
      TabItem item = getItem(i);
      MyDOM.setWidth(item.getElement(), width);
    }
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    WidgetHelper.doAttach(tabPanel);
    WidgetHelper.doAttach(stack);
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(tabPanel);
    WidgetHelper.doDetach(stack);
  }

  protected void onClose(TabItem item) {
    item.fireEvent(Events.Close);
    fireEvent(Events.Close, this, item);

    remove(item);

    if (item == selectedItem) {
      setSelection(getItem(0));
    }
  }

  protected void onLoad() {
    super.onLoad();
    if (attachItem != null) {
      setSelection(attachItem);
      attachItem = null;
    }
  }
  
  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName("my-tabfolder");

    tabPanelWrapper = DOM.createDiv();
    MyDOM.setStyleName(tabPanelWrapper, "my-tabfolder-header");

    tabPanel = new HorizontalPanel();

    stack = new WidgetContainer();
    stack.setStyleAttribute("position", "static");
    stackLayout = new StackLayout();
    stack.setLayout(stackLayout);

    if ((style & Style.BOTTOM) != 0) {
      bottom = true;
      // wrapper.add(stack);
      // wrapper.add(tabWrapper);
    } else {
      DOM.appendChild(tabPanelWrapper, tabPanel.getElement());
      DOM.appendChild(getElement(), tabPanelWrapper);
      DOM.appendChild(getElement(), stack.getElement());
    }

    renderAll();
  }
  
  protected void onResize(int width, int height) {
    recalculate();
  }

  private void renderAll() {
    int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      TabItem item = getItem(i);
      renderItem(item, i);
      if (i == 0 && attachItem == null) {
        attachItem = item;
      }
    }
  }

  private void renderItem(TabItem item, int index) {
    item.setWidth(defaultWidth + "px");
    tabPanel.insert(item, index);
    stack.insert(item.getContainer(), index);
  }

}
