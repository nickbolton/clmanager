/*
 * Copyright 2007 MyGWT.
 * 
 * Licensed under the MyGWT License, Version 1.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://mygwt.net/license
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.mygwt.ui.client.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Contains configuration data for a data load operations.
 */
public class LoadConfig implements IsSerializable {

  /**
   * start specifies the current cursor.
   */
  public int start;

  /**
   * limit specifies the number of records being requested.
   */
  public int limit;

  /**
   * sortField specifies the field to sort by.
   */
  public String sortField;

  /**
   * sortDir specifies the requested sort direction.
   */
  public int sortDir;

  /**
   * dataMap contains application specific load properties
   * 
   * @gwt.typeArgs <java.lang.String,com.google.gwt.user.client.rpc.IsSerializable>
   */
  protected Map dataMap;

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
   * Returns the config's data map.
   * 
   * @return the data map
   */
  public Map getDataMap() {
    return dataMap;
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

  /**
   * Sets the config's data map.
   * 
   * @param dataMap the data map
   */
  public void setDataMap(Map dataMap) {
    this.dataMap = dataMap;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("start=" + start);
    sb.append("&limit=" + limit);
    sb.append("&sortField=" + sortField);
    sb.append("&sortDir=" + sortDir);
    return sb.toString();
  }

}
