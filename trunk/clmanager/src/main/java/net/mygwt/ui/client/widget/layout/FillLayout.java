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
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.util.Rectangle;
import net.mygwt.ui.client.widget.Layout;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <code>FillLayout</code> lays out widgets in a single row or column, forcing
 * them to be the same size.
 */
public class FillLayout extends Layout {

  int margin = 0;
  int spacing = 0;
  int type = Style.HORIZONTAL;

  /**
   * Creates a new layout instance.
   */
  public FillLayout() {

  }

  /**
   * Creates a new instance with the given margin.
   * 
   * @param margin the margin in pixels
   */
  public FillLayout(int margin) {
    this.margin = margin;
  }

  /**
   * Returns the margin.
   * 
   * @return the margin
   */
  public int getMargin() {
    return margin;
  }

  /**
   * Returns the spacing.
   * 
   * @return the spacing in pixels
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * Returns the type.
   * 
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * Sets the number of pixels of margin that will be placed along the edges of
   * the layout. The default value is 0.
   * 
   * @param margin the margin in pixels
   */
  public void setMargin(int margin) {
    this.margin = margin;
  }

  /**
   * Sets the number of pixels between the edge of one cell and the edge of its
   * neighbouring cell. The default value is 0.
   * 
   * @param spacing the spacing in pixels.
   */
  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

  /**
   * Sets how widgets will be positioned within the layout. The default value is
   * HORIZONTAL.
   * 
   * @param type the type
   */
  public void setType(int type) {
    this.type = type;
  }

  protected void onLayout(WidgetContainer container, Element target) {
    super.onLayout(container, target);

    int count = container.getWidgetCount();
    if (count < 1) {
      return;
    }

    for (int i = 0; i < count; i++) {
      Widget w = container.getWidget(i);
      MyDOM.makePositionable(w.getElement(), count != 1);
    }

    Element ct = container.getLayoutTarget();

    Rectangle rect = MyDOM.getBounds(ct, true);

    int width = rect.width - margin * 2;
    int height = rect.height - margin * 2;

    if (type == Style.HORIZONTAL) {
      width -= (count - 1) * spacing;
      int x = rect.x + margin, extra = width % count;
      int y = rect.y + margin, cellWidth = width / count;
      x -= MyDOM.getScrollLeft(ct);
      y -= MyDOM.getScrollTop(ct);
      for (int i = 0; i < count; i++) {
        Widget child = container.getWidget(i);
        int childWidth = cellWidth;
        if (i == 0) {
          childWidth += extra / 2;
        } else {
          if (i == count - 1) childWidth += (extra + 1) / 2;
        }
        setBounds(child, x, y, childWidth, height);
        x += childWidth + spacing;
      }
    } else {
      height -= (count - 1) * spacing;
      int x = rect.x + margin, cellHeight = height / count;
      int y = rect.y + margin, extra = height % count;
      x -= MyDOM.getScrollLeft(ct);
      y -= MyDOM.getScrollTop(ct);
      for (int i = 0; i < count; i++) {
        Widget child = container.getWidget(i);
        int childHeight = cellHeight;
        if (i == 0) {
          childHeight += extra / 2;
        } else {
          if (i == count - 1) childHeight += (extra + 1) / 2;
        }
        setBounds(child, x, y, width, childHeight);
        y += childHeight + spacing;
      }
    }
  }

}
