/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.util.Markup;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A <code>Container</code> with an optional header that can be expanded and
 * collapsed.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>COLLAPSE, HEADER, CLOSE</dd>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeExpand</b> : (widget)<br>
 * <div>Fires before the panel is expanded. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : (widget)<br>
 * <div>Fires after the panel is expanded</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : (widget)<br>
 * <div>Fires before the panel is collpased. Listeners can set the
 * <code>doit</code> field <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : (widget)<br>
 * <div>Fires after the panel is collapsed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : (widget)<br>
 * <div>Fires before a content panel is closed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the operation.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget)<br>
 * <div>Fires after a content panel is closed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-cpanel ( the panel panel)</dd>
 * <dd>.my-cpanel .my-cpanel-hdr-text (the header text)</dd>
 * <dd>.my-cpanel-small ( smaller header section )</dd>
 * <dd>.my-cpanel-small .my-cpanel-hdr-text (the header text)</dd>
 * </dl>
 */
public class ContentPanel extends WidgetContainer implements SourcesMouseEvents {

  protected Item header;
  protected SimplePanel body;

  private boolean animateCollapse = true;
  private boolean titleCollapse;
  private String text, iconStyle;
  private boolean expanded = true;
  private boolean animating;
  private ToolButton collapseBtn;
  private MouseListenerCollection mouseListeners;
  private int padding;
  private String expandHeight;
  private boolean frame, collapse;
  private ToolBar toolBar;

  /**
   * Creates a new content panel with a header.
   */
  public ContentPanel() {
    this(Style.HEADER);
  }

  /**
   * Creates a new content panel.
   * 
   * @param style the style information
   */
  public ContentPanel(int style) {
    this(style, "my-cpanel");
  }

  /**
   * Creates a new content panel.
   * 
   * @param style the style information
   * @param baseStyle an alternate base style.
   */
  public ContentPanel(int style, String baseStyle) {
    this.style = style;
    this.baseStyle = baseStyle;

    if ((style & Style.COLLAPSE) != 0) {
      collapse = true;
    }
    header = new Item() {
      protected void onClick(BaseEvent be) {
        super.onClick(be);
        if (collapse && titleCollapse) {
          setExpanded(!isExpanded());
        }
      }
    };
  }

  /**
   * Adds a listener interface to receive mouse events.
   * 
   * @param listener the listener interface to add
   */
  public void addMouseListener(MouseListener listener) {
    if (mouseListeners == null) {
      mouseListeners = new MouseListenerCollection();
    }
    mouseListeners.add(listener);
  }

  /**
   * Returns <code>true</code> if animations are enabled.
   * 
   * @return the animation state
   */
  public boolean getAnimateCollapse() {
    return animateCollapse;
  }

  /**
   * Returns <code>true</code> if the panel can be collapsed.
   * 
   * @return the collapse state
   */
  public boolean getCollapse() {
    return collapse;
  }

  /**
   * @return <code>true</code> if the panel is framed.
   */
  public boolean getFrame() {
    return frame;
  }

  /**
   * Returns the panel's header widget.
   * 
   * @return the header
   */
  public Item getHeader() {
    return header;
  }

  public Element getLayoutTarget() {
    return body.getElement();
  }

  /**
   * Returns the panel's title text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns <code>true</code> if clicks on the header will expand and
   * collapse the panel.
   * 
   * @return the title collpase state
   */
  public boolean getTitleCollapse() {
    return titleCollapse;
  }

  /**
   * Returns the panel's tool bar.
   * 
   * @return the tool bar or <code>null</code>
   */
  public ToolBar getToolBar() {
    return toolBar;
  }

  /**
   * Returns <code>true</code> if the panel is expanded.
   * 
   * @return the expand state
   */
  public boolean isExpanded() {
    return expanded;
  }

  public void onBaseEvent(BaseEvent be) {
    super.onBaseEvent(be);
    switch (be.type) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT: {
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, be.event);
        }
        break;
      }
    }
  }

  /**
   * Removes a previously added listener interface.
   * 
   * @param listener the listener interface to remove
   */
  public void removeMouseListener(MouseListener listener) {
    if (mouseListeners != null) {
      mouseListeners.remove(listener);
    }
  }

  /**
   * Sets the animation state. When <code>true</code>, the expanding and
   * collpasing of the panel will be animated. Default value is
   * <code>true</code>.
   * 
   * @param animateCollapse <code>true</code> if enabled
   */
  public void setAnimateCollapse(boolean animateCollapse) {
    this.animateCollapse = animateCollapse;
  }

  /**
   * Sets whether the panel can be collapsed. Default value is <code>true</code>.
   * Has no effect if called after the panel is rendered.
   * 
   * @param collapse <code>true</code> to enable collapsing
   */
  public void setCollapse(boolean collapse) {
    this.collapse = collapse;
  }

  /**
   * Sets the panel's expand state.
   * 
   * @param expanded <code>true<code> true to expand
   */
  public void setExpanded(boolean expanded) {
    if (animating) {
      return;
    }
    this.expanded = expanded;
    if (rendered) {
      if (expanded && fireEvent(Events.BeforeExpand)) {
        onExpand();
      } else if (fireEvent(Events.BeforeCollapse)) {
        onCollapse();
      }
    }

  }

  /**
   * Sets whether the panel is framed. Default value is <code>false</code>.
   * Has no effect if called after the panel is rendered.
   * 
   * @param frame the frame state
   */
  public void setFrame(boolean frame) {
    this.frame = frame;
  }

  /**
   * Sets the panel's icon style.
   * 
   * @param style the icon style
   */
  public void setIconStyle(String style) {
    this.iconStyle = style;
    if (rendered && header != null) {
      header.setIconStyle(style);
    }
  }

  /**
   * Sets the padding of the content element.
   * 
   * @param padding the new padding
   */
  public void setPadding(int padding) {
    this.padding = padding;
    if (rendered) {
      DOM.setIntStyleAttribute(body.getElement(), "padding", padding);
    }
  }

  /**
   * Sets the panel's style. Has no effect if called after the panel is
   * rendered.
   * 
   * @param style the style
   */
  public void setStyle(int style) {
    if (!isRendered()) {
      this.style = style;
    }
  }

  /**
   * Sets the header's text. Does nothing if the header is not visible.
   * 
   * @param text the new header text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered && header != null) {
      header.setText(text);
    }
  }

  /**
   * Sets the title collapse state. When <code>true</code>, the panel will be
   * expanded and collapsed when clicking on the header. Defualt value is
   * <code>false</code>.
   * 
   * @param enabled <code>true</code> to enable title collapsing
   */
  public void setTitleCollapse(boolean enabled) {
    this.titleCollapse = enabled;
  }

  /**
   * Sets the panel's tool bar.
   * 
   * @param toolBar the toolbar
   */
  public void setToolBar(ToolBar toolBar) {
    this.toolBar = toolBar;
    if (rendered) {
      MyDOM.insertBefore(toolBar.getElement(), body.getElement());
      if (isAttached()) {
        WidgetHelper.doAttach(toolBar);
      }
      onResize(getOffsetWidth(), getOffsetHeight());
    }
  }

  protected void afterCollapse() {
    setHeight(header.getOffsetHeight());
    expanded = false;
    animating = false;
    fireEvent(Events.Collapse);
    fireEvent(Events.Resize);
  }

  protected void afterExpand() {
    expanded = true;
    animating = false;
    layout(true);
    fireEvent(Events.Expand);
    fireEvent(Events.Resize);
  }

  protected void afterRender() {
    super.afterRender();

    if (padding != 0) {
      setPadding(padding);
    }

    if (collapse && !expanded) {
      setExpanded(expanded);
    }
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    if (header != null) WidgetHelper.doAttach(header);
    if (toolBar != null) WidgetHelper.doAttach(toolBar);
    WidgetHelper.doAttach(body);
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    if (header != null) WidgetHelper.doDetach(header);
    if (toolBar != null) WidgetHelper.doDetach(toolBar);
    WidgetHelper.doDetach(body);
  }

  protected void onCollapse() {
    expandHeight = DOM.getStyleAttribute(getElement(), "height");
    collapseBtn.changeStyle("my-tool-down");
    if (animateCollapse && !animating) {
      animating = true;
      FXStyle fx = new FXStyle(body.getElement());
      fx.duration = 300;
      fx.addListener(Events.EffectComplete, new Listener() {
        public void handleEvent(BaseEvent be) {
          afterCollapse();
        }
      });
      fx.slideOut(Style.NORTH);
    } else {
      body.setVisible(false);
      afterCollapse();
    }
  }

  protected void onExpand() {
    setHeight(expandHeight);
    collapseBtn.changeStyle("my-tool-up");
    if (animateCollapse && !animating) {
      animating = true;
      FXStyle fx = new FXStyle(body.getElement());
      fx.duration = 300;
      fx.addListener(Events.EffectComplete, new Listener() {
        public void handleEvent(BaseEvent be) {
          afterExpand();
        }
      });
      fx.slideIn(Style.SOUTH);
    } else {
      body.setVisible(true);
      afterExpand();
    }
  }

  protected void onRender() {
    setElement(DOM.createDiv());
    setStyleName(baseStyle);
    header.baseStyle = baseStyle + "-hdr";

    MyDOM.setVisibility(getElement(), false);

    if ((style & Style.HEADER) != 0) {
      DOM.appendChild(getElement(), header.getElement());
      header.setWidth("100%");
      addStyleName(baseStyle + "-showheader");

      if (text != null) {
        header.setText(text);
      }
      if (iconStyle != null) {
        header.setIconStyle(iconStyle);
      }

      if (collapse) {
        collapseBtn = new ToolButton("my-tool-up");
        collapseBtn.addListener(Events.Click, new Listener() {
          public void handleEvent(BaseEvent be) {
            be.stopEvent();
            setExpanded(!isExpanded());
          }
        });

        collapseBtn.render();
        collapseBtn.setSize(15, 15);

        header.addWidget(collapseBtn);

      }

      if ((style & Style.CLOSE) != 0) {
        ToolButton closeBtn = new ToolButton("my-tool-close");
        closeBtn.addSelectionListener(new SelectionListener() {
          public void widgetSelected(BaseEvent be) {
            if (fireEvent(Events.BeforeClose)) {
              removeFromParent();
              fireEvent(Events.Close);
            }
          }
        });
        header.addWidget(closeBtn);
      }

    }

    body = new SimplePanel();
    body.setStyleName(baseStyle + "-body");

    if (frame) {
      addStyleName(baseStyle + "-frame");
      String html = Format.substitute(Markup.BOTTOM_BOX, baseStyle + "-box");
      DOM.appendChild(getElement(), MyDOM.create(html));
      Element center = MyDOM.findChild(baseStyle + "-box-mc", getElement());
      DOM.appendChild(center, body.getElement());
    } else {
      DOM.appendChild(getElement(), body.getElement());
    }

    if (header != null) {
      body.addStyleName(baseStyle + "-body-header");
    }

    if (toolBar != null) {
      MyDOM.insertBefore(toolBar.getElement(), body.getElement());
    }

    if (!expanded) {
      addListener(Events.Collapse, new Listener() {
        public void handleEvent(BaseEvent be) {
          removeListener(Events.Collapse, this);
          MyDOM.setVisibility(getElement(), true);
        }
      });
      setExpanded(false);
    } else {
      MyDOM.setVisibility(getElement(), true);
    }
  }

  protected void onResize(int width, int height) {
    if (height != Style.DEFAULT) {
      if (header != null) {
        height -= header.getHeight();
      }
      if (frame) {
        height -= 12;
      }
      if (toolBar != null) {
        height -= toolBar.getHeight();
      }
      MyDOM.setHeight(body.getElement(), height, true);
    }
    if (width != Style.DEFAULT) {
      if (frame) {
        width -= 12;
      }
      MyDOM.setWidth(body.getElement(), width, true);
    }
    layout();
  }

}
