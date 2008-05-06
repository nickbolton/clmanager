/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributers:
 *      Darell Meyer <darrell@mygwt.net> - Derived implementation
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.ScrollListener;
import net.mygwt.ui.client.event.TypedListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * A <code>Container</code> that scrolls its contents. The container is
 * created with scrolling disabled.
 */
public class ScrollContainer extends Container {

  private boolean scroll, alwaysScroll;
  private int scrollLeft = Style.DEFAULT;
  private int scrollTop = Style.DEFAULT;

  /**
   * Adds a listener to receive scroll events.
   * 
   * @param listener the listener to be added
   */
  public void addScrollListener(ScrollListener listener) {
    addListener(Events.Scroll, new TypedListener(listener));
  }

  /**
   * Ensures the child widget is visible.
   * 
   * @param child the child widget
   */
  public void ensureVisible(Widget child) {
    MyDOM.scrollIntoView(child.getElement(), getLayoutTarget(), true);
  }

  /**
   * Returns the horizontal scroll position.
   * 
   * @return the horizontal scroll position, in pixels
   */
  public int getHScrollPosition() {
    return rendered ? MyDOM.getScrollLeft(getElement()) : 0;
  }

  /**
   * Returns the container's scroll state.
   * 
   * @return the scroll state
   */
  public boolean getScrollEnabled() {
    return scroll;
  }

  /**
   * Returns the vertical scroll position.
   * 
   * @return the horizontal scroll position, in pixels
   */
  public int getVScrollPosition() {
    return rendered ? MyDOM.getScrollTop(getElement()) : 0;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeScrollListener(ScrollListener listener) {
    unhook(Events.Scroll, listener);
  }

  /**
   * Sets whether this panel always shows its scroll bars, or only when
   * necessary.
   * 
   * @param alwaysShow <code>true</code> to show scroll bars at all times
   */
  public void setAlwaysShowScrollBars(boolean alwaysShow) {
    if (rendered) {
      Element ct = getLayoutTarget();
      DOM.setStyleAttribute(ct, "overflow", alwaysShow ? "scroll" : "auto");
    }
  }

  /**
   * Sets the horizontal scroll position.
   * 
   * @param position the new horizontal scroll position, in pixels
   */
  public void setHScrollPosition(int position) {
    scrollLeft = position;
    if (rendered) {
      MyDOM.setScrollLeft(getLayoutTarget(), position);
    }
  }

  /**
   * Enables scrolling if <code>true</code>, otherwise disables.
   * 
   * @param scroll the scroll enabled state
   */
  public void setScrollEnabled(boolean scroll) {
    this.scroll = scroll;
    if (rendered) {
      Element ct = getLayoutTarget();
      String style = scroll ? "auto" : "hidden";
      DOM.setStyleAttribute(ct, "overflow", style);
    }
  }
  
  protected Element getLayoutTarget() {
    return getElement();
  }

  /**
   * Sets the vertical scroll position.
   * 
   * @param position the new horizontal scroll position, in pixels
   */
  public void setVScrollPosition(int position) {
    scrollTop = position;
    if (rendered) {
      MyDOM.setScrollTop(getLayoutTarget(), position);
    }
  }

  protected void afterRender() {
    super.afterRender();
    setScrollEnabled(scroll);
    if (scrollLeft != Style.DEFAULT) {
      setHScrollPosition(scrollLeft);
    }
    if (scrollTop != Style.DEFAULT) {
      setVScrollPosition(scrollTop);
    }
    if (alwaysScroll) {
      setAlwaysShowScrollBars(alwaysScroll);
    }
    MyDOM.addEventsSunk(getLayoutTarget(), Event.ONSCROLL);
  }

}
