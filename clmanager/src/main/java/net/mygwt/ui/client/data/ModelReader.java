/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;



/**
 * A <code>DataReader</code> implementation for <code>Model</code>
 * instances.
 */
public class ModelReader implements DataReader {

  public LoadResult read(Object data) {
    if (data instanceof LoadResult) {
      return (LoadResult) data;
    } else if (data instanceof DataList) {
      LoadResult result = new LoadResult((DataList) data);
      return result;
    } else if (data instanceof Model) {
      LoadResult result = new LoadResult((Model) data);
      return result;
    }
    LoadResult result = new LoadResult();
    result.success = false;
    return result;
  }

}
