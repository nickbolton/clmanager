/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.util.DelayedTask;

import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for text box's that monitor key presses using a
 * <code>DelayedTask</code>.
 */
public class KeyPressTextBox extends TextBox {

  private int delay = 300;
  private DelayedTask task;

  /**
   * Creates a new key press text box.
   */
  public KeyPressTextBox() {
    task = new DelayedTask(new Listener() {
      public void handleEvent(BaseEvent be) {
        onChange();
        setFocus(true);
      }
    });

    addKeyboardListener(new KeyboardListenerAdapter() {
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        task.delay(delay);
      }
    });
  }

  /**
   * Returns the delay.
   * 
   * @return the delay in milliseconds
   */
  public int getDelay() {
    return delay;
  }

  /**
   * Sets the delay. Default value is 300.
   * 
   * @param delay the delay in milliseconds
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * Subclasses should override as needed.
   */
  protected void onChange() {

  }

}
