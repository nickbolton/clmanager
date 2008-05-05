/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.util.Markup;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A horizontal component with 5 regions - left, mid left, center, mid right,
 * and right. Provides support for a icon in the 2nd region, text in the 3rd,
 * and widgets in the 4th. The first and last regions are used for styling such
 * as the rounded corners of a tab item.
 */
public class Item extends Component implements HasText {

  private static final Map elementCache = new HashMap();

  private static native Element clone(Element e, boolean deep) /*-{
      return e.cloneNode(deep);
    }-*/;

  // allow subclasses to overwrite if this component should sinkEvents
  protected boolean sinkEvents = true;

  protected boolean overStyleEnabled = true;
  protected String baseStyle, iconStyle;
  protected Element midleftElem, midRightElem;
  protected IconButton iconBtn;
  protected List widgets;
  protected HorizontalPanel widgetPanel;
  protected String markup;
  protected Element centerElem, textElem;

  private String text;
  private boolean selected;
  private int spacing;

  /**
   * Creates a new item.
   */
  public Item() {

  }

  /**
   * Creates a new item.
   * 
   * @param style the style information
   * @param baseStyle the base style
   */
  public Item(int style, String baseStyle) {
    super(style);
    this.baseStyle = baseStyle;
  }

  /**
   * Creates a new item.
   * 
   * @param baseStyle the base style
   */
  public Item(String baseStyle) {
    this.baseStyle = baseStyle;
  }

  /**
   * Adds a widget to region 4.
   * 
   * @param widget the widget to add
   */
  public void addWidget(Widget widget) {
    if (widgets == null) {
      widgets = new ArrayList();
    }
    widgets.add(widget);
    if (rendered) {
      if (widgetPanel == null) {
        widgetPanel = new HorizontalPanel();
        DOM.appendChild(midRightElem, widgetPanel.getElement());
        if (isAttached()) {
          WidgetHelper.doAttach(widgetPanel);
        }
      }
      widgetPanel.add(widget);
    }
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns a widget from region 4.
   * 
   * @param index the index
   * @return the widget
   */
  public Widget getWidget(int index) {
    if (widgets != null) {
      widgets.remove(index);
      if (rendered) {
        return widgetPanel.getWidget(index);
      }
    }
    return null;
  }

  /**
   * Inserts a widget into region 4.
   * 
   * @param widget the widget to insert
   * @param index the insert index
   */
  public void insertWidget(Widget widget, int index) {
    if (widgets == null) {
      widgets = new ArrayList();
    }
    widgets.add(index, widget);
    if (rendered) {
      if (widgetPanel == null) {
        widgetPanel = new HorizontalPanel();
        DOM.appendChild(midRightElem, widgetPanel.getElement());
        if (isAttached()) {
          WidgetHelper.doAttach(widgetPanel);
        }
      }
      widgetPanel.insert(widget, index);
    }
  }

  public void onBaseEvent(BaseEvent be) {
    Element source = be.widget.getElement();
    switch (be.type) {
      case Event.ONMOUSEOVER:
        Element from = DOM.eventGetFromElement(be.event);
        if (!DOM.isOrHasChild(source, from)) {
          onMouseOver(be);
        }
        break;
      case Event.ONMOUSEOUT:
        Element to = DOM.eventGetToElement(be.event);
        if (!DOM.isOrHasChild(source, to)) {
          onMouseOut(be);
        }
        break;
      case Event.ONMOUSEDOWN:
        onMouseDown(be);
        break;
      case Event.ONMOUSEUP:
        onMouseUp(be);
        break;
      case Event.ONCLICK:
        onClick(be);
        break;
    }

  }

  /**
   * Removes a widget from region 4.
   * 
   * @param widget the widget to remove
   */
  public void removeWidget(Widget widget) {
    if (widgets != null) {
      widgets.remove(widget);
      if (rendered) {
        widgetPanel.remove(widget);
      }
    }
  }

  /**
   * A CSS style which sets a background image to be used as the icon.
   * 
   * @param iconStyle the CSS class name
   */
  public void setIconStyle(String iconStyle) {
    this.iconStyle = iconStyle;
    if (rendered) {
      if (iconBtn == null) {
        iconBtn = new IconButton(iconStyle);
        DOM.appendChild(midleftElem, iconBtn.getElement());
        iconBtn.removeStyleName("my-nodrag");
      }
      iconBtn.changeStyle(iconStyle);
    }
  }

  /**
   * Sets the item's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      MyDOM.setInnerHTML(textElem, text);
    }
  }

  /**
   * Sets the amount of spacing around the widgets in the "tool" area.
   * 
   * @param spacing the spacing in pixels
   */
  public void setWidgetSpacing(int spacing) {
    this.spacing = spacing;
    if (rendered) {
      widgetPanel.setSpacing(spacing);
    }
  }

  protected void doAttachChildren() {
    if (widgetPanel != null) {
      WidgetHelper.doAttach(widgetPanel);
    }
  }

  protected void doDetachChildren() {
    if (widgetPanel != null) {
      WidgetHelper.doDetach(widgetPanel);
    }
  }

  protected boolean isSelected() {
    return selected;
  }

  protected void onClick(final BaseEvent be) {
    be.stopEvent();
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onMouseOut(be);
        fireEvent(Events.MouseOut, be);
      }
    });
  }

  protected void onDisable() {
    super.onDisable();
    if (overStyleEnabled) {
      removeStyleName(baseStyle + "-over");
      removeStyleName(baseStyle + "-down");
    }
    if (iconBtn != null) {
      iconBtn.setEnabled(false);
    }
  }

  protected void onEnable() {
    super.onEnable();
    if (iconBtn != null) {
      iconBtn.setEnabled(true);
    }
  }

  protected void onMouseDown(BaseEvent be) {
    addStyleName(baseStyle + "-down");
  }

  protected void onMouseOut(BaseEvent be) {
    if (overStyleEnabled) {
      removeStyleName(baseStyle + "-over");
      removeStyleName(baseStyle + "-down");
    }
  }

  protected void onMouseOver(BaseEvent be) {
    if (overStyleEnabled) {
      addStyleName(baseStyle + "-over");
    }
  }

  protected void onMouseUp(BaseEvent be) {
    removeStyleName(baseStyle + "-down");
  }

  protected void onRender() {
    if (markup == null) {
      markup = Markup.ITEM;
    }

    String cacheKey = baseStyle + ":" + markup;
    Element element = (Element) elementCache.get(cacheKey);
    if (element == null) {
      element = MyDOM.create(Format.substitute(markup, baseStyle));
      elementCache.put(cacheKey, element);
    }
    setElement(clone(element, true));

    midleftElem = MyDOM.findChild(baseStyle + "-ml", getElement());
    centerElem = DOM.getNextSibling(midleftElem);
    textElem = DOM.getFirstChild(centerElem);
    midRightElem = DOM.getNextSibling(centerElem);

    if (text != null) {
      setText(text);
    }

    if (iconStyle != null) {
      setIconStyle(iconStyle);
    }

    if (widgets != null) {
      widgetPanel = new HorizontalPanel();
      for (int i = 0; i < widgets.size(); i++) {
        widgetPanel.add((Widget) widgets.get(i));
      }
      DOM.appendChild(midRightElem, widgetPanel.getElement());
    }

    if (spacing > 0) {
      setWidgetSpacing(spacing);
    }

    disableTextSelection(true);

    if (sinkEvents) {
      sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS);
    }

  }

  protected void setSelected(boolean select) {
    selected = select;
    if (selected) {
      removeStyleName(baseStyle + "-over");
      addStyleName(baseStyle + "-sel");
    } else {
      removeStyleName(baseStyle + "-sel");
    }
  }
}
