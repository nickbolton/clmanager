/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.widget.tree.TreeItemUI;

import com.google.gwt.user.client.DOM;

class RootTreeTableItem extends TreeTableItem {

  public RootTreeTableItem(TreeTable treeTable) {
    super(new String[0]);
    setElement(DOM.createDiv());
    this.tree = treeTable;
  }

  protected TreeItemUI getTreeItemUI() {
    TreeTableItemUI ui = new TreeTableItemUI(this);
    ui.setContainer(getElement());
    setData("loaded", "true");
    return ui;
  }

  public String getId() {
    return "";
  }
  
  protected void renderChildren() {
    if (!childrenRendered) {
    super.renderChildren();
    }
  }

}