/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Instances of this class are sent as a result of model changes.
 * 
 * @see Model
 */
public class ChangeEvent {

  /**
   * The change type.
   * 
   * @see Model#Add
   * @see Model#Remove
   * @see Model#Update
   */
  public int type;

  /**
   * The model that fired the event.
   */
  public Model source;

  /**
   * The item being added or removed.
   */
  public Model item;
  
  /**
   * The location for inserts.
   */
  public int index;

  /**
   * Creates a new instance.
   * 
   * @param type the change type
   * @param source the object that was changed
   */
  public ChangeEvent(int type, Model source) {
    this.type = type;
    this.source = source;
  }

  /**
   * Creates a new instance.
   * 
   * @param type the change type
   * @param source the object that has changed
   * @param item the item that was added or removed
   */
  public ChangeEvent(int type, Model source, Model item) {
    this(type, source);
    this.item = item;
  }

}
