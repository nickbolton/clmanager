/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <code>Models</code> are generic data structures that notify listeners when
 * changed. The structure allows a form of 'introspection' as all property names
 * and values can be queried and retrieved at runtime.
 * 
 * <p>
 * All events fired by the model will bubble to all parents.
 * </p>
 * 
 * <p>
 * Model objects implement <code>Serializable</code> and can therefore
 * be used with GWT RPC. A model's children are not marked transient and will be
 * passed in remote procedure calls.
 * </p>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Model.Add</b> : (source, item)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>source : this</li>
 * <li>item : add item</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Model.Insert</b> : (source, item)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>source : this</li>
 * <li>item : insert item</li>
 * <li>index : insert index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Model.Update</b> : (source, item)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>source : this</li>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see ChangeListener
 * @see IsSerializable
 */
public class Model implements IsSerializable {

  /**
   * Fired when a child object is added to the model (value is 10).
   */
  public static final int Add = 10;

  /**
   * Fired when a child object is inserted to the model (value is 20).
   */
  public static final int Insert = 20;

  /**
   * Fired when a child object is removed from the model (value is 30).
   */
  public static final int Remove = 30;

  /**
   * Fired when the model has beed updated (value is 40).
   */
  public static final int Update = 40;

  /**
   * The model's parent.
   */
  protected Model parent;

  /**
   * The model's children.
   * 
   * @gwt.typeArgs <net.mygwt.ui.client.data.Model>
   */
  protected List children;

  /**
   * The model's properties.
   * 
   * @gwt.typeArgs <java.lang.String,com.google.gwt.user.client.rpc.IsSerializable>
   */
  protected Map properties;

  private transient List listeners;

  /**
   * Creates a new model instance.
   */
  public Model() {
    properties = new HashMap();
    children = new ArrayList();
  }

  /**
   * Creates a new model instance with the specified properties.
   * 
   * @param properties
   */
  public Model(Map properties) {
    this.properties = properties;
    children = new ArrayList();
  }

  /**
   * Adds a child to the model and fires an add event.
   * 
   * @param child the child to be added
   */
  public void add(Model child) {
    insert(child, getChildCount());
  }

  /**
   * Adds a listener to receive change events.
   * 
   * @param listener the listener to be added
   */
  public void addChangeListener(ChangeListener listener) {
    if (listeners == null) {
      listeners = new ArrayList();
    }
    listeners.add(listener);
  }

  /**
   * Returns a properties value.
   * 
   * @param name the property name
   * @return the value
   */
  public Object get(String name) {
    return properties.get(name);
  }

  /**
   * Returns a proprty value as a String by simplying calling toString on the
   * value. Subclasses should override to provide more specific behavior.
   * 
   * @param name the property name
   * @return the String value
   */
  public String getAsString(String name) {
    Object obj = get(name);
    if (obj instanceof Boolean) {
      if (((Boolean) obj).booleanValue()) {
        return "Yes";
      } else {
        return "No";
      }
    }
    return obj == null ? "" : obj.toString();
  }

  /**
   * Returns the child at the given index or <code>null</code> if the index is
   * out of range.
   * 
   * @param index the index to be retrieved
   * @return the model at the index
   */
  public Model getChild(int index) {
    if ((index < 0) || (index >= children.size())) return null;
    return (Model) children.get(index);
  }

  /**
   * Returns the number of children.
   * 
   * @return the number of children
   */
  public int getChildCount() {
    return children.size();
  }

  /**
   * Returns the model's children.
   * 
   * @return the children
   */
  public List getChildren() {
    return children;
  }

  /**
   * Returns the model's parent or <code>null</code> if no parent.
   * 
   * @return the parent
   */
  public Model getParent() {
    return parent;
  }

  /**
   * Returns an iterator for the model's property names.
   * 
   * @return a iterator
   */
  public Iterator getPropertyNames() {
    return properties.keySet().iterator();
  }

  /**
   * Inserts a child to the model and fires an insert event.
   * 
   * @param child the child to be inserted
   * @param index the location to insert the child
   */
  public void insert(Model child, int index) {
    adopt(child);
    children.add(index, child);
    if (index == getChildCount() - 1) {
      fireEvent(Add, child);
    } else {
      ChangeEvent evt = new ChangeEvent(Insert, this);
      evt.item = child;
      evt.index = index;
      notify(evt);
    }
  }

  /**
   * Removes the child at the given index.
   * 
   * @param index the child index
   */
  public void remove(int index) {
    if (index >= 0 && index < getChildCount()) {
      remove(getChild(index));
    }
  }

  /**
   * Removes the child from the model and fires a remove event.
   * 
   * @param child the child to be removed
   */
  public void remove(Model child) {
    child.parent = null;
    children.remove(child);
    fireEvent(Remove, child);
  }

  /**
   * Removes all the model's children.
   */
  public void removeAll() {
    for (int i = children.size() - 1; i >= 0; i--) {
      remove(getChild(i));
    }
  }

  /**
   * Removes a previously added change listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeChangeListener(ChangeListener listener) {
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

  /**
   * Sets the property and fires an update event.
   * 
   * @param name the property name
   * @param value the property value
   */
  public void set(String name, Object value) {
    properties.put(name, value);
    fireEvent(Update, this);
  }

  /**
   * Sets the model's children. All existing children are first removed.
   * 
   * @param children the children to be set
   */
  public void setChildren(List children) {
    removeAll();
    Model[] models = (Model[])children.toArray(new Model[children.size()]);
    for (int i = 0; i < models.length; i++) {
      add(models[i]);
    }
  }

  protected void fireEvent(int type) {
    notify(new ChangeEvent(type, this));
  }

  protected void fireEvent(int type, Model item) {
    notify(new ChangeEvent(type, this, item));
  }

  protected void notify(ChangeEvent evt) {
    if (listeners != null) {
      for (int i = 0; i < listeners.size(); i++) {
        ChangeListener listener = (ChangeListener) listeners.get(i);
        listener.modelChanged(evt);
      }
    }
    if (parent != null) {
      parent.notify(evt);
    }
  }

  private void adopt(Model child) {
    if (child.parent != null && child.parent != this) {
      child.parent.remove(child);
    }
    child.parent = this;
  }

}
