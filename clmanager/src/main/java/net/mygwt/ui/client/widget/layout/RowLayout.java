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
 * RowLayouts determine the size and position of the children of a
 * <code>Container</code> by placing them either in horizontal row or a
 * vertical column within the parent <code>Container</code>.
 * 
 * <p>
 * RowLayout aligns all widgets in one row if the type is set to horizontal, and
 * one column if it is set to vertical. Provides configurable margins and
 * spacing.
 * </p>
 * 
 * <p>
 * This layout is designed to work with containers with an explicit size and
 * will not render properly without a size.
 * </p>
 */
public class RowLayout extends Layout {

  int type = Style.VERTICAL;
  int margin = 0;
  int spacing = 0;

  /**
   * Creates a new vertical row layout.
   */
  public RowLayout() {
    this(Style.VERTICAL);
  }

  /**
   * Constructs a new row layout given the type.
   * 
   * @param type the type of row layout
   */
  public RowLayout(int type) {
    this.type = type;
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
   * @param margin the margin
   */
  public void setMargin(int margin) {
    this.margin = margin;
  }

  /**
   * Sets the amount of space between widgets.
   * 
   * @param spacing the spacing value
   */
  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

  /**
   * Sets whether the layout places controls in rows or columns. The default
   * value is VERTICAL.
   * 
   * Valid values are:
   * <ul>
   * <li>HORIZONTAL: Position the controls horizontally from left to right</li>
   * <li>VERTICAL: Position the controls vertically from top to bottom</li>
   * </ul>
   * 
   * @param type the type
   */
  public void setType(int type) {
    this.type = type;
  }

  protected void onLayout(WidgetContainer container, Element target) {
    super.onLayout(container, target);

    WidgetContainer wc = (WidgetContainer) container;
    Rectangle rect = MyDOM.getBounds(target, true);

    int size = container.getWidgetCount();

    int height = rect.height - (2 * margin);
    int width = rect.width - (2 * margin);
    int top = rect.y += margin;
    int left = rect.x += margin;
    left -= MyDOM.getScrollLeft(target);
    top -= MyDOM.getScrollTop(target);

    // adjust for spacing
    if (type == Style.HORIZONTAL) {
      width -= ((size - 1) * spacing);
    } else {
      height -= ((size - 1) * spacing);
    }

    int fillHeight = height;
    int fillWidth = width;

    int fillingHeight = 0;
    int fillingWidth = 0;

    // 1st pass
    for (int i = 0; i < size; i++) {
      Widget widget = container.getWidget(i);
      if (!widget.isVisible()) continue;

      MyDOM.makePositionable(widget.getElement(), true);

      RowData data = (RowData) wc.getLayoutData(widget);
      if (data == null) {
        data = new RowData();
        wc.setLayoutData(widget, data);
      }

      if (type == Style.HORIZONTAL) {
        if (data.fillWidth) {
          fillingWidth++;
        } else if (data.width != Style.DEFAULT) {
          fillWidth -= data.width;
          data.calcWidth = data.width;
        } else {
          int flowWidth = widget.getOffsetWidth();
          fillWidth -= flowWidth;
          data.calcWidth = flowWidth;
        }
        if (data.fillHeight) {
          data.calcHeight = height;
        } else if (data.height != Style.DEFAULT) {
          data.calcHeight = data.height;
        } else {
          data.calcHeight = widget.getOffsetHeight();
        }
      } else {
        if (data.fillHeight) {
          fillingHeight++;
        } else if (data.height != Style.DEFAULT) {
          fillHeight -= data.height;
          data.calcHeight = data.height;
        } else {
          int flowHeight = widget.getOffsetHeight();
          fillHeight -= flowHeight;
          data.calcHeight = flowHeight;
        }
        if (data.fillWidth) {
          data.calcWidth = width;
        } else if (data.width != Style.DEFAULT) {
          data.calcWidth = data.width;
        } else {
          data.calcWidth = widget.getOffsetWidth();
        }
      }

    }

    // 2nd pass
    for (int i = 0; i < size; i++) {
      Widget widget = (Widget) container.getWidget(i);
      if (!widget.isVisible()) continue;
      RowData data = (RowData) wc.getLayoutData(widget);

      int w = (int) data.calcWidth;
      int h = (int) data.calcHeight;

      if (type == Style.VERTICAL) {
        if (data.fillHeight) {
          h = fillHeight / fillingHeight;
        }
      } else {
        if (data.fillWidth) {
          w = fillWidth / fillingWidth;
        }
      }

      top = Math.max(0, top);

      // do not set size for normal flow
      int fh = h;
      int fw = w;
      if (!data.fillHeight && data.height == Style.DEFAULT) {
        fh = -1;
      }
      if (!data.fillWidth && data.width == Style.DEFAULT) {
        fw = -1;
      }

      setBounds(widget, left, top, fw, fh);

      if (type == Style.VERTICAL) {
        top = top + h + spacing;
      } else {
        left = left + w + spacing;
      }

    }
  }

}
