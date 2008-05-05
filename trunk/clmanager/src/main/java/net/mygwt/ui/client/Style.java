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
package net.mygwt.ui.client;

/**
 * Defines MyGWT public constants.
 */
public class Style {

  /**
   * A constant known to be zero (0), used in operations which take bit flags to
   * indicate that "no bits are set".
   */
  public static final int NONE = 0;

  /**
   * Indicates that a default value should be used (value is -1).
   */
  public static final int DEFAULT = -1;

  /**
   * Style constant for check box behavior (value is 1&lt;&lt;8).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>MenuItem</code></li>
   * <li><code>ToolBar</code></li>
   * <li><code>Tree</code></li>
   * </ul>
   * </p>
   */
  public static final int CHECK = 1 << 8;

  /**
   * Style constant for center behavior (value is 1&lt;&lt;24).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ButtonBar</code></li>
   * <li><code>BorderLayout</code></li>
   * <li><code>BorderLayoutData</code></li>
   * </ul>
   * </p>
   */
  public static final int CENTER = 1 << 24;

  /**
   * Style constant left alignment (value is 1&lt;&lt;25).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ButtonBar</code></li>
   * <li><code>El</code></li>
   * </ul>
   * </p>
   */
  public static final int LEFT = 1 << 25;

  /**
   * Style constant right alignment (value is 1&lt;&lt;26).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ButtonBar</code></li>
   * <li><code>El</code></li>
   * </ul>
   * </p>
   */
  public static final int RIGHT = 1 << 26;

  /**
   * Style constant for horizontal alignment or orientation behavior (value is
   * 1&lt;&lt;15).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Effect</code></li>
   * <li><code>FillLayout</code></li>
   * </ul>
   * </p>
   */
  public static final int HORIZONTAL = 1 << 15;

  /**
   * Style constant for vertical alignment or orientation behavior (value is
   * 1&lt;&lt;9).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Effect</code></li>
   * <li><code>FillLayout</code></li>
   * </ul>
   * </p>
   */
  public static final int VERTICAL = 1 << 9;

  /**
   * Style constant for close behavior (value is 1&lt;&lt;1).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shell</code></li>
   * <li><code>TabItem</code></li>
   * </ul>
   * </p>
   */
  public static final int CLOSE = 1 << 1;

  /**
   * Style constant for top behavior (value is 1&lt;&lt;11).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>TabFolder</code></li>
   * </ul>
   * </p>
   */
  public static final int TOP = 1 << 11;

  /**
   * Style constant for bottom behavior (value is 1&lt;&lt;12).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>TabFolder</code></li>
   * </ul>
   * </p>
   */
  public static final int BOTTOM = 1 << 12;

  /**
   * Style constant for east direction / region (value is 1&lt;&lt;1).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>BorderPanel</code></li>
   * <li><code>BorderLayout</code></li>
   * <li><code>SplitBar</code></li>
   * </ul>
   * </p>
   */
  public static final int EAST = 1 << 1;

  /**
   * Style constant for west direction / region (value is 1&lt;&lt;2).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>BorderPanel</code></li>
   * <li><code>BorderLayout</code></li>
   * <li><code>SplitBar</code></li>
   * </ul>
   * </p>
   */
  public static final int WEST = 1 << 2;

  /**
   * Style constant for south direction / region (value is 1&lt;&lt;3).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>BorderPanel</code></li>
   * <li><code>BorderLayout</code></li>
   * <li><code>SplitBar</code></li>
   * </ul>
   * </p>
   */
  public static final int SOUTH = 1 << 3;

  /**
   * Style constant for north direction / region (value is 1&lt;&lt;4).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>BorderPanel</code></li>
   * <li><code>BorderLayout</code></li>
   * <li><code>SplitBar</code></li>
   * </ul>
   * </p>
   */
  public static final int NORTH = 1 << 4;

  /**
   * Style constant for north west direction / region (value is 1&lt;&lt;10).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Resizable</code></li>
   * </ul>
   * </p>
   */
  public static final int NW = 1 << 10;

  /**
   * Style constant for south west direction / region (value is 1&lt;&lt;11).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Resizable</code></li>
   * </ul>
   * </p>
   */
  public static final int SW = 1 << 11;

  /**
   * Style constant for south east direction / region (value is 1&lt;&lt;12).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Resizable</code></li>
   * </ul>
   * </p>
   */
  public static final int SE = 1 << 12;

  /**
   * Style constant for north east direction / region (value is 1&lt;&lt;14).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Resizable</code></li>
   * </ul>
   * </p>
   */
  public static final int NE = 1 << 14;

  /**
   * Style constant for collapse behavior (value is 1&lt;&lt;6).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ContentPanel</code></li>
   * </ul>
   * </p>
   */
  public static final int COLLAPSE = 1 << 6;

  /**
   * Style constant for header behavior (value is 1&lt;&lt;7).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ContentPanel</code></li>
   * </ul>
   * </p>
   */
  public static final int HEADER = 1 << 7;

  /**
   * Style constant for framing behavior (value is 1&lt;&lt;3).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shadow</code></li>
   * </ul>
   * </p>
   */
  public static final int DROP = 1 << 3;

  /**
   * Style constant for side behavior (value is 1&lt;&lt;2).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shadow</code></li>
   * </ul>
   * </p>
   */
  public static final int SIDES = 1 << 2;

  /**
   * Style constant for single select behavior (value is 1&lt;&lt;10).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>List</code></li>
   * <li><code>Table</code></li>
   * </ul>
   * </p>
   */
  public static final int SINGLE = 1 << 10;

  /**
   * Style constant for multi select behavior (value is 1&lt;&lt;11).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>List</code></li>
   * <li><code>Table</code></li>
   * </ul>
   * </p>
   */
  public static final int MULTI = 1 << 11;

  /**
   * Style constant for flat behavior (value is 1&lt;&lt;19).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>List</code></li>
   * </ul>
   * </p>
   */
  public static final int FLAT = 1 << 19;

  /**
   * Style constant for ascending behavior (value is 1).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>TableColumn</code></li>
   * </ul>
   * </p>
   */
  public static final int ASC = 1;

  /**
   * Style constant for descending behavior (value is 2).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>TableColumn</code></li>
   * </ul>
   * </p>
   */
  public static final int DESC = 2;

  /**
   * Style constant for resize behavior (value is 1&lt;&lt;3).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shell</code></li>
   * </ul>
   * </p>
   */
  public static final int RESIZE = 1 << 3;

  /**
   * Style constant for modal behavior (value is 1&lt;&lt;20).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shell</code></li>
   * </ul>
   * </p>
   */
  public static final int MODAL = 1 << 20;

  /**
   * The <code>MessageBox</code> style constant for error icon behavior (value
   * is 1&lt;&lt;80).
   */
  public static final int ICON_ERROR = 1 << 80;

  /**
   * The <code>MessageBox</code> style constant for information icon behavior
   * (value is 1&lt;&lt;82).
   */
  public static final int ICON_INFO = 1 << 82;

  /**
   * The <code>MessageBox</code> style constant for question icon behavior
   * (value is 1&lt;&lt;84).
   */
  public static final int ICON_QUESTION = 1 << 84;

  /**
   * The <code>MessageBox</code> style constant for warning icon behavior
   * (value is 1&lt;&lt;86).
   */
  public static final int ICON_WARNING = 1 << 86;

  /**
   * The <code>MessageBox</code> style for Ok button (value is 1&lt;&lt;88).
   */
  public static final int OK = 1 << 88;

  /**
   * The <code>MessageBox</code> style for Ok and Cancel buttons (value is
   * 1&lt;&lt;90).
   */
  public static final int OK_CANCEL = 1 << 90;

  /**
   * The <code>MessageBox</code> style for Yes and No buttons (value is
   * 1&lt;&lt;92).
   */
  public static final int YES_NO = 1 << 92;

  /**
   * The <code>MessageBox</code> style for Yes, No, and Cancel buttons (value
   * is 1&lt;&lt;94).
   */
  public static final int YES_NO_CANCEL = 1 << 94;

  /**
   * Style constant for toggle button behavior (value is 1&lt;&lt;9).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ToolItem</code></li>
   * </ul>
   * </p>
   */
  public static final int TOGGLE = 1 << 9;

  /**
   * Style constant for push button behavior (value is 1&lt;&lt;3).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>MenuItem</code></li>
   * <li><code>ToolItem</code></li>
   * </ul>
   * </p>
   */
  public static final int PUSH = 1 << 3;

  /**
   * Style constant for drop menu behavior (value is 1&lt;&lt;30).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>MenuItem</code></li>
   * <li><code>ToolItem</code></li>
   * </ul>
   * </p>
   */
  public static final int MENU = 1 << 30;

  /**
   * Style constant for split menu behavior (value is 1&lt;&lt;32).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ToolItem</code></li>
   * </ul>
   * </p>
   */
  public static final int SPLIT_MENU = 1 << 32;

  /**
   * Style constant for line separator behavior (value is 1&lt;&lt;1).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>MenuItem</code></li>
   * <li><code>ToolItem</code></li>
   * </ul>
   * </p>
   */
  public static final int SEPARATOR = 1 << 1;

  /**
   * Style constant for radio behavior (value is 1&lt;&lt;12).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>MenuItem</code></li>
   * </ul>
   * </p>
   */
  public static final int RADIO = 1 << 12;

  /**
   * Style constant for radio behavior (value is 1&lt;&lt;29).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>ContentPanel</code></li>
   * <li><code>Shadow</code></li>
   * </ul>
   * </p>
   */
  public static final int FRAME = 1 << 29;

  /**
   * Style constant for auto hide behavior (value is 1&lt;&lt;25).
   * <p>
   * <b>Used By:</b>
   * <ul>
   * <li><code>Shell</code></li>
   * </ul>
   * </p>
   */
  public static final int AUTO_HIDE = 1 << 25;

  /**
   * Style constant for focus behavior (value is 1&lt;&lt;16).
   */
  public static final int FOCUSABLE = 1 << 16;

  /**
   * Style constant for parent behavior (value is 1&lt;&lt;48).
   */
  public static final int PARENT = 1 << 48;

  /**
   * Style constant for children behavior (value is 1&lt;&lt;49).
   */
  public static final int CHILDREN = 1 << 49;
  
  /**
   * Style constant for leaf behavior (value is 1&lt;&lt;51).
   */
  public static final int LEAF = 1 << 51;
  
  /**
   * Style constant for leaf behavior (value is 1&lt;&lt;52).
   */
  public static final int FILL = 1 << 52;

}
