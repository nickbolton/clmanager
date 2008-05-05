/*******************************************************************************
 * Copyright (c) 2007 MyGWT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.data.DataCallback;
import net.mygwt.ui.client.data.LoadConfig;
import net.mygwt.ui.client.data.LoadEvent;
import net.mygwt.ui.client.data.LoadResult;
import net.mygwt.ui.client.data.Loader;
import net.mygwt.ui.client.util.Observable;

/**
 * A <code>IStructuredContentProvider</code> implementation that supports the
 * remote loading of data and paging.
 */
public abstract class RemoteContentProvider extends Observable implements Loader,
    IStructuredContentProvider {

  /**
   * The owning viewer.
   */
  protected Viewer viewer;

  /**
   * The last load config.
   */
  protected LoadConfig lastConfig;

  private String sortField;
  private int sortDir = Style.NONE;
  private boolean remoteSort;
  private Object data;
  private int totalLength;

  /**
   * Returns the last data from the last load.
   * 
   * @return the current remote data
   */
  public Object getData() {
    return data;
  }

  /**
   * Subclasses must implement and return the remote data based on the given
   * load config. The viewer's setInput method will be called, passing the data
   * being returned from the callback.
   * 
   * @param config the load config
   * @param callback the callback
   */
  public abstract void getData(LoadConfig config, DataCallback callback);


  /* (non-Javadoc)
   * @see net.mygwt.ui.client.data.Loader#getLastConfig()
   */
  public LoadConfig getLastConfig() {
    return lastConfig;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.data.Loader#getRemoteSort()
   */
  public boolean getRemoteSort() {
    return remoteSort;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.mygwt.ui.client.data.Loader#getSortDir()
   */
  public int getSortDir() {
    return sortDir;
  }

  /**
   * Sets the sort field.
   * 
   * @return the sort field
   */
  public String getSortField() {
    return sortField;
  }

  /**
   * Returns the total number of records which may not equal the local elements
   * when using paging.
   * 
   * @return the total length
   */
  public int getTotalLength() {
    return totalLength;
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;
  }

  /**
   * Loads the data.
   */
  public void load() {
    load(new LoadConfig());
  }

  /**
   * Loads the data using the specified start and limit parameters.
   * 
   * @param start the start mark
   * @param limit the limit mark
   */
  public void load(int start, int limit) {
    LoadConfig config = new LoadConfig();
    config.start = start;
    config.limit = limit;
    load(config);
  }

  /**
   * Loads the data using the specified configuation.
   * 
   * @param config the configuration
   */
  public void load(LoadConfig config) {
    LoadEvent evt = new LoadEvent(this, config, null);
    if (fireEvent(BeforeLoad, evt)) {
      lastConfig = config;
      lastConfig.sortField = sortField;
      lastConfig.sortDir = sortDir;
      getData(lastConfig, new DataCallback() {
        public void setResult(LoadResult result) {
          onReceiveResults(result);
        }
      });
    }
  }

  /**
   * Reloads using the last load config.
   */
  public void reload() {
    if (lastConfig != null) {
      load(lastConfig);
    }
  }

  /**
   * Sets whether sorting should be done server side.
   * 
   * @param remoteSort <code>true</code> to enable remote sorting
   */
  public void setRemoteSort(boolean remoteSort) {
    this.remoteSort = remoteSort;
  }

  /**
   * Sets the sort direction.
   * 
   * @param sortDir the sort direction
   */
  public void setSortDir(int sortDir) {
    this.sortDir = sortDir;
  }

  /**
   * Sets the current sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Sorts the elements.
   * 
   * @param sortField the sort field
   * @param sortDir the sort direction
   */
  public void sort(String sortField, int sortDir) {
    this.sortField = sortField;
    this.sortDir = sortDir;
    load(lastConfig);
  }

  protected void onReceiveResults(LoadResult result) {
    LoadEvent evt = new LoadEvent(this, lastConfig, result);
    data = result.getData();
    if (result.success) {
      totalLength = result.totalLength;
      fireEvent(Load, evt);
    } else {
      fireEvent(LoadException, evt);
    }
  }

}
