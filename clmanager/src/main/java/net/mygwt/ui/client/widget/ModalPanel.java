/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.EffectListenerAdapter;
import net.mygwt.ui.client.fx.FXStyle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that grays out the view port and displays a widget above it.
 */
public class ModalPanel extends Composite implements EventPreview {

  private boolean blink = true;
  private SimplePanel panel;
  private Widget widget;
  private boolean blinking;
  private FramePanel framePanel;

  /**
   * Creates a new model panel.
   */
  public ModalPanel() {
    panel = new SimplePanel();
    initWidget(panel);
    panel.setStyleName("my-modal");
    panel.setWidth("100%");
  }

  /**
   * Hides the panel.
   */
  public void hide() {
    framePanel.onHide(getElement());
    FramePanel.push(framePanel);
    MyDOM.setZIndex(getElement(), -1);
    DOM.removeEventPreview(this);
    RootPanel.get().remove(this);
    RootPanel.get().remove(widget);
  }

  /**
   * Returns <code>true</code> if the panel will blink when clicks occur
   * outside the it's boundaries.
   * 
   * @return the blink state
   */
  public boolean isBlinkEnabled() {
    return blink;
  }

  public boolean onEventPreview(Event event) {
    Element target = DOM.eventGetTarget(event);
    if (DOM.isOrHasChild(widget.getElement(), target)) {
      return true;
    }
    switch (DOM.eventGetType(event)) {
      case Event.ONCLICK: {
        String tagName = DOM.getElementProperty(target, "tagName");
        // IE hack to stop flashing on a resize
        if (tagName.equals("BODY")) return false;
        if (blink && !blinking) {
          blinking = true;
          FXStyle fx = new FXStyle(widget.getElement());
          fx.duration = 400;
          if (widget instanceof Shell) {
            final Shell shell = (Shell) widget;
            fx.addEffectListener(new EffectListenerAdapter() {
              public void effectComplete(BaseEvent be) {
                if (shell.shadow != null) {
                  shell.shadow.setVisible(true);
                }
                blinking = false;
              }

              public void effectStart(BaseEvent be) {
                if (shell.shadow != null) {
                  shell.shadow.setVisible(false);
                }
              }
            });
          } else {
            fx.addEffectListener(new EffectListenerAdapter() {
              public void effectComplete(BaseEvent be) {
                blinking = false;
              }
            });
          }
          fx.blink();
        }
        break;
      }
    }
    return false;
  }

  /**
   * Sets the blink state. When <code>true</code> the modal widget will blink.
   * Default value is <code>true</code>.
   * 
   * @param blink <code>true</code> to enable blinking
   */
  public void setBlinkEnabled(boolean blink) {
    this.blink = blink;
  }

  /**
   * Displays the panel.
   */
  public void show(Widget widget) {
    this.widget = widget;
    RootPanel.get().add(this);
    RootPanel.get().add(widget);
    
    // body height may be less than viewport
    int bh = MyDOM.getScrollHeight(MyDOM.getBody());
    bh = Math.max(bh, Window.getClientHeight());
    setHeight(bh + "px");
    
    framePanel = FramePanel.pop();
    framePanel.onShow(getElement());
    
    framePanel.setZIndex(MyDOM.getZIndex());
    MyDOM.setZIndex(panel.getElement(), MyDOM.getZIndex());
    MyDOM.setZIndex(widget.getElement(), MyDOM.getZIndex());
    
    DOM.addEventPreview(this);
  }

}
