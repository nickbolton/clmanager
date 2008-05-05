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

public interface SelectionListener extends EventListener {

  /**
   * Fires when selection occurs in the widget.
   * 
   * @param be an event containing information about the selection
   */
  public void widgetSelected(BaseEvent be);

}
