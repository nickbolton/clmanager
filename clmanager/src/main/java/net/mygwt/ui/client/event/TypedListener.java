/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.event;

import java.util.EventListener;

import net.mygwt.ui.client.Events;

/**
 * Provides a mapping between the typed and untyped listener mechanisms that
 * MyGWT supports.
 */
public class TypedListener implements Listener {

  protected EventListener eventListener;

  /**
   * Creates a new typed listener.
   * 
   * @param listener the typed listener
   */
  public TypedListener(EventListener listener) {
    eventListener = listener;
  }

  /**
   * Returns the event listener.
   * 
   * @return the event listener
   */
  public EventListener getEventListener() {
    return eventListener;
  }

  public void handleEvent(BaseEvent be) {
    switch (be.type) {
      case Events.EffectStart:
        ((EffectListener) eventListener).effectStart(be);
        break;
      case Events.EffectCancel:
        ((EffectListener) eventListener).effectCancel(be);
        break;
      case Events.EffectComplete:
        ((EffectListener) eventListener).effectComplete(be);
        break;
      case Events.Attach:
        ((WidgetListener) eventListener).widgetAttached(be);
        break;
      case Events.Detach:
        ((WidgetListener) eventListener).widgetDetached(be);
        break;
      case Events.Resize:
        ((WidgetListener) eventListener).widgetResized(be);
        break;
      case Events.Close:
        ((ShellListener) eventListener).shellClosed(be);
        break;
      case Events.Activate:
        ((ShellListener) eventListener).shellActivated(be);
        break;
      case Events.Deactivate:
        ((ShellListener) eventListener).shellDeactivated(be);
        break;
      case Events.Select:
        ((SelectionListener) eventListener).widgetSelected(be);
        break;
      case Events.DragStart:
        ((DragListener) eventListener).dragStart(be);
        break;
      case Events.DragMove:
        ((DragListener) eventListener).dragMove(be);
        break;
      case Events.DragCancel:
        ((DragListener) eventListener).dragCancel(be);
        break;
      case Events.DragEnd:
        ((DragListener) eventListener).dragEnd(be);
        break;
      case Events.Scroll:
        ((ScrollListener) eventListener).widgetScrolled(be);
        break;
    }
  }

}
