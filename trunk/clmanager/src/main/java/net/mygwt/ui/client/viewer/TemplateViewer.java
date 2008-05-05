/*******************************************************************************
 * Copyright (c) 2007 MyGWT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Darrell Meyer <darrell@mygwt.net> - initial API and implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.widget.Component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * A <code>Viewer</code> that renders its layout using HTML fragments provided
 * by the label provider.
 * 
 * @see TemplateLabelProvider
 */
public class TemplateViewer extends Viewer {

  private Component container;
  private Object selected;
  private Element selectedEl;
  private TemplateLabelProvider lp;
  private Element containerEl;
  private Map idToElementMap;
  private Map elementToIdMap;
  private boolean selectOnOver;
  private boolean focusOnSelect = true;

  /**
   * Creates a new template viewer instance.
   */
  public TemplateViewer() {
    idToElementMap = new HashMap();
    elementToIdMap = new HashMap();
  }

  public Widget findItem(Object elem) {
    return null;
  }

  /**
   * Returns <code>true</code> if focus on select is enabled.
   * 
   * @return the focus on selec state
   */
  public boolean getFocusOnSelect() {
    return focusOnSelect;
  }

  /**
   * Returns <code>true</code> if elements are selected when the cursor is
   * over their location.
   * 
   * @return the selecte on over state
   */
  public boolean getSelectOnOver() {
    return selectOnOver;
  }

  public Widget getWidget() {
    return container;
  }

  /**
   * Moves the current selection down.
   */
  public void moveSelectionDown() {
    if (selectedEl != null) {
      int index = DOM.getChildIndex(containerEl, selectedEl);
      if (index < elements.length - 1) {
        Element el = DOM.getChild(containerEl, index + 1);
        Object elem = findModel(el);
        selectItem(elem, el);
      }
    } else if (elements.length > 0) {
      setSelection(new DefaultSelection(elements[0]));
    }
  }

  /**
   * Moves the current selection up.
   */
  public void moveSelectionUp() {
    if (selectedEl != null) {
      int index = DOM.getChildIndex(containerEl, selectedEl);
      if (index > 0) {
        Element el = DOM.getChild(containerEl, index - 1);
        Object elem = findModel(el);
        selectItem(elem, el);
      }
    }
  }

  public void remove(Object elem) {
    String id = (String) elementToIdMap.get(elem);
    if (id != null) {
      Element el = DOM.getElementById(id);
      DOM.removeChild(containerEl, el);
      unregister(id, elem);
    }
  }

  /**
   * Sets whether the component should receive focus when selected. Default
   * value is <code>true</code>.
   * 
   * @param focusOnSelect <code>true</code> to focus on select
   */
  public void setFocusOnSelect(boolean focusOnSelect) {
    this.focusOnSelect = focusOnSelect;
  }

  public void setLabelProvider(IBaseLabelProvider labelProvider) {
    super.setLabelProvider(labelProvider);
    lp = (TemplateLabelProvider) labelProvider;
  }

  public void setSelection(ISelection selection, boolean reveal) {
    Object elem = selection.getFirstElement();
    if (elem != null) {
      String id = (String) elementToIdMap.get(elem);
      Element el = DOM.getElementById(id);
      selectItem(elem, el);
    } else {
      selectItem(null, null);
    }

  }

  /**
   * Sets whether elements are selected when the cursor is over their location.
   * Default value is <code>fals</code>.
   * 
   * @param selectOnOver the select on over state
   */
  public void setSelectOnOver(boolean selectOnOver) {
    this.selectOnOver = selectOnOver;
  }

  public void update() {

  }

  public void update(Object elem) {

  }

  protected void add(Object elem) {
    renderItem(elem, elements.length);
  }

  protected Component createWidget() {
    Component c = new Component() {
      {
        setBorders(true);
      }

      public void onBaseEvent(BaseEvent be) {
        Element el = findItemElement(be.getTarget());
        Object elem = findModel(el);
        switch (be.type) {
          case Events.MouseOver:
            if (elem != null) {
              onMouseOver(elem, el, be);
            }
            break;
          case Events.MouseOut:
            if (elem != null) {
              onMouseOut(elem, el, be);
            }
            break;
          case Events.Click:
            if (elem != null) {
              onClick(elem, el, be);
            }
            break;
          case Events.KeyUp:
            onKeyPress(be);

            break;
        }
        be.stopEvent();
      }

      protected void onRender() {
        setElement(MyDOM.create(lp.render()));
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
      }
    };
    return c;
  }

  protected Element findItemElement(Element elem) {
    return MyDOM.findParentWithId(elem, 5);
  }

  protected Object findModel(Element item) {
    String id = MyDOM.getId(item);
    return idToElementMap.get(id);
  }

  protected List getSelectedFromWidget() {
    ArrayList list = new ArrayList();
    if (selected != null) {
      list.add(selected);
    }
    return list;
  }

  protected void insert(Object elem, int index) {
    renderItem(elem, index);
  }

  protected void onClick(Object elem, Element el, BaseEvent be) {
    selectItem(elem, el);
  }

  protected void onHightlight(Object elem, Element el, boolean highlight) {
    MyDOM.setStyleName(el, lp.getOverStyle(), highlight);
  }

  protected void onInputReceived(Object input) {
    render();
  }

  protected void onKeyPress(BaseEvent be) {
    switch (be.getKeyCode()) {
      case KeyboardListener.KEY_UP: {
        moveSelectionUp();
      }
      case KeyboardListener.KEY_DOWN: {
        moveSelectionDown();
      }
    }
  }

  protected void onMouseOut(Object elem, Element el, BaseEvent be) {
    onHightlight(elem, el, false);
  }

  protected void onMouseOver(Object elem, Element el, BaseEvent be) {
    if (selectOnOver) {
      selectItem(elem, el);
    } else {
      onHightlight(elem, el, true);
    }
  }

  protected void onSelect(Object elem, Element el, boolean select) {
    MyDOM.setStyleName(el, lp.getSelectStyle(), select);
  }

  private void register(String id, Object elem) {
    idToElementMap.put(id, elem);
    elementToIdMap.put(elem, id);
  }

  private void render() {
    if (container == null) {
      container = createWidget();
      selected = null;
    }
    if (containerEl != null) {
      MyDOM.setInnerHTML(containerEl, "");
    }

    containerEl = MyDOM.findChild("my-container", container.getElement());
    if (containerEl == null) {
      containerEl = container.getElement();
    }
    TemplateLabelProvider lp = (TemplateLabelProvider) getLabelProvider();

    Object[] elems = filter(elements);
    elems = sortElements(elems);

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < elems.length; i++) {
      sb.append(lp.renderElement(elems[i]));
    }
    MyDOM.setInnerHTML(containerEl, sb.toString());

    int cout = DOM.getChildCount(containerEl);
    Element[] items = new Element[cout];
    for (int i = 0; i < cout; i++) {
      items[i] = DOM.getChild(containerEl, i);
      String id = MyDOM.getId(items[i]);
      register(id, elems[i]);
    }

  }

  private void renderItem(Object elem, int position) {
    TemplateLabelProvider lp = (TemplateLabelProvider) getLabelProvider();
    Element el = MyDOM.create(lp.renderElement(elem));
    DOM.insertChild(containerEl, el, position);
    register(MyDOM.getId(el), elem);
  }

  private void selectItem(Object elem, Element el) {
    if (selected != null && selected == elem) return;
    if (selected != null) {
      onSelect(selected, selectedEl, false);
    }
    if (elem != null) {
      onSelect(elem, el, true);
      System.out.println("dddd");
      MyDOM.scrollIntoView(el, containerEl, false);
      if (focusOnSelect) {
        MyDOM.setFocus(containerEl, true);
      }
    }
    this.selected = elem;
    this.selectedEl = el;

    SelectionChangedEvent se = new SelectionChangedEvent(this, getSelection());
    fireSelectionChanged(se);
  }

  private void unregister(String id, Object elem) {
    idToElementMap.remove(id);
    elementToIdMap.remove(elem);
  }

}
