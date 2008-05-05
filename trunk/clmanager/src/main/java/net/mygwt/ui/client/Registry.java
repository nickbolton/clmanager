/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client;

import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.util.Observable;

/**
 * A local storage of objects stored by id.
 */
public class Registry extends Observable {

  protected static Map map = new HashMap();

  /**
   * Returns the object with the given id.
   * 
   * @param id the identifier
   * @return the object or <code>null</code> if no match
   */
  public static Object get(String id) {
    return map.get(id);
  }

  /**
   * Returns a map of all registered objects.
   * 
   * @return the object map
   */
  public static Map getAll() {
    return map;
  }

  /**
   * Registers an object.
   * 
   * @param id the indentifier
   * @param obj the object to be registred
   */
  public static void register(String id, Object obj) {
    map.put(id, obj);
  }

  /**
   * Unregisters an object.
   * 
   * @param id the identifier
   */
  public static void unregister(String id) {
    map.remove(id);
  }

  /**
   * Unregisters all registered objects.
   */
  public static void unregisterAll() {
    map.clear();
  }

  private Registry() {
  }

}
