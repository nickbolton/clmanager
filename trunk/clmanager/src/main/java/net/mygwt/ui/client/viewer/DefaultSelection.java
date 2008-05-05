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
import java.util.Iterator;
import java.util.List;

/**
 * A concrete implementation of the Selection interface, suitable for
 * instantiating.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 */
public class DefaultSelection implements ISelection {

  /**
   * The canonical empty selection. This selection should be used instead of
   * <code>null</code>.
   */
  public static final DefaultSelection EMPTY = new DefaultSelection();

  /**
   * The element that make up this structured selection.
   */
  private List elements;

  /**
   * Creates a new empty selection. See also the static field <code>EMPTY</code>
   * which contains an empty selection singleton.
   * 
   * @see #EMPTY
   */
  public DefaultSelection() {
    elements = new ArrayList();
  }

  /**
   * Creates a structured selection containing a single object. The object must
   * not be <code>null</code>.
   * 
   * @param element the element
   */
  public DefaultSelection(Object element) {
    elements = new ArrayList();
    elements.add(element);
  }

  /**
   * Creates a structured selection from the list of objects. The list must not
   * be <code>null</code>.
   * 
   * @param elements the elements
   */
  public DefaultSelection(List elements) {
    this.elements = elements;
  }

  public Object[] toArray() {
    return elements.toArray();
  }

  public List toList() {
    return elements;
  }

  public Object getFirstElement() {
    return isEmpty() ? null : elements.get(0);
  }

  public boolean isEmpty() {
    return elements == null || elements.size() == 0;
  }

  public Iterator iterator() {
    return elements.iterator();
  }

  public int size() {
    return elements.size();
  }

}
