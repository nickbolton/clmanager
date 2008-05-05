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
 * A listener which is notified when a viewer's selection changes.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 * 
 * @see ISelection
 * @see ISelectionProvider
 * @see SelectionChangedEvent
 */
public interface ISelectionChangedListener {

  /**
   * Notifies that the selection has changed.
   * 
   * @param event event object describing the change
   */
  public void selectionChanged(SelectionChangedEvent event);

}
