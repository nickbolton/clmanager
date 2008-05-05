/*******************************************************************************
 * Copyright (c) 2007 MyGWT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Darrell Meyer <darrell@mygwt.net> - initial API and implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

/**
 * Asynchronous <code>ContentProvider</code> data callback.
 */
public interface IAsyncContentCallback {

  /**
   * Returns the elements to the source viewer. <code>null</code> if the data
   * is unable to be retrieved.
   * 
   * @param elements the elements
   */
  public void setElements(Object[] elements);

}
