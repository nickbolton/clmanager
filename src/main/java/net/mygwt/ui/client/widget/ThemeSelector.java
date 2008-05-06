/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyGWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A ListBox for selecting the MyGWT themes. Changing themes will cause the
 * application to be reloaded.
 */
public class ThemeSelector extends ListBox {

  public ThemeSelector() {
    setStyleName("my-form-field");
    addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        String val = getValue(getSelectedIndex());
        if (val.indexOf("default") != -1) {
          MyGWT.switchTheme("default");
        } else {
          MyGWT.switchTheme("gray");
        }
        reload();
      }
    });

    addItem(MyGWT.MESSAGES.blueTheme(), "default");
    addItem(MyGWT.MESSAGES.grayTheme(), "gray");

    String theme = MyGWT.getTheme();
    if (theme != null && theme.indexOf("g") != -1) {
      setSelectedIndex(1);
    }

    setWidth("100px");
  }

  private native void reload() /*-{
    $wnd.location.reload();
   }-*/;

}
