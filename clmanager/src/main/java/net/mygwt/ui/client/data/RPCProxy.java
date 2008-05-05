/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <code>DataProxy</code> implementation that retrieves data using GWT RPC.
 */
public abstract class RPCProxy implements DataProxy, AsyncCallback {

  protected DataReader reader;
  protected DataCallback callback;
  protected LoadConfig config;

  public void load(DataReader reader, LoadConfig config, DataCallback cb) {
    this.reader = reader;
    this.config = config;
    callback = cb;
    loadInternal(this);
  }

  public void onFailure(Throwable caught) {
    LoadResult result = new LoadResult();
    result.success = false;
    callback.setResult(result);
  }

  public void onSuccess(Object data) {
    if (reader != null) {
      LoadResult result = reader.read(data);
      callback.setResult(result);
    } else if (data instanceof LoadResult) {
      callback.setResult((LoadResult) data);
    } else if (data instanceof DataList) {
      callback.setResult(new LoadResult((DataList) data));
    } else if (data instanceof Model) {
      callback.setResult(new LoadResult((Model) data));
    } else {
      LoadResult result = new LoadResult();
      result.success = false;
      callback.setResult(result);
    }
  }

  /**
   * Subclasses should make RPC call using the load configuration.
   * 
   * @param callback the callback to be used when making the rpc call.
   */
  protected abstract void loadInternal(AsyncCallback callback);
}
