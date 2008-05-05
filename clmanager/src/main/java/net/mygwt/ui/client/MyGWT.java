/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client;

import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.messages.MyMessages;
import net.mygwt.ui.client.state.CookieProvider;
import net.mygwt.ui.client.state.StateManager;
import net.mygwt.ui.client.util.CSS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * MyGWT core utilities and functions.
 */
public class MyGWT {

  /**
   * MyGWT messages.
   */
  public static MyMessages MESSAGES;

  /**
   * defaultTheme specifies the default theme. Supported theme names are "gray"
   * and "default". Default value is "default".
   */
  public static String defaultTheme = "default";

  /**
   * <code>true</code> if the browser is safari.
   */
  public static boolean isSafari;

  /**
   * <code>true</code> if the browser is opera.
   */
  public static boolean isOpera;

  /**
   * <code>true</code> if the browser is ie.
   */
  public static boolean isIE;

  /**
   * <code>true</code> if the browser is ie7.
   */
  public static boolean isIE7;

  /**
   * <code>true</code> if the browser is gecko.
   */
  public static boolean isGecko;

  /**
   * <code>true</code> if the browser is in strict mode.
   */
  public static boolean isStrict;

  /**
   * <code>true</code> if using https.
   */
  public static boolean isSecure;

  /**
   * <code>true</code> if mac os.
   */
  public static boolean isMac;

  /**
   * <code>true</code> if linux os.
   */
  public static boolean isLinux;

  /**
   * URL to a blank file used by MyGWT when in secure mode for iframe src to
   * prevent the IE insecure content. Default value is 'blank.html'.
   */
  public static String SSL_SECURE_URL;

  /**
   * URL to a 1x1 transparent gif image used by MyGWT to create inline icons
   * with CSS background images. Default value is
   * 'images/default/shared/clear.gif';
   */
  public static String BLANK_IMAGE_URL;

  private static boolean initialized;

  /**
   * Returns the browser's user agent.
   * 
   * @return the user agent
   */
  public native static String getUserAgent() /*-{
     return $wnd.navigator.userAgent.toLowerCase();
   }-*/;

  /**
   * Sets whether the html used in MyDOM.setInnerHTML is escaped for cross-site
   * scripting (XSS) exploits. Enabling will effect performance as all HTML must
   * be parsed. Default value is <code>false</code>.
   * 
   * @param escape <code>true</code> to escape html
   */
  public native static void enableEscapeHTML(boolean escape) /*-{
     $wnd.escapeFlag = escape;
   }-*/;

  /**
   * Changes the theme. Supported theme names are "gray" and "default".
   * 
   * @param theme the new theme name.
   */
  public static void switchTheme(String theme) {
    if (theme.equals("gray")) {
      CSS.addStyleSheet("mygwt-all-gray", "mygwt-all-gray.css");
    } else {
      CSS.removeStyleSheet("mygwt-all-gray");
    }
    StateManager.set("theme", theme.equals("gray") ? "gray" : "default");
  }

  /**
   * Returns the current theme.
   * 
   * @return the theme
   */
  public static String getTheme() {
    return StateManager.getString("theme");
  }

  /**
   * Hides the loading panel.
   * 
   * @param id the loading panel id
   */
  public static void hideLoadingPanel(String id) {
    final Element loading = DOM.getElementById(id);
    if (loading != null) {
      FXStyle fx = new FXStyle(loading);
      fx.duration = 300;
      fx.hideOnComplete = true;
      fx.fadeOut();
    }
  }

  /**
   * Initializes MyGWT.
   */
  public static void init() {
    if (initialized) {
      return;
    }
    try {
      initialized = true;
      MESSAGES = (MyMessages) GWT.create(MyMessages.class);
      SSL_SECURE_URL = GWT.getModuleBaseURL() + "blank.html";
      BLANK_IMAGE_URL = GWT.getModuleBaseURL() + "images/default/shared/clear.gif";

      String ua = getUserAgent();
      isSafari = ua.indexOf("webkit") != -1;
      isOpera = ua.indexOf("opera") != -1;
      isIE = ua.indexOf("msie") != -1;
      isIE7 = ua.indexOf("msie 7") != -1;
      isGecko = ua.indexOf("gecko") != -1;
      isMac = ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1;
      isLinux = ua.indexOf("linux") != -1;

      String mode = DOM.getElementProperty(MyDOM.getDocument(), "compatMode");
      isStrict = mode != null ? mode.equals("CSS1Compat") : false;

      isSecure = isSecure();

      String cls = "";
      if (isIE) {
        cls = "ext-ie";
      } else if (isGecko) {
        cls = "ext-gecko";
      } else if (isOpera) {
        cls = "ext-opera";
      } else if (isSafari) {
        cls = "ext-safari";
      }

      if (isMac) {
        cls += " ext-mac";
      }

      if (isLinux) {
        cls += " ext-linux";
      }

      MyDOM.setStyleName(MyDOM.getBody(), cls);

      CookieProvider provider = new CookieProvider("/", null, null, false);
      StateManager.setProvider(provider);

      String theme = StateManager.getString("theme");
      if (theme == null || theme.equals("")) {
        theme = defaultTheme;
      }

      initInternal(theme);
    } catch (Throwable e) {
    }
  }

  private static native void initInternal(String theme) /*-{
    var links = $doc.getElementsByTagName('link');
    for (var i = 0; i < links.length; i++) {
      var link = links[i];
      var href = link.href;
      href = href.substring(href.lastIndexOf('/') + 1, href.length);
      if (href == 'mygwt-all.css') {
        link.setAttribute('id','mygwt-all');
      }
      if (href == 'mygwt-all-gray.css') {
        link.setAttribute('id','mygwt-all-gray');
        if (theme != 'gray') {
          link.setAttribute('disabled', true);
          link.parentNode.removeChild(link);
        }
      }
    }
  }-*/;

  private static native boolean isSecure() /*-{
    return $wnd.location.href.toLowerCase().indexOf("https") === 0;
  }-*/;

}
