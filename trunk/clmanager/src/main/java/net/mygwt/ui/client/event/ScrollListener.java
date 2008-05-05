/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.event;

import java.util.EventListener;

/**
 * Event interface for scroll events.
 */
public interface ScrollListener extends EventListener {

  /**
   * Fired when a widget is scrolled.
   * 
   * @param be an event containing information about the event
   */
  public void widgetScrolled(BaseEvent be);

}
