/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Defines the interface for objects that can retrieve data.
 */
public interface DataProxy {

  /**
   * Data should be retrived using the specified load config.
   * 
   * @param reader the reader instance
   * @param config the config
   * @param cb the data callback
   */
  public void load(DataReader reader, LoadConfig config, DataCallback cb);

}
