/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.util.Point;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.util.Size;
import net.mygwt.ui.client.util.TextMetrics;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays a loading message and adds a gray overlay.
 */
public class LoadingPanel extends Widget {

  private static LoadingPanel instance;

  /**
   * Returns the singleton instance.
   * 
   * @return the panel
   */
  public static LoadingPanel get() {
    if (instance == null) {
      instance = new LoadingPanel();
    }
    return instance;
  }

  private boolean enabled = true;
  private Element panel;
  private Widget container;
  private Element root;

  private LoadingPanel() {
    root = DOM.createDiv();
    MyDOM.setStyleAttribute(root, "position", "absolute");
    MyDOM.setStyleName(root, "my-mask");
    DOM.appendChild(MyDOM.getBody(), root);

    panel = DOM.createSpan();
    setElement(DOM.createDiv());
    MyDOM.setStyleAttribute(getElement(), "position", "absolute");
    DOM.appendChild(getElement(), panel);
    setStyleName("my-loading-panel");
    RootPanel.get().add(this);
    hide();
  }

  /**
   * Hides the panel.
   */
  public void hide() {
    MyDOM.setLeftTop(root, -1000, -1000);
    MyDOM.setLeftTop(getElement(), -1000, -1000);
  }

  /**
   * Returns <code>true</code> if enabled, <code>false</code> otherwise.
   * 
   * @return the enabled state
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Enables and disables the loading panel.
   * 
   * @param enable <code>true</code> to enable
   */
  public void setEnabled(boolean enable) {
    this.enabled = enable;
  }

  /**
   * Displays the panel filling the viewport.
   */
  public void show() {
    show(RootPanel.get(), null);
  }

  /**
   * Desplays the panel filling the viewport.
   * 
   * @param text the loading text
   */
  public void show(String text) {
    show(RootPanel.get(), text);
  }

  /**
   * Displays the panel filling the given container.
   * 
   * @param container the container
   */
  public void show(Widget container) {
    show(container, null);
  }

  /**
   * Displays the panel filling the given container.
   * 
   * @param container the container
   * @param text the loading text
   */
  public void show(Widget container, String text) {
    if (!enabled) {
      return;
    }

    this.container = container;

    String txt = text == null ? MyGWT.MESSAGES.loading() : text;
    MyDOM.setInnerHTML(panel, txt);
    DOM.setIntStyleAttribute(root, "zIndex", MyDOM.getZIndex());
    DOM.setIntStyleAttribute(getElement(), "zIndex", MyDOM.getZIndex());
    
    TextMetrics metrics = TextMetrics.get();
    metrics.bind(panel);
    int width = metrics.getWidth(txt);
    setWidth(width + 26 + "px");
    update();
  }

  private void update() {
    if (isAttached()) {
      Rectangle rect = null;
      if (container == RootPanel.get()) {
        Size size = MyDOM.getViewportSize();
        rect = new Rectangle(0, 0, size.width, size.height);
      } else {
        rect = MyDOM.getBounds(container.getElement());
      }
      MyDOM.setBounds(root, rect);

      int w = rect.x + (rect.width / 2) - (getOffsetWidth() / 2);
      int h = rect.y + (rect.height / 2) - (getOffsetHeight() / 2);

      if (w < 0 || h < 0) {
        return;
      }

      MyDOM.setXY(getElement(), new Point(w, h));
    }
  }

}
