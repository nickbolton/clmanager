/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyDOM;

/**
 * A component created from html text.
 */
public class HTMLComponent extends Component {

  protected String html;
  
  public HTMLComponent(String html) {
    this.html = html;
  }
  
  protected void onRender() {
    setElement(MyDOM.create(html));
  }

}
