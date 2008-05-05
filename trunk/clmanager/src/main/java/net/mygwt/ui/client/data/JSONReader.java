/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * A <code>DataReader</code> implementation that reads JSON data using a
 * <code>ModelType</code> definition and produces a set of <code>Model</code>
 * instances.
 */
public class JSONReader implements DataReader {

  private ModelType modelType;

  public JSONReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public LoadResult read(Object data) {
    JSONObject jsonRoot = (JSONObject) JSONParser.parse((String) data);
    JSONArray root = (JSONArray) jsonRoot.get(modelType.root);
    int size = root.size();
    DataList records = new DataList();
    for (int i = 0; i < size; i++) {
      JSONObject obj = (JSONObject) root.get(i);
      Map values = new HashMap();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        JSONValue value = obj.get(map);

        if (value == null) continue;
        if (value.isArray() != null) {
          // nothing
        } else if (value.isBoolean() != null) {
          boolean b = ((JSONBoolean) value).booleanValue();
          values.put(field.name, new Boolean(b));
        } else if (value.isNumber() != null) {
          double d = ((JSONNumber) value).getValue();
          values.put(field.name, new Double(d));
        } else if (value.isObject() != null) {
          // nothing
        } else if (value.isString() != null) {
          String s = ((JSONString) value).stringValue();
          if (field.type != null) {
            if (field.type.equals("date")) {
              DateTimeFormat format = DateTimeFormat.getFormat(field.format);
              Date d = format.parse(s);
              values.put(field.name, d);
            }
          } else {
            values.put(field.name, s);
          }

        } else if (value.isNull() != null) {
          values.put(field.name, null);
        }
      }
      records.add(new Model(values));
    }
    LoadResult result = new LoadResult();
    result.data = records;
    return result;
  }
}
