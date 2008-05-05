/*******************************************************************************
 * Copyright (c) 2007 MyGWT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Darrell Meyer <darrell@mygwt.net> - initial API and implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import net.mygwt.ui.client.data.ChangeEvent;
import net.mygwt.ui.client.data.ChangeListener;
import net.mygwt.ui.client.data.Model;

/**
 * A <code>RemoteContentProvider</code> for model instances.
 */
public abstract class ModelRemoteContentProvider extends RemoteContentProvider implements
    ChangeListener {

  /**
   * Specifies if model changes should be monitored. Default value is
   * <code>true</code>.
   */
  public boolean monitorChanges = true;

  protected Viewer viewer;

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;
    if (monitorChanges) {
      if (oldInput != null) {
        Model m = (Model) oldInput;
        m.removeChangeListener(this);
      }
      Model m = (Model) newInput;
      m.addChangeListener(this);
    }
  }

  public void modelChanged(ChangeEvent event) {
    int type = event.type;
    switch (type) {
      case Model.Add: {
        viewer.add(event.item);
        break;
      }
      case Model.Insert: {
        viewer.insert(event.item, event.index);
        break;
      }
      case Model.Remove: {
        viewer.remove(event.item);
        break;
      }
      case Model.Update: {
        viewer.update(event.item);
        break;
      }
    }
  }

}
