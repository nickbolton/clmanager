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
 * Interface for objects that are notified of MyGWT events.
 */
public interface Listener extends EventListener {

  /**
   * Sent when an event that the listener has registered for occurs.
   * 
   * @param be the event which occurred
   */
  public void handleEvent(BaseEvent be);
  
}
