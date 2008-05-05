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

import net.mygwt.ui.client.widget.tree.Tree;

/**
 * A <code>ViewerFilterTextBox</code> for trees.
 */
public class TreeViewerFilterTextBox extends ViewerFilterTextBox {

  Tree tree;
  
  public void bind(TreeViewer viewer) {
    super.bind(viewer);
    this.tree = viewer.getTree();
  }
  
  protected void onChange() {
    super.onChange();
    if (!getText().equals("")) {
      tree.expandAll();
    } else {
      tree.collapseAll();
    }
  }

}
