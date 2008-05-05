/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

/**
 * A concrete <code>DataProxy</code> that retrieves data using
 * <code>RequestBulder</code> instances.
 * 
 * @see RequestBuilder
 */
public class HTTPProxy implements DataProxy, RequestCallback {

  private RequestBuilder builder;
  private DataReader reader;
  private DataCallback callback;
  private LoadConfig config;

  public HTTPProxy(RequestBuilder builder) {
    this.builder = builder;
  }

  public void load(DataReader reader, LoadConfig cfg, DataCallback cb) {
    this.reader = reader;
    this.config = cfg;
    callback = cb;
    try {
      builder.sendRequest(cfg.toString(), this);
    } catch (Exception e) {
      LoadResult result = new LoadResult();
      result.success = false;
      callback.setResult(result);
    }
  }

  public void onError(Request request, Throwable exception) {
    LoadResult result = new LoadResult();
    result.success = false;
    callback.setResult(result);
  }

  public void onResponseReceived(Request request, Response response) {
    String text = response.getText();
    LoadResult result = reader.read(text);
    result.success = true;
    result.cursor = config.start;
    callback.setResult(result);
  }

}