/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

/**
 * Abstract base class for objects that register listeners and fire events.
 * 
 * @see Listener
 * @see BaseEvent
 */
public class Observable {

  public EventTable eventTable;

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener) {
    if (eventTable == null) {
      eventTable = new EventTable();
    }
    eventTable.hook(eventType, listener);
  }

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener) {
    if (eventTable != null) {
      eventTable.unhook(eventType, listener);
    }
  }

  /**
   * Removes all listeners.
   */
  public void removeAllListeners() {
    if (eventTable != null) {
      eventTable.removeAllListeners();
    }
  }

  /**
   * Fires an event.
   * 
   * @param eventType the event type
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType) {
    return fireEvent(eventType, new BaseEvent());
  }

  /**
   * Fires an event.
   * 
   * @param eventType eventType the event type
   * @param be the base event
   * @return <code>true</code> if any listeners cancel the event.
   */
  public boolean fireEvent(int eventType, BaseEvent be) {
    be.type = eventType;
    be.source = this;
    if (eventTable != null) {
      return eventTable.sendEvent(be);
    }
    return true;
  }

}
