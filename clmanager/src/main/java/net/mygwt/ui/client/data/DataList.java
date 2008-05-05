/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A <code>ArrayList<code> that implements <code>IsSerializable</code>. GWT compiler 
 * throws an error when trying to assign a ArrayList as a Serializable instance.
 */
public class DataList extends ArrayList implements IsSerializable {

  public DataList() {
    super();
  }

  public DataList(Collection c) {
    super(c);
  }

}
