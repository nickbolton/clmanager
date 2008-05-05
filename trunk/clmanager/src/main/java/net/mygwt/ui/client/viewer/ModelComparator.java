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
import net.mygwt.ui.client.util.DefaultComparator;

/**
 * A <code>Comparator</code> for <code>Model</code> instances.
 */
public class ModelComparator extends DefaultComparator {

  public String compareProperty = "name";

  public ModelComparator() {

  }

  public ModelComparator(String compareProperty) {
    this.compareProperty = compareProperty;
  }

  public int compare(Object o1, Object o2) {
    Model m1 = (Model) o1;
    Model m2 = (Model) o2;

    o1 = m1.get(compareProperty);
    o2 = m2.get(compareProperty);

    return super.compare(o1, o2);
  }

}
