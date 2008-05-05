/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.Timer;

/**
 * A <code>Timer</code> that is cancelled if a new request is made.
 */
public class DelayedTask {

  private Timer timer;
  private Listener listener;

  /**
   * Creates a new delayed task.
   * 
   * @param listener the listener to be called
   */
  public DelayedTask(final Listener listener) {
    this.listener = listener;
  }

  /**
   * Cancels the task.
   */
  public void cancel() {
    if (timer != null) {
      timer.cancel();
    }
  }
  
  /**
   * Cancels any running timers and starts a new one.
   * 
   * @param delay the delay in ms
   */
  public void delay(int delay) {
    if (timer != null) {
      timer.cancel();
      timer.schedule(delay);
    } else {
      timer = new Timer() {
        public void run() {
          timer = null;
          listener.handleEvent(null);
        }
      };
      timer.schedule(delay);
    }
  }

}
