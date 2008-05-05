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

import net.mygwt.ui.client.data.Model;

/**
 * A <code>CellLabelProvider</code> for <code>Model</code> instances.
 */
public class ModelCellLabelProvider extends CellLabelProvider {

  protected ViewerCell viewerCell;
  
  public void update(ViewerCell cell) {
    this.viewerCell = cell;
    Model m = (Model) cell.getElement();
    cell.setText(getModelProperty(m, cell.getColumnId()));
  }

  /**
   * Returns the cell's text by calling {@link Model#getAsString(String)} using
   * the column's id. Method can be subclassed to provide specialized behavior.
   * 
   * @param model the model
   * @param columnId the column id
   * @return the cell's property value
   */
  public String getModelProperty(Model model, String columnId) {
    return model.getAsString(columnId);
  }

}
