/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the fields and structure of a <code>Model</code>. Used by
 * <code>DataReaders</code> when parsing raw data.
 */
public class ModelType {

  /**
   * The root element name.
   */
  public String root;

  /**
   * The record property name.
   */
  public String recordName;

  /**
   * The total property name.
   */
  public String totalName;

  private List fields;

  /**
   * Creates a new instance.
   */
  public ModelType() {
    fields = new ArrayList();
  }

  /**
   * Adds a field to the model.
   * 
   * @param field the field to be added
   */
  public void addField(DataField field) {
    fields.add(field);
  }

  /**
   * Adds a field to the model.
   * 
   * @param name the field name
   */
  public void addField(String name) {
    addField(new DataField(name));
  }

  /**
   * Adds a field to the model.
   * 
   * @param name the field name
   * @param map the map name
   */
  public void addField(String name, String map) {
    addField(new DataField(name, map));
  }

  /**
   * Returns the field at the given index.
   * 
   * @param index the index of the field to return
   * @return the field at the given index
   */
  public DataField getField(int index) {
    if (index < 0 || index >= fields.size()) return null;
    return (DataField) fields.get(index);
  }

  /**
   * Returns the field with the given name.
   * 
   * @param name the name of the field to return
   * @return the field with the given name of <code>null</code> if no match
   */
  public DataField getField(String name) {
    for (int i = 0; i < fields.size(); i++) {
      DataField field = getField(i);
      if (field.name.equals(name)) {
        return field;
      }
    }
    return null;
  }

  /**
   * Returns the number of fields.
   * 
   * @return the field count
   */
  public int getFieldCount() {
    return fields.size();
  }

}
