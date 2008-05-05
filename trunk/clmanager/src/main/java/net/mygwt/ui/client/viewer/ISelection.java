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
 *     Tom Schindl <tom.schindl@bestsolution.at> - bugfixes in issues: 41
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.Iterator;
import java.util.List;

/**
 * A selection containing elements.
 */
public interface ISelection {

  /**
   * Returns the first element in this selection, or null if the selection is
   * empty.
   * 
   * @return an element, or null if none
   */
  public Object getFirstElement();

  /**
   * Returns whether this selection is empty.
   * 
   * @return <code>true</code> if this selection is empty, and
   *         <code>false</code> otherwise
   */
  public boolean isEmpty();

  /**
   * Returns an iterator over the elements of this selection.
   * 
   * @return an iterator over the selected elements
   */
  public Iterator iterator();

  /**
   * Returns the number of elements selected in this selection.
   * 
   * @return the number of elements selected
   */
  public int size();

  /**
   * Returns the elements in this selection as an array.
   * 
   * @return the selected elements as an array
   */
  public Object[] toArray();

  /**
   * Returns the elements in this selection as a <code>List</code>.
   * 
   * @return the selected elements as a list
   */
  public List toList();

}
