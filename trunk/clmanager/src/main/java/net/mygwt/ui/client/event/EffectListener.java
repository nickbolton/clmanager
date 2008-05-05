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
 * Event interface for effect events.
 */
public interface EffectListener extends EventListener {

  /**
   * Fires prior to an effect starting.
   * 
   * @param be an event containing information about the event
   */
  public void effectStart(BaseEvent be);

  /**
   * Fires after an effect has been cancelled.
   * 
   * @param be an event containing information about the event
   */
  public void effectCancel(BaseEvent be);

  /**
   * Fires after an effect has completed.
   * 
   * @param be an event containing information about the event
   */
  public void effectComplete(BaseEvent be);

}
