/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import net.mygwt.ui.client.event.BaseEvent;

/**
 * Instances of this class are sent as a result of load operations.
 */
public class LoadEvent extends BaseEvent {

  /**
   * The source loader.
   */
  public Loader loader;

  /**
   * The config object.
   */
  public LoadConfig config;

  /**
   * The load result.
   */
  public LoadResult result;

  public LoadEvent(Loader loader, LoadConfig config, LoadResult result) {
    this.loader = loader;
    this.config = config;
    this.result = result;
  }

}
