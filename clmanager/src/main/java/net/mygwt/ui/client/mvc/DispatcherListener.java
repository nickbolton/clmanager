/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.mvc;

import java.util.EventListener;

/**
 * Event interface for dispatcher events.
 */
public interface DispatcherListener extends EventListener {

  /**
   * Fires before an event is dispatched. Listeners can set the
   * <code>doit</code> field to <code>false</code> to cancel the action.
   * 
   * @param ae the app event to be dispatched
   */
  public void beforeDispatch(AppEvent ae);

  /**
   * Fires after an event has been dispatched.
   * 
   * @param ae the event that was dispatched
   */
  public void afterDispatch(AppEvent ae);

}
