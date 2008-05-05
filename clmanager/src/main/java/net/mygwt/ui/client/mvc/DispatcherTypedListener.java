/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.mvc;

import java.util.EventListener;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

/**
 * Provides a mapping between the typed and untyped listener mechanisms that for
 * <code>Dispatchers</code>.
 */
class DispatcherTypedListener implements Listener {

  protected EventListener eventListener;

  /**
   * Creates a new typed listener.
   * 
   * @param listener the typed listener
   */
  public DispatcherTypedListener(EventListener listener) {
    eventListener = listener;
  }

  /**
   * Returns the event listener.
   * 
   * @return the event listener
   */
  public EventListener getEventListener() {
    return eventListener;
  }

  public void handleEvent(BaseEvent be) {
    AppEvent ae = (AppEvent) be.value;
    switch (be.type) {
      case Dispatcher.BeforeDispatch:
        ((DispatcherListener) eventListener).beforeDispatch(ae);
        be.doit = ae.doit;
        break;
      case Dispatcher.AfterDispatch:
        ((DispatcherListener) eventListener).afterDispatch(ae);
        be.doit = ae.doit;
        break;
    }
  }
}
