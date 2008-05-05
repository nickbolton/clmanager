/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

/**
 * Describes a field in a <code>Model</code>. Used when mapping raw data to a
 * model's properties.
 */
public class DataField {

  /**
   * Field type constant for dates.
   */
  public static final String DATE_TYPE = "date";

  /**
   * The name of the field.
   */
  public String name;

  /**
   * An optional field used when the property name of the model is different
   * than the property name of the raw data.
   */
  public String map;

  /**
   * The data type of the field.
   */
  public String type;

  /**
   * Format is used when converting raw data to object instances.
   */
  public String format;

  /**
   * Creates a new field.
   * 
   * @param name the name
   */
  public DataField(String name) {
    this.name = name;
  }

  /**
   * Creates a new field instance.
   * 
   * @param name the field name
   * @param map the map name
   */
  public DataField(String name, String map) {
    this.name = name;
    this.map = map;
  }

}
