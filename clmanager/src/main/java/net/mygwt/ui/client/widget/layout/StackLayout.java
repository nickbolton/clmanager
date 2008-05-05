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
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.Layout;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This Layout stacks all the widgets one on top of the other and resizes all
 * widgets to have the same size and location. The widget specified in topWidget
 * is visible and all other widgets are not visible.
 */
public class StackLayout extends Layout {

  Widget topWidget;

  /**
   * Returns the top widget.
   * 
   * @return the top widget
   */
  public Widget getTopWidget() {
    return topWidget;
  }

  /**
   * Sets the widget that is displayed at the top of the stack.
   * 
   * @param topWidget the top widget
   */
  public void setTopWidget(Widget topWidget) {
    this.topWidget = topWidget;
  }

  protected void onLayout(WidgetContainer container, Element target) {
    super.onLayout(container, target);
    
    if (container.getWidgetCount() == 0) {
      return;
    }

    Rectangle rect = MyDOM.getBounds(target, true);

    int size = container.getWidgetCount();
    for (int i = 0; i < size; i++) {
      Widget child = (Widget) container.getWidget(i);
      child.setVisible(topWidget == child);
      if (topWidget == child) {
        setSize(child, rect.width, rect.height);
      }
    }
  }
}
