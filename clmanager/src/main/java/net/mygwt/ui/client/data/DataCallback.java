/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Callback used when sending remote data back to the client.
 */
public interface DataCallback {

  /**
   * Sets the load results.
   * 
   * @param result the result of the data load
   */
  public void setResult(LoadResult result);

}
