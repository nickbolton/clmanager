/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tom Schindl <tom.schindl@bestsolution.at> - port to MyGWT (issue 39)
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import net.mygwt.ui.client.util.DefaultComparator;

/**
 * Instances of this class represent a column of a <code>ColumnViewer</code>.
 * Label providers can be configured for each column separately.
 */
public abstract class ViewerColumn {
  
  private static final ViewerSorter sorter = new ViewerSorter(new DefaultComparator());

  static String COLUMN_VIEWER_KEY = "mygwt.columnviewer";

  protected ColumnViewer viewer;
  private ViewerSorter viewerSorter;
  private CellLabelProvider labelProvider;

  protected ViewerColumn(ColumnViewer viewer) {
    this.viewer = viewer;
  }

  public ViewerSorter getViewerSorter() {
    if (viewerSorter == null) {
      return sorter;
    }
    return viewerSorter;
  }

  public void setViewerSorter(ViewerSorter viewerSorter) {
    this.viewerSorter = viewerSorter;
  }

  /**
   * Returns the label provider for the column.
   * 
   * @return the label provider
   */
  public CellLabelProvider getLabelProvider() {
    return labelProvider;
  }

  /**
   * Set the label provider for the column.
   * 
   * @param labelProvider
   */
  public void setLabelProvider(CellLabelProvider labelProvider) {
    this.labelProvider = labelProvider;
  }

}
