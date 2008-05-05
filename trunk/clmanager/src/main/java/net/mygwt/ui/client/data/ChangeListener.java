/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Interface for objects that listen for model change events.
 * 
 * @see ChangeEvent
 * @see Model
 */
public interface ChangeListener {

  /**
   * Fired when the model's state has changed.
   * 
   * @param event an event describing the change
   */
  public void modelChanged(ChangeEvent event);

}
