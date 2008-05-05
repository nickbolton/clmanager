package net.mygwt.ui.client.widget.treetable;

import net.mygwt.ui.client.widget.table.ITable;
import net.mygwt.ui.client.widget.table.TableHeader;

public class TreeTableHeader extends TableHeader {

  public TreeTableHeader(TreeTable treeTable) {
    super(treeTable);
  }

  protected void init(ITable table) {
    super.init(table);
  }

  protected void updateSplitBars() {
    super.updateSplitBars();
  }

  protected void resizeColumns(boolean fireEvent, boolean resizeBody) {
    super.resizeColumns(fireEvent, resizeBody);
  }

  protected void doTableComponentResize() {
    if (table instanceof TreeTable) {
      ((TreeTable) table).getView().resize();
    } else {
      super.doTableComponentResize();
    }
  }
  
  protected void doTableComponentResizeCells(int columnIndex) {
    if (table instanceof TreeTable) {
      ((TreeTable) table).getView().resizeCells(columnIndex);
    } else {
      super.doTableComponentResizeCells(columnIndex);
    }
  }
  
  protected void doTableComponentShowColumn(int index, boolean show) {
    if (table instanceof TreeTable) {
      ((TreeTable) table).getView().showColumn(index, show);
    } else {
      super.doTableComponentShowColumn(index, show);
    }
  }
}
