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
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;

/**
 * A selectable user interface object corresponding to a tab for a page in a
 * <code>TabFolder</code>.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CLOSE</dd>
 * 
 * <dt><b>Events:</b></dt>
 * <dd><b>Close</b> : (widget)<br>
 * <div>Fires after a item is closed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tabitem (the tab item)</dd>
 * <dd>.my-tabitem .my-tabitem-text</dd>
 * </dl>
 */
public class TabItem extends Item {

  /**
   * The item's close button.
   */
  protected IconButton closeBtn;

  TabFolder tabFolder;
  WidgetContainer container;

  /**
   * Creates a new tab item.
   */
  public TabItem() {
    this(Style.NONE);
  }

  /**
   * Creates a new tab item.
   * 
   * @param style the widget style
   */
  public TabItem(int style) {
    super(style, "my-tabitem");
    if ((style & Style.CLOSE) != 0) {
      closeBtn = new IconButton("my-tab-close");
      closeBtn.addStyleName("my-tool-btn");
      addStyleName("my-tabitem-close");
      closeBtn.addListener(Events.Click, new Listener() {
        public void handleEvent(BaseEvent be) {
          close();
        }
      });
      addWidget(closeBtn);
    }
    container = new WidgetContainer();
  }

  /**
   * Closes the tab item.
   */
  public void close() {
    tabFolder.onClose(this);
  }

  /**
   * Returns the tab item's content cotainer.
   * 
   * @return the container
   */
  public WidgetContainer getContainer() {
    return container;
  }

  /**
   * Returns the item's parent tab folder.
   * 
   * @return the tab folder
   */
  public TabFolder getTabFolder() {
    return tabFolder;
  }

  /**
   * Sets the item's icon style.
   * 
   * @param style the icon style
   */
  public void setIconStyle(String style) {
    addStyleName("my-tabitem-icon");
    super.setIconStyle(style);
  }

  /**
   * Sets a url for the content area of the item.
   * 
   * @param url the url
   */
  public void setURL(String url) {
    Frame f = new Frame(url);
    MyDOM.setStyleAttribute(f.getElement(), "frameborder", "0");
    f.setSize("100%", "100%");
    container.layout = null;
    container.removeAll();
    container.add(f);
  }

  protected void onClick(BaseEvent be) {
    tabFolder.setSelection(this);
  }

  protected void onRender() {
    super.onRender();
    sinkEvents(Event.ONCLICK);
  }

  void onSelectedChange(boolean select) {

  }

}
