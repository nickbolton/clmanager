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
package net.mygwt.ui.client.util;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.TypedListener;

/**
 * Maps listeners by event type.
 */
public class EventTable {

  private HashMap map;

  /**
   * Returns <code>true</code> if the event table has any listeners for the
   * given event type.
   * 
   * @param eventType the event type
   * @return the listener state
   */
  public boolean hasListener(int eventType) {
    if (map == null) {
      return false;
    }
    return map.containsKey(new Integer(eventType));
  }

  /**
   * Hooks the listener by the given event type and element.
   * 
   * @param eventType the event type
   * @param listener the listener to be added
   */
  public void hook(int eventType, Listener listener) {
    if (map == null) {
      map = new HashMap();
    }
    Integer type = new Integer(eventType);
    List listeners = (List) map.get(type);
    if (listeners == null) {
      listeners = new ArrayList();
      map.put(type, listeners);
    }
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Removes all mapped listeners.
   */
  public void removeAllListeners() {
    map.clear();
  }

  /**
   * Sends the event to any registered listeners.
   * 
   * @param be the base event
   * @return <code>true</code> if event was canceled by any listeners
   */
  public boolean sendEvent(BaseEvent be) {
    if (map == null) return true;
    List list = (List) map.get(new Integer(be.type));
    if (list == null) return true;
    for (int i = 0; i < list.size(); i++) {
      Listener l = (Listener) list.get(i);
      l.handleEvent(be);
    }
    return be.doit;
  }

  public void unhook(int eventType, EventListener listener) {
    if (map == null) return;
    List list = (List) map.get(new Integer(eventType));
    if (list == null) return;
    for (int i = list.size() - 1; i >= 0; i--) {
      TypedListener l = (TypedListener) list.get(i);
      if (l.getEventListener() == listener) {
        list.remove(l);
      }
    }
  }

  /**
   * Unhooks the given listener for the given event type.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void unhook(int eventType, Listener listener) {
    if (map == null) return;
    Integer type = new Integer(eventType);
    List list = (List) map.get(type);
    if (list == null) return;
    list.remove(listener);
  }
}
