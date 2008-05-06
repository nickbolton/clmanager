/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Style;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * Allows any widget to be placed in a tool bar.
 */
public class ToolItemAdapter extends ToolItem {

  private Widget widget;

  /**
   * Creates a new adapter.
   * 
   * @param widget the widget to be adapted
   */
  public ToolItemAdapter(Widget widget) {
    super(Style.PUSH);
    this.widget = widget;
    if (isAttached()) {
      WidgetHelper.doAttach(widget);
    }
    overStyleEnabled = false;
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    DOM.appendChild(getElement(), widget.getElement());
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    WidgetHelper.doAttach(widget);
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(widget);
  }

}
