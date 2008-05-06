/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.ShellListener;
import net.mygwt.ui.client.event.TypedListener;
import net.mygwt.ui.client.fx.Draggable;
import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.fx.Resizable;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.util.Markup;
import net.mygwt.ui.client.util.Rectangle;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A <code>Shell</code> is a window with a header and content area. If the
 * shell locations is not set either by calling {@link #setPagePosition} or
 * #setBounds it will be centered when opened.
 * 
 * <p>
 * Shells must have a pixel width. If a width is not set the default width will
 * be used. Default width is 250. If a height is not specified it will be
 * determined by the size of the content.
 * </p>
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CLOSE, RESIZE, MODAL, AUTO_HIDE</dd>
 * <p>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Activate</b> : (widget)<br>
 * <div>Fires after a shell is activated (receives focus).</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Deactivate</b> : (widget)<br>
 * <div>Fires after a shell is deactivated.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : (widget)<br>
 * <div>Fires before a shell is closed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the operation.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : (widget)<br>
 * <div>Fires after a shell is closed.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeShow</b> : (widget)<br>
 * <div>Fires before the shell is opened. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the operation.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : (widget)<br>
 * <div>Fires after the shell is opened.</div>
 * <ul>
 * <li>widget : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyPress</b> : (widget, event)<br>
 * <div>Fires after a key is pressed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>event : event</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS Styles:</b></dt>
 * <dd>.my-shell { the shell itself }</dd>
 * <dd>.my-shell-hdr { header }</dd>
 * <dd>.my-shell-hdr-text { header text }</dd>
 * <dd>.my-shell-body { body }</dd>
 * <dd>.my-shell-plain { add for plain content area }</dd>
 * </dl>
 */
public class Shell extends Component implements HasText {

  protected WidgetContainer content;
  protected Element bodyWrapper, bodyWrapperInner, bodyElem, contentElem, footerElem;
  protected Shadow shadow;

  private int minimumWidth = 200;
  private int minimumHeight = 100;
  private boolean animate = false;
  private int shadowPosition = Style.SIDES;
  private int animateDuration = 300;
  private boolean blinkModal = true;
  private boolean keepLastBounds = true;
  private Item header;
  private ModalPanel modal;
  private ToolButton closeBtn;
  private Draggable draggable;
  private Resizable resizer;
  private boolean drag = true, resize;
  private boolean showing, resizing, created, autoHide;
  private Listener dragListener;
  private EventPreview eventPreview;
  private Rectangle lastBounds;
  private FramePanel framePanel;

  /**
   * Creates a new window that is initialy invisible. A new shell is created
   * with default style settings.
   * <p>
   * Default styles: CLOSE, RESIZE.
   * </p>
   */
  public Shell() {
    this(Style.CLOSE | Style.RESIZE);
  }

  /**
   * Creates a new shell instance.
   * 
   * @param style the style information
   */
  public Shell(int style) {
    this.style = style;
    baseStyle = "my-shell";
    header = new Item("my-shell-hdr") {
      protected void onClick(BaseEvent be) {
        super.onClick(be);
        activateShell(be.event);
      }
    };
    content = new WidgetContainer();
    content.setStyleAttribute("position", "relative");

    autoHide = (style & Style.AUTO_HIDE) != 0;
    resize = (style & Style.RESIZE) != 0;
  }

  /**
   * Adds a listener interface to receive shell events.
   * 
   * @param listener the listener to be added
   */
  public void addShellListener(ShellListener listener) {
    TypedListener typedListener = new TypedListener(listener);
    addListener(Events.Activate, typedListener);
    addListener(Events.Deactivate, typedListener);
    addListener(Events.Close, typedListener);
  }

  /**
   * Centers the shell.
   */
  public void center() {
    if (rendered) {
      MyDOM.center(getElement());
      onResize(getWidth(), getHeight());
    }
  }

  /**
   * Closes the shell.
   */
  public void close() {
    if (!showing) {
      return;
    }
    if (!fireEvent(Events.BeforeClose)) {
      return;
    }
    showing = false;

    lastBounds = getBounds();

    if (animate) {
      FXStyle fx = new FXStyle(getElement());
      fx.duration = animateDuration;
      fx.addListener(Events.EffectComplete, new Listener() {
        public void handleEvent(BaseEvent be) {
          afterHide();
        }
      });
      fx.fadeOut();
    } else {
      afterHide();
    }

    ShellManager.get().unregister(this);
  }

  /**
   * Returns <code>true</code> if animations are enabled.
   * 
   * @return the animate state
   */
  public boolean getAnimate() {
    return animate;
  }

  /**
   * Returns the animation duration.
   * 
   * @return the duration in milliseconds
   */
  public int getAnimateDuration() {
    return animateDuration;
  }

  /**
   * Returns <code>true</code> if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean getAutoHide() {
    return autoHide;
  }

  /**
   * Returns the blink modal state.
   * 
   * @return <code>true</code> if blinking is enabled
   */
  public boolean getBlinkModal() {
    return blinkModal;
  }

  /**
   * Returns the content panel of the shell.
   * 
   * @return the panel
   */
  public WidgetContainer getContent() {
    return content;
  }

  /**
   * Returns the shell's draggable instance. This method will cause the shell to
   * be rendered.
   * 
   * @return the draggable instance
   */
  public Draggable getDraggable() {
    if (!isRendered()) {
      getElement();
    }
    return draggable;
  }

  /**
   * Returns the shell's header.
   * 
   * @return the header
   */
  public Item getHeader() {
    return header;
  }

  /**
   * Returns <code>true</code> if the shell will remember its last postion.
   * 
   * @return the keep last bounds state
   */
  public boolean getKeepLastBounds() {
    return keepLastBounds;
  }

  /**
   * Returns the minimum height.
   * 
   * @return the minimu height
   */
  public int getMinimumHeight() {
    return minimumHeight;
  }

  /**
   * Returns the minimum width.
   * 
   * @return the minimum width
   */
  public int getMinimumWidth() {
    return minimumWidth;
  }

  /**
   * Returns the shadow position.
   * 
   * @return the shadow position
   */
  public int getShadowPosition() {
    return shadowPosition;
  }

  /**
   * Returns the shell's header text.
   * 
   * @return the text
   */
  public String getText() {
    return header.getText();
  }

  public void hide() {
    super.hide();
    if (shadow != null && !isVisible()) {
      shadow.hide();
    }
  }

  /**
   * Returns <code>true</code> if dragging is enabled.
   * 
   * @return the drag enabled state
   */
  public boolean isDragEnabled() {
    return drag;
  }

  public void onBrowserEvent(Event event) {
    if (DOM.eventGetType(event) == Event.ONCLICK) {
      activateShell(event);
    }
  }

  /**
   * Opens the shell.
   */
  public void open() {
    if (!rendered) {
      render();
    }

    if (showing) {
      return;
    }

    if (!fireEvent(Events.BeforeOpen)) {
      return;
    }

    // when removing from panel, position gets removed
    setStyleAttribute("position", "absolute");

    showing = true;

    if (!created) {
      createContent(getContent());
      created = true;
    }

    if (modal != null) {
      modal.show(this);
    } else {
      RootPanel.get().add(this);
    }

    int width = Math.max(minimumWidth, getOffsetWidth());

    if (width == minimumWidth) {
      setWidth(width);
    }

    if (resizer != null) {
      resizer.minHeight = minimumHeight;
      resizer.minWidth = minimumWidth;
    }

    if (keepLastBounds && lastBounds != null) {
      MyDOM.setLeftTop(getElement(), lastBounds.x, lastBounds.y);
      setSize(lastBounds.width, lastBounds.height);
      onResize(lastBounds.width, lastBounds.height);
    } else {
      int x = MyDOM.getLeft(getElement());
      int y = MyDOM.getTop(getElement());
      // not positioned then center
      if (x < 1 || y < 1) {
        MyDOM.center(getElement());
        y = MyDOM.getTop(getElement());
        if (y < 0) {
          setPagePosition(MyDOM.getLeft(getElement()), 4);
        }
      }
    }

    ShellManager.get().register(this);
    ShellManager.get().setActive(this);

    final Shell fShell = this;

    framePanel.onShow(getElement());
    int z = Math.max(100, DOM.getIntStyleAttribute(getElement(), "zIndex"));
    framePanel.setZIndex(z);

    if (animate) {
      FXStyle fx = new FXStyle(getElement());
      if (shadow != null) {
        fx.addListener(Events.EffectComplete, new Listener() {
          public void handleEvent(BaseEvent be) {
            shadow.show(fShell);
            afterShow();
          }
        });
      }
      fx.duration = animateDuration;
      fx.fadeIn();
    } else {
      if (shadow != null) {
        shadow.setVisible(true);
        shadow.show(this);
      }
      afterShow();
    }

  }

  /**
   * Removes a previously added listener interface.
   * 
   * @param listener the listener interface to remove
   */
  public void removeShellListener(ShellListener listener) {
    unhook(Events.Activate, listener);
    unhook(Events.Deactivate, listener);
    unhook(Events.Close, listener);
  }

  /**
   * Specifies if the the open and closing of the shell should be animated.
   * Default value is <code>false</code>.
   * 
   * @param animate <code>true</code> to enable animations
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * Specifies the length of the fade effect. Default value is 300;
   * 
   * @param animateDuration the duration in milliseconds
   */
  public void setAnimateDuration(int animateDuration) {
    this.animateDuration = animateDuration;
  }

  /**
   * Sets the auto hide state. Has no effect if called after the
   * 
   * @param autoHide <code>true</code> to auto hide
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * Specifies if the shell should blink when clicks occur outside it's
   * boundaries. When <code>true</code>, close() should not be called within
   * any button listeners. Default value is <code>true</code>.
   * 
   * @param blinkModal <code>true</code> to enable blinking
   */
  public void setBlinkModal(boolean blinkModal) {
    this.blinkModal = blinkModal;
  }

  /**
   * Enables and disables shell dragging.
   * 
   * @param enabled the drag enabled state
   */
  public void setDragEnabled(boolean enabled) {
    this.drag = enabled;
    if (draggable != null) {
      draggable.setEnabled(enabled);
      String c = enabled ? "move" : "default";
      header.setStyleAttribute("cursor", c);
    }
  }

  /**
   * Sets the shell's header icon.
   * 
   * @param style the icon style
   */
  public void setIconStyle(String style) {
    header.setIconStyle(style);
  }

  /**
   * Specifies if the shell should be returned to the same size and location
   * when it was last closed. Default value is <code>true</code>.
   * 
   * @param keepLastBounds <code>true</code> to enable
   */
  public void setKeepLastBounds(boolean keepLastBounds) {
    this.keepLastBounds = keepLastBounds;
  }

  /**
   * Sets the shell's location based on page coordinates.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void setPagePosition(int x, int y) {
    // will cause render
    MyDOM.setLeftTop(getElement(), x, y);
    if (shadow != null) {
      shadow.sync(getBounds());
    }
    if (framePanel != null) {
      framePanel.sync(getElement());
    }
  }

  /**
   * Specifies the minimum height. Default value is 100;
   * 
   * @param minimumHeight the minimum height
   */
  public void setMinimumHeight(int minimumHeight) {
    this.minimumHeight = minimumHeight;
  }

  /**
   * Sets the shell's minimum size to the size specified by the arguments.
   * 
   * @param width the minimum width
   * @param height the minimum height
   */
  public void setMinimumSize(int width, int height) {
    minimumWidth = width;
    minimumHeight = height;
  }

  /**
   * Specifies the minimum width. Default value is 200.
   * 
   * @param minimumWidth the minium width
   */
  public void setMinimumWidth(int minimumWidth) {
    this.minimumWidth = minimumWidth;
  }

  /**
   * Enables and disables shell resizing. Only applies to shell's with a RESIZE
   * style. Does nothing if called before shell is rendered.
   * 
   * @param enabled the resize enabled state
   */
  public void setResizeEnabled(boolean enabled) {
    this.resize = enabled;
    if (resizer != null) {
      resizer.setEnabled(enabled);
    }
  }

  /**
   * Specifies the shadow position. Valid values are NONE, DROP, SIDES, FRAME.
   * Default values is SIDES.
   * 
   * @param shadowPosition the shadow position
   */
  public void setShadowPosition(int shadowPosition) {
    this.shadowPosition = shadowPosition;
  }

  /**
   * Sets the shell's style. Has no effect if called after the shell has been
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
   * Sets the shell's header text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    header.setText(text);
  }

  /**
   * Sets a url for the content area of the shell.
   * 
   * @param url the url
   * @return the frame widget
   */
  public Frame setURL(String url) {
    Frame f = new Frame(url);
    MyDOM.setStyleAttribute(f.getElement(), "frameborder", "0");
    f.setSize("100%", "100%");
    getContent().removeAll();
    getContent().add(f);
    return f;
  }

  public void show() {
    super.show();
    if (shadow != null && isVisible()) {
      shadow.show();
    }
  }

  protected void activateShell(Event event) {
    // ignore close button
    if (closeBtn != null) {
      if (DOM.isOrHasChild(closeBtn.getElement(), DOM.eventGetTarget(event))) {
        return;
      }
    }
    ShellManager.get().setActive(this);
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    framePanel.onHide(getElement());
    resizing = false;

    if (shadow != null) {
      shadow.remove();
    }

    if (modal != null) {
      modal.hide();
    }

    if (eventPreview != null) {
      DOM.removeEventPreview(eventPreview);
    }

    fireEvent(Events.Close);
  }

  protected void afterShow() {
    if (eventPreview != null) {
      DOM.addEventPreview(eventPreview);
    }
    if (resizer != null) {
      setBounds(getBounds());
    }
    content.setStyleAttribute("overflow", "auto");
    fireEvent(Events.Open);
  }

  /**
   * Subclasses should override and add content to container.
   * 
   * @param container the content container
   */
  protected void createContent(WidgetContainer container) {

  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(header);
    WidgetHelper.doAttach(content);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(header);
    WidgetHelper.doDetach(content);
  }

  protected void onKeyPress(Event event) {
    int keyCode = DOM.eventGetKeyCode(event);
    if (keyCode == KeyboardListener.KEY_ESCAPE) {
      close();
    }
  }

  protected void onRender() {
    super.onRender();
    setElement(DOM.createDiv());
    setStyleName(baseStyle);
    MyDOM.setStyleAttribute(getElement(), "position", "absolute");

    if (!header.isRendered()) {
      header.baseStyle = baseStyle + "-hdr";
    }
    DOM.appendChild(getElement(), header.getElement());

    String html = Format.substitute(Markup.BOTTOM_BOX, baseStyle + "-body");
    bodyWrapper = MyDOM.create("<div>" + html + "</div>");
    bodyWrapperInner = DOM.getFirstChild(bodyWrapper);
    bodyElem = DOM.getFirstChild(bodyWrapperInner);
    contentElem = MyDOM.findChild(baseStyle + "-body-mc", bodyElem);
    footerElem = MyDOM.findChild(baseStyle + "-body-bc", bodyElem);
    DOM.appendChild(getElement(), bodyWrapper);

    DOM.appendChild(contentElem, content.getElement());

    // close button
    if ((style & Style.CLOSE) != 0) {
      closeBtn = new ToolButton("my-tool-close");
      closeBtn.addListener(Events.Click, new Listener() {
        public void handleEvent(BaseEvent be) {
          close();
        }
      });
      header.addWidget(closeBtn);
    }

    eventPreview = new EventPreview() {
      public boolean onEventPreview(Event event) {
        if (autoHide) {
          Element target = DOM.eventGetTarget(event);
          if (!DOM.isOrHasChild(getElement(), target)) {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
              if (resizing) {
                // ignore
                resizing = false;
                return false;
              }
              close();
              return false;
            }
          }
        }
        int type = DOM.eventGetType(event);
        if (type == Event.ONKEYPRESS) {
          onKeyPress(event);
        }
        if (modal != null && modal.isVisible()) {
          modal.onEventPreview(event);
        }
        return true;
      }
    };

    // resizable
    if (resize) {
      // deferred command needed for safari
      final Shell fShell = this;
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          resizer = new Resizable(fShell);
          resizer.minWidth = minimumWidth;
          resizer.minHeight = minimumHeight;
          resizer.addListener(Events.ResizeStart, new Listener() {
            public void handleEvent(BaseEvent be) {
              resizing = true;
            }
          });
        }
      });
    } else {
      setResizeEnabled(false);
    }

    if ((style & Style.MODAL) != 0) {
      modal = new ModalPanel();
      modal.setBlinkEnabled(blinkModal);
    }

    framePanel = FramePanel.pop();

    dragListener = new Listener() {
      public void handleEvent(BaseEvent be) {
        switch (be.type) {
          case Events.DragStart:
            MyDOM.addStyleName(bodyWrapper, baseStyle + "-body-wrapper");
            MyDOM.addStyleName(bodyWrapperInner, baseStyle + "-body-wrapper-inner");
            MyDOM.setVisible(bodyElem, false);
            if (shadow != null) {
              shadow.setVisible(false);
            }
            break;
          case Events.DragMove:
            framePanel.sync(getElement());
            break;
          case Events.DragEnd:
            MyDOM.removeStyleName(bodyWrapper, baseStyle + "-body-wrapper");
            MyDOM.removeStyleName(bodyWrapperInner, baseStyle + "-body-wrapper-inner");
            MyDOM.setVisible(bodyElem, true);

            int z = Math.max(100, DOM.getIntStyleAttribute(getElement(), "zIndex"));
            framePanel.setZIndex(z);

            if (shadow != null) {
              shadow.setVisible(true);
              shadow.sync(getBounds());
            }
            // split bars are added to rootpanel and will not get updated when
            // shell is moved.
            SplitBar.updateHandles();

            framePanel.sync(getElement());
            break;
        }
      }

    };

    draggable = new Draggable(this, header);
    draggable.useProxy = false;
    draggable.addListener(Events.DragStart, dragListener);
    draggable.addListener(Events.DragMove, dragListener);
    draggable.addListener(Events.DragEnd, dragListener);

    if (!drag) {
      setDragEnabled(false);
    }

    if (shadowPosition != Style.NONE) {
      shadow = new Shadow(shadowPosition);
    }

    if (attachBounds.width == Style.DEFAULT) {
      setWidth(250);
    }

    sinkEvents(Event.ONCLICK | Event.KEYEVENTS | Event.MOUSEEVENTS);
  }

  protected void onResize(int width, int height) {
    int h = height;
    int w = width;

    if (h == Style.DEFAULT) {
      h = getOffsetHeight();
    }

    if (getOffsetHeight() < minimumHeight) {
      MyDOM.setHeight(getElement(), minimumHeight);
      h = minimumHeight;
    }

    w -= 12;
    h -= header.getHeight();
    MyDOM.setHeight(bodyWrapper, h);
    MyDOM.setHeight(bodyWrapperInner, h);

    h -= MyDOM.getHeight(footerElem);

    // adjust for borders of center content
    w -= MyDOM.getBorderWidth(contentElem, Style.LEFT | Style.RIGHT);
    h -= MyDOM.getBorderWidth(contentElem, Style.TOP | Style.BOTTOM);

    if (width != Style.DEFAULT) {
      MyDOM.setWidth(content.getElement(), w);
    }

    if (h > 10) {
      MyDOM.setHeight(content.getElement(), h);
    }

    content.layout(true);

    if (shadow != null) {
      shadow.sync(getBounds());
    }

    int ow = getOffsetWidth();
    ow = Math.max(ow, MyDOM.getWidth(bodyElem));

    if (ow > width) {
      setWidth(ow);
      return;
    }
    
    if (framePanel != null) {
      framePanel.sync(getElement());
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        SplitBar.updateHandles();
      }
    });
  }
}
