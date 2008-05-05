/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.Stack;

import net.mygwt.ui.client.MyGWT;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays an iframe under a element to stop IE bleed through and applet
 * related issues.
 */
public class FramePanel extends Widget {

  private static Stack frameStack = new Stack();

  /**
   * Returns a frame panel from the stack.
   * 
   * @return the frame panel
   */
  public static FramePanel pop() {
    FramePanel panel = frameStack.size() > 0 ? (FramePanel) frameStack.pop() : null;
    if (panel == null) {
      panel = new FramePanel();
    }
    return panel;
  }

  /**
   * Pushes a panel back onto the stack.
   * 
   * @param panel the panel
   */
  public static void push(FramePanel panel) {
    frameStack.push(panel);
  }

  /**
   * Creates a new frame panel.
   */
  private FramePanel() {
    Element frame = DOM.createIFrame();
    setElement(frame);
    if (MyGWT.isIE && MyGWT.isSecure) {
      DOM.setElementProperty(getElement(), "src", MyGWT.SSL_SECURE_URL);
    }
  }

  /**
   * Removes the panel.
   * 
   * @param element the parent element
   */
  public native void onHide(Element element) /*-{
   var frame = this.@com.google.gwt.user.client.ui.UIObject::element;
   frame.parentNode.removeChild(frame);
  }-*/;

  /**
   * Displays the panel behind the given element.
   * 
   * @param element the element
   */
  public void onShow(Element element) {
    if (MyGWT.isIE) {
      bindIE(element, getElement());
    } else {
      bindOther(element, getElement());
    }
  }

  /**
   * Displays the panel behind the given element.
   * 
   * @param element the element
   * @param zIndex the z index
   */
  public void onShow(Element element, int zIndex) {
    onShow(element);
    setZIndex(zIndex);
  }

  /**
   * Sets the z-index of the panel.
   * 
   * @param index the z index
   */
  public void setZIndex(int index) {
    index = Math.max(1, index);
    if (MyGWT.isIE) {
      setZIndexIE(index);
    } else {
      DOM.setIntStyleAttribute(getElement(), "zIndex", index);
    }
  }

  public void sync(Element element) {
    if (MyGWT.isIE) {
      syncIE(element, getElement());
    } else {
      syncOther(element, getElement());
    }
  }

  private native void bindIE(Element parent, Element frame) /*-{
     frame.scrolling = 'no';
     frame.frameBorder = 0;
     frame.style.position = 'absolute';
     frame.className = 'my-frame-panel';
     parent.__frame = frame;
     frame.__parent = parent;
     frame.style.setExpression('left', parent.offsetLeft);
     frame.style.setExpression('top', parent.offsetTop);
     frame.style.setExpression('width', parent.offsetWidth);
     frame.style.setExpression('height', parent.offsetHeight);
     parent.parentElement.insertBefore(frame, parent);
   }-*/;

  private native void bindOther(Element parent, Element frame) /*-{
     frame.scrolling = 'no';
     frame.frameBorder = 0;
     frame.style.position = 'absolute';
     frame.className = 'my-frame-panel';
     parent.__frame = frame;
     frame.__parent = parent;
     frame.style.left = parent.offsetLeft + 'px';
     frame.style.top = parent.offsetTop + 'px';
     frame.style.width = parent.offsetWidth + 'px';
     frame.style.height = parent.offsetHeight + 'px'; 
     parent.parentNode.insertBefore(frame, parent);
   }-*/;

  private native void setZIndexIE(int index) /*-{
     var frame = this.@com.google.gwt.user.client.ui.UIObject::element;
     frame.style.setExpression('zIndex', index);
   }-*/;

  private native void syncIE(Element parent, Element frame) /*-{
     frame.style.setExpression('left', parent.offsetLeft);
     frame.style.setExpression('top', parent.offsetTop);
     frame.style.setExpression('width', parent.offsetWidth);
     frame.style.setExpression('height', parent.offsetHeight);
  }-*/;

  private native void syncOther(Element parent, Element frame) /*-{
     frame.style.left = parent.offsetLeft + 'px';
     frame.style.top = parent.offsetTop + 'px';
     frame.style.width = parent.offsetWidth + 'px';
     frame.style.height = parent.offsetHeight + 'px'; 
  }-*/;

}
