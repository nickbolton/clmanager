/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Forces all basic java types to be added to the serialization policy. This is
 * need as all <code>Model</code> properties are stored in a <code>Map</code>.
 */
public class RPCTypes implements IsSerializable {
  
  private static final long serialVersionUID = -1373411581609517810L;
  
  String _string;
  Date _date;
  Byte _byte;
  Short _short;
  Integer _integer;
  Long _long;
  Boolean _boolean;
  Float _float;
  Double _double;
}
