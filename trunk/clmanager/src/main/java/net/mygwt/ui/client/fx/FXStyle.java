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

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.Element;

/**
 * Effect for changing a single css style property for a given element.
 * 
 * <p>
 * This code is based on code from the MooTools Project by Valerio Proietti.
 * </p>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>EffectStart</b> : (source)<br>
 * <div>Fires after a effect is started.</div>
 * <ul>
 * <li>source : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>EffecCancel</b> : (source)<br>
 * <div>Fires after an effect has been cancelled.</div>
 * <ul>
 * <li>source : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>EffecComplete</b> : (source)<br>
 * <div>Fires after an effect has been completed.</div>
 * <ul>
 * <li>source : this</li>
 * </ul>
 * </dd>
 * </dl>
 * <dl>
 */
public class FXStyle extends FX {

  protected String style;
  protected Element elem;

  /**
   * Creates a new instance.
   * 
   * @param element the animation element
   */
  public FXStyle(Element element) {
    this.elem = element;
  }

  /**
   * Blinks the element.
   */
  public void blink() {
    if (running) return;
    fps = 20;
    start(new Effect.Blink(elem));
  }

  /**
   * Fades the element in.
   */
  public void fadeIn() {
    if (running) return;
    Effect e = new Effect.FadeIn(elem);
    start(e);
  }

  /**
   * Changes the size of the element.
   * 
   * @param width the new width
   * @param height the new height
   */
  public void size(int width, int height) {
    if (running) return;
    Effect we = new Effect(elem, "width", MyDOM.getWidth(elem), width);
    Effect he = new Effect(elem, "height", MyDOM.getHeight(elem), height);
    start(new Effect[] {we, he});
  }

  /**
   * Fades out the element.
   */
  public void fadeOut() {
    if (running) return;
    Effect e = new Effect.FadeOut(elem);
    start(e);
  }

  /**
   * Fades the element in or out.
   */
  public void fadeToggle() {
    if (running) return;
    if (MyDOM.isVisibility(elem)) {
      fadeOut();
    } else {
      fadeIn();
    }
  }

  /**
   * Moves a element to a new location.
   * 
   * @param x the end x coordinate
   * @param y the end y coordinate
   */
  public void move(int x, int y) {
    if (running) return;
    Rectangle r = MyDOM.getBounds(elem, false);
    Effect xe = new Effect(elem, "x", r.x, x);
    Effect ye = new Effect(elem, "y", r.y, y);
    start(new Effect[] {xe, ye});
  }

  /**
   * Moves and sizes the element.
   * 
   * @param x the new x coordinate
   * @param y the new y coordinate
   * @param width the new width
   * @param height the new height
   */
  public void zoom(int x, int y, int width, int height) {
    if (running) return;
    Effect xe = new Effect(elem, "left", MyDOM.getX(elem), x);
    Effect ye = new Effect(elem, "top", MyDOM.getY(elem), y);
    Effect we = new Effect(elem, "width", MyDOM.getWidth(elem), width);
    Effect he = new Effect(elem, "height", MyDOM.getHeight(elem), height);
    start(new Effect[] {xe, ye, we, he});
  }

  /**
   * Slides the element in.
   * 
   * @param dir the direction
   */
  public void slideIn(int dir) {
    if (running) return;
    start(new Effect.SlideIn(dir, elem));
  }

  /**
   * Slides the element out.
   * 
   * @param dir the direction
   */
  public void slideOut(int dir) {
    if (running) return;
    start(new Effect.SlideOut(dir, elem));
  }

  /**
   * Creates and starts a new effect.
   * 
   * @param style the css style being modified
   * @param from the start value
   * @param to the end value
   */
  public void start(String style, double from, double to) {
    Effect e = new Effect(elem, style, from, to);
    start(e);
  }

}
