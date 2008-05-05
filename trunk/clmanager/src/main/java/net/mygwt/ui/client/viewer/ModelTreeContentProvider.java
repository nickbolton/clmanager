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

import net.mygwt.ui.client.data.ChangeEvent;
import net.mygwt.ui.client.data.ChangeListener;
import net.mygwt.ui.client.data.Model;

/**
 * A <code>TreeContentProvider</code> implementation for <code>Model</code>
 * instances.
 * 
 * @see Model
 */
public class ModelTreeContentProvider implements ITreeContentProvider, ChangeListener {

  /**
   * Specifies if model changes should be monitored. Default value is
   * <code>true</code>.
   */
  public boolean monitorChanges = true;

  protected ITreeViewer viewer;

  public void getChildren(Object parent, IAsyncContentCallback callback) {
    callback.setElements(((Model) parent).getChildren().toArray());
  }

  public Object getParent(Object element) {
    return ((Model)element).getParent();
  }

  public boolean hasChildren(Object element) {
    return ((Model) element).getChildCount() > 0;
  }

  public void getElements(Object input, IAsyncContentCallback callback) {
    callback.setElements(((Model) input).getChildren().toArray());
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = (ITreeViewer) viewer;
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
        Model parent = event.source;
        Model add = event.item;
        viewer.add(parent, add);
        break;
      }
      case Model.Insert: {
        Model parent = event.source;
        Model insert = event.item;
        viewer.insert(parent, insert, event.index);
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
