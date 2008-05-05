/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.mvc;

/**
 * <code>AppEvents</code> are used to pass messages between
 * <code>Controllers</code> and <code>Views</code>. All events have a
 * specific type which are used to identify the event. Typically, applications
 * will define all application events in a constants class.
 */
public class AppEvent {

  /**
   * The type of the event.
   */
  public int type;

  /**
   * Application specific data such as the model.
   */
  public Object data;
  
  /**
   * Depending on the event, a flag indicating whether the operation should be
   * allowed.
   */
  public boolean doit = true;

  /**
   * Creates a new event with the given type.
   * 
   * @param type the event type
   */
  public AppEvent(int type) {
    this.type = type;
  }

  /**
   * Creates a new event with the given type and data.
   * 
   * @param type the event type
   * @param data the data
   */
  public AppEvent(int type, Object data) {
    this.type = type;
    this.data = data;
  }

  public String toString() {
    return "Event Type: " + type;
  }
}
