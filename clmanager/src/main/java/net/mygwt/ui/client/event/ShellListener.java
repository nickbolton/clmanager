/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.event;

import java.util.EventListener;

/**
 * Classes which implement this interface provide methods that deal with changes
 * in state of <code>Shells</code>.
 */
public interface ShellListener extends EventListener {

  /**
   * Fires when a shell becomes the active window.
   * 
   * @param be an event containing information about the activation
   */
  public void shellActivated(BaseEvent be);

  /**
   * Fires when a shell is closed.
   * 
   * @param be an event containing information about the close
   * 
   */
  public void shellClosed(BaseEvent be);

  /**
   * Fires when a shell stops being the active window.
   * 
   * @param be an event containing information about the deactivation
   */
  public void shellDeactivated(BaseEvent be);
}
