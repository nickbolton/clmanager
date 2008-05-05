/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.mvc;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Observable;

/**
 * Dispatchers are responsible for dispatching application events to
 * controllers.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Dispatcher.BeforeDispatch</b> : (source, value)<br>
 * <div>Fires before an event is dispatched. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>source : this</li>
 * <li>value : the app event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Dispatcher.AfterDispatch</b> : (source, data)<br>
 * <div>Fires after an event has been dispatched.</div>
 * <ul>
 * <li>source : this</li>
 * <li>value : the app event</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 * 
 * @see DispatcherListener
 */
public class Dispatcher extends Observable {

  /**
   * Fires before an event is dispatched (value is 1100).
   */
  public static final int BeforeDispatch = 1100;

  /**
   * Fires after an event has been dispatched (value is 1110).
   */
  public static final int AfterDispatch = 1110;

  private static Dispatcher instance;

  /**
   * Forwards an app event to the dispatcher.
   * 
   * @param event the app event
   */
  public static void forwardEvent(AppEvent event) {
    instance.dispatch(event);
  }

  /**
   * Creates and forwards an app event to the dispatcher.
   * 
   * @param eventType the app event type
   */
  public static void forwardEvent(int eventType) {
    instance.dispatch(eventType);
  }

  /**
   * Returns the singleton instance.
   * 
   * @return the dispatcher
   */
  public static Dispatcher get() {
    if (instance == null) {
      instance = new Dispatcher();
    }
    return instance;
  }

  private List controllers;

  private Dispatcher() {
    instance = this;
    controllers = new ArrayList();
  }

  /**
   * Adds a controller.
   * 
   * @param controller the controller to be added
   */
  public void addController(Controller controller) {
    if (!controllers.contains(controller)) {
      controllers.add(controller);
    }
  }

  /**
   * Adds a listener to receive dispatch events.
   * 
   * @param listener the listener to add
   */
  public void addDispatcherListener(DispatcherListener listener) {
    DispatcherTypedListener typedListener = new DispatcherTypedListener(listener);
    addListener(BeforeDispatch, typedListener);
    addListener(AfterDispatch, typedListener);
  }

  /**
   * The dispatcher will query its controllers and pass the app event to any
   * controllers that can handle the particular event type.
   * 
   * @param event the app event
   */
  public void dispatch(AppEvent event) {
    BaseEvent be = new BaseEvent();
    be.source = this;
    be.value = event;
    if (fireEvent(BeforeDispatch, be)) {
      for (int i = 0; i < controllers.size(); i++) {
        Controller controller = (Controller) controllers.get(i);
        if (controller.canHandle(event)) {
          if (!controller.initialized) {
            controller.initialized = true;
            controller.initialize();
          }
          controller.handleEvent(event);
        }
      }
      fireEvent(AfterDispatch, be);
    }
  }

  /**
   * The dispatcher will query its controllers and pass the app event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   */
  public void dispatch(int type) {
    dispatch(new AppEvent(type));
  }

  /**
   * The dispatcher will query its controllers and pass the app event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   * @param data the app event data
   */
  public void dispatch(int type, Object data) {
    dispatch(new AppEvent(type, data));
  }

  /**
   * Returns all controllers.
   * 
   * @return the list of controllers
   */
  public List getControllers() {
    return controllers;
  }

  /**
   * Removes a controller.
   * 
   * @param controller the controller to be removed
   */
  public void removeController(Controller controller) {
    controllers.remove(controller);
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeDispatcherListener(DispatcherListener listener) {
    if (eventTable != null) {
      eventTable.unhook(BeforeDispatch, listener);
      eventTable.unhook(AfterDispatch, listener);
    }
  }

}
