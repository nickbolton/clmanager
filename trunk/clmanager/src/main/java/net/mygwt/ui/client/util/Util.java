/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import java.util.List;

import net.mygwt.ui.client.widget.Component;

/**
 * Various utility funcitons.
 */
public class Util {

  /**
   * Creates a Component[] from a list of Component's.
   * 
   * @param list the list
   * @return the array
   */
  public static Component[] createArray(List list) {
    int size = list.size();
    Component[] components = new Component[size];
    for (int i = 0; i < components.length; i++) {
      components[i] = (Component) list.get(i);
    }
    return components;
  }

  /**
   * Populates a list with an array of elements.
   * 
   * @param list the list
   * @param elements the elements to be added to the list
   */
  public static void fill(List list, Object[] elements) {
    for (int i = 0; i < elements.length; i++) {
      list.add(elements[i]);
    }
  }

  /**
   * Tests if the value is an integer.
   * 
   * @param value the value to test
   * @return the integer state
   */
  public static boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
