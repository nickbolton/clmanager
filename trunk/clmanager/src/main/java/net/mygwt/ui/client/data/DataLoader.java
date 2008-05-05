/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.List;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.util.Observable;

/**
 * <code>DataLoaders</code> are used to retrieve and convert raw data. Data is
 * retrieved using a <code>DataProxy</code> and optionally read and parsed
 * using a <code>DataReader</code>.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeLoad</b> : (loader, config)<br>
 * <div>Fires before a load operation. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Load</b> : (loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>LoadException</b> : (loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see DataProxy
 * @see DataReader
 */
public class DataLoader extends Observable implements Loader {

  /**
   * totalLength specifies the total number of records which may not equal the
   * local elements when using paging.
   */
  public int totalLength;
  private boolean remoteSort;

  private String sortField;
  private int sortDir = Style.NONE;

  private Object data;
  private LoadConfig lastConfig;
  private DataProxy proxy;
  private DataReader reader;

  /**
   * Creates a new loader instance with the given proxy. A reader is not
   * specified and will not be passed to the proxy at load time.
   * 
   * @param proxy the data proxy
   */
  public DataLoader(DataProxy proxy) {
    this.proxy = proxy;
  }

  /**
   * Creates a new loader with the given proxy and reader.
   * 
   * @param proxy the data proxy
   * @param reader the data reader
   */
  public DataLoader(DataProxy proxy, DataReader reader) {
    this.proxy = proxy;
    this.reader = reader;
  }

  /**
   * Returns the last data from the last load.
   * 
   * @return the remote data
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the elements from the last load as a list.
   * 
   * @deprecated Use {@link #getData()}.
   * @return the elements
   */
  public List getElements() {
    if (data instanceof List) {
      return (List) data;
    }
    return null;
  }

  public LoadConfig getLastConfig() {
    return lastConfig;
  }

  public boolean getRemoteSort() {
    return remoteSort;
  }

  public int getSortDir() {
    return sortDir;
  }

  public String getSortField() {
    return sortField;
  }

  public int getTotalLength() {
    return totalLength;
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

  public void load(LoadConfig config) {
    LoadEvent evt = new LoadEvent(this, config, null);
    if (fireEvent(BeforeLoad, evt)) {
      lastConfig = config;
      lastConfig.sortField = sortField;
      lastConfig.sortDir = sortDir;
      proxy.load(reader, config, new DataCallback() {
        public void setResult(LoadResult result) {
          onReceivedResult(result);
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
   * Sets if sorting is handled by the server. Defult value is
   * <code>false</code>.
   * 
   * @param remoteSort <code>true</code> to enable server side sorting
   */
  public void setRemoteSort(boolean remoteSort) {
    this.remoteSort = remoteSort;
  }

  public void setSortDir(int sortDir) {
    this.sortDir = sortDir;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  public void sort(String sortField, int sortDir) {
    this.sortField = sortField;
    this.sortDir = sortDir;
    load(lastConfig);
  }

  protected void onReceivedResult(LoadResult result) {
    LoadEvent evt = new LoadEvent(this, lastConfig, result);
    evt.result = result;
    data = result.data;
    if (result.success) {
      totalLength = result.totalLength;
      fireEvent(Load, evt);
    } else {
      fireEvent(LoadException, evt);
    }
  }
}
