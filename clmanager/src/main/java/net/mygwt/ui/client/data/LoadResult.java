/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Contains the results of a data load operation.
 */
public class LoadResult implements IsSerializable {

  /**
   * success specifies if the load was successful. Default value is
   * <code>true</code>.
   */
  public boolean success = true;

  /**
   * add specifies if the results should be added.
   */
  public boolean add;

  /**
   * totalLength specifies the total number of elements. This may not be the
   * same as the number of elements when implementing paging.
   */
  public int totalLength;

  /**
   * cursor specifies the current location of the data.
   */
  public int cursor;

  /**
   * The returned data.
   */
  public IsSerializable data;

  /**
   * dataMap contains application specific load properties
   * 
   * @gwt.typeArgs <java.lang.String,com.google.gwt.user.client.rpc.IsSerializable>
   */
  public Map dataMap;

  /**
   * Creates a new load result instance.
   */
  public LoadResult() {

  }

  /**
   * Creates a new load result and sets the success field to <code>true</code>.
   * 
   * @param data the returned data
   */
  public LoadResult(IsSerializable data) {
    this.data = data;
    success = true;
  }

  /**
   * Sets the result data.
   * 
   * @param data the result data
   */
  public void setData(IsSerializable data) {
    this.data = data;
  }

  /**
   * Returns the result data.
   * 
   * @return the data
   */
  public IsSerializable getData() {
    return data;
  }

  /**
   * Returns the application specific load property for the given name, or
   * <code>null</code> if it has not been set.
   * 
   * @param key the name of the property
   * @return the value or <code>null</code> if it has not been set
   */
  public Object getData(String key) {
    if (dataMap == null) return null;
    return dataMap.get(key);
  }

  /**
   * Sets the application specific load property with the given name.
   * 
   * @param key the name of the property
   * @param config the new value for the property
   */
  public void setData(String key, Object config) {
    if (dataMap == null) dataMap = new HashMap();
    dataMap.put(key, config);
  }
}
