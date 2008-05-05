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

import java.util.List;

/**
 * A basic <code>IStructuredContentProvider</code> that supports arrays and
 * lists.
 */
public class DefaultContentProvider implements IStructuredContentProvider {

  public void getElements(Object input, IAsyncContentCallback callback) {
    if (input instanceof Object[]) {
      callback.setElements((Object[]) input);
    } else if (input instanceof List) {
      callback.setElements(((List) input).toArray());
    }
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
