/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.Arrays;
import java.util.Comparator;

import net.mygwt.ui.client.util.DefaultComparator;

/**
 * A viewer sorter is used by a <code>Viewer</code> to reorder the elements
 * provided by its content provider.
 */
public class ViewerSorter {

  public static DefaultComparator DEFAULT_COMPARATOR = new DefaultComparator();
  
  protected Comparator comparator;

  /**
   * Creates a new instance that uses a <code>DefaultComparator</code>.
   */
  public ViewerSorter() {
    comparator = DEFAULT_COMPARATOR;
  }

  /**
   * Creates a new sorter instance with the given comparator.
   * 
   * @param comparator the sorter comparator
   */
  public ViewerSorter(Comparator comparator) {
    this.comparator = comparator;
  }

  /**
   * Returns a negative, zero, or positive number depending on whether the first
   * element is less than, equal to, or greater than the second element.
   * 
   * @param viewer the viewer
   * @param e1 the first element
   * @param e2 the second element
   * @return a negative number if the first element is less than the second
   *         element; the value <code>0</code> if the first element is equal
   *         to the second element; and a positive number if the first element
   *         is greater than the second element
   */
  public int compare(Viewer viewer, Object e1, Object e2) {
    return comparator.compare(e1, e2);
  }

  /**
   * Sorts the given elements in-place, modifying the given array.
   * 
   * @param viewer the viewer
   * @param elements the elemnts to sort
   */
  public void sort(final Viewer viewer, Object[] elements) {
    Arrays.sort(elements, new Comparator() {
      public int compare(Object a, Object b) {
        return ViewerSorter.this.compare(viewer, a, b);
      }
    });
  }

}
