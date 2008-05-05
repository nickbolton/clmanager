/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client;

import com.google.gwt.user.client.Event;

/**
 * Defines the MyGWT event types.
 */
public class Events {

  /**
   * Click event type (value is Event.ONCLICK).
   */
  public static final int Click = Event.ONCLICK;

  /**
   * DoubleClick event type (value is Event.ONDBLCLICK).
   */
  public static final int DoubleClick = Event.ONDBLCLICK;

  /**
   * MouseOver event type (value is Event.ONMOUSEOVER).
   */
  public static final int MouseOver = Event.ONMOUSEOVER;

  /**
   * MouseMove event type (value is Event.ONMOUSEMOVE).
   */
  public static final int MouseMove = Event.ONMOUSEMOVE;

  /**
   * MouseDown event type (value is Event.ONMOUSEDOWN).
   */
  public static final int MouseDown = Event.ONMOUSEDOWN;

  /**
   * MouseUp event type (value is Event.ONMOUSEUP).
   */
  public static final int MouseUp = Event.ONMOUSEUP;

  /**
   * MouseOut event type (value is Event.ONMOUSEOUT).
   */
  public static final int MouseOut = Event.ONMOUSEOUT;

  /**
   * Scroll event type (value is Event.ONSCROLL).
   */
  public static final int Scroll = Event.ONSCROLL;

  /**
   * BeforeAdd event type (value is 111).
   */
  public static final int BeforeAdd = 111;

  /**
   * BeforeRemove event type (value is 131).
   */
  public static final int BeforeRemove = 151;

  /**
   * Add event type (value is 110).
   */
  public static final int Add = 110;

  /**
   * Remove event type (value is 150).
   */
  public static final int Remove = 150;

  /**
   * BeforeExpand event type (value is 220).
   */
  public static final int BeforeExpand = 220;

  /**
   * Expand event type (value is 210).
   */
  public static final int Expand = 210;

  /**
   * BeforeCollapse event type (value is 230).
   */
  public static final int BeforeCollapse = 230;

  /**
   * Collapse event type (value is 240).
   */
  public static final int Collapse = 240;

  /**
   * KeyPress event type (value is Event.ONKEYPRESS).
   */
  public static final int KeyPress = Event.ONKEYPRESS;

  /**
   * KeyUp event type (value is Event.ONKEYUP).
   */
  public static final int KeyUp = Event.ONKEYUP;

  /**
   * KeyDown event type (value is Event.ONKEYDOWN).
   */
  public static final int KeyDown = Event.ONKEYDOWN;

  /**
   * CheckChange event type (value is 580).
   */
  public static final int CheckChange = 580;

  /**
   * Resize event type (value is 590).
   */
  public static final int Resize = 590;

  /**
   * SelectionChange event type (value is 600).
   */
  public static final int SelectionChange = 600;

  /**
   * Select event type (value is 610).
   */
  public static final int Select = 610;

  /**
   * Close event type (value is 705).
   */
  public static final int BeforeClose = 705;

  /**
   * Close event type (value is 710).
   */
  public static final int Close = 710;
  
  /**
   * BeforeOpen event type (value is 712).
   */
  public static final int BeforeOpen = 712;

  /**
   * Open event type (value is 714).
   */
  public static final int Open = 714;

  /**
   * Enable event type (value is 750).
   */
  public static final int Enable = 750;

  /**
   * Disable event type (value is 760).
   */
  public static final int Disable = 760;

  /**
   * Attach event type (value is 800).
   */
  public static final int Attach = 800;

  /**
   * Detach event type (value is 810).
   */
  public static final int Detach = 810;

  /**
   * Render event type (value is 500).
   */
  public static final int Render = 0;

  /**
   * DragStart event type (value is 850).
   */
  public static final int DragStart = 850;

  /**
   * DragCancel event type (value is 855).
   */
  public static final int DragCancel = 855;

  /**
   * DragMove event type (value is 858).
   */
  public static final int DragMove = 858;

  /**
   * DragEnd event type (value is 860).
   */
  public static final int DragEnd = 860;

  /**
   * EffectStart event type (value is 900).
   */
  public static final int EffectStart = 900;

  /**
   * EffectComplete event type (value is 910).
   */
  public static final int EffectComplete = 910;

  /**
   * EffectCancel event type (value is 920).
   */
  public static final int EffectCancel = 920;

  /**
   * ResizeStart event type (value is 922).
   */
  public static final int ResizeStart = 922;

  /**
   * ResizeEnd event type (value is 924).
   */
  public static final int ResizeEnd = 924;

  /**
   * ColumnClick event type (value is 930).
   */
  public static final int ColumnClick = 930;
  
  /**
   * SortChange event type (value is 932).
   */
  public static final int SortChange = 932;

  /**
   * CellClick event type (value is 940).
   */
  public static final int CellClick = 940;

  /**
   * CellDoubleClick event type (value is 950).
   */
  public static final int CellDoubleClick = 950;

  /**
   * RowClick event type (value is 960).
   */
  public static final int RowClick = 960;

  /**
   * RowDoubleClick event type (value is 970).
   */
  public static final int RowDoubleClick = 970;

  /**
   * Activate event type (value is 980).
   */
  public static final int Activate = 30;

  /**
   * Deactivate event type (value is 990).
   */
  public static final int Deactivate = 32;

  /**
   * BeforeShow event type (value is 400).
   */
  public static final int BeforeShow = 400;

  /**
   * Show event type (value is 410).
   */
  public static final int Show = 410;

  /**
   * BeforeHide event type (value is 420).
   */
  public static final int BeforeHide = 420;

  /**
   * Hide event type (value is 430).
   */
  public static final int Hide = 430;

  /**
   * StateChange event type (value is 440).
   */
  public static final int StateChange = 440;

  /**
   * ContextMenu event type (value is 450).
   */
  public static final int ContextMenu = 450;

  /**
   * BeforeDispose event type (value is 300).
   */
  public static final int BeforeDispose = 300;

  /**
   * Dispose event type (value is 310).
   */
  public static final int Dispose = 310;
  
  /**
   * Valid event type (value is 374).
   */
  public static final int Valid = 374;
  
  /**
   * Invalid event type (value is 375).
   */
  public static final int Invalid = 375;
  
  /**
   * Focus event type (value is 310).
   */
  public static final int Focus = 310;

  /**
   * Blur event type (value is 360).
   */
  public static final int Blur = 360;

  /**
   * Change event type (value is 370).
   */
  public static final int Change = 370;
  
}
