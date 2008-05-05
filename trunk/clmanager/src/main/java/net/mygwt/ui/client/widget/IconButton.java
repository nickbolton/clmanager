/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.TypedListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * A simple css styled button with 3 states: normal, over, and disabled.
 * 
 * <p>
 * Note: To change the icon style after construction use
 * {@link #changeStyle(String)}.
 * </p>
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : (widget, event)<br>
 * <div>Fires after the item is selected.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-icon-btn (the button itself)</dd>
 * </dl>
 */
public class IconButton extends Component {

  protected String style;
  protected boolean cancelBubble = false;

  /**
   * Creates a new icon button. When using the default constructor,
   * {@link #changeStyle(String)} must be called to initialize the button.
   */
  public IconButton() {
    this("none");
  }

  /**
   * Creates a new icon button. The 'over' style and 'disabled' style names
   * determined by adding '-over' and '-disabled' to the base style name.
   * 
   * @param style the base style
   */
  public IconButton(String style) {
    this.style = style;
  }

  /**
   * Adds a listener interface to receive selection events.
   * 
   * @param listener the listener to add
   */
  public void addSelectionListener(SelectionListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.Select, tl);
  }

  /**
   * Changes the icon style.
   * 
   * @param style the new icon style
   */
  public void changeStyle(String style) {
    removeStyleName(this.style);
    removeStyleName(this.style + "-over");
    removeStyleName(this.style + "-disabled");
    addStyleName(style);
    this.style = style;
  }

  public void onBaseEvent(BaseEvent be) {
    switch (be.type) {
      case Event.ONMOUSEOVER:
        addStyleName(style + "-over");
        break;
      case Event.ONMOUSEOUT:
        removeStyleName(style + "-over");
        break;
      case Event.ONCLICK:
        onClick(be);
        break;
    }
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    unhook(Events.EffectStart, listener);
  }

  protected void onClick(BaseEvent be) {
    if (cancelBubble) {
      be.cancelBubble();
    }
    removeStyleName(style + "-over");
    fireEvent(Events.Select);
  }

  protected void onDisable() {
    super.onDisable();
    addStyleName(style + "-disabled");
  }

  protected void onEnable() {
    super.onEnable();
    removeStyleName(style + "-disabled");
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    addStyleName("my-icon-btn");
    addStyleName("my-nodrag");
    addStyleName(style);
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
  }

}
