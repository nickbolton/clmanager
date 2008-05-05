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

import java.util.ArrayList;

/**
 * A viewer filter is used by a viewer to extract a subset of elements provided
 * by its content provider.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 * 
 * @see IContentProvider
 * @see Viewer
 */
public abstract class ViewerFilter {
  
  /**
   * Creates a new viewer filter.
   */
  protected ViewerFilter() {
  }

  /**
   * Filters the given elements for the given viewer. The input array is not
   * modified.
   * <p>
   * The default implementation of this method calls <code>select</code> on
   * each element in the array, and returns only those elements for which
   * <code>select</code> returns <code>true</code>.
   * </p>
   * 
   * @param viewer the viewer
   * @param parent the parent element
   * @param elements the elements to filter
   * @return the filtered elements
   */
  public Object[] filter(Viewer viewer, Object parent, Object[] elements) {
    int size = elements.length;
    ArrayList out = new ArrayList(size);
    for (int i = 0; i < size; ++i) {
      Object element = elements[i];
      if (select(viewer, parent, element)) {
        out.add(element);
      }
    }
    return out.toArray();
  }

  /**
   * Returns whether the given element makes it through this filter.
   * 
   * @param viewer the viewer
   * @param parentElement the parent element
   * @param element the element
   * @return <code>true</code> if element is included in the filtered set, and
   *         <code>false</code> if excluded
   */
  public abstract boolean select(Viewer viewer, Object parentElement, Object element);

}
