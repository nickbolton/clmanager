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
 * Event interface for widget events.
 */
public interface WidgetListener extends EventListener {

  /**
   * Fires after a widget is moved or resized.
   * 
   * @param be an event containing information about the event
   */
  public void widgetResized(BaseEvent be);

  /**
   * Fires after a widget is attached.
   * 
   * @param be an event containing information about the event
   */
  public void widgetAttached(BaseEvent be);

  /**
   * Fires after a widget is detached.
   * 
   * @param be an event containing information about the event
   */
  public void widgetDetached(BaseEvent be);

}
