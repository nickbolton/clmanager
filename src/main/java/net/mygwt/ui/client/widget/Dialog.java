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
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A <code>Shell</code> with a button bar.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CLOSE, RESIZE, MODAL</dd>
 * <dd>OK, OK_CANCEL, YES_NO, YES_NO_CANCEL</dd>
 * </dl>
 */
public class Dialog extends Shell {

  /**
   * Button id for a "Cancel" button (value 1).
   */
  public static final int CANCEL_ID = 1;

  /**
   * Button id for a "No" button (value 3).
   */
  public static final int NO_ID = 3;

  /**
   * Button id for an "Ok" button (value 0).
   */
  public static final int OK_ID = 0;

  /**
   * Button id for a "Yes" button (value 2).
   */
  public static final int YES_ID = 2;

  private boolean closeOnButtonClick = false;
  private HorizontalPanel footerPanel;
  private Item status;
  private ButtonBar buttonBar;
  private String statusText, statusIconStyle;

  /**
   * Creates a dialog that can be closed and resized.
   */
  public Dialog() {
    this(Style.CLOSE | Style.RESIZE);
  }

  /**
   * Creates a new dialog with the given style.
   * 
   * @param style the style information
   */
  public Dialog(int style) {
    super(style);

    buttonBar = new ButtonBar(Style.RIGHT);

    if ((style & Style.OK) != 0) {
      createButton(OK_ID, MyGWT.MESSAGES.ok());
    }
    if ((style & Style.OK_CANCEL) != 0) {
      createButton(OK_ID, MyGWT.MESSAGES.ok());
      createButton(CANCEL_ID, MyGWT.MESSAGES.cancel());
    }
    if ((style & Style.YES_NO) != 0) {
      createButton(YES_ID, MyGWT.MESSAGES.yes());
      createButton(NO_ID, MyGWT.MESSAGES.no());
    }
    if ((style & Style.YES_NO_CANCEL) != 0) {
      createButton(YES_ID, MyGWT.MESSAGES.yes());
      createButton(NO_ID, MyGWT.MESSAGES.no());
      createButton(CANCEL_ID, MyGWT.MESSAGES.cancel());
    }
  }

  /**
   * Adds a button to the dialog.
   * 
   * @param button the button to be added
   */
  public void addButton(Button button) {
    buttonBar.add(button);
  }

  /**
   * Adds a button to the dialog.
   * 
   * @param text the button text
   * @param listener the click listener
   * 
   * @return the new button
   */
  public Button addButton(String text, SelectionListener listener) {
    Button btn = new Button(text);
    btn.addSelectionListener(listener);
    addButton(btn);
    return btn;
  }

  /**
   * Clears the dialog's status area.
   */
  public void clearStatus() {
    status.setText("");
    status.setIconStyle("none");
  }

  /**
   * Returns the dialog's button bar.
   * 
   * @return the button bar
   */
  public ButtonBar getButtonBar() {
    return buttonBar;
  }

  /**
   * Returns the button with the specified button id.
   * 
   * @param buttonId the button id
   * @return the button or <code>null</code> if no match
   */
  public Button getButtonById(int buttonId) {
    return buttonBar.getButtonById(buttonId);
  }

  /**
   * Returns the last pressed button.
   * 
   * @return the button or <code>null</code> if no button pressed
   */
  public Button getButtonPressed() {
    return buttonBar.getButtonPressed();
  }

  /**
   * Returns <code>true</code> if the dialog will be closed on any button
   * click.
   * 
   * @return the close on click state
   */
  public boolean getCloseOnButtonClick() {
    return closeOnButtonClick;
  }

  /**
   * Removes the button.
   * 
   * @param button the button to be removed
   */
  public void removeButton(Button button) {
    buttonBar.remove(button);
  }

  /**
   * Sets the close on button click state. When <code>true</code>, the dialog
   * will be closed after a button is clicked. Default value is
   * <code>false</code>.
   * 
   * @param closeOnButtonClick the close on button state
   */
  public void setCloseOnButtonClick(boolean closeOnButtonClick) {
    this.closeOnButtonClick = closeOnButtonClick;
  }

  /**
   * Sets the dialog's status text and an optional icon.
   * 
   * @param text the status text
   * @param iconStyle the icon style
   */
  public void setStatus(String text, String iconStyle) {
    statusText = text;
    statusIconStyle = iconStyle;
    if (rendered) {
      if (status == null) {
        status = new Item("my-dialog-status");
        footerPanel.add(status);
        footerPanel.setCellWidth(status, "100%");
      }
      status.setText(text);
      if (iconStyle != null) {
        status.setIconStyle(iconStyle);
      }
    }
  }

  protected void afterRender() {
    if (statusText != null) {
      setStatus(statusText, statusIconStyle);
    }
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    WidgetHelper.doAttach(footerPanel);
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(footerPanel);
  }

  /**
   * Called after a button in the button bar is selected. Default implementation
   * closes the dialog if closeOnButtonClick = true.
   * 
   * @param be the base event
   */
  protected void onButtonPressed(BaseEvent be) {
    if (closeOnButtonClick) {
      close();
    }
  }

  protected void onRender() {
    super.onRender();
    if (!buttonBar.isRendered()) {
      buttonBar.render();
    }
    buttonBar.addListener(Events.Click, new Listener() {
      public void handleEvent(BaseEvent be) {
        onButtonPressed(be);
      }
    });

    footerPanel = new HorizontalPanel();
    footerPanel.setWidth("100%");

    if (statusText != null) {
      setStatus(statusText, statusIconStyle);
    }

    footerPanel.add(buttonBar);
    DOM.appendChild(footerElem, footerPanel.getElement());
  }

  protected void createButton(int id, String text) {
    Button btn = new Button(text);
    btn.setButtonId(id);
    addButton(btn);
  }

}
