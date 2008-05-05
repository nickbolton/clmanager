/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Interface for objects that can read raw data and produce a set of
 * <code>Model</code> objects.
 * 
 * @see Model
 */
public interface DataReader {

  /**
   * Reads the raw data and creates a set of Model instances.
   * 
   * @param data the data to read
   * @return a load result
   */
  public LoadResult read(Object data);

}
