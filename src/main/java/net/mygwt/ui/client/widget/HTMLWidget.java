/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyDOM;

import com.google.gwt.user.client.ui.Widget;

/**
 * A widget created from html text.
 */
public class HTMLWidget extends Widget {

  public HTMLWidget(String html) {
    setElement(MyDOM.create(html));
  }
   
}
