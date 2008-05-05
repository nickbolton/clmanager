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

import java.util.List;

import net.mygwt.ui.client.data.ChangeEvent;
import net.mygwt.ui.client.data.ChangeListener;
import net.mygwt.ui.client.data.Model;

/**
 * A <code>ContentProvider</code> implementation for <code>Models</code>.
 */
public class ModelContentProvider implements IStructuredContentProvider, ChangeListener {

  /**
   * monitorChanges specifies if model changes should be monitored. Default
   * value is <code>true</code>.
   */
  public boolean monitorChanges = true;

  /**
   * ensureChild specifies if model changes should be tested to ensure the
   * updated model instance is a direct child of the viewer's input. Default
   * value is <code>true</code>.
   */
  public boolean ensureChild = true;

  protected Viewer viewer;

  public void getElements(Object input, IAsyncContentCallback callback) {
    if (input instanceof Model) {
      callback.setElements(((Model) input).getChildren().toArray());
      return;
    } else if (input instanceof List) {
      callback.setElements(((List) input).toArray(new Model[0]));
      return;
    }
    callback.setElements(null);
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;
    if (monitorChanges) {
      if (oldInput != null) {
        if (oldInput instanceof List) {
          List list = (List) oldInput;
          int count = list.size();
          for (int i = 0; i < count; i++) {
            Model m = (Model) list.get(i);
            m.removeChangeListener(this);
          }

        } else if (oldInput instanceof Model) {
          Model m = (Model) oldInput;
          m.removeChangeListener(this);
        }
      }
      if (newInput instanceof List) {
        List list = (List) newInput;
        int count = list.size();
        for (int i = 0; i < count; i++) {
          Model m = (Model) list.get(i);
          m.addChangeListener(this);
        }
      } else if (newInput instanceof Model) {
        Model m = (Model) newInput;
        m.addChangeListener(this);
      }

    }
  }

  public void modelChanged(ChangeEvent event) {
    int type = event.type;
    switch (type) {
      case Model.Add: {
        if (!ensureChild || (ensureChild && event.source == viewer.getInput())) {
          viewer.add(event.item);
        }
        break;
      }
      case Model.Insert: {
        if (!ensureChild || (ensureChild && event.source == viewer.getInput())) {
          viewer.insert(event.item, event.index);
        }
        break;
      }
      case Model.Remove: {
        if (!ensureChild || (ensureChild && event.source == viewer.getInput())) {
          viewer.remove(event.item);
        }
        break;
      }
      case Model.Update: {
        viewer.update(event.item);
        break;
      }
    }
  }

}
