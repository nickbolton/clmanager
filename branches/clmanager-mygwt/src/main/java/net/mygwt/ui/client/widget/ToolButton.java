/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

/**
 * A <code>IconButton</code> that supports a set of predefined styles.
 * 
 * <dt><b>Supported Styles:</b></dt>
 * <dd>.my-tool-close</dd>
 * <dd>.my-tool-minimize</dd>
 * <dd>.my-tool-maximize</dd>
 * <dd>.my-tool-restore</dd>
 * <dd>.my-tool-gear</dd>
 * <dd>.my-tool-pin</dd>
 * <dd>.my-tool-unpin</dd>
 * <dd>.my-tool-right</dd>
 * <dd>.my-tool-left</dd>
 * <dd>.my-tool-up</dd>
 * <dd>.my-tool-down</dd>
 * <dd>.my-tool-refresh</dd>
 * <dd>.my-tool-minus</dd>
 * <dd>.my-tool-plus</dd>
 * <dd>.my-tool-search</dd>
 * <dd>.my-tool-save</dd>
 * <dd>.my-tool-help</dd>
 * </dl>
 */
public class ToolButton extends IconButton {

  /**
   * Creates a new tool button.
   * 
   * @param style the button style
   */
  public ToolButton(String style) {
    super(style);
  }

  protected void onRender() {
    super.onRender();
    addStyleName("my-tool");
  }

}
