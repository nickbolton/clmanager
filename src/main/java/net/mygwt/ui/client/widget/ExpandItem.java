/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Darrell Meyer <darrell@mygwt.net> - derived implementation
 *******************************************************************************/
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A item in a <code>ExpandBar</code>.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeExpand</b> : (widget)<br>
 * <div>Fires before an item is expanded. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget)<br>
 * <div>Fires before an item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget)<br>
 * <div>Fires after an item has been expanded.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-expand-item (the item itself)</dd>
 * <dd>.my-expand-item-hdr (the item header)</dd>
 * <dd>.my-expand-item-hdr-text (the header text)</dd>
 * </dl>
 */
public class ExpandItem extends Component {

  protected WidgetContainer content;
  protected IconButton collapseBtn;

  boolean expanded;
  ExpandBar parent;
  Item header;
  private boolean expandOnAttach;

  /**
   * Creates a new expand item.
   */
  public ExpandItem() {
    baseStyle = "my-expand-item";
    header = new Item() {
      protected void onClick(BaseEvent be) {
        super.onClick(be);
        if (parent.headerCollapse) {
          setExpanded(!isExpanded());
        }
      }
    };
    content = new WidgetContainer();
    content.setStyleAttribute("position", "relative");
  }

  /**
   * Returns the item's content container.
   * 
   * @return the content container
   */
  public WidgetContainer getContainer() {
    return content;
  }

  /**
   * Returns the item's header.
   * 
   * @return the header
   */
  public Item getHeader() {
    return header;
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return header.getText();
  }

  /**
   * Returns <code>true</code> if the item is expanded, and <code>false</code>
   * otherwise.
   * 
   * @return the expanded state
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Sets the expanded state of the item.
   * 
   * @param expanded the new expanded state
   */
  public void setExpanded(boolean expanded) {
    if (!isAttached()) {
      if (expanded) {
        expandOnAttach = true;
      }
      return;
    }
    if (expanded) {
      if (parent.fireEvent(Events.BeforeExpand, parent, this)
          && fireEvent(Events.BeforeExpand)) {
        this.expanded = expanded;
        parent.expand(this);
      }
    } else {
      if (parent.fireEvent(Events.BeforeCollapse, parent, this)
          && fireEvent(Events.BeforeCollapse)) {
        this.expanded = expanded;
        parent.collapse(this);
      }
    }

  }

  public void setHeight(int height) {
    header.setHeight(height);
  }

  /**
   * Sets the item's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    header.setIconStyle(iconStyle);
  }

  /**
   * Sets the item's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    header.setText(text);
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(header);
    WidgetHelper.doAttach(content);

    content.layout();
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(header);
    WidgetHelper.doDetach(content);
  }

  protected void onLoad() {
    if (expandOnAttach) {
      expandOnAttach = false;
      Timer t = new Timer() {
        public void run() {
          setExpanded(true);
        }
      };
      t.schedule(200);
    }
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName(baseStyle);

    collapseBtn = new ToolButton("my-tool-plus");
    collapseBtn.addListener(Events.Click, new Listener() {
      public void handleEvent(BaseEvent be) {
        setExpanded(!isExpanded());
        be.stopEvent();
      }
    });

    header.baseStyle = baseStyle + "-hdr";
    header.addWidget(collapseBtn);

    DOM.appendChild(getElement(), header.getElement());
    DOM.appendChild(getElement(), content.getElement());

    content.setStyleName(baseStyle + "-body");
    content.setVisible(false);

    header.setWidth("100%");

  }

}
