/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import java.util.Comparator;

/**
 * Default <code>Comparator</code> implementation.
 */
public class DefaultComparator implements Comparator {

  public int compare(Object o1, Object o2) {
    if (o1 instanceof Comparable) {
      return ((Comparable)o1).compareTo(o2);
    } else {
      return compareStrings(o1.toString(), o2.toString());
    }
  }

  protected int compareStrings(String s1, String s2) {
    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }

}
