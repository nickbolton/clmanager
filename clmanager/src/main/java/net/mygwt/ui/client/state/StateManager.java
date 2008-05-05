/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.state;

import java.util.Date;

/**
 * This is the global state manager. In order for this class to be useful, it
 * must be initialized with a provider when your application initializes. By
 * default, MyGWT initializes the StateManager with a CookieProvider. The
 * provider can be replaced as needed.
 * 
 * <dl>
 * <dt>Events:</dt>
 * <dd><b>StateChange</b> : (source this, name, value) <br>
 * <div>Fires after a state change.</div>
 * <ul>
 * <li>source : the state manager</li>
 * <li>name : the key name</li>
 * <li>value : the value or <code>null</code> if cleared</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class StateManager {

  private static Provider provider;

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public static Object get(String name) {
    return provider.get(name);
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public static Date getDate(String name) {
    return provider.getDate(name);
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public static int getInteger(String name) {
    return provider.getInteger(name);
  }

  /**
   * Returns the manager's state provider.
   * 
   * @return the provider
   */
  public static Provider getProvider() {
    return provider;
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public static String getString(String name) {
    return provider.getString(name);
  }

  /**
   * Sets a key.
   * 
   * @param name the key name
   * @param value the value
   */
  public static void set(String name, Object value) {
    provider.set(name, value);
  }

  /**
   * Sets the manager's state provider.
   * 
   * @param stateProvider the provider
   */
  public static void setProvider(Provider stateProvider) {
    provider = stateProvider;
  }

}
