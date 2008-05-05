/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.viewer.RemoteContentProvider;

/**
 * Interface for objects that can retrieve remote data.
 * 
 * @see DataLoader
 * @see RemoteContentProvider
 */
public interface Loader {

  /**
   * Fires before a request is made for data (value is 300).
   */
  public static final int BeforeLoad = 300;

  /**
   * Fires when new data has been loaded (value is 301).
   */
  public static final int Load = 301;

  /**
   * Fires if an exception occurs while retrieving data (value is 302).
   */
  public static final int LoadException = 302;

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener);

  /**
   * Returns the last load config.
   * 
   * @return the last load config
   */
  public LoadConfig getLastConfig();
  
  /**
   * Returns <code>true</code> if remote sorting is enabled.
   * 
   * @return the remote sort state
   */
  public boolean getRemoteSort();
  
  /**
   * Returns the current sort direction.
   * 
   * @return the sort direction
   */
  public int getSortDir();

  /**
   * Returns the current sort field.
   * 
   * @return the sort field
   */
  public String getSortField();

  /**
   * Returns the total number of records which may not equal the local elements
   * when using paging.
   * 
   * @return the total length
   */
  public int getTotalLength();

  /**
   * Loads the data using the specified configuation.
   * 
   * @param config the configuration
   */
  public void load(LoadConfig config);

  /**
   * Reloads using the last load config.
   */
  public void reload();

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener);

  /**
   * Sets the current sort dir.
   * 
   * @param dir the sort dir
   */
  public void setSortDir(int dir);

  /**
   * Sets the current sort field.
   * 
   * @param field the sort field
   */
  public void setSortField(String field);

  /**
   * Sorts the elements.
   * 
   * @param sortField the sort field
   * @param sortDir the sort direction
   */
  public void sort(String sortField, int sortDir);
}
