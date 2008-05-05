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
 * Event interface for drag events.
 */
public interface DragListener extends EventListener {

  /**
   * Fires after a drag begins.
   * 
   * @param be an event containing information about the event
   */
  public void dragStart(BaseEvent be);

  /**
   * Fires after the mouse moves.
   * 
   * @param be an event containing information about the event
   */
  public void dragMove(BaseEvent be);

  /**
   * Fires after a drag ends.
   * 
   * @param be an event containing information about the event
   */
  public void dragEnd(BaseEvent be);

  /**
   * Fires after a drag is cancelled.
   * 
   * @param be an event containing information about the event
   */
  public void dragCancel(BaseEvent be);

}
