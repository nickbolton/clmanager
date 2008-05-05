/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.impl;

import com.google.gwt.user.client.Element;

public class MyDOMImplIE6 extends MyDOMImpl {

  public native String getStyle(Element elem, String property) /*-{
   switch( property ) {
   case 'opacity' :// IE opacity uses filter
   var val = 100;
   try { // will error if no DXImageTransform
   val = elem.filters['DXImageTransform.Microsoft.Alpha'].opacity;

   } catch(e) {
   try { // make sure its in the document
   val = elem.filters('alpha').opacity;
   } catch(e) {
   }
   }
   return val / 100;
   break;
   default:
   // test currentStyle before touching
   var value = elem.currentStyle ? elem.currentStyle[property] : null;
   return ( elem.style[property] || value || null );
   }
   }-*/;

  public native void setStyle(Element elem, String property, String value) /*-{
   switch (property) {
   case 'opacity':
   if ( typeof elem.style.filter == 'string' ) { // in case not appended
   elem.style.filter = 'alpha(opacity=' + value * 100 + ')';

   if (!elem.currentStyle || !elem.currentStyle.hasLayout) {
   elem.style.zoom = 1; // when no layout or cant tell
   }
   }
   break;
   default:
   elem.style[property] = value;
   }
   }-*/;
}
