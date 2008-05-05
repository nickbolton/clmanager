/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

/**
 * Raw html content.
 */
public class Markup {

  public static String ITEM;

  public static String ITEM_CHECK;

  public static String BUTTON;

  public static String BOTTOM_BOX;

  public static String BOX;

  public static String TREE_ITEM;

  public static String SHADOW;

  public static String TREETABLE_ITEM;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<table class={0} cellpadding=0 cellspacing=0><tbody><tr>");
    sb.append("<td class={0}-l><div>&nbsp;</div></td>");
    sb.append("<td class={0}-ml></td>");
    sb.append("<td class={0}-c><span class={0}-text></span></td>");
    sb.append("<td class={0}-mr></td>");
    sb.append("<td class={0}-r><div>&nbsp;</div></td>");
    sb.append("</tr></tbody></table>");

    ITEM = sb.toString();

    sb = new StringBuffer();
    sb.append("<table class={0} cellpadding=0 cellspacing=0><tbody><tr>");
    sb.append("<td class={0}-l><div>&nbsp;</div></td>");
    sb.append("<td class={0}-ml></td>");
    sb.append("<td class={0}-c><button class={0}-text></button></td>");
    sb.append("<td class={0}-mr></td>");
    sb.append("<td class={0}-r><div>&nbsp;</div></td>");
    sb.append("</tr></tbody></table>");

    BUTTON = sb.toString();

    sb = new StringBuffer();
    sb.append("<table class={0} cellpadding=0 cellspacing=0><tbody><tr>");
    sb.append("<td class={0}-l><div>&nbsp;</div></td>");
    sb.append("<td class={0}-check></td>");
    sb.append("<td class={0}-ml></td>");
    sb.append("<td class={0}-c><span class={0}-text></span></td>");
    sb.append("<td class={0}-mr></td>");
    sb.append("<td class={0}-r><div>&nbsp;</div></td>");
    sb.append("</tr></tbody></table>");

    ITEM_CHECK = sb.toString();

    sb = new StringBuffer();
    sb.append("<div><table class={0} cellpadding=0 cellspacing=0><tbody>");
    sb.append("<tr><td class={0}-ml><div></div></td><td class={0}-mc></td><td class={0}-mr><div></div></td></tr>");
    sb.append("<tr><td class={0}-bl><div></div></td><td class={0}-bc></td><td class={0}-br><div></div></td></tr>");
    sb.append("</tbody></table></div>");

    BOTTOM_BOX = sb.toString();

    sb = new StringBuffer();
    sb.append("<table class={0} cellpadding=0 cellspacing=0><tbody>");
    sb.append("<tr class={0}-trow><td class={0}-tl><div>&nbsp;</div></td><td class={0}-tc></td><td class={0}-tr><div>&nbsp;</div></td></tr>");
    sb.append("<tr><td class={0}-ml></td><td class={0}-mc></td><td class={0}-mr></td></tr>");
    sb.append("<tr class={0}-brow><td class={0}-bl></td><td class={0}-bc></td><td class={0}-br></td></tr>");
    sb.append("</tr></tbody></table>");

    BOX = sb.toString();

    sb = new StringBuffer();
    sb.append("<table cellpadding=0 cellspacing=0>");
    sb.append("<tbody><tr><td><div class=my-tree-indent></div></td>");
    sb.append("<td class=my-tree-joint align=center valign=middle><div>&nbsp;</div></td>");
    sb.append("<td class=my-tree-left><div></div></td>");
    sb.append("<td class=my-tree-check><div class=my-tree-notchecked></div></td>");
    sb.append("<td class=my-tree-icon><div>&nbsp;</div></td>");
    sb.append("<td class=my-tree-item-text><span>{0}</span></td>");
    sb.append("<td class=my-tree-right><div></div></td></tr></tbody></table>");
    sb.append("<div class=my-tree-ct style='display: none'></div>");

    TREE_ITEM = sb.toString();

    sb = new StringBuffer();
    sb.append("<div class=my-shadow><div class=my-shadow-t><div class=my-shadow-tl></div><div class=my-shadow-tc></div><div class=my-shadow-tr></div></div>");
    sb.append("<div class=my-shadow-c><div class=my-shadow-ml></div><div class=my-shadow-mc></div><div class=my-shadow-mr></div></div>");
    sb.append("<div class=my-shadow-b><div class=my-shadow-bl></div><div class=my-shadow-bc></div><div class=my-shadow-br></div></div></div>");

    SHADOW = sb.toString();

    sb = new StringBuffer();
    sb.append("<div class=my-treetbl-item><table cellpadding=0 cellspacing=0 tabIndex=1 style='table-layout: fixed;'><tbody><tr>");
    sb.append("<td class=my-treetbl-cell index=0><div class=my-treetbl-cell-overflow><div class=my-treetbl-cell-text>");
    sb.append("<table cellpadding=0 cellspacing=0>");
    sb.append("<tbody><tr><td><div class=my-treetbl-indent></div></td>");
    sb.append("<td class=my-treetbl-joint align=center valign=middle><div>&nbsp;</div></td>");
    sb.append("<td class=my-treetbl-left><div></div></td>");
    sb.append("<td class=my-treetbl-check><div class=my-treetbl-notchecked></div></td>");
    sb.append("<td class=my-treetbl-icon><div>&nbsp;</div></td>");
    sb.append("<td class=my-treetbl-item-text><span>{0}</span></td>");
    sb.append("<td class=my-treetbl-right><div></div></td></tr></tbody></table></div></div></td></tr></tbody></table></div>");
    sb.append("<div class=my-treetbl-ct style='display: none'></div>");

    TREETABLE_ITEM = sb.toString();

  }

}
