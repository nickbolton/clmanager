/*
 * Copyright (c) 2006-2007 Valerio Proietti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * Contributors:
 *     Valerio Proietti - initial API and implementation
 *     MyGWT - derived implementation
 */
package net.mygwt.ui.client.fx;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Effects are a single fx operation involving an element, property, and to /
 * from values.
 * 
 * <p>
 * This code is based on code from the MooTools Project by Valerio Proietti.
 * </p>
 * 
 * @see FX
 */
public class Effect {

  /**
   * A blink effect.
   */
  public static class Blink extends Effect {

    public Blink(Element element) {
      super(element);
      from = 0;
      to = 20;
    }

    public void increase(double now) {
      if (to == now) {
        MyDOM.setVisibility(elem, true);
      } else {
        MyDOM.setVisibility(elem, !MyDOM.isVisibility(elem));
      }
    }
  }

  /**
   * Fades an element in by adjusting its opacity from 0 to 1.
   */
  public static class FadeIn extends Effect {
    public FadeIn(Element element) {
      super(element);
      style = "opacity";
      from = 0;
      to = 1;
    }

    protected void onStart() {
      MyDOM.setDoubleStyleAttribute(elem, "opacity", 0);
      MyDOM.setVisibility(elem, true);

    }

    protected void onComplete() {
      MyDOM.setStyleAttribute(elem, "filter", "");
    }

  }

  /**
   * Fades the element out by adjusting its opacity from 1 to 0.
   */
  public static class FadeOut extends Effect {
    public FadeOut(Element element) {
      super(element);
      style = "opacity";
      from = 1;
      to = 0;
    }

    protected void onComplete() {
      MyDOM.setVisibility(elem, false);
      super.onComplete();
    }

  }

  /**
   * Slides an element into view.
   * 
   */
  public static class SlideIn extends Slide {

    /**
     * Creates a new slide in effect.
     * 
     * @param dir the direction of travel. Values can be either My.HORIZONTAL or
     *            My.VERTICAL
     * @param element the animation effect
     */
    public SlideIn(int dir, Element element) {
      super(dir, element);
    }

    protected void onComplete() {
      MyDOM.unwrap(wrapElem, elem, oBounds, index);
      DOM.setStyleAttribute(elem, "overflow", overflow);
      super.onComplete();
    }

    protected void onStart() {
      overflow = DOM.getStyleAttribute(elem, "overflow");
      wrapElem = DOM.createDiv();
      
      index = DOM.getChildIndex(DOM.getParent(elem), elem);
      oBounds = MyDOM.wrap(elem, wrapElem);

      int h = oBounds.height;
      int w = oBounds.width;

      MyDOM.setWidth(wrapElem, w);
      MyDOM.setHeight(wrapElem, h);

      MyDOM.setVisible(elem, true);
      MyDOM.setVisible(wrapElem, true);

      switch (dir) {
        case Style.SOUTH:
          MyDOM.setHeight(wrapElem, 1);
          style = "height";
          from = 1;
          to = oBounds.height;
          break;
        case Style.EAST:
          style = "width";
          from = 1;
          to = oBounds.width;
          break;
        case Style.WEST:
          MyDOM.setWidth(wrapElem, 1);
          style = "width";
          from = 1;
          to = oBounds.width;
          break;
        case Style.NORTH:
          MyDOM.setHeight(wrapElem, 1);
          style = "height";
          from = 1;
          to = oBounds.height;
      }
    }

    protected void increase(double now) {
      int v = (int) now;
      switch (dir) {
        case Style.WEST:
          MyDOM.setLeft(wrapElem, (oBounds.width - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
        case Style.NORTH:
          MyDOM.setTop(wrapElem, (oBounds.height - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
        case Style.SOUTH:
          DOM.setIntStyleAttribute(elem, "marginTop", -(oBounds.height - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
        case Style.EAST:
          DOM.setIntStyleAttribute(elem, "marginLeft", -(oBounds.width - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
      }
    }

  }

  /**
   * Slides an element out of view.
   */
  public static class SlideOut extends Slide {

    /**
     * Creates a new slide effect.
     * 
     * @param dir the direction of travel (NORTH, EAST, SOUTH, or WEST)
     * @param element the effect element
     */
    public SlideOut(int dir, Element element) {
      super(dir, element);
    }

    protected void onComplete() {
      MyDOM.setVisible(elem, false);
      MyDOM.unwrap(wrapElem, elem, oBounds);
      DOM.setStyleAttribute(elem, "overflow", overflow);
      super.onComplete();
    }

    protected void onStart() {
      overflow = DOM.getStyleAttribute(elem, "overflow");
      wrapElem = DOM.createDiv();
      oBounds = MyDOM.wrap(elem, wrapElem);

      int h = oBounds.height;
      int w = oBounds.width;

      MyDOM.setWidth(wrapElem, w);
      MyDOM.setHeight(wrapElem, h);

      MyDOM.setVisible(wrapElem, true);
      MyDOM.setVisible(elem, true);

      switch (dir) {
        case Style.NORTH:
          style = "height";
          from = oBounds.height;
          to = 1;
          break;
        case Style.WEST:
          style = "width";
          from = oBounds.width;
          to = 0;
          break;
        case Style.EAST:
          style = "left";
          from = MyDOM.getX(wrapElem);
          to = from + MyDOM.getWidth(wrapElem);
          break;

        case Style.SOUTH:
          style = "top";
          from = MyDOM.getY(wrapElem);
          to = from + MyDOM.getHeight(wrapElem);
          break;
      }

    }

  }

  private static class Slide extends Effect {

    protected int dir;
    protected int index;
    protected Element wrapElem;
    protected Rectangle oBounds;
    protected String overflow;

    public Slide(int dir, Element element) {
      super(element);
      this.dir = dir;
    }

    protected void increase(double now) {
      int v = (int) now;

      switch (dir) {
        case Style.WEST:
          DOM.setIntStyleAttribute(elem, "marginLeft", -(oBounds.width - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
        case Style.NORTH:
          DOM.setIntStyleAttribute(elem, "marginTop", -(oBounds.height - v));
          DOM.setIntStyleAttribute(wrapElem, style, v);
          break;
        case Style.SOUTH:
          MyDOM.setY(elem, v);
          break;
        case Style.EAST:
          MyDOM.setX(elem, v);
          break;
      }

      if (dir == Style.HORIZONTAL || dir == Style.VERTICAL) {
        int m = dir == Style.VERTICAL ? oBounds.height - v : oBounds.width - v;
        String s = dir == Style.VERTICAL ? "marginTop" : "marginLeft";
        DOM.setIntStyleAttribute(elem, s, -m);
        DOM.setIntStyleAttribute(wrapElem, style, v);
      }

    }

  }

  /**
   * The effect operation.
   */
  public int operation;

  /**
   * The effect element.
   */
  public Element elem;

  /**
   * The css style be adjusted.
   */
  public String style;

  /**
   * The start value.
   */
  public double from;

  /**
   * The end value.
   */
  public double to;

  /**
   * Creates a new effect.
   * 
   * @param elem the animation element
   */
  public Effect(Element elem) {
    this.elem = elem;
  }

  /**
   * Creates a new effect.
   * 
   * @param elem the effect element
   * @param style the style
   * @param from the from value
   * @param to the to value
   */
  public Effect(Element elem, String style, double from, double to) {
    this.elem = elem;
    this.style = style;
    this.from = from;
    this.to = to;
  }

  protected void increase(double now) {
    if (style.equalsIgnoreCase("x")) {
      MyDOM.setX(elem, (int) now);
    } else if (style.equalsIgnoreCase("y")) {
      MyDOM.setY(elem, (int) now);
    } else {
      MyDOM.setDoubleStyleAttribute(elem, style, now);
    }

  }

  /**
   * Called after the effect has been completed.
   */
  protected void onComplete() {

  }

  /**
   * Called before the effect starts.
   */
  protected void onStart() {

  }

}
