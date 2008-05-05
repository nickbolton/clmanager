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

import java.util.EventObject;

/**
 * Event object describing a selection change. The source of these events is a
 * selection provider.
 */
public class SelectionChangedEvent extends EventObject {

  /**
   * The selection.
   */
  protected ISelection selection;

  /**
   * Creates a new event for the given source and selection.
   * 
   * @param source the selection provider
   * @param selection the selection
   */
  public SelectionChangedEvent(ISelectionProvider source, ISelection selection) {
    super(source);
    this.selection = selection;
  }

  /**
   * Returns the selection.
   * 
   * @return the selection
   */
  public ISelection getSelection() {
    return selection;
  }

  /**
   * Returns the selection provider that is the source of this event.
   * 
   * @return the originating selection provider
   */
  public ISelectionProvider getSelectionProvider() {
    return (ISelectionProvider) getSource();
  }

}