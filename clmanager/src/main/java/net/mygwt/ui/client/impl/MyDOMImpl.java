/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.impl;

import com.google.gwt.user.client.Element;

public class MyDOMImpl {
  
  public native String getStyle(Element elem, String property) /*-{
   var value = null;
   var computed = $wnd.document.defaultView.getComputedStyle(elem, '');
   if (computed) { // test computed before touching for safari
   value = computed[property];
   }
   return elem.style[property] || value || null;
   }-*/;

  public native void setStyle(Element elem, String property, String value) /*-{
   elem.style[property] = value;
   }-*/;

}
