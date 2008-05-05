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

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.util.Format;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Instances of this class are used to inform or warn the user.
 * 
 * <dl>
 * <dt><b>Icons:</b></dt>
 * <dd>ICON_ERROR, ICON_INFO, ICON_QUESTION, ICON_WARNING</dd>
 * 
 * <dt><b>Styles:</b></dt>
 * <dd>OK, OK_CANCEL, YES_NO, YES_NO_CANCEL</dd>
 * </dl>
 */
public class MessageBox extends Dialog {

  private String message;
  private int icon;

  /**
   * Creates a new message box instance with the given icon and button(s).
   * 
   * @param icon the icon to display
   * @param style the buttons to display
   */
  public MessageBox(int icon, int style) {
    super(style);
    this.icon = icon;
    setCloseOnButtonClick(true);
  }

  /**
   * Returns the dialog's message.
   * 
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the dialog's message.
   * 
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
    if (rendered) {
      Element text = MyDOM.findChild("my-mbox-text", getElement());
      DOM.setInnerHTML(text, message);
    }
  }

  protected void createContent(WidgetContainer container) {
    StringBuffer sb = new StringBuffer();
    sb.append("<table width=100% height=100%><tr>");
    sb.append("<td class='my-mbox-icon'><div class='my-mbox-icon {0}'></div></td>");
    sb.append("<td width=100% class=my-mbox-text>{1}</td>");
    sb.append("</tr></table>");

    String path = null;
    switch (icon) {
      case Style.ICON_ERROR:
        path = "my-mbox-error";
        break;
      case Style.ICON_INFO:
        path = "my-mbox-info";
        break;
      case Style.ICON_QUESTION:
        path = "my-mbox-question";
        break;
      case Style.ICON_WARNING:
        path = "my-mbox-warning";
        break;
    }

    String html = Format.substitute(sb.toString(), new String[] {path, message});
    Element elem = MyDOM.create(html);
    DOM.appendChild(container.getElement(), elem);

  }

  protected void onRender() {
    super.onRender();
    addStyleName("my-message-box");
    addStyleName("my-shell-plain");
  }

}
