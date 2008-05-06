/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.Stack;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.Markup;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A transparent shadow.
 * 
 * <dl>
 * <dt>Styles:</dt>
 * <dd>DROP, FRAME, SIDES</dd>
 * </dl>
 */
public class Shadow extends Component {

  private static Stack shadowStack = new Stack();

  /**
   * Returns a Shadow from the stack.
   * 
   * @return the shadow
   */
  public static Shadow pop() {
    Shadow shadow = shadowStack.size() > 0 ? (Shadow) shadowStack.pop() : null;
    if (shadow == null) {
      shadow = new Shadow(Style.SIDES);
    }
    return shadow;
  }

  /**
   * Pushes a shadow back onto the stack.
   * 
   * @param shadow the shadow
   */
  public static void push(Shadow shadow) {
    shadowStack.push(shadow);
  }

  private int style;
  private Component component;
  private Rectangle adjusts;
  private int offset = 4;
  private Listener listener;

  /**
   * Creates a new shadow widget.
   * 
   * @param style the style information
   */
  public Shadow(int style) {
    this.style = style;
    listener = new Listener() {

      public void handleEvent(BaseEvent be) {
        switch (be.type) {
          case Events.Resize:
            sync(component.getBounds());
            break;
          case Events.Attach:
            if (!isAttached()) {
              initialize();
            }
        }
      }
    };
  }

  /**
   * Removes the shadow.
   */
  public void remove() {
    MyDOM.removeFromParent(getElement());
  }

  /**
   * Displays the shadow behind the specified component.
   * 
   * @param component the component
   */
  public void show(final Component component) {
    if (this.component != null) {
      this.component.removeListener(Events.Resize, listener);
      this.component.removeListener(Events.Attach, listener);
    }

    this.component = component;

    component.addListener(Events.Resize, listener);
    component.addListener(Events.Attach, listener);

    if (component.isAttached()) {
      Element target = component.getElement();
      if (!DOM.compare(DOM.getParent(getElement()), target)) {
        DOM.insertBefore(DOM.getParent(target), getElement(), target);
      }
      sync(component.getBounds());
    }

  }

  public void sync(Rectangle rect) {
    if (component == null) return;
    MyDOM.setLeft(getElement(), rect.x + adjusts.x);
    MyDOM.setTop(getElement(), rect.y + adjusts.y);

    int sw = rect.width + adjusts.width;
    int sh = rect.height + adjusts.height;
    if (getWidth() != sw || getHeight() != sh) {
      MyDOM.setWidth(getElement(), sw);
      MyDOM.setHeight(getElement(), sh);
      if (!MyGWT.isIE) {
        int w = Math.max(0, sw - 12);
        MyDOM.setWidth(getChild(0, 1), w);
        MyDOM.setWidth(getChild(1, 1), w);
        MyDOM.setWidth(getChild(2, 1), w);
        int h = Math.max(0, sh - 12);
        Element middle = DOM.getChild(getElement(), 1);
        MyDOM.setHeight(middle, h);
      }
    }
  }

  protected void onRender() {
    if (MyGWT.isIE) {
      setElement(DOM.createDiv());
      setStyleName("my-ie-shadow");
    } else {
      setElement(MyDOM.create(Markup.SHADOW));
    }

    if (MyGWT.isIE) {
      setStyleAttribute("filter", "progid:DXImageTransform.Microsoft.alpha("
          + "opacity=50) progid:DXImageTransform.Microsoft.Blur(pixelradius=" + offset
          + ")");
    }

    adjusts = new Rectangle();
    int radius = offset / 2;

    switch (style) {
      case Style.SIDES:
        adjusts.width = offset * 2;
        adjusts.x = -offset;
        adjusts.y = offset - 1;
        if (MyGWT.isIE) {
          adjusts.x -= (offset - radius);
          adjusts.y -= (offset + radius);
          adjusts.x += 1;
          adjusts.width -= (offset - radius) * 2;
          adjusts.width -= radius + 1;
          adjusts.height -= 1;
        }
        break;
      case Style.FRAME:
        adjusts.width = adjusts.height = (offset * 2);
        adjusts.x = adjusts.y = -offset;
        adjusts.y += 1;
        adjusts.height -= 2;
        if (MyGWT.isIE) {
          adjusts.x -= (offset - radius);
          adjusts.y -= (offset - radius);
          adjusts.width -= (offset + radius);
          adjusts.width += 1;
          adjusts.height -= (offset + radius);
          adjusts.height += 3;
        }
        break;
      default:
        adjusts.width = 0;
        adjusts.x = adjusts.y = offset;
        adjusts.y -= 1;
        if (MyGWT.isIE) {
          adjusts.x -= offset + radius;
          adjusts.y -= offset + radius;
          adjusts.width -= radius;
          adjusts.height -= radius;
          adjusts.y += 1;
        }
        break;

    }

  }

  private Element getChild(int index, int subindex) {
    Element e = DOM.getChild(getElement(), index);
    return DOM.getChild(e, subindex);
  }

  private void initialize() {
    Element target = component.getElement();
    if (!DOM.compare(DOM.getParent(getElement()), target)) {
      DOM.insertBefore(DOM.getParent(target), getElement(), target);
    }
    sync(component.getBounds());
  }
}
