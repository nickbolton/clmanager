/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.widget;

import net.mygwt.ui.client.MyGWT;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.data.DataLoader;
import net.mygwt.ui.client.data.LoadConfig;
import net.mygwt.ui.client.data.LoadEvent;
import net.mygwt.ui.client.data.LoadResult;
import net.mygwt.ui.client.data.Loader;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.messages.MyMessages;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.util.Util;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetHelper;

/**
 * A specialized toolbar that is bound to a {@link DataLoader} and provides
 * automatic paging controls.
 */
public class PagingToolBar extends Component implements Listener {

  /**
   * True to use the last load config which allows state data to persist load
   * requests (defaults to false).
   */
  public boolean useLastConfig;

  /**
   * The paging status message to display (defaults to "Displaying {0} - {1} of
   * {2}"). Note that this string is formatted using the braced numbers 0-2 as
   * tokens that are replaced by the values for start, end and total
   * respectively. These tokens should be preserved when overriding this string
   * if showing those values is desired.
   */
  public String displayMsg;

  /**
   * Customizable piece of the default paging text (defaults to "Page").
   */
  public String beforePageText;

  /**
   * Customizable piece of the default paging text (defaults to "of {0}").
   */
  public String afterPageText;

  /**
   * Customizable piece of the default paging text (defaults to "First Page").
   */
  public String firstText;

  /**
   * Customizable piece of the default paging text (defaults to "Previous
   * Page").
   */
  public String prevText;

  /**
   * Customizable piece of the default paging text (defaults to "Next Page").
   */
  public String nextText;

  /**
   * Customizable piece of the default paging text (defaults to "Last Page").
   */
  public String lastText;

  /**
   * Customizable piece of the default paging text (defaults to "Refresh").
   */
  public String refreshText;
  
  /**
   * The message to display when no records are found (defaults to "No data to display").
   */
  public String emptyMsg;

  protected Loader loader;
  protected int cursor, pageSize;
  protected int activePage, pages;
  protected HorizontalPanel panel;
  protected ToolItem first, prev, next, last, refresh;
  protected Label afterText;
  protected Label displayText;
  protected TextBox pageText;

  /**
   * Creates a new paging tool bar with the given page size.
   * 
   * @param pageSize the page size
   */
  public PagingToolBar(final int pageSize) {
    this.pageSize = pageSize;
    baseStyle = "my-paging-toolbar";
  }

  /**
   * Binds the toolbar to the loader.
   * 
   * @param loader the loader
   */
  public void bind(Loader loader) {
    if (this.loader != null) {
      this.loader.removeListener(Loader.BeforeLoad, this);
      this.loader.removeListener(Loader.Load, this);
      this.loader.removeListener(Loader.LoadException, this);
    }
    this.loader = loader;
    loader.addListener(Loader.BeforeLoad, this);
    loader.addListener(Loader.Load, this);
    loader.addListener(Loader.LoadException, this);
  }

  /**
   * Returns the active page.
   * 
   * @return the active page
   */
  public int getActivePage() {
    return activePage;
  }

  /**
   * Returns the current page size.
   * 
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the total number of pages.
   * 
   * @return the
   */
  public int getTotalPages() {
    return pages;
  }

  public void handleEvent(BaseEvent be) {
    switch (be.type) {
      case DataLoader.BeforeLoad:
        refresh.setIconStyle("icon-wait");
        break;
      case DataLoader.Load:
        onLoad((LoadEvent) be);
        refresh.setIconStyle("icon-done");
        break;
      case DataLoader.LoadException:
        refresh.setIconStyle("icon-done");
        break;
    }
  }

  /**
   * Sets the active page.
   * 
   * @param page the page
   */
  public void setActivePage(int page) {
    if (page != activePage && page > 0 && page <= pages) {
      LoadConfig config = getLoadConfig();
      config.start = --page * pageSize;
      config.limit = pageSize;
      loader.load(config);
    } else {
      pageText.setText(String.valueOf((int) activePage));
    }
  }

  /**
   * Sets the current page size. This method does not effect the data currently
   * being displayed. The new page size will not be used until the next load
   * request.
   * 
   * @param pageSize the new page size
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(panel);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(panel);
  }

  protected LoadConfig getLoadConfig() {
    if (useLastConfig && loader.getLastConfig() != null) {
      return loader.getLastConfig();
    } else {
      return new LoadConfig();
    }
  }

  protected void onFirst() {
    LoadConfig config = getLoadConfig();
    config.start = 0;
    config.limit = pageSize;
    loader.load(config);
  }

  protected void onLast() {
    int total = loader.getTotalLength();
    int extra = total % pageSize;
    int lastStart = extra > 0 ? (total - extra) : total - pageSize;
    LoadConfig config = getLoadConfig();
    config.start = lastStart;
    config.limit = pageSize;
    loader.load(config);
  }

  protected void onLoad(LoadEvent event) {
    LoadResult result = event.result;
    cursor = result.cursor;
    int total = loader.getTotalLength();
    activePage = (int) Math.ceil((double) (cursor + pageSize) / pageSize);
    pageText.setText(String.valueOf((int) activePage));
    pages = total < pageSize ? 1 : (int) Math.ceil((double) total / pageSize);
    
    String after = null, display = null;
    if (afterPageText != null) {
      after = Format.substitute(afterPageText, "" + pages);
    } else {
      after = MyGWT.MESSAGES.afterPage((int) pages);
    }

    
    afterText.setText(after);
    first.setEnabled(activePage != 1);
    prev.setEnabled(activePage != 1);
    next.setEnabled(activePage != pages);
    last.setEnabled(activePage != pages);
    int temp = activePage == pages ? total : cursor + pageSize;
    
    if (display != null) {
      String[] params = new String[]{"" + (cursor + 1), "" + temp, "" + total};
      display = Format.substitute(afterPageText, params);
    } else {
      display = MyGWT.MESSAGES.displaying(cursor + 1, (int) temp, (int) total);
    }
    
    String msg = display;
    if (total == 0) {
      msg = emptyMsg;
    }
    displayText.setText(msg);
  }

  protected void onNext() {
    LoadConfig config = getLoadConfig();
    config.start = cursor + pageSize;
    config.limit = pageSize;
    loader.load(config);
  }

  protected void onPageChange() {
    String value = pageText.getText();
    if (value.equals("") || !Util.isInteger(value)) {
      pageText.setText(String.valueOf((int) activePage));
      return;
    }
    int p = Integer.parseInt(value);
    setActivePage(p);
  }

  protected void onPrevious() {
    LoadConfig config = getLoadConfig();
    config.start = Math.max(0, cursor - pageSize);
    config.limit = pageSize;
    loader.load(config);
  }

  protected void onRefresh() {
    LoadConfig config = getLoadConfig();
    config.start = cursor;
    config.limit = pageSize;
    loader.load(config);
  }

  protected void onRender() {
    MyMessages msg = MyGWT.MESSAGES;
    

    refreshText = refreshText == null ? msg.refresh() : refreshText;
    nextText = nextText == null ? msg.nextPage() : nextText;
    prevText = prevText == null ? msg.previousPage() : prevText;
    firstText = firstText == null ? msg.firstPage() : firstText;
    lastText = lastText == null ? msg.lastPage() : lastText;
    beforePageText = beforePageText == null ? msg.beforePage() : beforePageText;
    emptyMsg = emptyMsg == null ? msg.emptyMsg() : emptyMsg;
    
    panel = new HorizontalPanel();
    panel.setSpacing(3);
    panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    setElement(panel.getElement());
    setStyleName(baseStyle);

    first = new ToolItem();
    first.setIconStyle("page-first");
    first.setToolTip(firstText);
    first.addSelectionListener(new SelectionListener() {
      public void widgetSelected(BaseEvent be) {
        onFirst();
      }
    });

    prev = new ToolItem();
    prev.setIconStyle("page-prev");
    prev.setToolTip(prevText);
    prev.addSelectionListener(new SelectionListener() {
      public void widgetSelected(BaseEvent be) {
        onPrevious();
      }
    });

    next = new ToolItem();
    next.setIconStyle("page-next");
    next.setToolTip(nextText);
    next.addSelectionListener(new SelectionListener() {
      public void widgetSelected(BaseEvent be) {
        onNext();
      }
    });

    last = new ToolItem();
    last.setIconStyle("page-last");
    last.setToolTip(lastText);
    last.addSelectionListener(new SelectionListener() {
      public void widgetSelected(BaseEvent be) {
        onLast();
      }
    });

    refresh = new ToolItem();
    refresh.setIconStyle("icon-done");
    refresh.setToolTip(refreshText);
    refresh.addSelectionListener(new SelectionListener() {
      public void widgetSelected(BaseEvent be) {
        onRefresh();
      }
    });

    Label beforePage = new Label(beforePageText);
    beforePage.setStyleName("my-paging-text");
    afterText = new Label();
    afterText.setStyleName("my-paging-text");
    pageText = new TextBox();
    if (!MyGWT.isGecko && !MyGWT.isSafari) {
      pageText.addKeyboardListener(new KeyboardListenerAdapter() {
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
          if (keyCode == KeyboardListener.KEY_ENTER) {
            onPageChange();
          }
        }
      });
    }
    pageText.setWidth("30px");
    pageText.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        onPageChange();
      }
    });

    displayText = new Label();
    displayText.setStyleName("my-paging-display");

    panel.add(first);
    panel.add(prev);
    panel.add(new ToolItem(Style.SEPARATOR));
    panel.add(beforePage);
    panel.add(pageText);
    panel.add(afterText);
    panel.add(new ToolItem(Style.SEPARATOR));
    panel.add(next);
    panel.add(last);
    panel.add(new ToolItem(Style.SEPARATOR));
    panel.add(refresh);
    panel.add(displayText);
    panel.setCellWidth(displayText, "100%");
    panel.setCellHorizontalAlignment(displayText, HorizontalPanel.ALIGN_RIGHT);
  }

}
