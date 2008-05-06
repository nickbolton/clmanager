/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Manages <code>Shell</code> instances. The manager ensures the active shell
 * is always displayed over other shells and fires shell events.
 */
public class ShellManager {

  private static ShellManager instance;

  /**
   * Returns the singleton instance.
   * 
   * @return the shell manager
   */
  public static ShellManager get() {
    if (instance == null) instance = new ShellManager();
    return instance;
  }

  private List windows;
  private Shell active;

  private ShellManager() {
    instance = this;
    windows = new ArrayList();
  }

  /**
   * Closes all open shells.
   */
  public void closeAll() {
    int size = windows.size();
    for (int i = size - 1; i >= 0; i--) {
      Shell shell = (Shell) windows.get(i);
      shell.close();
    }
  }

  /**
   * Returns the active shell, or <code>null</code> if no active shell.
   * 
   * @return the active shell or <code>null</code>
   */
  public Shell getActive() {
    return active;
  }

  /**
   * Returns <code>true</code> if active, <code>false</code> otherwise.
   * 
   * @param shell the shell
   * @return the active state
   */
  public boolean isActive(Shell shell) {
    return active != null && active == shell;
  }

  /**
   * Registers a shell with the manager.
   * 
   * @param shell the shell to be registered
   */
  public void register(Shell shell) {
    windows.add(shell);
  }

  /**
   * Sets the active shell.
   * 
   * @param shell the shell to activate
   */
  public void setActive(Shell shell) {
    if (active != null && active == shell) {
      return;
    }
    if (active != null) {
      active.fireEvent(Events.Deactivate);
    }
    active = shell;
    if (active.shadow != null) {
      setZIndex(active.shadow, MyDOM.getZIndex());
    }
    setZIndex(active, MyDOM.getZIndex());
    active.fireEvent(Events.Activate);
  }

  /**
   * Unregisters a shell.
   * 
   * @param shell the shell to unregister
   */
  public void unregister(Shell shell) {
    if (shell == active) active = null;
    windows.remove(shell);
  }

  private void setZIndex(Widget widget, int zIndex) {
    DOM.setIntStyleAttribute(widget.getElement(), "zIndex", zIndex);
  }

}
