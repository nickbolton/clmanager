/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * A <code>DataReader</code> implementation that reads XML data using a
 * <code>ModelType</code> definition and creates <code>Model</code>
 * instances.
 */
public class XMLReader implements DataReader {

  private ModelType modelType;

  public XMLReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public LoadResult read(Object data) {
    Document doc = XMLParser.parse((String) data);
    Node root = doc.getFirstChild();
    NodeList list = doc.getElementsByTagName(modelType.recordName);
    DataList records = new DataList();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      Element elem = (Element) node;
      Map values = new HashMap();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        String v = getValue(elem, map);
        values.put(field.name, v);
      }
      records.add(new Model(values));
    }

    int totalCount = records.size();

    if (modelType.totalName != null) {
      Node totalNode = root.getAttributes().getNamedItem(modelType.totalName);
      if (totalNode != null) {
        String sTot = totalNode.getNodeValue();
        totalCount = Integer.parseInt(sTot);
      }
    }

    LoadResult result = new LoadResult(records);
    result.totalLength = totalCount;
    return result;
  }

  protected String getValue(Element elem, String name) {
    if (elem.hasAttribute(name)) {
      return elem.getAttribute(name);
    } else {
      NodeList elems = elem.getElementsByTagName(name);
      if (elems != null) {
        elem = (Element)elems.item(0);
        if (elem != null && elem.getFirstChild() != null) {
          return elem.getFirstChild().getNodeValue();
        }
      }
      return "";
    }
  }

}
