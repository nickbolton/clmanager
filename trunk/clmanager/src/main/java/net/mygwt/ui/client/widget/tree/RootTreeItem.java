/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.tree;

import net.mygwt.ui.client.MyDOM;

import com.google.gwt.user.client.DOM;

class RootTreeItem extends TreeItem {

  public RootTreeItem(Tree tree) {
    setElement(DOM.createDiv());
    this.tree = tree;
  }

  protected TreeItemUI getTreeItemUI() {
    TreeItemUI ui = new TreeItemUI(this);
    ui.containerEl = getElement();
    setData("loaded", "true");
    MyDOM.setStyleAttribute(ui.containerEl, "padding", "4px");
    return ui;
  }

  // protected void onRender() {
  // root = true;
  // ui = new TreeItemUI(this);
  // ui.containerEl = getElement();
  // setData("loaded", "true");
  // MyDOM.setStyleAttribute(ui.containerEl, "padding", "4px");
  // }

//  public String getId() {
//    return "";
//  }

}
