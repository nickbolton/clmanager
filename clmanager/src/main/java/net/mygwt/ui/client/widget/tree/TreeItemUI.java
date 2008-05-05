/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.tree;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.fx.FXStyle;
import net.mygwt.ui.client.util.Markup;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class TreeItemUI {

  protected static Listener clickListener = new Listener() {
    public void handleEvent(BaseEvent be) {
      if (be.type == Events.Click) {
        TreeItem item = (TreeItem) be.widget;
        Element target = be.getTarget();
        if (target != null && be.within(item.getUI().jointEl)) {
          item.toggle();
        } else {
          item.fireEvent(Events.Select);
        }
        be.cancelBubble();
      } else if (be.type == Events.DoubleClick) {
        TreeItem item = (TreeItem) be.widget;
        item.toggle();
      }
    }
  };
  protected static Listener mouseListener = new Listener() {
    public void handleEvent(BaseEvent be) {
      int type = be.type;
      TreeItem item = (TreeItem) be.widget;
      switch (type) {
        case Events.MouseOver:
          item.getUI().onOverChange(true);
          break;
        case Events.MouseOut:
          item.getUI().onOverChange(false);
          break;
      }
      be.cancelBubble();
    }
  };
  private static Listener jointListener = new Listener() {
    public void handleEvent(BaseEvent be) {
      int type = be.type;
      TreeItem item = (TreeItem) be.widget;
      switch (type) {
        case Events.MouseOver:
          MyDOM.setStyleName(item.getUI().itemEl, item.getUI().styleTreeJointOver, true);
          break;
        case Events.MouseOut:
          MyDOM.setStyleName(item.getUI().itemEl, item.getUI().styleTreeJointOver, false);
          break;
      }
      be.stopEvent();
    }
  };

  protected int indentWidth = 18;
  protected Element containerEl;
  protected Element jointEl, jointDivEl;

  protected TreeItem item;
  protected Element itemEl, indentEl;
  protected Element checkEl, checkDivEl;
  protected Element iconEl, iconDivEl;
  protected Element textEl, textSpanEl;
  private String textStyle;

  protected String styleTreeOver = "my-tree-over";
  protected String styleTreeJointOver = "my-tree-joint-over";
  protected String styleTreeChecked = "my-tree-checked";
  protected String styleTreeNotChecked = "my-tree-notchecked";
  protected String styleTreeLoading = "my-tree-loading";
  protected String styleTreeSelected = "my-tree-sel";
  protected String classTreeOpen = "my-tree-open";
  protected String classTreeClose = "my-tree-close";

  protected static Listener listener = new Listener() {
    public void handleEvent(BaseEvent be) {
      int type = be.type;
      TreeItem item = (TreeItem) be.widget;
      TreeItemUI ui = item.getUI();
      Element target = be.getTarget();
      switch (type) {
        case Events.Click:
        case Events.DoubleClick:
          if (DOM.isOrHasChild(ui.checkEl, target)) {
            be.stopEvent();
            item.setChecked(!item.isChecked());
          } else {
            clickListener.handleEvent(be);
          }
          return;
        case Events.MouseOver:
        case Events.MouseOut:
          if (DOM.isOrHasChild(ui.jointEl, target)) {
            jointListener.handleEvent(be);

          } else if (DOM.isOrHasChild(ui.iconEl, target)
              || DOM.isOrHasChild(ui.textEl, target)
              || DOM.isOrHasChild(ui.checkEl, target)) {
            mouseListener.handleEvent(be);
          }
          break;
      }

    }
  };

  public TreeItemUI(TreeItem item) {
    this.item = item;
    render();
  }

  public void onMouseOut(BaseEvent be) {

  }

  public void collapse() {
    if (item.isRoot()) {
      return;
    }
    onIconStyleChange(null);
    if (item.tree.getAnimate()) {
      animCollapse();
    } else {
      afterCollapse();
    }
  }

  public void expand() {
    if (item.isRoot()) {
      return;
    }
    updateJoint();
    if (item.getItemCount() == 0) {
      return;
    }
    onIconStyleChange(null);
    if (item.tree.getAnimate()) {
      animExpand();
    } else {
      MyDOM.setVisible(containerEl, true);
      afterExpand();
    }
  }

  public Element getJointEl() {
    return jointEl;
  }

  public Element getCheckEl() {
    return checkEl;
  }

  public void onCheckChange(boolean checked) {
    String cls = checked ? styleTreeChecked : styleTreeNotChecked;
    MyDOM.setStyleName(checkDivEl, cls);
    item.fireEvent(Events.CheckChange);
  }

  public void onClick(BaseEvent be) {
    Element target = be.getTarget();
    if (target != null && be.within(jointEl)) {
      item.toggle();
    }
    be.cancelBubble();
  }

  public void onDoubleClick(BaseEvent be) {
    item.toggle();
    be.cancelBubble();
  }

  public void onLoadingChange(boolean loading) {
    if (loading) {
      item.addStyleName(styleTreeLoading);
    } else {
      item.removeStyleName(styleTreeLoading);
    }
  }

  public void onIconStyleChange(String style) {
    if (item.iconStyle != null) {
      MyDOM.setVisible(iconEl, true);
      MyDOM.setStyleName(iconDivEl, item.iconStyle);
      return;
    }
    if (!item.leaf) {
      String s = "";
      if (item.isExpanded() && item.tree.getOpenNodeImageStyle() != null) {
        s = item.tree.getOpenNodeImageStyle();
      } else if (item.isExpanded() && item.tree.getNodeImageStyle() != null) {
        s = item.tree.getNodeImageStyle();
      } else if (!item.isExpanded()) {
        s = item.tree.getNodeImageStyle();
      }
      MyDOM.setVisible(iconEl, true);
      MyDOM.setStyleName(iconDivEl, s);
    } else {
      MyDOM.setVisible(iconEl, true);
      MyDOM.setStyleName(iconDivEl, item.tree.getItemImageStyle());
      return;
    }

  }

  public void onSelectedChange(boolean selected) {
    if (item.isRendered()) {
      MyDOM.setStyleName(itemEl, styleTreeSelected, selected);
      if (!selected) {
        onOverChange(false);
      }
    }
  }

  public void onOverChange(boolean over) {
    MyDOM.setStyleName(itemEl, styleTreeOver, over);
  }

  public void removeItem(TreeItem child) {
    DOM.removeChild(containerEl, child.getElement());
  }

  protected void afterCollapse() {
    item.tree.enable();
    updateJoint();
    MyDOM.setStyleName(itemEl, styleTreeJointOver, false);
    item.fireEvent(Events.Collapse);
  }

  protected void afterExpand() {
    item.tree.enable();
    MyDOM.setStyleName(itemEl, styleTreeJointOver, false);

    item.fireEvent(Events.Expand);
  }

  protected void onTextChange(String text) {
    if (!item.root) {
      MyDOM.setInnerHTML(textSpanEl, text);
    }
  }

  public Listener getListener() {
    return listener;
  }

  protected void onTextStyleChange(String style) {
    if (textStyle != null) {
      MyDOM.removeStyleName(textEl, textStyle);
    }
    textStyle = style;
    if (style != null) {
      MyDOM.addStyleName(textEl, style);
    }
  }

  protected void render() {
    if (item.isRoot() == true) {
      return;
    }
    item.setElement(DOM.createDiv());
    item.setStyleName("my-treeitem");

    int index = item.parentItem.indexOf(item);
    DOM.insertChild(item.parentItem.getUI().containerEl, item.getElement(), index);

    DOM.setInnerHTML(item.getElement(), Markup.TREE_ITEM);
    itemEl = DOM.getFirstChild(item.getElement());
    Element td = MyDOM.getSubChild(itemEl, 3);
    indentEl = DOM.getFirstChild(td);
    jointEl = DOM.getNextSibling(td);
    jointDivEl = DOM.getFirstChild(jointEl);
    checkEl = DOM.getNextSibling(DOM.getNextSibling(jointEl));
    checkDivEl = DOM.getFirstChild(checkEl);
    iconEl = DOM.getNextSibling(checkEl);
    iconDivEl = DOM.getFirstChild(iconEl);
    textEl = DOM.getNextSibling(iconEl);
    textSpanEl = DOM.getFirstChild(textEl);
    Element tbl = DOM.getFirstChild(item.getElement());
    containerEl = DOM.getNextSibling(tbl);

    int style = item.tree.getStyle();
    if ((style & Style.CHECK) != 0) {
      MyDOM.setVisible(checkEl, true);
    } else {
      MyDOM.setVisible(checkEl, false);
    }

    onTextChange(item.getText());
    onIconStyleChange(item.getIconStyle());

    if (item.getTextStyle() != null) {
      onTextStyleChange(item.getTextStyle());
    }

    if (item.isChecked()) {
      onCheckChange(true);
    }

    MyDOM.setWidth(indentEl, getIndent());

    if (!MyGWT.isIE) {
      DOM.setElementPropertyInt(item.getElement(), "tabIndex", 0);
    }

    updateJoint();
    item.disableTextSelection(true);

  }

  void animCollapse() {
    FXStyle fx = new FXStyle(containerEl);
    fx.duration = 300;
    fx.addListener(Events.EffectComplete, new Listener() {
      public void handleEvent(BaseEvent be) {
        afterCollapse();
      }
    });
    item.tree.disable();
    fx.slideOut(Style.NORTH);
  }

  void animExpand() {
    FXStyle fx = new FXStyle(containerEl);
    fx.duration = 300;
    fx.addListener(Events.EffectComplete, new Listener() {
      public void handleEvent(BaseEvent be) {
        afterExpand();
      }
    });
    item.tree.disable();
    fx.slideIn(Style.SOUTH);
  }

  public void updateJoint() {
    if (item.isRoot()) {
      return;
    }
    boolean loaded = item.getData("loaded") != null;
    boolean viewer = item.tree.isViewer;
    boolean children = item.getItemCount() != 0;
    boolean open = (!viewer && children) || (viewer && (children || !loaded));
   
    if (!item.leaf && open) {
      String cls = item.isExpanded() ? classTreeOpen : classTreeClose;
      MyDOM.setStyleName(jointDivEl, cls);
    } else {
      MyDOM.setStyleName(jointDivEl, "none");
    }

    int style = item.tree.getStyle();
    if ((style & Style.CHECK) != 0) {
      switch (item.tree.checkNodes) {
        case Style.DEFAULT:
          MyDOM.setVisible(checkEl, true);
          break;
        case Style.PARENT:
          MyDOM.setVisible(checkEl, !item.isLeaf());
          break;
        case Style.LEAF:
          MyDOM.setVisible(checkEl, item.isLeaf());
          break;
      }

    }
  }

  protected int getIndent() {
    return (item.getDepth() - 1) * item.tree.getIndentWidth();
  }
}
