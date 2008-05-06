/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.DelayedTask;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A container that fills the viewport and monitors window resizing. The
 * container is automatically added to the root panel.
 */
public class Viewport extends WidgetContainer {

  private DelayedTask task = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
    }
  });

  private boolean initialized;

  /**
   * Creates a new viewport.
   */
  public Viewport() {
    Window.addWindowResizeListener(new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        task.delay(400);
      }
    });

    Window.enableScrolling(false);
    RootPanel.get().add(this);
  }

  protected void onLayout() {
    if (!initialized) {
      initialized = true;
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
    }
    lastSize = null;
    super.onLayout();
  }

  /**
   * Hides the loading panel.
   * 
   * @param id the loading panel id
   */
  public void hideLoadingPanel(String id) {
    MyGWT.hideLoadingPanel(id);
  }

  protected void onRender() {
    super.onRender();
    setStyleAttribute("position", "absolute");
  }

}
