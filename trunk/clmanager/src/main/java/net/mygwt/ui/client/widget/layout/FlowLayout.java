/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.layout;

import net.mygwt.ui.client.widget.Component;
import net.mygwt.ui.client.widget.Layout;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <code>Layout</code> that positions its children using normal HTML layout
 * behavior. The margin and spacing can be specified. The layout method will be
 * called to any child widgets that are containers. Recalculate will be called
 * on any child widgets that are components.
 */
public class FlowLayout extends Layout {

  private int margin = 0;
  private int spacing = 0;

  /**
   * Creates a new layout instance.
   */
  public FlowLayout() {

  }

  /**
   * Creates a new layout instance with the given margin.
   * 
   * @param margin the margin
   */
  public FlowLayout(int margin) {
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
   * Returns the spacing value.
   * 
   * @return the spacing in pixels
   */
  public int getSpacing() {
    return spacing;
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

  protected void onLayout(WidgetContainer container, Element target) {
    super.onLayout(container, target);
    if (margin != 0) {
      DOM.setIntStyleAttribute(target, "margin", margin);
    }
  }

  protected void renderWidget(Widget widget, int index, Element target) {
    super.renderWidget(widget, index, target);
    DOM.setStyleAttribute(widget.getElement(), "position", "static");

    if (index != 0 && spacing > 0) {
      DOM.setIntStyleAttribute(widget.getElement(), "marginTop", spacing);
      DOM.setIntStyleAttribute(widget.getElement(), "marginRight", spacing);
    }

    if (widget instanceof WidgetContainer) {
      ((WidgetContainer) widget).layout();
    } else if (widget instanceof Component) {
      ((Component) widget).recalculate();
    }
  }

}
