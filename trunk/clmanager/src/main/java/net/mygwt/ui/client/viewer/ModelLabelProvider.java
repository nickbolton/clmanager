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

import net.mygwt.ui.client.data.Model;

/**
 * A <code>LabelProvider</code> implementation for <code>Model</code>
 * instances. The text is retrieved from the property specified by the
 * 'textProperty'.
 */
public class ModelLabelProvider extends LabelProvider {

  /**
   * textProperty specifies the property name to be used to retrieve a model's
   * label. Default value is "name".
   */
  public String textProperty = "name";

  /**
   * Creates a new label provider.
   */
  public ModelLabelProvider() {

  }

  /**
   * Creates a new label provider.
   * 
   * @param textPropery the property name
   */
  public ModelLabelProvider(String textPropery) {
    this.textProperty = textPropery;
  }

  public String getText(Object element) {
    Model m = (Model) element;
    return m.getAsString(textProperty);
  }

}
