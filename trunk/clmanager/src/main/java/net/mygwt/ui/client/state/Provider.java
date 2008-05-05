/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.state;

import java.util.Date;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Observable;

/**
 * Abstract base class for state provider implementations. This class provides
 * methods for encoding and decoding objects.
 */
public abstract class Provider extends Observable {

  /**
   * Clears a value.
   * 
   * @param name the key name
   */
  public void clear(String name) {
    clearKey(name);
    BaseEvent be = new BaseEvent();
    be.name = name;
    fireEvent(Events.StateChange, be);
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public Object get(String name) {
    String val = getValue(name);
    if (val == null) return null;
    Object obj = decodeValue(val);
    return obj;
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public String getString(String name) {
    String val = getValue(name);
    if (val == null) return null;
    String obj = (String) decodeValue(val);
    return obj;
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public boolean getBoolean(String name) {
    String val = getValue(name);
    if (val == null) return false;
    Boolean bVal = (Boolean) decodeValue(val);
    return bVal.booleanValue();
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value
   */
  public Date getDate(String name) {
    String val = getValue(name);
    if (val == null) return null;
    Date date = (Date) decodeValue(val);
    return date;
  }

  /**
   * Returns the current value for a key.
   * 
   * @param name the key name
   * @return the value or -1
   */
  public int getInteger(String name) {
    String val = getValue(name);
    Integer iVal = (Integer) decodeValue(val);
    if (iVal == null) {
      return -1;
    }
    return iVal.intValue();
  }

  /**
   * Sets a key.
   * 
   * @param name the key name
   * @param value the value
   */
  public void set(String name, Object value) {
    String val = encodeValue(value);
    setValue(name, val);
    BaseEvent be = new BaseEvent();
    be.name = name;
    be.value = value;
    fireEvent(Events.StateChange, be);
  }

  protected abstract void clearKey(String name);

  protected Object decodeValue(String value) {
    if (value == null) return null;
    String type = value.substring(0, 2);
    String val = value.substring(2);
    if (type.equals("i:")) {
      return Integer.decode(val);
    } else if (type.equals("d:")) {
      long time = Long.parseLong(val);
      return new Date(time);
    } else if (type.equals("b:")) {
      return new Boolean(val);
    }
    return val;
  }

  protected String encodeValue(Object value) {
    if (value instanceof Date) {
      return "d:" + ((Date) value).getTime();
    } else if (value instanceof Integer) {
      return "i:" + value;
    } else if (value instanceof Float) {
      return "i:" + value;
    } else if (value instanceof Boolean) {
      return "b:" + value;
    }
    return "s:" + value.toString();
  }

  protected abstract String getValue(String name);

  protected abstract void setValue(String name, String value);

}
