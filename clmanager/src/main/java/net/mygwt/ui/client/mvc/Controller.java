/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <code>Controllers</code> process and respond to application events.
 */
public abstract class Controller {

  /**
   * Forward an event to a view. Ensures the view is initialized before
   * forwarding the event.
   * 
   * @param view the view to forward the event
   * @param event the event to be forwarded
   */
  public static void forwardToView(View view, AppEvent event) {
    if (!view.initialized) {
      view.initialize();
      view.initialized = true;
    }
    view.handleEvent(event);
  }

  protected Controller parent;
  protected List children;
  
  boolean initialized;
  private HashSet events;

  /**
   * Add a child controller.
   * 
   * @param controller the controller to added
   */
  public void addChild(Controller controller) {
    if (children == null) children = new ArrayList();
    children.add(controller);
    controller.parent = this;
  }

  /**
   * Determines if the controller can handle the particular event. Default
   * implementation checks against registered event types then queries all child
   * controllers.
   * 
   * @param event the event
   * @return <code>true</code> if event can be handled, <code>false</code>
   *         otherwise
   */
  public boolean canHandle(AppEvent event) {
    if (events != null && events.contains(new Integer(event.type))) return true;
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        Controller c = (Controller) children.get(i);
        if (c.canHandle(event)) return true;
      }
    }
    return false;
  }

  /**
   * Forwards an event to any child controllers who can handle the event.
   * 
   * @param event the event to forward
   */
  public void forwardToChild(AppEvent event) {
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        Controller c = (Controller) children.get(i);
        if (!c.initialized) {
          c.initialize();
          c.initialized = true;
        } 
        if (c.canHandle(event)) {
          c.handleEvent(event);
        }
      }
    }
  }

  /**
   * Processes the event.
   * 
   * @param event the current event
   */
  public abstract void handleEvent(AppEvent event);

  /**
   * Called once prior to handleEvent being called.
   */
  public void initialize() {

  }

  /**
   * Registers the event type.
   * 
   * @param type the event type
   */
  public void registerEventType(int type) {
    if (events == null) {
      events = new HashSet();
    }
    events.add(new Integer(type));
  }

}
