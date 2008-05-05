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

/**
 * A content provider mediates between the viewer's model and the viewer itself.
 */
public interface IContentProvider {

  /**
   * Notifies this content provider that the given viewer's input has been
   * switched to a different element.
   * 
   * @param viewer the viewer
   * @param oldInput the previous input or <code>null</code> if none
   * @param newInput the new input
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput);



}
