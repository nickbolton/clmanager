/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.util.Format;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays information in the bottom right region of the browser for a
 * specified amount of time.
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-info (the info itself)</dd>
 * <dd>.my-info .my-info-text (the info text)</dd>
 * </dl>
 * 
 * <p>
 * Note: At this time, the widget size is fixed and should not be modified.
 * </p>
 */
public class Info extends Component {

  /**
   * display specifies the time in millseconds to display a message. Default
   * value is 2500.
   */
  public static int display = 2500;

  private static int width = 225;
  private static int height = 75;

  private static List slots = new ArrayList();

  /**
   * Displays a message.
   * 
   * @param title the message title
   * @param text the message
   * @param value the value to be substitute
   */
  public static void show(String title, String text, String value) {
    show(title, text, new String[] {value});
  }

  /**
   * Displays a message.
   * 
   * @param title the message title
   * @param text the message
   * @param value1 the value to be substituted
   * @param value2 the value to be substituted
   */
  public static void show(String title, String text, String value1, String value2) {
    show(title, text, new String[] {value1, value2});
  }

  /**
   * Displays the given text.
   * 
   * @param title the title text
   * @param text the message
   * @param values the values to be substituted
   */
  public static void show(String title, String text, String[] values) {
    int avail = firstAvail();
    if (text != null && values != null) text = Format.substitute(text,
        values);

    Info message = new Info(avail, title, text);
    slots.add(avail, message);
    message.showInternal();
  }

  private static int firstAvail() {
    int size = slots.size();
    for (int i = 0; i < size; i++) {
      if (slots.get(i) == null) {
        return i;
      }
    }
    return size;
  }

  private Element titleElem, textElem;
  private int level;

  private Info(int level, String title, String text) {
    this.level = level;
    String html = "<div class={0}><div class={0}-title></div><div class={0}-text></div></div>";
    html = Format.substitute(html, "my-info");
    setElement(MyDOM.create(html));
    
    titleElem = MyDOM.findChild("my-info-title", getElement());
    textElem = MyDOM.findChild("my-info-text", getElement());
    
    MyDOM.setInnerHTML(titleElem, title);
    MyDOM.setInnerHTML(textElem, text);

    MyDOM.setStyleAttribute(getElement(), "position", "absolute");

  }

  private void showInternal() {
    int cw = Window.getClientWidth();
    int ch = Window.getClientHeight();

    int left = (cw - width - 10);
    int top = ch - height - 10 - (level * (height + 10));

    MyDOM.setLeft(getElement(), left);
    MyDOM.setTop(getElement(), top);
    MyDOM.setVisibility(getElement(), false);
    
    setSize(width, height);
    
    RootPanel.get().add(this);

    FXStyle fx = new FXStyle(getElement());
    fx.fadeIn();

    final Widget fWidget = this;
    Timer t = new Timer() {
      public void run() {
        FXStyle fx = new FXStyle(getElement());
        fx.addListener(Events.EffectComplete, new Listener() {
          public void handleEvent(BaseEvent be) {
            slots.set(level, null);
            RootPanel.get().remove(fWidget);
          }
        });
        fx.fadeOut();
      }
    };
    t.schedule(display);
  }

}
